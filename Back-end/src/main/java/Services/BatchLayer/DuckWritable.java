package Services.BatchLayer;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapred.lib.db.DBWritable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class DuckWritable implements Writable, DBWritable {

    private int minute;                         // 4 Bytes * 3 = 12B
    private int service;
    private int messageCount = 0;
    private double cpuUtilizationMean = 0;      // 8 Bytes * 6 = 48 + 12 = 60B
    private double diskUtilizationMean = 0;
    private double ramUtilizationMean = 0;
    private double cpuUtilizationPeak = 0;
    private double diskUtilizationPeak = 0;
    private double ramUtilizationPeak = 0;

    public DuckWritable(DayRecord record){
        this.cpuUtilizationMean = record.getCpuUtilizationMean();
        this.diskUtilizationMean = record.getDiskUtilizationMean();
        this.minute = record.getMinute();
        this.service = record.getService();
        this.messageCount = record.getMessageCount();
        this.cpuUtilizationMean = record.getCpuUtilizationMean();
        this.diskUtilizationMean = record.getDiskUtilizationMean();
        this.ramUtilizationMean = record.getRamUtilizationMean();
        this.cpuUtilizationPeak = record.getCpuUtilizationPeak();
        this.diskUtilizationPeak = record.getDiskUtilizationPeak();
        this.ramUtilizationPeak = record.getRamUtilizationPeak();

    }

    public void write(DataOutput out) throws IOException {
        out.writeInt(minute);
        out.writeInt(service);
        out.writeInt(messageCount);
        out.writeDouble(cpuUtilizationMean);
        out.writeDouble(diskUtilizationMean);
        out.writeDouble(ramUtilizationMean);
        out.writeDouble(cpuUtilizationPeak);
        out.writeDouble(diskUtilizationPeak);
        out.writeDouble(ramUtilizationPeak);
    }

    public void readFields(DataInput in) throws IOException {
        minute = in.readInt();
        service = in.readInt();
        messageCount = in.readInt();
        cpuUtilizationMean = in.readDouble();
        diskUtilizationMean = in.readDouble();
        ramUtilizationMean = in.readDouble();
        cpuUtilizationPeak = in.readDouble();
        diskUtilizationPeak = in.readDouble();
        ramUtilizationPeak = in.readDouble();
    }

    public void write(PreparedStatement statement) throws SQLException {
        statement.setInt(1, minute);
        statement.setInt(2, service);
        statement.setInt(3, messageCount);
        statement.setDouble(4, cpuUtilizationMean);
        statement.setDouble(5, diskUtilizationMean);
        statement.setDouble(6, ramUtilizationMean);
        statement.setDouble(7, cpuUtilizationPeak);
        statement.setDouble(8, diskUtilizationPeak);
        statement.setDouble(9, ramUtilizationPeak);
    }

    public void readFields(ResultSet resultSet) throws SQLException {
        minute = resultSet.getInt(1);
        service = resultSet.getInt(2);
        messageCount = resultSet.getInt(3);
        cpuUtilizationMean = resultSet.getInt(4);
        diskUtilizationMean = resultSet.getInt(5);
        ramUtilizationMean = resultSet.getInt(6);
        cpuUtilizationPeak = resultSet.getInt(7);
        diskUtilizationPeak = resultSet.getInt(8);
        ramUtilizationPeak = resultSet.getInt(9);
    }
}
