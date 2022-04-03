package Services;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;


public class DiskUtilizationMean {

    public static class TokenizerMapper extends Mapper<Object, Text, Text, DoubleWritable>{


        public void map(Object key, Text value, Context context) throws IOException, InterruptedException{
            String line = value.toString();
            String[] tuple = line.split("\\n");
            JSONParser jsonParser = new JSONParser();
            String service;
            JSONObject disk;
            double free;
            double total;
            try{
                for (String s : tuple) {

                    JSONObject obj = (JSONObject) jsonParser.parse(s);
                    service = obj.getAsString("serviceName");
                    disk = (JSONObject) obj.get("Disk");
                    free = disk.getAsNumber("Free").doubleValue();
                    total = disk.getAsNumber("Total").doubleValue();
                    context.write(new Text(service), new DoubleWritable((total - free)/total));
                }
            }
            catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public static class DiskUtilizationReducer extends Reducer<Text, DoubleWritable, Text, DoubleWritable> {

        public void reduce(Text key, Iterable<DoubleWritable> values, Context context) throws IOException, InterruptedException {
            double sum = 0;
            int i = 0;

            for (DoubleWritable val : values) {
                sum += val.get();
                i++;
            }
            DoubleWritable mean = new DoubleWritable(sum/i);
            context.write(key, mean);
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "Mean Disk Utilization");
        job.setJarByClass(DiskUtilizationMean.class);
        job.setMapperClass(TokenizerMapper.class);
        job.setCombinerClass(DiskUtilizationReducer.class);
        job.setReducerClass(DiskUtilizationReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(DoubleWritable.class);

        FileInputFormat.addInputPath(job, new Path("hdfs://hadoop-master:9000/hello/mama.txt"));
        FileOutputFormat.setOutputPath(job, new Path("hdfs://hadoop-master:9000/mama/"));
    }
}
