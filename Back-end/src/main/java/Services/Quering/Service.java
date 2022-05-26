package Services.Quering;

import java.sql.*;

public class Service implements Runnable {
    Connection con;
    String folder, day;
    int start;
    int end;
    int serviceNumber;
    String condition;
    String result = "";

    public Service( String folder, String day, int start, int end, int serviceNumber, String condition) throws ClassNotFoundException, SQLException {

        Class.forName("org.duckdb.DuckDBDriver");

        this.con = DriverManager.getConnection("jdbc:duckdb:");
        this.folder = folder;
        this.day = day;
        this.start = start;
        this.end = end;
        this.serviceNumber = serviceNumber;
        this.condition = condition;
    }

    @Override
    public void run() {
        String path;
        if(folder.compareTo("realTimeViews") == 0)
            path = System.getProperty("user.dir") + "/realTimeViews/old_view.parquet";
        else
            path = System.getProperty("user.dir") + "/" + this.folder + "/" + this.day +
                "/" + this.day + "_" + this.serviceNumber + "-r-00000.parquet";
        try {
            readFile(path);
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }
    private void readFile(String path) throws SQLException {

        Statement stmt = this.con.createStatement();

        ResultSet rs = stmt.executeQuery(String.format("SELECT sum(MessageCount), " +
                        "sum(CpuUtilizationMean)/count(CpuUtilizationMean), " +
                        "sum(DiskUtilizationMean)/count(DiskUtilizationMean), " +
                        "sum(RamUtilizationMean)/count(RamUtilizationMean), " +
                        "max(CpuUtilizationPeak), max(DiskUtilizationPeak), max(RamUtilizationPeak)" +
                        " FROM '%s' " + condition, path));
        rs.next();

        this.result = "service : " + serviceNumber +
                "   MessageCount: " + rs.getInt(1) +
                "   CpuUtilizationMean:  " + rs.getDouble(2) +
                "   RamUtilizationMean:  " + rs.getDouble(3) +
                "   DiskUtilizationMean:  " + rs.getDouble(4) +
                "   CpuUtilizationPeak:  " + rs.getDouble(5) +
                "   RamUtilizationPeak: " + rs.getDouble(6) +
                "   DiskUtilizationPeak:  " + rs.getDouble(7);
//        System.out.println(result);
    }

    public String getResult() {
        return result;
    }


}

