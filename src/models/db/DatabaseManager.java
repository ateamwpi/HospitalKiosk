package models.db;

import models.dir.Directory;
import models.path.Node;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by mattm on 3/29/2017.
 */
public class DatabaseManager {

    private Connection conn;

    public DatabaseManager() {

    }

    public void connect() throws SQLException, ClassNotFoundException {
        Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        System.out.println("Successfully located database drivers.");
        this.conn = DriverManager.getConnection("jdbc:derby:hospitalDB;create=false");
        System.out.println("Successfully connected to database.");
    }

    public ArrayList<Directory> getAllDirectories() {
        return null;
    }

    public ArrayList<Node> getAllNodes() {
        return null;
    }

}
