package be.uantwerpen.labplanner.Model;

import be.uantwerpen.labplanner.common.model.users.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Step extends AbstractPersistable<Long> {
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user")
    private User user;
    @OneToOne
    @JoinColumn(name= "device",nullable = false)
    private Device device;
    @Column(name = "start")
    private String start;
    @Column(name = "end")
    private String end;
    @ManyToOne
    @JoinColumn(name = "stepType")
    private StepType stepType;
    @Column(name = "startHour")
    private String startHour;
    @Column(name = "endHour")
    private String endHour;
    @Column(name = "comment")
    private String comment;

    public Step(){
    }

    public Step(User user, Device device, String start, String end, String startHour, String endHour,String comment) {
        this.user=  user;
        this.device = device;
        this.start=start;
        this.end=end;
        this.startHour=startHour;
        this.endHour=endHour;
        this.comment=comment;
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

    public StepType getStepType() {
        return stepType;
    }

    public void setStepType(StepType stepType) {
        this.stepType = stepType;
    }
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}

