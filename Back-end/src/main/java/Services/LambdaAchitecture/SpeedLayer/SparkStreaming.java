package Services.LambdaAchitecture.SpeedLayer;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

public class SparkStreaming{
    public static void main(String[] args) throws InterruptedException {
        Logger.getLogger("org.apache").setLevel(Level.WARN);
        SparkConf conf = new SparkConf().setAppName("sparkApp").setMaster("local[*]");
        JavaStreamingContext jssc  = new JavaStreamingContext(conf, Durations.seconds(60));
        JavaReceiverInputDStream<String> data = jssc.socketTextStream("localhost", 9999);
        data.print();
        jssc.start();
        jssc.awaitTermination();
        jssc.stop();
    }
}