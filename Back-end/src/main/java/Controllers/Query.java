package Controllers;



public class Query {
    int End;
    int Start;
    String startDay;

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

    String endDay;

    public String getStatment() {
        return statment;
    }

    public void setStatment(String statment) {
        this.statment = statment;
    }

    String statment;

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



    public String getQuery() {
        return Query;
    }

    public void setQuery(String query) {
        Query = query;
    }

    String Query;






}