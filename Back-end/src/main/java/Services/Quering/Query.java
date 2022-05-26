package Services.Quering;

public class Query {
    private int End;
    private int Start;
    private String startDay;
    private String endDay;

    public Query(int end, int start, String startDay, String endDay) {
        this.End = end;
        this.Start = start;
        this.startDay = startDay;
        this.endDay = endDay;
    }


    public String getStartDay() {
        return startDay;
    }

    public void setStartDay(String startDay) {
        this.startDay = startDay;
    }

    public String getEndDay() {
        return endDay;
    }

    public void setEndDay(String endDay) {
        this.endDay = endDay;
    }


    public int getEnd() {
        return End;
    }

    public void setEnd(int end) {
        End = end;
    }

    public int getStart() {
        return Start;
    }

    public void setStart(int start) {
        Start = start;
    }


}