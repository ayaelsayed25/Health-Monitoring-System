package Services.SeparateServices.Mappers;


import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class CountMapper extends Mapper<Object, Text, Text, IntWritable> {

    private final static IntWritable one = new IntWritable(1);

    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException{
        String line = value.toString();
        String[] tuple = line.split("\\n");
        JSONParser jsonParser = new JSONParser();
        String service;
        try{
            for (String s : tuple) {
                JSONObject obj = (JSONObject) jsonParser.parse(s);
                service = obj.getAsString("serviceName");
                context.write(new Text(service), one);
            }
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
    }
}