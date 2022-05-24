import java.sql.*;

public class Try {
    public static void main(String[] args) throws Exception {
//        Runner runner = new Runner();
//        runner.jobRun();
        Class.forName("org.duckdb.DuckDBDriver");
        Connection conn = DriverManager.getConnection("jdbc:duckdb:");
        Statement stmt = conn.createStatement();
        boolean resultSet = stmt.execute("SELECT COUNT(*) FROM read_parquet('/home/user/Downloads/part-r-00000.parquet');");
        System.out.println(resultSet);
//        CpuUtilizationMean mean = new CpuUtilizationMean();
//        mean.calculate("01-01-2023", "01-01-2023");
//        String s = "01-01-2023 23:51 service-2";
//        int hours = Integer.parseInt(s.substring(11, 13)) * 60;
//        int minutes = hours + Integer.parseInt(s.substring(14, 16));
//        System.out.println(minutes);
//        System.out.println(s.substring(25));
    }
}
