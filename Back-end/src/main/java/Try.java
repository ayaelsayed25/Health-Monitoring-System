import Services.LambdaAchitecture.ServingLayer.ServingLayerRunner;

import java.io.File;

public class Try {

    public static void main(String[] args) throws Exception {

        // Create Batch Views
        File file = new File("/home/hadoop/health_messages_csv");
        File[] files = file.listFiles();
        assert files != null;
        for (File f : files)
        {
            System.out.println(f.getName());
            ServingLayerRunner runner = new ServingLayerRunner();
            runner.jobRun(f.getName().substring(0, 10));
        }
//        runner.jobRun("01-01-2023.csv");
//        Runner runner = new Runner();
//        runner.beepForAnHour();
//        String pattern = "dd-MM-yyyy";
//        String dateInString =new SimpleDateFormat(pattern).format(new Date());
//        System.out.println(dateInString);
//        Class.forName("org.duckdb.DuckDBDriver");
//        Connection conn = DriverManager.getConnection("jdbc:duckdb:");
//        Statement stmt = conn.createStatement();
//        boolean resultSet = stmt.execute("SELECT COUNT(*) FROM read_parquet('/home/user/Downloads/part-r-00000.parquet');");
//        System.out.println(resultSet);
//        CpuUtilizationMean mean = new CpuUtilizationMean();
//        mean.calculate("01-01-2023", "01-01-2023");
//        String s = "01-01-2023 23:51 service-2";
//        int hours = Integer.parseInt(s.substring(11, 13)) * 60;
//        int minutes = hours + Integer.parseInt(s.substring(14, 16));
//        System.out.println(minutes);
//        System.out.println(s.substring(25));
//        String s = "01-01-2023.csv";
//        System.out.println(s.substring(0, 10));
    }
}
