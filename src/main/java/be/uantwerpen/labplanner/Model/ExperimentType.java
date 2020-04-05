package be.uantwerpen.labplanner.Model;

import be.uantwerpen.labplanner.common.model.users.User;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import java.util.List;
@Entity
public class ExperimentType extends AbstractPersistable<Long> {




    @Column(name = "expname",unique = true,nullable = false)
    private String expname;

    @OneToMany
    @JoinColumn(name = "stepTypes")
    private List<StepType> stepTypes;

   public ExperimentType(){}
   public ExperimentType(String expname,List<StepType> stepTypes) {
       this.expname=expname;
       this.stepTypes=stepTypes;
   }
   public void addStepType(StepType stepType){
       stepTypes.add(stepType);
   }

//    public void addStep(DeviceType deviceType) {
//        //First step wil be added without continuity
//        if (deviceTypes.size() == 0) {
//            deviceTypes.add(deviceType);
//        } else {
//            deviceTypes.add(deviceType);
//            continuities.add(new Continuity(deviceTypes.get(deviceTypes.size() - 1), deviceType));
//        }
//    }
//
//    public void addStep(DeviceType deviceType, ContinuityType continuityType, int hours, int minutes) {
//        //First step wil be added without continuity
//        if (deviceTypes.size() == 0) {
//            deviceTypes.add(deviceType);
//        } else {
//            deviceTypes.add(deviceType);
//            continuities.add(new Continuity(continuityType, hours, minutes, deviceTypes.get(deviceTypes.size() - 1), deviceType));
//        }
//    }


    public String getExpname() {
        return expname;
    }

    public void setExpname(String expname) {
        this.expname = expname;
    }

    public List<StepType> getStepTypes() {
        return stepTypes;
    }


    public void setStepTypes(List<StepType> stepTypes) {
        this.stepTypes = stepTypes;
    }
    public String getExperimentTypeName() {
        return expname;
    }
    public void setExperimentTypeName(String expname){this.expname=expname;}

}
