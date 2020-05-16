package ija.ija2019.traffic.maps;

public class Timetable {
    private Stop stop;
    private String time;

    private Timetable(){}

    public Stop getStop() {
        return stop;
    }

    public void setStop(Stop stop) {
        this.stop = stop;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
