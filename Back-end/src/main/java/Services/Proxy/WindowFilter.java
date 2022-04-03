package Services.Proxy;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathFilter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public class WindowFilter  extends Configured implements PathFilter {

    Pattern pattern;
    Configuration conf;

    @Override
    public boolean accept(Path path) {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

        String[] tokens = path.toString().split("[\\\\|/]");
        String f = tokens[tokens.length - 1];
        String fileName = f.substring(0, f.length() - 4);

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
    }
}
