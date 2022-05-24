package Services.LambdaAchitecture.ServingLayer;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class KeyWritable implements Writable, Comparable {
    private Text dateTime;
    private Text serviceName;

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dateTime.write(dataOutput);
        serviceName.write(dataOutput);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        dateTime.readFields(dataInput);
        serviceName.readFields(dataInput);

    }

    @Override
    public String toString() {
        return dateTime + ", " + serviceName;
    }

    @Override
    public int compareTo(Object o) {
        return o.toString().compareTo(toString());
    }


    public Text getDateTime() {
        return dateTime;
    }

    public void setDateTime(Text dateTime) {
        this.dateTime = dateTime;
    }

    public Text getServiceName() {
        return serviceName;
    }

    public void setServiceName(Text serviceName) {
        this.serviceName = serviceName;
    }


}
