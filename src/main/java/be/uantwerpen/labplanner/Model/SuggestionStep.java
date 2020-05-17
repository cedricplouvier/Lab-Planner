package be.uantwerpen.labplanner.Model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;

public class SuggestionStep {

    private DeviceType deviceType;
    private Continuity continuity;
    private boolean hasFixedLength;
    private String fixedTimeType = "No";
    private int fixedTimeHours;
    private int fixedTimeMinutes;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime start;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime end;
    private StepType stepType;
    private long deviceId=0;

    public long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(long deviceId) {
        this.deviceId = deviceId;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(DeviceType deviceType) {
        this.deviceType = deviceType;
    }

    public Continuity getContinuity() {
        return continuity;
    }

    public void setContinuity(Continuity continuity) {
        this.continuity = continuity;
    }

    public boolean isHasFixedLength() {
        return hasFixedLength;
    }

    public void setHasFixedLength(boolean hasFixedLength) {
        this.hasFixedLength = hasFixedLength;
    }

    public String getFixedTimeType() {
        return fixedTimeType;
    }

    public void setFixedTimeType(String fixedTimeType) {
        this.fixedTimeType = fixedTimeType;
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

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public StepType getStepType() {
        return stepType;
    }

    public void setStepType(StepType stepType) {
        this.stepType = stepType;
    }
}
