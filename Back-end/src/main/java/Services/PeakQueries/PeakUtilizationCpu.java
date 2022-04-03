package Services.PeakQueries;

import Services.Mappers.CPUMapper;
import Services.Reducers.PeakReducer;
import Services.Proxy.WindowFilter;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;



public class PeakUtilizationCpu {

    public void calculate(String start, String end) throws Exception {

        Configuration conf = new Configuration();
        conf.set("start_date", start);
        conf.set("end_date", end);

        String inputPath = "hdfs://hadoop-master:9000/input";
        String outputPath = "hdfs://hadoop-master:9000/output/PeakCPU"+ start + end + ".log";

        Job job = Job.getInstance(conf, "Peak CPU Utilization");
        job.setJarByClass(PeakUtilizationCpu.class);
        job.setMapperClass(CPUMapper.class);
        job.setCombinerClass(PeakReducer.class);
        job.setReducerClass(PeakReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(DoubleWritable.class);

        FileInputFormat.setInputPathFilter(job, WindowFilter.class);
        FileInputFormat.addInputPath(job, new Path(inputPath));
        FileOutputFormat.setOutputPath(job, new Path(outputPath));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
