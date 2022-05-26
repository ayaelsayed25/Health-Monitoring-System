
package Controllers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;

public class Response {
    private int realTimeLimit;
    private String currentDay;
    private int currentMinute;

    public String getResponse(int start, int end, String startDay, String endDay) throws InterruptedException, SQLException, ClassNotFoundException, ParseException {

        getCurrentData();

        if (end > currentMinute || start > currentMinute)
            return "";

        // Case 1 : Query in Real Time View
        if (in_Range(start, startDay) && in_Range(end, endDay))
            return getData("realTimeViews", start, end, currentDay);

        // Case 2 : Both in batches and real
        // end must be in range for the real views
        // end must be found in real time views
        if (in_Range(end, endDay))
        {
            String realTimeResult = getData("realTimeViews", realTimeLimit, end, currentDay);
            String batchResult = getBatchViews(start, realTimeLimit, startDay, endDay);
            return realTimeResult + batchResult;
        }
        else
            return getBatchViews(start, end, startDay, endDay);
    }


    private String getBatchViews(int start, int end, String startDay, String endDay) throws ParseException, SQLException, InterruptedException, ClassNotFoundException {

        StringBuilder batchResult = new StringBuilder();

        boolean isFirstDay = false;

        while(!startDay.equals(endDay)) {
            isFirstDay = true;
            batchResult.append(getData("batchViews", start, 1440, startDay));
            startDay = addDay(startDay);
            start = 0;
        }

        if(isFirstDay)
             batchResult.append(getData("batchViews", 0, end, endDay));
        else
            batchResult.append(getData("batchViews", start, end, endDay));

       return batchResult.toString();
    }

    private String getData(String folder, int start, int end, String day) throws InterruptedException, SQLException, ClassNotFoundException {
        int numberOfServices = 4;

        Thread[] services = new Thread[numberOfServices];
        Service[] services_result = new Service[numberOfServices];
        StringBuilder result = new StringBuilder();
        for(int i = 0; i < numberOfServices; i++){
            services_result[i] = new Service(folder, day, start, end, i+1);
            services[i] = new Thread(services_result[i]);
        }

        for(int i = 0; i < numberOfServices; i++)
            services[i].start();

        for(int i = 0; i < numberOfServices; i++)
            services[i].join();

        for(int i = 0; i < numberOfServices; i++)
            result.append(services_result[i].getResult());

        return result.toString();

    }


    private void getCurrentData() {
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        int month = now.getMonthValue();
        int day = now.getDayOfMonth();
        int hour = now.getHour();
        int minutes = now.getMinute();

        this.currentMinute = hour * 60 + minutes;
        this.currentDay = String.format("%02d-%02d-%d",  day, month, year);

        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader("batchTime.txt"));
            this.realTimeLimit = Integer.parseInt(br.readLine());
            br.close();
            System.out.println(realTimeLimit);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean in_Range(int number, String Day) {
        return number <= currentMinute && number >= realTimeLimit && Day.equals(currentDay);
    }


    private String addDay(String dateBefore) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(dateBefore));

        cal.add(Calendar.DAY_OF_MONTH, 1);

        //date after adding three days to the given date
       return sdf.format(cal.getTime());
    }

    public static void main(String[] args) throws SQLException, InterruptedException, ClassNotFoundException, ParseException {

        System.out.println("Working Directory = " + System.getProperty("user.dir"));

        Response response = new Response();

        response.getResponse(10,20,"01-01-2023","01-01-2023");
       // C:\Users\Blu-Ray\IdeaProjects\demo\batchViews\01-01-2023_4-r-00000.parquet
    }


}