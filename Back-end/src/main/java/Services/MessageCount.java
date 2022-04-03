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


public class MessageCount {

    public static class Mapper extends Mapper<Object, Text, Text, IntWritable>{

        private final static IntWritable one = new IntWritable(1);
        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException{
            String line = value.toString();
            String[] tuple = line.split("\\n");
            JSONParser jsonParser = new JSONParser();
            String service;
            try{
                for (String s : tuple) {
                    JSONObject obj = (JSONObject) jsonParser.parse(s);
                    service = obj.getAsString("ServiceName")
                    context.write(new Text(service), one);
                }
            }
            catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public static class MessageCountReducer extends Reducer<Text,IntWritable,Text,IntWritable> {

        public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable val : values) {
                sum += val.get();
            }
            context.write(key, new IntWritable(sum);
        }
    }

    public void calculateMessageCount(String inputPath, String outputPath) throws IOException {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "Message Count");
        job.setJarByClass(MessageCount.class);
        job.setMapperClass(Mapper.class);
        job.setCombinerClass(MessageCountReducer.class);
        job.setReducerClass(MessageCountReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        FileInputFormat.addInputPath(job, new Path(inputPath));
        FileOutputFormat.setOutputPath(job, new Path(outputPath));
    }
}
