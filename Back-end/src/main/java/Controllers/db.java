package Controllers;

import java.sql.*;

public class db {



    public db() throws SQLException, ClassNotFoundException {
        Class.forName("org.duckdb.DuckDBDriver");
        Connection conn = DriverManager.getConnection("jdbc:duckdb:");
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT 42");
    }
}
