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

    @Column(name = "hasFixedLength")
    private boolean hasFixedLength;
    @Column(name = "fixedTimeType")
    private String fixedTimeType;
    @Column(name = "fixedTimeHours")
    private int fixedTimeHours;
    @Column(name = "fixedTimeMinutes")
    private int fixedTimeMinutes;

    public StepType() {
    }

    public StepType(DeviceType deviceType, Continuity continuity, String stepTypeName) {
        this.deviceType = deviceType;
        this.name = stepTypeName;
        this.continuity = continuity;
        this.fixedTimeType = "No";
    }

    public StepType(DeviceType deviceType, Continuity continuity, String stepTypeName, boolean hasFixedLength, String fixedTimeType, int fixedTimeHours, int fixedTimeMinutes) {
        this.deviceType = deviceType;
        this.name = stepTypeName;
        this.continuity = continuity;
        this.hasFixedLength = hasFixedLength;
        this.fixedTimeType = fixedTimeType;
        this.fixedTimeHours = fixedTimeHours;
        this.fixedTimeMinutes = fixedTimeMinutes;

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

    public boolean getHasFixedLength() {
        return hasFixedLength;
    }

    public void setHasFixedLength(boolean hasFixedLength) {
        this.hasFixedLength = hasFixedLength;
    }

    public int getFixedTimeHours() {
        return fixedTimeHours;
    }

    public void setFixedTimeHours(int fixedTimeHours) {
        this.fixedTimeHours = fixedTimeHours;
    }

    public int getFixedTimeMinutes() {
        return fixedTimeMinutes;
    }

    public void setFixedTimeMinutes(int fixedTimeMinutes) {
        this.fixedTimeMinutes = fixedTimeMinutes;
    }

    public String getFixedTimeType() {
        return fixedTimeType;
    }

    public void setFixedTimeType(String fixedTimeType) {
        this.fixedTimeType = fixedTimeType;
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
