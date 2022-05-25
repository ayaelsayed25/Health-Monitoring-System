package Controllers;

import java.sql.*;

public class Service implements Runnable {
    Connection con;
    String folder, day, query;
    int start;
    int end;
    int serviceNumber;
    String result = "";

    public Service( String folder, String day, int start, int end, int serviceNumber,String query) throws ClassNotFoundException, SQLException {
        Class.forName("org.duckdb.DuckDBDriver");
        this.con = DriverManager.getConnection("jdbc:duckdb:");
        this.folder = folder;
        this.day = day;
        this.start = start;
        this.end = end;
        this.serviceNumber = serviceNumber;
        this.query=query;
    }

    @Override
    public void run() {
        String path = System.getProperty("user.dir") + "\\" + this.folder + "\\" + this.day + "_" +
                this.serviceNumber + "-r-00000.parquet";
        try {
            readFile(path);
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }
    private void readFile(String path) throws SQLException {

            Statement stmt = this.con.createStatement();
            System.out.printf("SELECT * FROM '%s';%n", path);
            ResultSet rs = stmt.executeQuery(String.format("SELECT %s FROM '%s' WHERE Minute BETWEEN %d AND %d ;",
                                                                    query, path, start, end));
           // System.out.println(rs);
            while(rs.next()) {
                this.result += String.format("service : %d   MessgeCount:  %s\n", serviceNumber, rs.getInt(query));
            }
            System.out.println(result);
    }

    public String getResult() {
        return result;
    }


}

