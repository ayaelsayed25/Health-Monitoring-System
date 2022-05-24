package Services.BatchLayer;

import org.apache.avro.Schema;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.parquet.avro.AvroParquetOutputFormat;

import java.io.IOException;
import java.security.acl.Group;


public class Runner {

    private String schema = "{\n" +
                    "\"type\" : \"record\"," +
                    "\"namespace\" : \"BatchLayer\"," +
                    "\"name\" : \"Day\"," +
                    "\"fields\" : [" +
                    "{ \"name\" : \"Minute\", \"type\": \"int\" }, " +
                    "{ \"name\" : \"Service\", \"type\": \"string\" }," +
                    "{ \"name\" : \"MessageCount\", \"type\": \"int\" }," +
                    "{ \"name\" : \"CpuUtilizationMean\", \"type\": \"double\" }," +
                    "{ \"name\" : \"DiskUtilizationMean\", \"type\": \"double\" }," +
                    "{ \"name\" : \"RamUtilizationMean\", \"type\": \"double\" }," +
                    "{ \"name\" : \"CpuUtilizationPeak\", \"type\": \"double\" }," +
                    "{ \"name\" : \"DiskUtilizationPeak\", \"type\": \"double\" }," +
                    "{ \"name\" : \"RamUtilizationPeak\", \"type\": \"double\" }" +
                    "]}";
    private final Schema mySchema =  new Schema.Parser().parse(schema);

    public void jobRun () throws IOException, InterruptedException, ClassNotFoundException {


        Configuration conf = new Configuration();

        String inputPath = "hdfs://hadoop-master:9000/try/health_messages_csv/01-01-2023.csv";
        String outputPath = "hdfs://hadoop-master:9000/hiii";

        Job job = Job.getInstance(conf, "Mean CPU Utilization");
        job.setJarByClass(Runner.class);
        job.setMapperClass(Mapper.class);
//        job.setCombinerClass(Reducer.class);
        job.setReducerClass(Reducer.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        job.setOutputKeyClass(NullWritable.class);
        job.setOutputValueClass(Group.class);
        job.setOutputFormatClass(AvroParquetOutputFormat.class);
        AvroParquetOutputFormat.setSchema(job, mySchema);

        FileInputFormat.addInputPath(job, new Path(inputPath));
        FileOutputFormat.setOutputPath(job, new Path(outputPath));

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
