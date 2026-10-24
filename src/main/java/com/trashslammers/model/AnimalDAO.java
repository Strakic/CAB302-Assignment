package com.trashslammers.model;

import com.trashslammers.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Saves and loads admin-added animals using the animals table in the SQLite database.
 */
public class AnimalDAO implements IAnimalDAO{


    private final Connection connection;

    /**
     * Constructs a new AnimalDAO and creates the animals table if needed.
     */
    public AnimalDAO() {
        connection = DatabaseConnection.getInstance();
        createTable();
    }

    /**
     * Creates the animals table if it doesn't exist yet.
     */
    private void createTable() {

        String sql = "CREATE TABLE IF NOT EXISTS animals ("
                + "id VARCHAR PRIMARY KEY,"
                + "name VARCHAR NOT NULL UNIQUE COLLATE NOCASE,"
                + "species VARCHAR, "
                + "rarity VARCHAR NOT NULL,"
                + "cost INTEGER NOT NULL,"
                + "spriteFile VARCHAR,"
                + "habitat VARCHAR,"
                + "fact VARCHAR)";
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Could not create animals table", e);
        }
    }

    /**
     * Saves a new animal to the database.
     * @param animal The animal to save
     * @return true if the animal was saved, otherwise false
     */
    @Override
    public boolean addAnimal(Animal animal) {
        String sql = "INSERT INTO animals "
                + "(id, name, species, rarity, cost, spriteFile, habitat, fact) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, animal.getId());
            ps.setString(2, animal.getName());
            ps.setString(3, animal.getSpecies());
            ps.setString(4, animal.getRarity().name());
            ps.setInt(5, animal.getCost());
            ps.setString(6, animal.getSpriteFile());
            ps.setString(7, animal.getHabitat());
            ps.setString(8, animal.getFact());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Checks whether a saved animal already has this name, ignoring case.
     * @param name The name to check
     * @return true if the name is taken, otherwise false
     */
    @Override
    public boolean nameExists(String name) {
        String sql = "SELECT 1 FROM animals WHERE name = ? COLLATE NOCASE";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * Loads a saved animal by its id.
     * @param id The id of the animal
     * @return The animal, or null if there isn't one with that id
     */
    @Override
    public Animal getAnimalById(String id) {
        String sql = "SELECT * FROM animals WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Loads every saved animal in the order they were added.
     * @return A list of all saved animals
     */
    @Override
    public List<Animal> getAllAnimals() {
        List<Animal> animals = new ArrayList<>();
        //  new animals will appear at the end of the shop
        String sql = "SELECT * FROM animals ORDER BY rowid";
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                animals.add(mapRow(rs));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return animals;
    }

    /**
     * Turns the current row of a query result into an Animal.
     * @param rs The query result, positioned on the row to read
     * @return The animal stored in that row
     * @throws SQLException If a column can't be read
     */
    private Animal mapRow(ResultSet rs) throws SQLException {
        return new Animal(
                rs.getString("id"),
                rs.getString("name"),
                rs.getString("species"),
                parseRarity(rs.getString("rarity")),
                rs.getInt("cost"),
                rs.getString("spriteFile"),
                rs.getString("habitat"),
                rs.getString("fact"));
    }

    /**
     * Turns the rarity text stored in the database back into a Rarity.
     * @param text The rarity text from the database
     * @return The matching rarity, or COMMON if the text isn't recognised
     */
    private static Rarity parseRarity(String text) {
        try {
            return Rarity.valueOf(text);
        }
        catch (IllegalArgumentException | NullPointerException e) {
            return Rarity.values()[0];
        }
    }


}
