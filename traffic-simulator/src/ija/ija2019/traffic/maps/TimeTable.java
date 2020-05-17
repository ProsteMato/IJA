package ija.ija2019.traffic.maps;

import java.time.LocalTime;

/**
 * This class represents TimeTable of connections
 * @version 1.0
 * @author <a href="xkocim05@stud.fit.vutbr.cz">Martin Koči</a>
 * @author <a href="xkoval17@stud.fit.vutbr.cz">Michal Koval</a>
 */
public class TimeTable {
    private Stop stop;
    private String time;

    private TimeTable(){}

    /**
     * This function returns stop
     * @return stop
     */
    public Stop getStop() {
        return stop;
    }

    /**
     * This function sets stop
     * @param stop Stop
     */
    public void setStop(Stop stop) {
        this.stop = stop;
    }

    /**
     * This method is for setting time when connection will land on stop.
     * @param time time when connection will land on stop.
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * This function gets time when connection will land on stop in String format.
     * @return time of arrive in String format.
     */
    public String getTime() {
        return time;
    }

    /**
     * This function returns time when connection will land on stop in LocalTime format
     * @return LocalTime format of time.
     */
    public LocalTime getLocalTime() {
        return LocalTime.parse(time);
    }


}
