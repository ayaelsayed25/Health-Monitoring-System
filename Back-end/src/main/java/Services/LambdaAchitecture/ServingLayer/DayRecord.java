package Services.LambdaAchitecture.ServingLayer;

public class DayRecord {
    private int minute;                         // 4 Bytes * 3 = 12B
    private int service;
    private int messageCount = 0;
    private double cpuUtilizationMean = 0;      // 8 Bytes * 6 = 48 + 12 = 60B
    private double diskUtilizationMean = 0;
    private double ramUtilizationMean = 0;
    private double cpuUtilizationPeak = 0;
    private double diskUtilizationPeak = 0;
    private double ramUtilizationPeak = 0;




    public int getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        int hours = Integer.parseInt(minute.substring(0, 2)) * 60;
        int minutes = hours + Integer.parseInt(minute.substring(3));
        this.minute = minutes;
    }

    public int getService() {
        return service;
    }

    public void setService(String service) {
        this.service = Integer.parseInt(service.substring(25));
    }

    public int getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(int messageCount) {
        this.messageCount = messageCount;
    }

    public double getCpuUtilizationMean() {
        return cpuUtilizationMean;
    }

    public void setCpuUtilizationMean(double cpuUtilizationMean) {
        this.cpuUtilizationMean = cpuUtilizationMean;
    }

    public double getDiskUtilizationMean() {
        return diskUtilizationMean;
    }

    public void setDiskUtilizationMean(double diskUtilizationMean) {
        this.diskUtilizationMean = diskUtilizationMean;
    }

    public double getRamUtilizationMean() {
        return ramUtilizationMean;
    }

    public void setRamUtilizationMean(double ramUtilizationMean) {
        this.ramUtilizationMean = ramUtilizationMean;
    }

    public double getCpuUtilizationPeak() {
        return cpuUtilizationPeak;
    }

    public void setCpuUtilizationPeak(double cpuUtilizationPeak) {
        this.cpuUtilizationPeak = cpuUtilizationPeak;
    }

    public double getDiskUtilizationPeak() {
        return diskUtilizationPeak;
    }

    public void setDiskUtilizationPeak(double diskUtilizationPeak) {
        this.diskUtilizationPeak = diskUtilizationPeak;
    }

    public double getRamUtilizationPeak() {
        return ramUtilizationPeak;
    }

    public void setRamUtilizationPeak(double ramUtilizationPeak) {
        this.ramUtilizationPeak = ramUtilizationPeak;
    }
}
