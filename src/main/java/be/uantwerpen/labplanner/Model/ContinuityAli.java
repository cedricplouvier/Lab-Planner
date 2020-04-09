package be.uantwerpen.labplanner.Model;

import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class ContinuityAli extends AbstractPersistable<Long> {


    @Column(name = "hour")
    private int hour;
    @Column(name = "minutes")
    private int minutes;
    @Column(name="type")
    private String type;

    public ContinuityAli(){}
    public ContinuityAli(int hour,int minutes,String type){
        this.hour=hour;
        this.minutes=minutes;
        this.type=type;
    }
    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
