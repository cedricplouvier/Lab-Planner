package be.uantwerpen.labplanner.Model;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.time.LocalDateTime;

public class SuggestionResponseBody {
    String steps;
    Integer currentStep;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    LocalDateTime dateTime;
    Boolean withinOfficeHours;
    Boolean overlapAllowed;
    public Integer getCurrentStep() {
        return currentStep;
    }

    public void setCurrentStep(Integer currentStep) {
        this.currentStep = currentStep;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public Boolean getWithinOfficeHours() {
        return withinOfficeHours;
    }

    public void setWithinOfficeHours(Boolean withinOfficeHours) {
        this.withinOfficeHours = withinOfficeHours;
    }

    public Boolean getOverlapAllowed() {
        return overlapAllowed;
    }

    public void setOverlapAllowed(Boolean overlapAllowed) {
        this.overlapAllowed = overlapAllowed;
    }
}
