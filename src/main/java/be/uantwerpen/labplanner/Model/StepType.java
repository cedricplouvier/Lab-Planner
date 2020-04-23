package be.uantwerpen.labplanner.Model;

import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class StepType extends AbstractPersistable<Long> {
    @OneToOne
    @JoinColumn(name = "deviceType", nullable = false)
    private DeviceType deviceType;
    @Column(name = "name")
    private String name;
    @OneToOne
    @JoinColumn(name = "continuity")
    private Continuity continuity;

    public StepType() {
    }

    public StepType(DeviceType deviceType, Continuity continuity, String stepTypeName) {
        this.deviceType = deviceType;
        this.name = stepTypeName;
        this.continuity = continuity;
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

    public Continuity getContinuity() {
        return continuity;
    }

    public void setContinuity(Continuity continuity) {
        this.continuity = continuity;
    }

    @Override
    public Long getId() {
        return super.getId();
    }

    @Override
    public void setId(Long id) {
        super.setId(id);
    }
}
