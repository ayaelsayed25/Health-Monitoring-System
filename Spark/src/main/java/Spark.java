import org.apache.spark.sql.*;
import org.apache.spark.sql.streaming.OutputMode;
import org.apache.spark.sql.streaming.StreamingQuery;
import org.apache.spark.sql.streaming.Trigger;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;

import java.util.regex.Pattern;

import static org.apache.spark.sql.functions.*;

public final class Spark {
    private static final Pattern SPACE = Pattern.compile(" ");

    public static void main(String[] args) throws Exception {
        //prepare spark session
        SparkSession spark = SparkSession
                .builder()
                .appName("Speed Layer")
                .master("local[*]")
                .config("spark.driver.bindAddress", "127.0.0.1")
                .getOrCreate();

        //create json schema
        StructType RAM = new StructType()
                .add("Total", DataTypes.FloatType)
                .add("Free", DataTypes.FloatType);
        StructType Disk = new StructType()
                .add("Total", DataTypes.FloatType)
                .add("Free", DataTypes.FloatType);

        StructType schema = new StructType()
                .add("serviceName", DataTypes.StringType)
                .add("Timestamp", DataTypes.TimestampType)
                .add("CPU", DataTypes.FloatType)
                .add("RAM", RAM)
                .add("Disk", Disk);

        //read stream from json file
        Dataset<Row> rawData = spark.readStream()
                .format("json")
                .schema(schema)
                .json("/home/sarah/Documents/streaming_dir");

        StreamingQuery query = rawData.writeStream().queryName("stream")
                .outputMode(OutputMode.Append())
                .format("memory")
                .trigger(Trigger.ProcessingTime(1_000))
                .start();

       Dataset<Row> data = spark.sql("SELECT * FROM stream");
        //create realtime view
        Dataset<Row> rawData2 = data.select(col("serviceName"), col("Timestamp"), col("CPU"),col("RAM.*"),
                col("Disk.*"));
        Dataset<Row> flattenedDS = rawData2.toDF("serviceName", "Timestamp", "CPU", "Total RAM","Free RAM","Total Disk","Free Disk");
        final Column col_1 = functions.coalesce(flattenedDS.col("Total RAM"), functions.lit(0));
        final Column col_2 = functions.coalesce(flattenedDS.col("Free RAM"), functions.lit(0));
        final Column col_3 = functions.coalesce(flattenedDS.col("Total Disk"), functions.lit(0));
        final Column col_4 = functions.coalesce(flattenedDS.col("Free Disk"), functions.lit(0));
        Column ram_diff =  functions.abs(col_1.minus(col_2));
        Column disk_diff =  functions.abs(col_3.minus(col_4));
        Column utilization_ram = functions.abs(ram_diff.divide(col_1));
        Column utilization_disk = functions.abs(disk_diff.divide(col_3));
        flattenedDS = flattenedDS.withColumn("RAM Utilization", utilization_ram);
        flattenedDS = flattenedDS.withColumn("Disk Utilization", utilization_disk);
        flattenedDS = flattenedDS.drop("Total RAM", "Free RAM", "Total Disk", "Free Disk");


        Dataset<Row> intervalsDS = flattenedDS
                .withColumn("time", current_timestamp())
                .withWatermark("time", "10 minutes")
                .groupBy(window(flattenedDS.col("Timestamp"), "1 minutes"), flattenedDS.col("serviceName"))
                .agg(count("*").alias("Message_Count"),
                        avg("CPU").alias("CPU_Utilization_Mean"),
                        avg("RAM Utilization").alias("RAM_Utilization_Mean"),
                        avg("Disk Utilization").alias("Disk_Utilization_Mean"),
                        max("CPU").alias("CPU_Utilization_Peak"),
                        max("RAM Utilization").alias("RAM_Utilization_Peak"),
                        max("Disk Utilization").alias("Disk_Utilization_Peak"))
                .orderBy("window");



        final String parquetFile = "test.parquet";
        final String codec = "parquet";

        intervalsDS.write().format(codec).mode(SaveMode.Append).save(parquetFile);
        query.awaitTermination();

//        intervalsDS.write().mode(SaveMode.Append).parquet("mama/hello.parquet");

        //write stream in parquet
//        intervalsDS.writeStream()
////                .format("parquet")
////                .option("path","parquets")
////                .option("checkpointLocation","checkpoints")
//                .format("console")
//                .outputMode("append")
//                .trigger(ProcessingTime(1_000))
////                .outputMode(OutputMode.Append())
//////                .option("truncate", "false")
//                .start()
//                .awaitTermination();
//        spark.close();





    }

}