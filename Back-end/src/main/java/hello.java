import Services.PeakQueries.PeakUtilizationCpu;

public class hello {
    public static void main(String[] args) throws Exception {
        PeakUtilizationCpu utilizationCpu = new PeakUtilizationCpu();
        utilizationCpu.calculate("17-05-2023", "18-05-2023");
    }
}

