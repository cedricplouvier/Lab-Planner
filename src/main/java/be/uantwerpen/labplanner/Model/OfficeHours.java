package be.uantwerpen.labplanner.Model;


import java.util.ArrayList;
import java.util.List;

public class OfficeHours {
    private int startMinute;
    private int startHour;
    private int endMinute;
    private int endHour;
    private boolean officeHoursOn;
    private boolean weekendOn;
    private boolean holidaysOn;


    public OfficeHours(int startMinute, int startHour, int endMinute, int endHour, boolean officeHoursOn, boolean weekendOn, boolean holidaysOn) {
        this.startMinute = startMinute;
        this.startHour = startHour;
        this.endMinute = endMinute;
        this.endHour = endHour;
        this.officeHoursOn = officeHoursOn;
        this.weekendOn = weekendOn;
        this.holidaysOn = holidaysOn;
    }

    public OfficeHours() {
        this.startHour = 9;
        this.startMinute = 0;
        this.endHour = 17;
        this.endMinute = 0;
        this.officeHoursOn = true;
    }

    public int getStartMinute() {
        return startMinute;
    }

    public void setStartMinute(int startMinute) {
        if (startMinute < 0) {
            this.startMinute = 0;
        } else if (startMinute > 59) {
            this.startMinute = 59;
        } else {
            this.startMinute = startMinute;
        }
    }

    public int getStartHour() {
        return startHour;
    }

    public void setStartHour(int startHour) {
        if (startHour < 0) {
            this.startHour = 0;
        } else if (startHour > 23) {
            this.startHour = 23;
        } else {
            this.startHour = startHour;
        }
    }

    public int getEndHour() {
        return endHour;
    }

    public void setEndHour(int endHour) {
        if (endHour < 0) {
            this.endHour = 0;
        } else if (endHour > 23) {
            this.endHour = 23;
        } else {
            this.endHour = endHour;
        }
    }

    public int getEndMinute() {
        return endMinute;
    }

    public void setEndMinute(int endMinute) {
        if (endMinute < 0) {
            this.endMinute = 0;
        } else if (endMinute > 59) {
            this.endMinute = 59;
        } else {
            this.endMinute = endMinute;
        }
    }

    public boolean isOfficeHoursOn() {
        return officeHoursOn;
    }

    public void setOfficeHoursOn(boolean officeHoursOn) {
        this.officeHoursOn = officeHoursOn;
    }

    public boolean isWeekendOn() {
        return weekendOn;
    }

    public void setWeekendOn(boolean weekendOn) {
        this.weekendOn = weekendOn;
    }

    public boolean isHolidaysOn() {
        return holidaysOn;
    }

    public void setHolidaysOn(boolean holidaysOn) {
        this.holidaysOn = holidaysOn;
    }
}
