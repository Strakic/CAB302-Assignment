package com.trashslammers.model;

import com.trashslammers.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class AnimalDAO implements IAnimalDAO{


    private final Connection connection;

    public AnimalDAO() {
        connection = DatabaseConnection.getInstance();
        createTable();
    }


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

    /** Falls back to the first rarity for junk text */
    private static Rarity parseRarity(String text) {
        try {
            return Rarity.valueOf(text);
        }
        catch (IllegalArgumentException | NullPointerException e) {
            return Rarity.values()[0];
        }
    }


}
