package be.uantwerpen.labplanner.Model;


public class Timeslot {

    private TimePoint start;
    private TimePoint end;

    public Timeslot(){}

    public Timeslot(TimePoint start, TimePoint end){
        this.start = start;
        this.end = end;
    }

    public TimePoint getStart() {
        return start;
    }

    public void setStart(TimePoint start) {
        this.start = start;
    }

    public TimePoint getEnd() {
        return end;
    }

    public void setEnd(TimePoint end) {
        this.end = end;
    }
}
