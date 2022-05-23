package Services.BatchLayer;

import org.apache.hadoop.io.ArrayWritable;
import org.apache.hadoop.io.Text;

import java.io.IOException;

public class Mapper extends org.apache.hadoop.mapreduce.Mapper<Object, Text, KeyWritable, ArrayWritable> {

    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {

        String[] data = value.toString().split(",", -1);

        String timestamp = data[1];
        String dateTime = timestamp.substring(0, 16);

        double totalRAM = Double.parseDouble(data[3]);
        double freeRAM = Double.parseDouble(data[4]);

        double totalDisk = Double.parseDouble(data[5]);
        double freeDisk = Double.parseDouble(data[6]);

        double ram = (totalRAM - freeRAM)/totalRAM;
        double disk = (totalDisk - freeDisk)/totalDisk;

        String[] array = {data[2], Double.toString(ram), Double.toString(disk)};
        ArrayWritable writable = new ArrayWritable(array);

        KeyWritable keyWritable = new KeyWritable();
        keyWritable.setServiceName(new Text(data[0]));
        keyWritable.setDateTime(new Text(dateTime));

        context.write(keyWritable, writable);

    }
}