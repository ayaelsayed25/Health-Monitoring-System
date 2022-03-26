package UDP;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.conf.Configuration;

import java.io.*;
import java.net.URI;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.util.Progressable;

import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

import static java.lang.System.out;

public class aloo {


    public static void main(String[] args) throws IOException, URISyntaxException {


            Configuration configuration = new Configuration();
            configuration.set("fs.defaultFS", "hdfs://206.189.108.184");
            FileSystem fileSystem = FileSystem.get(configuration);
            //Create a path
            String fileName = "read_write_hdfs_example.txt";
            Path hdfsWritePath = new Path("/user/javadeveloperzone/javareadwriteexample/" + fileName);
            FSDataOutputStream fsDataOutputStream = fileSystem.create(hdfsWritePath,true);
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fsDataOutputStream, StandardCharsets.UTF_8));
            bufferedWriter.write("Java API to write data in HDFS");
            bufferedWriter.newLine();
            bufferedWriter.close();
            fileSystem.close();

            //Source file in the local file system

    }

}
