package Services.LambdaAchitecture.ServingLayer;

import org.apache.hadoop.io.Text;

import java.io.IOException;

public class Mapper extends org.apache.hadoop.mapreduce.Mapper<Object, Text, Text, Text> {

    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {

        String[] data = value.toString().split(",", -1);

        String timestamp = data[1];

        double totalRAM = Double.parseDouble(data[3]);
        double freeRAM = Double.parseDouble(data[4]);

        double totalDisk = Double.parseDouble(data[5]);
        double freeDisk = Double.parseDouble(data[6]);

        double ram = (totalRAM - freeRAM)/totalRAM;
        double disk = (totalDisk - freeDisk)/totalDisk;

        String newValue = data[2] + "," + ram + "," + disk;
        Text writable = new Text(newValue);

        Text dateService = new Text(timestamp + "," + data[0]);

        context.write(dateService, writable);

    }
}