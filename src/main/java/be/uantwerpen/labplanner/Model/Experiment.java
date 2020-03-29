package be.uantwerpen.labplanner.Model;

import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.List;


@Entity
public class Experiment extends AbstractPersistable<Long> {
    @OneToMany
    @JoinColumn(name = steps)
    private List<Step> steps;

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public String getExperimentname() {
        return experimentname;
    }

    public void setExperimentname(String experimentname) {
        this.experimentname = experimentname;
    }

    @Column(name = experimentname)
    private String experimentname;

    public Experiment(){}

    public Experiment(List<Step> steps, String experimentname){
        this.experimentname=experimentname;
        this.steps=steps;
    }

}
