package Services.LambdaAchitecture.Scheduler;

import Services.LambdaAchitecture.ServingLayer.ServingLayerRunner;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import static java.util.concurrent.TimeUnit.MINUTES;


public class Runner {
    private final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(1);

    public void createBatchView() {
        final Runnable createBatchView = () -> {
            ServingLayerRunner runner = new ServingLayerRunner();
            String pattern = "dd-MM-yyyy";
            System.out.println(pattern);
            String dateInString = new SimpleDateFormat(pattern).format(new Date());

            LocalDateTime now = LocalDateTime.now();
            int hour = now.getHour();
            int minutes = hour * 60 + now.getMinute();

            try {
                runner.jobRun(dateInString);

                FileWriter myWriter = new FileWriter("batchTime.txt");
                myWriter.write(Integer.toString(minutes));
                myWriter.close();

                File file = new File("realTimeViews/old_view.parquet");

                File file2 = new File("realTimeViews/new_view.parquet");

                file.delete();

                file2.renameTo(file);

            } catch (IOException | InterruptedException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        };

        final ScheduledFuture<?> Handler =
                scheduler.scheduleAtFixedRate(createBatchView, 15, 15, MINUTES);

//        Cancel Scheduler
//        scheduler.schedule(() -> { Handler.cancel(true); }, 60, MINUTES);
    }

}
