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


public class PeakUtilizationRAM {

    public static class Mapper extends Mapper<Object, Text, Text, DoubleWritable>{


        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException{

            String line = value.toString();
            String[] tuple = line.split("\\n");
            JSONParser jsonParser = new JSONParser();
            String service;
            JSONObject RAM;
            double free;
            double total;
            try{
                for (String s : tuple) {
                    JSONObject obj = (JSONObject) jsonParser.parse(s);
                    service = obj.getAsString("ServiceName");

                    RAM = (JSONObject)obj.get("RAM");
                    free = (double) RAM.getAsNumber("Free");
                    total = (double) RAM.getAsNumber("Total");
                    context.write(new Text(service), new DoubleWritable((total - free)/total));
                }
            }
            catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public static class PeakUtilizationRAMReducer extends Reducer<Text, DoubleWritable, Text, DoubleWritable> {

        public void reduce(Text key, Iterable<DoubleWritable> values, Context context) throws IOException, InterruptedException {
            double max = 0;
            for (DoubleWritable val : values) {
                if(max < val.get())
                    max = val.get();
            }
            context.write(key, new DoubleWritable(max));
        }
    }

    public void calculateRAMMean(String inputPath, String outputPath) throws IOException {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "Peak Utilization RAM");
        job.setJarByClass(PeakUtilizationRAM.class);
        job.setMapperClass(Mapper.class);
        job.setCombinerClass(PeakUtilizationRAMReducer.class);
        job.setReducerClass(PeakUtilizationRAMReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(DoubleWritable.class);
        FileInputFormat.addInputPath(job, new Path(inputPath));
        FileOutputFormat.setOutputPath(job, new Path(outputPath));
    }
}
