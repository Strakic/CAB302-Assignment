/**
 * had difficulties with "Error: JavaFX runtime components are missing, and are required to run this application "
 * eventually found work around of wrapping the main method in a separate launcher. No clue why this works will talk about tomorrow
 */
public class App {
    public static void main(String[] args) {
        Main.main(args);
    }
}