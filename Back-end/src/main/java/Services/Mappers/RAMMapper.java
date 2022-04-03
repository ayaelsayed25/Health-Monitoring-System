package Services.Mappers;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class RAMMapper extends Mapper<Object, Text, Text, DoubleWritable> {


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