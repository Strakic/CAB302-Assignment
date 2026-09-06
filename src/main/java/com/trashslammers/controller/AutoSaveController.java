package com.trashslammers.controller;

import javafx.animation.Animation;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.util.Duration;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public final class AutoSaveController implements AutoCloseable {

    private static final Duration IDLE_DELAY = Duration.seconds(1.5);
    private static final Duration MAX_DELAY = Duration.seconds(10);
    private static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("HH.mm.ss");

    private static final List<AutoSaveController> ACTIVE = new CopyOnWriteArrayList<>();

    private final Path target;
    private final Path temp;
    private final Supplier<String> content;
    private final ExecutorService writer;
    private final PauseTransition idleTimer;
    private final PauseTransition maxTimer;
    private final ReadOnlyStringWrapper status = new ReadOnlyStringWrapper("Ready");

    private String lastQueued;
    private boolean closed;

    public AutoSaveController(Path target, Supplier<String> content) {
        this.target = target;
        this.temp = target.resolveSibling(target.getFileName() + ".tmp");
        this.content = content;
        this.lastQueued = content.get();

        this.writer = Executors.newSingleThreadExecutor(r -> {
            Thread t = new Thread(r, "autosave-" + target.getFileName());
            t.setDaemon(true);
            return t;
        });

        this.idleTimer = new PauseTransition(IDLE_DELAY);
        this.idleTimer.setOnFinished(e -> saveNow());
        this.maxTimer = new PauseTransition(MAX_DELAY);
        this.maxTimer.setOnFinished(e -> saveNow());

        ACTIVE.add(this);
    }

    public AutoSaveController watch(ObservableValue<?>... properties) {
        for (ObservableValue<?> p : properties) {
            p.addListener((obs, oldVal, newVal) -> touch());
        }
        return this;
    }

    public void touch() {
        if (closed) {
            return;
        }
        idleTimer.playFromStart();
        if (maxTimer.getStatus() != Animation.Status.RUNNING) {
            maxTimer.playFromStart();
        }
        status.set("Unsaved changes\u2026");
    }

    public void saveNow() {
        idleTimer.stop();
        maxTimer.stop();
        if (closed) {
            return;
        }
        String snapshot = content.get();
        if (snapshot.equals(lastQueued)) {
            return;
        }
        lastQueued = snapshot;

        writer.submit(() -> {
            try {
                writeAtomically(snapshot);
                Platform.runLater(() -> status.set("Saved " + LocalTime.now().format(TIME)));
            } catch (IOException ex) {
                Platform.runLater(() -> {
                    lastQueued = null;
                    status.set("Auto-save FAILED: " + ex.getMessage());
                });
            }
        });
    }

    public ReadOnlyStringProperty statusProperty() {
        return status.getReadOnlyProperty();
    }

    private void writeAtomically(String text) throws IOException {
        Files.createDirectories(target.getParent());
        Files.writeString(temp, text, StandardCharsets.UTF_8);
        try {
            Files.move(temp, target, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (AtomicMoveNotSupportedException e) {
            Files.move(temp, target, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    @Override
    public void close() throws Exception {
        if (closed) {
            return;
        }
        closed = true;
        idleTimer.stop();
        maxTimer.stop();

        writer.shutdown();
        writer.awaitTermination(3, TimeUnit.SECONDS);

        String pending = content.get();
        if (!pending.equals(lastQueued)) {
            writeAtomically(pending);
        }
        ACTIVE.remove(this);
    }

    public static void closeALL() {
        for (AutoSaveController saver : List.copyOf(ACTIVE)) {
            try {
                saver.close();
            } catch (Exception e) {
                System.err.print("Final auto-save failed for " + saver.target + ": " + e);
            }
        }
    }
}

