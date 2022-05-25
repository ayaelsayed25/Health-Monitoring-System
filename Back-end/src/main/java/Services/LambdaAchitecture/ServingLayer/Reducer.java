package Services.LambdaAchitecture.ServingLayer;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;


import java.io.IOException;

public class Reducer extends org.apache.hadoop.mapreduce.Reducer<Text, Text, Void, GenericRecord> {

    private final String schema = "{\n" +
            "\"type\" : \"record\"," +
            "\"namespace\" : \"BatchLayer\"," +
            "\"name\" : \"Day\"," +
            "\"fields\" : [" +
            "{ \"name\" : \"Minute\",             \"type\": \"int\" }, " +
            "{ \"name\" : \"MessageCount\",       \"type\": \"int\" }," +
            "{ \"name\" : \"CpuUtilizationMean\", \"type\": \"double\" }," +
            "{ \"name\" : \"DiskUtilizationMean\",\"type\": \"double\" }," +
            "{ \"name\" : \"RamUtilizationMean\", \"type\": \"double\" }," +
            "{ \"name\" : \"CpuUtilizationPeak\", \"type\": \"double\" }," +
            "{ \"name\" : \"DiskUtilizationPeak\",\"type\": \"double\" }," +
            "{ \"name\" : \"RamUtilizationPeak\", \"type\": \"double\" }" +
            "]}";
    private final Schema mySchema =  new Schema.Parser().parse(schema);
    private final GenericRecord myGenericRecord = new GenericData.Record(mySchema);

    private MultipleOutputs mos;
    public void setup(Context context) {

        mos = new MultipleOutputs(context);
    }
    public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

        int[] messageCount = new int[4];
        double[] cpuUtilizationMean = new double[4];
        double[] diskUtilizationMean = new double[4];
        double[] ramUtilizationMean = new double[4];
        double[] cpuUtilizationPeak = new double[4];
        double[] diskUtilizationPeak = new double[4];
        double[] ramUtilizationPeak = new double[4];


        for (Text val : values) {
            String[] data = val.toString().split(",", -1);

            double cpu = Double.parseDouble(data[0]);
            double ram = Double.parseDouble(data[1]);
            double disk = Double.parseDouble(data[2]);
            int service = Integer.parseInt(data[3].substring(8)) - 1;

            messageCount[service] += 1;

            cpuUtilizationMean[service] += cpu;
            diskUtilizationMean[service] += disk;
            ramUtilizationMean[service] += ram;

            cpuUtilizationPeak[service] = Math.max(cpuUtilizationPeak[service], cpu);
            diskUtilizationPeak[service] = Math.max(diskUtilizationPeak[service], disk);
            ramUtilizationPeak[service] = Math.max(ramUtilizationPeak[service], ram);

        }
        int hours = Integer.parseInt(key.toString().substring(11, 13)) * 60;
        int minutes = hours + Integer.parseInt(key.toString().substring(14, 16));

        for (int i = 0; i < 4; i++) {

            cpuUtilizationMean[i] /= messageCount[i];
            diskUtilizationMean[i] /= messageCount[i];
            ramUtilizationMean[i] /= messageCount[i];

            myGenericRecord.put("Minute", minutes);
            myGenericRecord.put("MessageCount", messageCount[i]);
            myGenericRecord.put("CpuUtilizationMean", cpuUtilizationMean[i]);
            myGenericRecord.put("DiskUtilizationMean", diskUtilizationMean[i]);
            myGenericRecord.put("RamUtilizationMean", ramUtilizationMean[i]);
            myGenericRecord.put("CpuUtilizationPeak", cpuUtilizationPeak[i]);
            myGenericRecord.put("DiskUtilizationPeak", diskUtilizationPeak[i]);
            myGenericRecord.put("RamUtilizationPeak", ramUtilizationPeak[i]);

            mos.write("parquet", NullWritable.get(), myGenericRecord,
                    key.toString().substring(0, 10) + "_" + (i+1));
        }
    }

    public void cleanup(Context context) throws IOException, InterruptedException {
        mos.close();
    }


}
