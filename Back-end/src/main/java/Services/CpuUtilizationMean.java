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

    public static class TokenizerMapper extends Mapper<Object, Text, Text, DoubleWritable>{


        public void map(Object key, Text value, Context context) throws IOException, InterruptedException{

            String line = value.toString();
            String[] tuple = line.split("\\n");
            JSONParser jsonParser = new JSONParser();
            String service;
            double cpu;
            try{
                for (String s : tuple) {
                    JSONObject obj = (JSONObject) jsonParser.parse(s);
                    service = obj.getAsString("serviceName");
                    cpu = obj.getAsNumber("CPU").doubleValue();
                    context.write(new Text(service), new DoubleWritable(cpu));
                }
            }
            catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }


    public static class CpuUtilizationReducer extends Reducer<Text, DoubleWritable, Text, DoubleWritable> {

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
        Job job = Job.getInstance(conf, "Mean CPU Utilization");
        job.setJarByClass(CpuUtilizationMean.class);
        job.setMapperClass(TokenizerMapper.class);
        job.setCombinerClass(CpuUtilizationReducer.class);
        job.setReducerClass(CpuUtilizationReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(DoubleWritable.class);

        FileInputFormat.addInputPath(job, new Path("hdfs://hadoop-master:9000/input"));
        FileOutputFormat.setOutputPath(job, new Path("hdfs://hadoop-master:9000/output"));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}

