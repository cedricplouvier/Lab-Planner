package be.uantwerpen.labplanner.Model;

import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class Continuity extends AbstractPersistable<Long> {


    @Column(name = "hours")
    private int hours;
    @Column(name = "minutes")
    private int minutes;
    
    @Column(name = "type")
    private String type;
    @Column(name = "directionType")
    private String directionType    ;

    public Continuity() {
        type = "No";
        hours = 0;
        minutes = 0;
        directionType = "After";
    }

    public Continuity(int hours, int minutes, String type, String directionType) {
        this.hours = hours;
        this.minutes = minutes;
        this.type = type;
        this.directionType = directionType;
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

    public String getDirectionType() {
        return directionType;
    }

    public void setDirectionType(String directionType) {
        this.directionType = directionType;
    }

    @Override
    public Long getId() {
        return super.getId();
    }

    @Override
    protected void setId(Long id) {
        super.setId(id);
    }
}
