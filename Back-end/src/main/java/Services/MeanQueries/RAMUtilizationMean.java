package Services.MeanQueries;

import Services.Mappers.RAMMapper;
import Services.Reducers.MeanReducer;
import Services.Proxy.WindowFilter;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


public class RAMUtilizationMean {

    public void calculate(String start, String end) throws Exception {

        Configuration conf = new Configuration();
        conf.setIfUnset("start_date", start);
        conf.setIfUnset("end_date", end);

        String inputPath = "hdfs://hadoop-master:9000/data/processed";
        String outputPath = "hdfs://hadoop-master:9000/output/MeanRAM"+ start + end + ".log";

        Job job = Job.getInstance(conf, "Mean RAM Utilization");
        job.setJarByClass(RAMUtilizationMean.class);
        job.setMapperClass(RAMMapper.class);
        job.setCombinerClass(MeanReducer.class);
        job.setReducerClass(MeanReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(DoubleWritable.class);

        FileInputFormat.setInputPathFilter(job, WindowFilter.class);
        FileInputFormat.addInputPath(job, new Path(inputPath));
        FileOutputFormat.setOutputPath(job, new Path(outputPath));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
