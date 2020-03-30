package be.uantwerpen.labplanner.Model;

import be.uantwerpen.labplanner.common.model.users.User;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.List;
@Entity
public class ExperimentType extends AbstractPersistable<Long> {


    @Column(name = "experimentTypeName",unique = true,nullable = false)
    private String experimentTypeName;
    @OneToMany
    @JoinColumn(name = "step", nullable = false)
    private List<Step> steps;


    @OneToMany
    @JoinColumn(name = "continuity", nullable = false)
    private List<Continuity> continuities;

   public ExperimentType(){}
   public ExperimentType(String experimentTypeName, List<Step> steps,List<Continuity> continuities) {
       this.steps=steps;
       this.continuities=continuities;
       this.experimentTypeName=experimentTypeName;
   }

    public void addStep(Step step) {
        //First step wil be added without continuity
        if (steps.size() == 0) {
            steps.add(step);
        } else {
            steps.add(step);
            continuities.add(new Continuity(steps.get(steps.size() - 1), step));
        }
    }

    public void addStep(Step step, ContinuityType continuityType, int hours, int minutes) {
        //First step wil be added without continuity
        if (steps.size() == 0) {
            steps.add(step);
        } else {
            steps.add(step);
            continuities.add(new Continuity(continuityType, hours, minutes, steps.get(steps.size() - 1), step));
        }
    }
    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public List<Continuity> getContinuities() {
        return continuities;
    }

    public void setContinuities(List<Continuity> continuities) {
        this.continuities = continuities;
    }
    public String getExperimentTypeName() { return experimentTypeName; }
    public void setExperimentTypeName(String experimentTypeName) { this.experimentTypeName = experimentTypeName; }

}
