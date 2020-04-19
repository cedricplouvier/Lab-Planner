package be.uantwerpen.labplanner.Model;

import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class ExperimentType extends AbstractPersistable<Long> {


    @Column(name = "expname",nullable = false)
    private String expname;

    @OneToMany
    @JoinColumn(name = "stepTypes")
    private List<StepType> stepTypes;

    public ExperimentType() {
    }

    public ExperimentType(String expname, List<StepType> stepTypes) {
        this.expname = expname;
        this.stepTypes = stepTypes;
    }

    public void addStepType(StepType stepType) {
        stepTypes.add(stepType);
    }

    public String getExpname() {
        return expname;
    }

    public void setExpname(String expname) {
        this.expname = expname;
    }

    public List<StepType> getStepTypes() {
        return stepTypes;
    }

    @Override
    public Long getId() {
        return super.getId();
    }

    @Override
    public void setId(Long id) {
        super.setId(id);
    }

    public void setStepTypes(List<StepType> stepTypes) {
        this.stepTypes = stepTypes;
    }

    public String getExperimentTypeName() {
        return expname;
    }

    public void setExperimentTypeName(String expname) {
        this.expname = expname;
    }

}
