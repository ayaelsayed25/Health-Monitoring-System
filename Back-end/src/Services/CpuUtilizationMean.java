package Services;

import java.io.IOException;

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


public class CpuUtilizationMean {

    public static class TokenizerMapper extends Mapper<Object, Text, Text, FloatWritable>{


        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException{

            String line = value.toString();
            String[] tuple = line.split("\\n");
            JSONParser jsonParser = new JSONParser();
            String service;
            float cpu;
            try{
                for (String s : tuple) {
                    JSONObject obj = (JSONObject) jsonParser.parse(s);
                    service = obj.getAsString("ServiceName");
                    cpu = (Float) obj.getAsNumber("CPU");
                    context.write(new Text(service), new FloatWritable(cpu));
                }
            }
            catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public static class CpuUtilizationReducer extends Reducer<Text, FloatWritable, Text, FloatWritable> {

        public void reduce(Text key, Iterable<FloatWritable> values, Context context) throws IOException, InterruptedException {
            float sum = 0;
            int i = 0;
            JSONObject obj = new JSONObject();

            for (FloatWritable val : values) {
                sum += val.get();
                i++;
            }
            FloatWritable mean = new FloatWritable(sum/i);
            context.write(key, mean);
        }
    }

    public void calculateCpuMean(String inputPath, String outputPath) throws IOException {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "Mean CPU Utilization");
        job.setJarByClass(CpuUtilizationMean.class);
        job.setMapperClass(TokenizerMapper.class);
        job.setCombinerClass(CpuUtilizationReducer.class);
        job.setReducerClass(CpuUtilizationReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(FloatWritable.class);
        FileInputFormat.addInputPath(job, new Path(inputPath));
        FileOutputFormat.setOutputPath(job, new Path(outputPath));
    }
}
