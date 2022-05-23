package Services.SeparateServices.Mappers;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class CPUMapper extends Mapper<Object, Text, Text, DoubleWritable>{

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