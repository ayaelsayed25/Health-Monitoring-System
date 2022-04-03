package Services.Proxy;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathFilter;

import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WindowFilter  extends Configured implements PathFilter {


    Configuration conf;
    FileSystem fs ;


    @Override
    public boolean accept(Path path) {
        String f = path.toString();
        if (f.substring(f.length() - 4).compareTo(".log") != 0) {
            return true;
        }

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

        String fileName = f.substring(f.length() - 14, f.length() - 4);

        Date start = null;
        Date end = null;
        Date file = null;
        try {
            start = format.parse(conf.get("start_date"));
            end = format.parse(conf.get("end_date"));
            file = format.parse(fileName);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        assert end != null;
        return end.compareTo(file) >= 0 && start.compareTo(file) <= 0;

    }

    @Override
    public void setConf(Configuration conf) {
        this.conf = conf;
        if (conf != null) {
            try {
                fs = FileSystem.get(conf);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
