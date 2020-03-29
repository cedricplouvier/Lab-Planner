package be.uantwerpen.labplanner.Model;

import be.uantwerpen.labplanner.common.model.users.User;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Step extends AbstractPersistable<Long> {
    @ManyToOne
    @JoinColumn(name = "user")
    private User user;
    @OneToOne
    @JoinColumn(name= "device",nullable = false)
    private Device device;



    /*@OneToOne
        @Column(name="timeslot",nullable = false)
        private Timeslot timeslot;*/
    @Column(name = "start",nullable = false)
    private String start;
    @Column(name = "end",nullable = false)
    private String end;



    @Column(name = "startHour", nullable = false)
    private String startHour;
    @Column(name = "endHour", nullable = false)
    private String endHour;
    @Column(name= "stepName", unique = true)
    private String stepName;



    public Step(){
    }

    public Step(User user, Device device, String start, String end, String startHour, String endHour, String stepName) {
        this.user=  user;
        this.device = device;
        this.start=start;
        this.end=end;
        this.startHour=startHour;
        this.endHour=endHour;
        this.stepName=stepName;
    }

    @Override
    public Long getId() {
        return super.getId();
    }

    @Override
    public void setId(Long id) {
        super.setId(id);
    }

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }
/*
    public Timeslot getTimeslot() {
        return timeslot;
    }

    public void setTimeslot(Timeslot timeslot) {
        this.timeslot = timeslot;
    }*/

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }
    public String getStartHour() {
        return startHour;
    }

    public void setStartHour(String startHour) {
        this.startHour = startHour;
    }

    public String getEndHour() {
        return endHour;
    }

    public void setEndHour(String endHour) {
        this.endHour = endHour;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }
}