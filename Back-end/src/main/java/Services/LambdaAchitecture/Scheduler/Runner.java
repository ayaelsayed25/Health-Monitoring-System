package Services.LambdaAchitecture.Scheduler;

import Services.LambdaAchitecture.ServingLayer.ServingLayerRunner;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

public class Runner {
    private final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(1);

    public void createBatchView() {
        final Runnable createBatchView = () -> {
            ServingLayerRunner runner = new ServingLayerRunner();
            String pattern = "dd-MM-yyyy";
            System.out.println(pattern);
            String dateInString = new SimpleDateFormat(pattern).format(new Date());
            try {
                runner.jobRun();
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
