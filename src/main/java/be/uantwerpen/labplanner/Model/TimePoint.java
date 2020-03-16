package be.uantwerpen.labplanner.Model;

import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class TimePoint extends AbstractPersistable<Long> {
    @Column(name = "year",nullable = false)
    private int year;
    @Column(name = "month",nullable = false)
    private int month;
    @Column(name = "day",nullable = false)
    private int day;
    @Column(name = "hour",nullable = false)
    private int hour;
    @Column(name = "min",nullable = false)
    private int min;

    public TimePoint(){}

    TimePoint(int year, int month, int day, int hour, int min){
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.min = min;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }


}
