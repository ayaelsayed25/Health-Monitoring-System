package Services.Reducers;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class MeanReducer  extends Reducer<Text, DoubleWritable, Text, DoubleWritable> {

    public void reduce(Text key, Iterable<DoubleWritable> values, Context context) throws IOException, InterruptedException {
        double sum = 0;
        int i = 0;

        for (DoubleWritable val : values) {
            sum += val.get();
            i++;
        }
        DoubleWritable mean = new DoubleWritable(sum / i);
        context.write(key, mean);
    }
}