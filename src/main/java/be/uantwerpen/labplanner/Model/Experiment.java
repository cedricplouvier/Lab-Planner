package be.uantwerpen.labplanner.Model;

import be.uantwerpen.labplanner.common.model.users.User;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

/*
Experiment
    - fixed experiment
        - wheel tracking experiment:
            relatively easy experiment with not to many devices and not so strict boundary conditions.
        - ITS-R experiment:
            more complex experiment
    - custom experiment
        it is basically a step by step booking of different devices.
        For custom experiments, the same boundary conditions apply as
        for the fixed experiments (except for the continuity).
*/
@Entity
public class Experiment extends AbstractPersistable<Long> {




    @ManyToOne
    @JoinColumn(name = "user",unique = true)
    private User user;
    @OneToOne
    @JoinColumn(name = "experimentType",nullable = false)
    private ExperimentType experimentType;

    @Column(name = "experimentname",unique = true, nullable = false)
    private String experimentname;
    @OneToOne
    @JoinColumn(name = "expMixture")
    private Mixture mixture;
    @Column(name = "expMixtureComment")
    private String mixtureComment;



    @Column(name = "startDate")
    private String startDate;
    @Column(name = "endDate")
    private String endDate;

    public Experiment() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ExperimentType getExperimentType() {
        return experimentType;
    }

    public void setExperimentType(ExperimentType experimentType) {
        this.experimentType = experimentType;
    }

    public String getExperimentname() {
        return experimentname;
    }

    public void setExperimentname(String experimentname) {
        this.experimentname = experimentname;
    }
    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
    public Mixture getMixture() {
        return mixture;
    }

    public void setMixture(Mixture mixture) {
        this.mixture = mixture;
    }

    public String getMixtureComment() {
        return mixtureComment;
    }

    public void setMixtureComment(String mixtureComment) {
        this.mixtureComment = mixtureComment;
    }
}
