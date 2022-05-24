package Services.LambdaAchitecture.ServingLayer;

import org.apache.avro.Schema;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import org.apache.parquet.avro.AvroParquetOutputFormat;

import java.io.IOException;
import java.security.acl.Group;


public class Runner {

    private final String schema = "{\n" +
                    "\"type\" : \"record\"," +
                    "\"namespace\" : \"BatchLayer\"," +
                    "\"name\" : \"Day\"," +
                    "\"fields\" : [" +
                    "{ \"name\" : \"Minute\", \"type\": \"int\" }, " +
                    "{ \"name\" : \"MessageCount\", \"type\": \"int\" }," +
                    "{ \"name\" : \"CpuUtilizationMean\", \"type\": \"double\" }," +
                    "{ \"name\" : \"DiskUtilizationMean\", \"type\": \"double\" }," +
                    "{ \"name\" : \"RamUtilizationMean\", \"type\": \"double\" }," +
                    "{ \"name\" : \"CpuUtilizationPeak\", \"type\": \"double\" }," +
                    "{ \"name\" : \"DiskUtilizationPeak\", \"type\": \"double\" }," +
                    "{ \"name\" : \"RamUtilizationPeak\", \"type\": \"double\" }" +
                    "]}";
    private final Schema mySchema =  new Schema.Parser().parse(schema);

    public void jobRun (String filePath) throws IOException, InterruptedException, ClassNotFoundException {


        Configuration conf = new Configuration();

        String inputPath = "hdfs://hadoop-master:9000/try/health_messages_csv/" + filePath;
        String outputPath = "/home/user/Documents/GitHub/Health-Monitoring-System/Back-end/src/main/java/output/out1";

        Job job = Job.getInstance(conf, "Mean CPU Utilization");
        job.setJarByClass(Runner.class);
        job.setMapperClass(Mapper.class);
        job.setReducerClass(Reducer.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        job.setOutputKeyClass(NullWritable.class);
        job.setOutputValueClass(Group.class);
        job.setOutputFormatClass(AvroParquetOutputFormat.class);
        AvroParquetOutputFormat.setSchema(job, mySchema);

        MultipleOutputs.addNamedOutput(job, "parquet", AvroParquetOutputFormat.class,
                NullWritable.class, Group.class);

        FileInputFormat.addInputPath(job, new Path(inputPath));
        FileOutputFormat.setOutputPath(job, new Path(outputPath));

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
