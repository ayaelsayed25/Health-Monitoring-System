//package Controllers;
//
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.util.*;
//public class UserService {
//
//    @ResponseBody
//    public Response processQuery(Job job)
//    {
////        { 'Id': 'query1', query: 'The mean-CPU utilization for each service' },
////        { 'Id': 'query2', query: 'The mean Disk utilization for each service' },
////        { 'Id': 'query3', query: 'The mean RAM utilization for each service' },
////        { 'Id': 'query4', query: 'The peak time of utilization for CPU' },
////        { Id: 'query5', query: 'The peak time of utilization for RAM' },
////        { Id: 'query6', query: 'The peak time of utilization for Disk' },
////        { Id: 'query7', query: 'The count of health messages received for each service' }
//        String q = "";
//        switch(job.Query)
//        {
//            case "The mean RAM utilization for each service":
//                q = "MeanRAM";
//                break;
//            case "The mean-CPU utilization for each service":
//                q = "MeanCPU";
//                break;
//            case "The mean Disk utilization for each service":
//                q = "MeanDisk";
//                break;
//            case "The peak time of utilization for CPU":
//                q = "PeakCPU";
//                break;
//            case "The peak time of utilization for RAM":
//                q = "PeakRAM";
//                break;
//            case "The peak time of utilization for Disk":
//                q = "PeakDisk";
//                break;
//            case "The count of health messages received for each service":
//                q = "MessageCount";
//                break;
//        }
//        String file = "hdfs://hadoop-master:9000/output/"+ q + job.Start + job.End + ".txt";
//        ArrayList<String[]> list = new ArrayList<String[]>();
//        try
//        {
//            FileInputStream fis=new FileInputStream(file);
//            Scanner sc=new Scanner(fis);    //file to be scanned
//            while(sc.hasNextLine())
//            {
//                String l = sc.nextLine();
//                String[] line = l.split("\t");
//                list.add(line);
//            }
//            sc.close();
//        }
//        catch(IOException e)
//        {
//            e.printStackTrace();
//        }
//        ServiceInfo[] arr = new ServiceInfo[l.size()];
//        int i = 0;
//        for(l : list) {
//            ServiceInfo info = new ServiceInfo(l[0], l[1]);
//            arr[i] = info;
//            i++;
//        }
//
//    }
//        return new Response(arr);
//}
//}