package Services;

import net.minidev.json.JSONObject;

import java.io.IOException;

public class QueryFactory {

    /*
        0 -> The mean CPU utilization for each service
        1 -> The mean Disk utilization for each service
        2 -> The mean RAM utilization for each service
        3 -> The peak time of utilization for each resource for each service
        4 -> The count of health messages received for each service
     */

    public JSONObject createQuery(int id){
        switch (id){
            case 0:
                CpuUtilizationMean utilizationMean = new CpuUtilizationMean();
//                    utilizationMean.calculateCpuMean("", "");
                break;
            case 1:
//                try {
                    DiskUtilizationMean UtilizationMean = new DiskUtilizationMean();
//                    UtilizationMean.calculateDiskMean("", "");
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                break;
            case 2:
                RAMUtilizationMean ramUtilizationMean = new RAMUtilizationMean();
//                    ramUtilizationMean("", "");
                break;
        }
        return null;
    }
}
