package Services.BatchLayer;

import org.apache.hadoop.io.ArrayWritable;
import java.io.IOException;

public class Reducer extends org.apache.hadoop.mapreduce.Reducer<KeyWritable, ArrayWritable, KeyWritable, ArrayWritable> {

    public void reduce(KeyWritable key, Iterable<ArrayWritable> values, Context context) throws IOException, InterruptedException {

        int messageCount = 0;
        double cpuUtilizationMean = 0;
        double diskUtilizationMean = 0;
        double ramUtilizationMean = 0;
        double cpuUtilizationPeak = 0;
        double diskUtilizationPeak = 0;
        double ramUtilizationPeak = 0;


        for (ArrayWritable val : values) {
            String[] data = (String[]) val.toArray();

            double cpu = Double.parseDouble(data[0]);
            double ram = Double.parseDouble(data[1]);
            double disk = Double.parseDouble(data[2]);

            messageCount += 1;

            cpuUtilizationMean += cpu;
            diskUtilizationMean += disk;
            ramUtilizationMean += ram;

            cpuUtilizationPeak = Math.max(cpuUtilizationPeak, cpu);
            diskUtilizationPeak = Math.max(diskUtilizationPeak, disk);
            ramUtilizationPeak = Math.max(ramUtilizationPeak, ram);

        }
        cpuUtilizationMean /= messageCount;
        diskUtilizationMean /= messageCount;
        ramUtilizationMean /= messageCount;

        String[] arr = {Integer.toString(messageCount), Double.toString(cpuUtilizationMean),
                Double.toString(diskUtilizationMean), Double.toString(ramUtilizationMean),
                Double.toString(cpuUtilizationPeak), Double.toString(diskUtilizationPeak),
                Double.toString(ramUtilizationPeak)};

        ArrayWritable writable = new ArrayWritable(arr);
        context.write(key, writable);
    }
}
