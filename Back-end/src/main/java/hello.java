import Services.PeakQueries.PeakUtilizationCpu;

public class hello {
    public static void main(String[] args) throws Exception {
        PeakUtilizationCpu utilizationCpu = new PeakUtilizationCpu();
        utilizationCpu.calculate("01-01-2023", "01-01-2023");

    }
}
