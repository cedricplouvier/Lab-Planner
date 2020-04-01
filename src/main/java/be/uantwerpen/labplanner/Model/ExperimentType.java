package be.uantwerpen.labplanner.Model;

import be.uantwerpen.labplanner.common.model.users.User;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import java.util.List;
@Entity
public class ExperimentType extends AbstractPersistable<Long> {


    @Column(name = "name",unique = true,nullable = false)
    private String name;

    @OneToMany
    @JoinColumn(name = "deviceTypes")
    private List<DeviceType> deviceTypes;

    @OneToMany
    @JoinColumn(name = "continuity", nullable = false)
    private List<Continuity> continuities;

   public ExperimentType(){}
   public ExperimentType(String experimentTypeName,List<Continuity> continuities,List<DeviceType> stepTypes) {
       this.continuities=continuities;
       this.name=experimentTypeName;
       this.deviceTypes=stepTypes;
   }
   public void addStepType(DeviceType deviceType){
       deviceTypes.add(deviceType);
   }

    public void addStep(DeviceType deviceType) {
        //First step wil be added without continuity
        if (deviceTypes.size() == 0) {
            deviceTypes.add(deviceType);
        } else {
            deviceTypes.add(deviceType);
            continuities.add(new Continuity(deviceTypes.get(deviceTypes.size() - 1), deviceType));
        }
    }

    public void addStep(DeviceType deviceType, ContinuityType continuityType, int hours, int minutes) {
        //First step wil be added without continuity
        if (deviceTypes.size() == 0) {
            deviceTypes.add(deviceType);
        } else {
            deviceTypes.add(deviceType);
            continuities.add(new Continuity(continuityType, hours, minutes, deviceTypes.get(deviceTypes.size() - 1), deviceType));
        }
    }

    public List<Continuity> getContinuities() {
        return continuities;
    }

    public void setContinuities(List<Continuity> continuities) {
        this.continuities = continuities;
    }
    public String getExperimentTypeName() { return name; }
    public void setExperimentTypeName(String experimentTypeName) { this.name = experimentTypeName; }
    public List<DeviceType> getDeviceTypes() {
        return deviceTypes;
    }

    public void setStepTypes(List<DeviceType> deviceTypes) {
        this.deviceTypes = deviceTypes;
    }


}
