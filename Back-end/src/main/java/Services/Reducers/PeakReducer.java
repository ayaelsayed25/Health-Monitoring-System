package Services.Reducers;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class PeakReducer  extends Reducer<Text, DoubleWritable, Text, DoubleWritable> {

    public void reduce(Text key, Iterable<DoubleWritable> values, Context context) throws IOException, InterruptedException {
        double max = 0;

        for (DoubleWritable val : values) {
            if(max < val.get())
                max = val.get();
        }
        context.write(key, new DoubleWritable(max));
    }
}
