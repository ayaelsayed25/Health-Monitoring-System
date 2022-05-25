import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.streaming.OutputMode;
import org.apache.spark.sql.streaming.StreamingQuery;

import java.util.Arrays;
import java.util.regex.Pattern;

public final class Spark {
    private static final Pattern SPACE = Pattern.compile(" ");

    public static void main(String[] args) throws Exception {
        SparkSession spark = SparkSession
                .builder()
                .appName("JavaWordCount")
                .master("local")
                .getOrCreate();

        Dataset<Row> lines = spark
                .readStream()
                .format("socket")
                .option("host", "localhost")
                .option("port", 65432)
                .load();

//// Split the lines into words
//        Dataset<String> words = lines
//                .as(Encoders.STRING())
//                .flatMap((FlatMapFunction<String, String>) x -> Arrays.asList(x.split(" ")).iterator(), Encoders.STRING());

// Generate running word count
//        Dataset<Row> wordCounts = words.groupBy("value").count();

// Split the lines into words
        Dataset<String> words = lines
                .as(Encoders.STRING())
                .flatMap((FlatMapFunction<String, String>) x -> Arrays.asList(x.split(" ")).iterator(), Encoders.STRING());

// Generate running word count
        Dataset<Row> wordCounts = words.groupBy("value").count();

        // Start running the query that prints the running counts to the console
        StreamingQuery query = wordCounts.writeStream()
                .outputMode("complete")
                .format("console")
                .start();

//        lines.writeStream().format("parquet")
//                .option("path","tryparquet")
//                .option("checkpointLocation","checkpoint")
//                .start().awaitTermination();
//        spark.stop();

//        StreamingQuery query = wordCounts
//                .writeStream()
//                .format("parquet")
//                .option("path","tryparquet")
//                .option("checkpointLocation","checkpoint")
//                .outputMode(OutputMode.Append())
//                .start();
        query.awaitTermination();
       spark.stop();

    }
}