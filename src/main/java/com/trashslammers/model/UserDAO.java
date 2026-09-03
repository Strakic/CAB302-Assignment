package com.trashslammers.model;

import com.trashslammers.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements IUserDAO {
    private final Connection connection;

    public UserDAO(Connection connection) {
        this.connection = DatabaseConnection.getInstance();
    }

    public void createTable() {
        try {
            Statement createTable = connection.createStatement();
            createTable.execute(
                    "CREATE TABLE IF NOT EXISTS trashObjects ("
                            + "username VARCHAR NOT NULL,"
                            + "passwordHash VARCHAR NOT NULL "
                            + ")"
            );
        } catch (SQLException ex) {
            System.err.println(ex);
        }

    }

    public void insert(TrashItem trashItem) {
        // Todo Later: Create a PreparedStatement to run the INSERT query
    }

    public void update(TrashItem trashItem) {
        // Todo Later: Create a PreparedStatement to run the UPDATE query
    }

    public List<TrashItem> getAll() {
        List<TrashItem> trashObject = new ArrayList<>();
        // Todo Later: Create a Statement to run the SELECT * query
        return trashObject;
    }

    public TrashItem getByName(String name) {
        // Todo Later: Create a PreparedStatement to run the conditional SELECT query
        return null;
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

}
