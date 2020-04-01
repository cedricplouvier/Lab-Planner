package be.uantwerpen.labplanner.Model;

import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

// Enum that describes type of continuity
enum ContinuityType {
    EQUAL,      //desired time between steps has to be equal to selected time
    MORE,       //desired time between steps has to be equal or longer than selected time
    LESS        //desired time between steps has to be equal or shorter than selected time
}

@Entity
public class Continuity extends AbstractPersistable<Long> {

    private final ContinuityType DEFAULT_CONTINUITY_TYPE = ContinuityType.EQUAL;
    private final int DEFAULT_HOURS = 1;
    private final int DEFAULT_MINUTES = 0;

    //type of continuity (can be EQUAL, MORE, LESS)
    private ContinuityType continuityType;

    //hours between current step and next step
    private int hours;

    //minutes between current step and next step
    private int minutes;

    //current step to which we set the continuity
    @OneToOne
    @JoinColumn(name = "currentStep", nullable = false)
    private Step currentStep;
    @OneToOne
    @JoinColumn(name = "currentDeviceType")
    private DeviceType currentDeviceType;
    @OneToOne
    @JoinColumn(name = "nextDeviceType")
    private DeviceType nextDeviceType;

    //step that comes after current step
    @OneToOne
    @JoinColumn(name = "nextStep", nullable = false)
    private Step nextStep;

    public Continuity() {
    }

    public Continuity(ContinuityType continuityType, int hours, int minutes, Step currentStep, Step nextStep) {
        this.continuityType = continuityType;
        this.hours = hours;
        this.minutes = minutes;
        this.currentStep = currentStep;
        this.nextStep = nextStep;
    }

    public Continuity(Step currentStep, Step nextStep) {
        this.continuityType = DEFAULT_CONTINUITY_TYPE;
        this.hours = DEFAULT_HOURS;
        this.minutes = DEFAULT_MINUTES;
        this.currentStep = currentStep;
        this.nextStep = nextStep;
    }

    public Continuity(DeviceType currentStepType,DeviceType nextStepType){
        this.currentDeviceType=currentStepType;
        this.nextDeviceType=nextStepType;
        this.continuityType = DEFAULT_CONTINUITY_TYPE;
        this.hours = DEFAULT_HOURS;
        this.minutes = DEFAULT_MINUTES;
    }
    public Continuity(ContinuityType continuityType, int hours, int minutes, DeviceType currentDeviceType,DeviceType nextDeviceType){
        this.continuityType = continuityType;
        this.hours = hours;
        this.minutes = minutes;
        this.currentDeviceType=currentDeviceType;
        this.nextDeviceType=nextDeviceType;
    }
}
