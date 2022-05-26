import Services.Quering.Query;
import Services.Quering.UserService;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Try {
    public static int getRandomNumberUsingNextInt(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }
    public static void main(String[] args) throws Exception {

        // Create Batch Views
//        File file = new File("/home/hadoop/health_messages_csv");
//        File[] files = file.listFiles();
//        assert files != null;
//        for (File f : files)
//        {
//            System.out.println(f.getName());
//            ServingLayerRunner runner = new ServingLayerRunner();
////        runner.jobRun("26-01-2024");
////        runner.jobRun("01-01-2023");
//            runner.jobRun(f.getName().substring(0, 10));
//        }
//        UserService userService = new UserService();
//        Query query = new Query();
//        query.setStartDay("01-01-2023");
//        query.setEndDay("01-01-2023");
//        query.setStart(0);
//        query.setEnd(5);
//        userService.processQuery(query);
//        FileWriter myWriter = new FileWriter("batchTime.txt");
//        int m = 90;
//        myWriter.write(m);
//        myWriter.close();
        UserService userService = new UserService();
        String[] dates = {"01-01-2023", "16-05-2023", "15-09-2022", "30-11-2024", "21-04-2025", "26-04-2024", "05-07-2023",
                "5-06-2022",  "30-08-2022", "04-12-2024",  "10-05-2024",  "15-08-2023",  "20-07-2022",
                "25-06-2023",  "30-08-2023", "05-01-2023", "10-05-2025", "15-08-2024", "20-07-2023",  "25-07-2022",  "30-09-2022",
                "05-01-2025", "10-06-2022", "15-09-2022", "20-07-2024", "25-07-2024", "30-09-2023",
                "05-02-2023", "10-06-2023", "15-10-2022", "20-08-2022", "25-08-2022", "30-09-2024",
                "05-02-2024", "10-06-2024", "15-10-2023", "20-08-2023", "25-08-2023", "30-10-2022",
                "05-02-2025", "10-07-2023", "15-10-2024", "20-09-2022", "25-09-2022", "30-10-2024"};

        Query query = new Query();
        long diff = 0;

        for (int i = 0; i < 10000; i++) {
            int startMinutes = getRandomNumberUsingNextInt(0, 1439);
            int increment = getRandomNumberUsingNextInt(0, 10);
            int endMinutes = getRandomNumberUsingNextInt(startMinutes, startMinutes + increment);
            int index = getRandomNumberUsingNextInt(0,49);

            query.setStartDay(dates[index]);
            query.setEndDay(dates[index]);
            query.setStart(startMinutes);
            query.setEnd(endMinutes);

            long startTime = System.nanoTime();
            userService.processQuery(query);
            long endTime = System.nanoTime();

            diff += (endTime - startTime);
        }
        System.out.println("Throughput = " + diff/10000);

//        ServingLayerRunner runner = new ServingLayerRunner();
//        runner.jobRun();
//        System.out.printf("SELECT * FROM '%s';%n", "path");
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
