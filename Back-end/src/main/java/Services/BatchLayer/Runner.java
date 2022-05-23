package Services.BatchLayer;

import Services.SeparateServices.Proxy.WindowFilter;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.ArrayWritable;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Runner {

    public void jobRun () throws IOException, InterruptedException, ClassNotFoundException, SQLException {
//        Class.forName("org.duckdb.DuckDBDriver");

        Configuration conf = new Configuration();
//        Connection conn = DriverManager.getConnection("jdbc:duckdb:");
        conf.setIfUnset("start_date", "01-01-2023");
        conf.setIfUnset("end_date", "01-01-2023");
        String inputPath = "hdfs://hadoop-master:9000/processed";
        String outputPath = "hdfs://hadoop-master:9000/1.log";


        Job job = Job.getInstance(conf, "Mean CPU Utilization");
        job.setJarByClass(Runner.class);
        job.setMapperClass(Mapper.class);
        job.setCombinerClass(Reducer.class);
        job.setReducerClass(Reducer.class);
        job.setMapOutputKeyClass(KeyWritable.class);
        job.setMapOutputValueClass(ArrayWritable.class);
        job.setOutputKeyClass(KeyWritable.class);
        job.setOutputValueClass(ArrayWritable.class);

        FileInputFormat.setInputPathFilter(job, WindowFilter.class);
        FileInputFormat.addInputPath(job, new Path(inputPath));
        FileOutputFormat.setOutputPath(job, new Path(outputPath));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
