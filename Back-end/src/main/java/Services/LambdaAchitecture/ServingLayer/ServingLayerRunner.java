package Services.LambdaAchitecture.ServingLayer;

import org.apache.avro.Schema;
import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import org.apache.parquet.avro.AvroParquetOutputFormat;
import org.apache.parquet.example.data.Group;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;


public class ServingLayerRunner {

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

        String inputPath = "hdfs://hadoop-master:9000/try/health_messages_csv/" + filePath + ".csv";
        String outputPath = "/home/hadoop/Health-Monitoring-System/Back-end/batchViews/" + filePath;

        if (Files.exists(java.nio.file.Path.of(outputPath)))
            FileUtils.deleteDirectory(new File(outputPath));


        Job job = Job.getInstance(conf, "Batch View Creation");
        job.setJarByClass(ServingLayerRunner.class);
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