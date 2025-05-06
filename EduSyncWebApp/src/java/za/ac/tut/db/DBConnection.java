package za.ac.tut.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String DB_URL = "jdbc:mysql://172.20.10.3:3306/edusync_db";
    private static final String DB_USER = "kgj";
    private static final String DB_PASSWORD = "kgotso";

    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Connecting to the MySQL database...");
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("MySQL database connection failed: " + e.getMessage());
            throw new SQLException("Error connecting to MySQL database", e);
        }
    }
}
