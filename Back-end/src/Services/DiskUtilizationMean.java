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

    public static class TokenizerMapper extends Mapper<Object, Text, Text, FloatWritable>{


        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException{

            String line = value.toString();
            String[] tuple = line.split("\\n");
            JSONParser jsonParser = new JSONParser();
            String service;
            JSONObject disk;
            float free;
            float total;
            try{
                for (String s : tuple) {
                    JSONObject obj = (JSONObject) jsonParser.parse(s);
                    service = obj.getAsString("ServiceName");

                    disk = (JSONObject)obj.get("Disk");
                    free = (float) disk.getAsNumber("Free");
                    total = (float) disk.getAsNumber("Total");
                    context.write(new Text(service), new FloatWritable((total - free)/total));
                }
            }
            catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public static class DiskUtilizationReducer extends Reducer<Text, FloatWritable, Text, FloatWritable> {

        public void reduce(Text key, Iterable<FloatWritable> values, Context context) throws IOException, InterruptedException {
            float sum = 0;
            int i = 0;

            for (FloatWritable val : values) {
                sum += val.get();
                i++;
            }
            FloatWritable mean = new FloatWritable(sum/i);
            context.write(key, mean);
        }
    }

    public void calculateDiskMean(String inputPath, String outputPath) throws IOException {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "Mean Disk Utilization");
        job.setJarByClass(DiskUtilizationMean.class);
        job.setMapperClass(DiskUtilizationMean.TokenizerMapper.class);
        job.setCombinerClass(DiskUtilizationMean.DiskUtilizationReducer.class);
        job.setReducerClass(DiskUtilizationMean.DiskUtilizationReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(FloatWritable.class);
        FileInputFormat.addInputPath(job, new Path(inputPath));
        FileOutputFormat.setOutputPath(job, new Path(outputPath));
    }
}
