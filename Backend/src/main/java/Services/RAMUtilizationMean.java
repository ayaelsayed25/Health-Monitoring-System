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


public class RAMUtilizationMean {

    public static class TokenizerMapper extends Mapper<Object, Text, Text, FloatWritable>{


        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException{

            String line = value.toString();
            String[] tuple = line.split("\\n");
            JSONParser jsonParser = new JSONParser();
            String service;
            JSONObject ram;
            float free;
            float total;
            try{
                for (String s : tuple) {
                    JSONObject obj = (JSONObject) jsonParser.parse(s);
                    service = obj.getAsString("ServiceName");
                    ram = (JSONObject) obj.get("RAM");
                    free = (float) ram.getAsNumber("Free");
                    total = (float) ram.getAsNumber("Total");
                    context.write(new Text(service), new FloatWritable((total - free)/total));
                }
            }
            catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public static class RamUtilizationReducer extends Reducer<Text, FloatWritable, Text, FloatWritable> {

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

    public static void main(String[] args) throws IOException {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "Mean Ram Utilization");
        job.setJarByClass(RAMUtilizationMean.class);
        job.setMapperClass(TokenizerMapper.class);
        job.setCombinerClass(RamUtilizationReducer.class);
        job.setReducerClass(RamUtilizationReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(FloatWritable.class);

        FileInputFormat.addInputPath(job, new Path("hdfs://hadoop-master:9000/hello/"));
        FileOutputFormat.setOutputPath(job, new Path("hdfs://hadoop-master:9000/hello/"));
    }
}
