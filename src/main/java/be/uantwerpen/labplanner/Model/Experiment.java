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
    @JoinColumn(name = "user")
    private User user;

    @OneToMany
    @JoinColumn(name = "step", nullable = false)
    private List<Step> steps;


    @OneToMany
    @JoinColumn(name = "continuity", nullable = false)
    private List<Continuity> continuities;

    public Experiment() {
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
}
