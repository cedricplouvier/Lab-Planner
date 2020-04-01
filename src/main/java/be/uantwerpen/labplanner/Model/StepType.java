package be.uantwerpen.labplanner.Model;

import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class StepType extends AbstractPersistable<Long> {
    @OneToOne
    @JoinColumn(name = "deviceType",nullable = false)
    private DeviceType deviceType;
    @Column(name = "name")
    private String name;


    public StepType(){}
    public StepType(DeviceType deviceType,String stepTypeName){
        this.deviceType=deviceType;
        this.name=stepTypeName;
    }
    public DeviceType getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(DeviceType deviceType) {
        this.deviceType = deviceType;
    }

    public String getStepTypeName() {
        return name;
    }

    public void setStepTypeName(String stepTypeName) {
        this.name = stepTypeName;
    }
}
