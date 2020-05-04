package be.uantwerpen.labplanner.Model;


import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

@Entity
public class OfficeHours extends AbstractPersistable<Long> {
    @Column(name = "startMinute")
    private int startMinute;
    @Column(name = "startHour")
    private int startHour;
    @Column(name = "endMinute")
    private int endMinute;
    @Column(name = "endHour")
    private int endHour;


    public OfficeHours(int startMinute, int startHour, int endMinute, int endHour) {
        this.startMinute = startMinute;
        this.startHour = startHour;
        this.endMinute = endMinute;
        this.endHour = endHour;
    }

    public OfficeHours() {
        this.startHour = 9;
        this.startMinute = 0;
        this.endHour = 17;
        this.endMinute = 0;
    }

    public int getStartMinute() {
        return startMinute;
    }

    public void setStartMinute(int startMinute) {
        if (startMinute < 0) {
            this.startMinute = 0;
        } else if (startMinute > 59) {
            this.startMinute = 59;
        } else {
            this.startMinute = startMinute;
        }
    }

    public int getStartHour() {
        return startHour;
    }

    public void setStartHour(int startHour) {
        if (startHour < 0) {
            this.startHour = 0;
        } else if (startHour > 23) {
            this.startHour = 23;
        } else {
            this.startHour = startHour;
        }
    }

    public int getEndHour() {
        return endHour;
    }

    public void setEndHour(int endHour) {
        if (endHour < 0) {
            this.endHour = 0;
        } else if (endHour > 23) {
            this.endHour = 23;
        } else {
            this.endHour = endHour;
        }
    }

    public int getEndMinute() {
        return endMinute;
    }

    public void setEndMinute(int endMinute) {
        if (endMinute < 0) {
            this.endMinute = 0;
        } else if (endMinute > 59) {
            this.endMinute = 59;
        } else {
            this.endMinute = endMinute;
        }
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
