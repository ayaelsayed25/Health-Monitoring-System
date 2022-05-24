package Services.BatchLayer;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;

import java.io.IOException;

public class Reducer extends org.apache.hadoop.mapreduce.Reducer<Text, Text, Void, GenericRecord> {

    private final String schema = "{\n" +
            "\"type\" : \"record\"," +
            "\"namespace\" : \"BatchLayer\"," +
            "\"name\" : \"Day\"," +
            "\"fields\" : [" +
            "{ \"name\" : \"Minute\", \"type\": \"int\" }, " +
            "{ \"name\" : \"Service\", \"type\": \"string\" }," +
            "{ \"name\" : \"MessageCount\", \"type\": \"int\" }," +
            "{ \"name\" : \"CpuUtilizationMean\", \"type\": \"double\" }," +
            "{ \"name\" : \"DiskUtilizationMean\",\"type\": \"double\" }," +
            "{ \"name\" : \"RamUtilizationMean\", \"type\": \"double\" }," +
            "{ \"name\" : \"CpuUtilizationPeak\", \"type\": \"double\" }," +
            "{ \"name\" : \"DiskUtilizationPeak\",\"type\": \"double\" }," +
            "{ \"name\" : \"RamUtilizationPeak\", \"type\": \"double\" }" +
            "]}";
    private final Schema mySchema =  new Schema.Parser().parse(schema);
    private final GenericRecord myGenericRecord = new GenericData.Record(mySchema);


    public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

        System.out.println("HIIIIIIIIIIIIIIIIIIIIIIIIIIIIII");
        int messageCount = 0;
        double cpuUtilizationMean = 0;
        double diskUtilizationMean = 0;
        double ramUtilizationMean = 0;
        double cpuUtilizationPeak = 0;
        double diskUtilizationPeak = 0;
        double ramUtilizationPeak = 0;


        for (Text val : values) {
            String[] data = val.toString().split(",", -1);

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

        int hours = Integer.parseInt(key.toString().substring(11, 13)) * 60;
        int minutes = hours + Integer.parseInt(key.toString().substring(14, 16));

        myGenericRecord.put("Minute", minutes);
        myGenericRecord.put("Service", key.toString().substring(25));
        System.out.println( key.toString().substring(25));
        myGenericRecord.put("MessageCount", messageCount);
        myGenericRecord.put("CpuUtilizationMean", cpuUtilizationMean);
        myGenericRecord.put("DiskUtilizationMean", diskUtilizationMean);
        myGenericRecord.put("RamUtilizationMean", ramUtilizationMean);
        myGenericRecord.put("CpuUtilizationPeak", cpuUtilizationPeak);
        myGenericRecord.put("DiskUtilizationPeak", diskUtilizationPeak);
        myGenericRecord.put("RamUtilizationPeak", ramUtilizationPeak);

//        String value = messageCount + "," + cpuUtilizationMean + "," +
//                diskUtilizationMean + "," + ramUtilizationMean + "," +
//                cpuUtilizationPeak + "," + diskUtilizationPeak + "," +
//                ramUtilizationPeak;
//
//        Text writable = new Text(value);

        context.write(null, myGenericRecord);
    }
}
