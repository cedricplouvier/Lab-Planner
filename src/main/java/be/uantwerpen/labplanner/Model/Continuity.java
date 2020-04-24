package be.uantwerpen.labplanner.Model;

import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Continuity extends AbstractPersistable<Long> {


    @Column(name = "hours")
    private int hours;
    @Column(name = "minutes")
    private int minutes;
    @Column(name="type")
    private String type;

    public Continuity(){}
    public Continuity(int hours, int minutes, String type){
        this.hours=hours;
        this.minutes=minutes;
        this.type=type;
    }
    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
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