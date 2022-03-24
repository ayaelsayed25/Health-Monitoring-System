package System;

import com.sun.management.OperatingSystemMXBean;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.io.File;
import java.lang.management.ManagementFactory;

import static sun.management.ManagementFactoryHelper.getOperatingSystemMXBean;


public class PerformanceMonitor {
    public  double getCpuUsage() throws Exception {

        MBeanServer mbs    = ManagementFactory.getPlatformMBeanServer();
        ObjectName name    = ObjectName.getInstance("java.lang:type=OperatingSystem");
        AttributeList list = mbs.getAttributes(name, new String[]{ "SystemCpuLoad" });

        if (list.isEmpty())     return Double.NaN;

        Attribute att = (Attribute)list.get(0);
        Double value  = (Double)att.getValue();

        // usually takes a couple of seconds before we get real values
        if (value == -1.0)      return Double.NaN;
        // returns a percentage value with 1 decimal point precision
        return ((int)(value * 10) / 10.0);
    }
    public double getTotalDiskSpace()
    {
        double rs = new File("/").getTotalSpace()  /1073741824.0;
        return Math.round(rs * 100.0) / 100.0;
    }
    public double getFreeDiskSpace()
    {
        double rs = new File("/").getFreeSpace() /1073741824.0;
        return Math.round(rs * 100.0) / 100.0;
    }
    public double getTotalMemorySize()
    {
        long memorySize = ((com.sun.management.OperatingSystemMXBean) ManagementFactory
                .getOperatingSystemMXBean()).getTotalPhysicalMemorySize();
        return Math.round((memorySize /1073741824.0)* 100.0) / 100.0;
    }
    public double getFreeMemorySize()
    {
        long freeMemorySize = ((com.sun.management.OperatingSystemMXBean)
                ManagementFactory.getOperatingSystemMXBean()).getFreePhysicalMemorySize();
        return Math.round((freeMemorySize /1073741824.0 )* 100.0) / 100.0;
    }
}