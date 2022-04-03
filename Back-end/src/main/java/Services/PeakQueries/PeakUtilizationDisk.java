package Services.PeakQueries;

import Services.Mappers.DiskMapper;
import Services.Reducers.PeakReducer;
import Services.Proxy.WindowFilter;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class PeakUtilizationDisk {

    public void calculate(String start, String end) throws Exception {

        Configuration conf = new Configuration();
        conf.set("start_date", start);
        conf.set("end_date", end);

        String inputPath = "hdfs://hadoop-master:9000/input";
        String outputPath = "hdfs://hadoop-master:9000/output/PeakDisk"+ start + end + ".txt";

        Job job = Job.getInstance(conf, "Peak Disk Utilization");
        job.setJarByClass(PeakUtilizationDisk.class);
        job.setMapperClass(DiskMapper.class);
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
