package UDP;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.sql.Timestamp;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.json.simple.JSONObject;
import System.PerformanceMonitor;

public class Client
{
    final static int PORT = 8080;
    static InetAddress ip;

    static {
        try {
            ip = InetAddress.getByName("localhost");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) throws IOException
    {
        ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);
        exec.scheduleAtFixedRate(new Runnable() {
            public void run() {
                try {
                    sendMessage();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 0, 2, TimeUnit.SECONDS);

    }
    public static void sendMessage() throws Exception {
        byte buf[] = getMessage();
        System.out.println("length : " + buf.length);
        DatagramSocket ds = new DatagramSocket();
        DatagramPacket DpSend = new DatagramPacket(buf, buf.length, ip, PORT);
        ds.send(DpSend);
    }
    public static byte[] getMessage() throws Exception {
        JSONObject obj=new JSONObject();
        PerformanceMonitor monitor = new PerformanceMonitor();
        obj.put("ServiceName", "Client 1");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        obj.put("Timestamp", timestamp);
        obj.put("CPU", monitor.getCpuUsage());
        JSONObject ram =new JSONObject();
        ram.put("Total", monitor.getTotalMemorySize());
        ram.put("Free", monitor.getFreeMemorySize());
        obj.put("RAM", ram);
        JSONObject disk =new JSONObject();
        disk.put("Total", monitor.getTotalDiskSpace());
        disk.put("Free", monitor.getFreeDiskSpace());
        obj.put("Disk", disk);
        return obj.toString().getBytes("utf-8");
    }
}
