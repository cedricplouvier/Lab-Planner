package be.uantwerpen.labplanner.Model;

import be.uantwerpen.labplanner.common.model.users.User;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import java.util.List;
@Entity
public class Experiment extends AbstractPersistable<Long> {


    @ManyToOne
    @JoinColumn(name = "user")
    private User user;

    @OneToOne
    @JoinColumn(name = "experimentType")
    private ExperimentType experimentType;

    @Column(name = "experimentname", unique = true, nullable = false)
    private String experimentname;

    @OneToOne
    @JoinColumn(name = "expMixture")
    private Mixture mixture;

    @Column(name = "expMixtureComment")
    private String mixtureComment;

    @Column(name = "expmixtureAmount")
    private int mixtureAmount;

    @OneToMany
    @JoinColumn(name = "steps")
    private List<Step> steps;

    @Column(name = "startDate")
    private String startDate;

    @Column(name = "endDate")
    private String endDate;

    //constructor used to create experiment with ID
    public Experiment(Experiment experiment) {
        this.experimentType = experiment.experimentType;
        this.steps = experiment.steps;
        this.user = experiment.user;
        this.experimentname = experiment.experimentname;
        this.mixtureAmount = experiment.mixtureAmount;
        this.mixture = experiment.mixture;
        this.mixtureComment = experiment.mixtureComment;
        this.startDate = experiment.startDate;
        this.endDate = experiment.endDate;
    }

    @Override
    public Long getId() {
        return super.getId();
    }

    @Override
    public void setId(Long id) {
        super.setId(id);
    }


    public Experiment() {
    }

    public Experiment(ExperimentType experimentType, List<Step> steps, User user, String experimentname, Mixture mixture, String mixtureComment,int mixtureAmount, String startDate, String endDate) {
       this.experimentType = experimentType;
        this.steps = steps;
        this.user = user;
        this.experimentname = experimentname;
        this.mixtureAmount = mixtureAmount;
        this.mixture = mixture;
        this.mixtureComment = mixtureComment;
        this.startDate = startDate;
        this.endDate = endDate;
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

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public int getMixtureAmount() {
        return mixtureAmount;
    }

    public void setMixtureAmount(int mixtureAmount) {
        this.mixtureAmount = mixtureAmount;
    }

}
