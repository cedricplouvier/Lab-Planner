package be.uantwerpen.labplanner.Model;

import be.uantwerpen.labplanner.common.model.users.User;

public class Step{
    private User user;
    private Device device;
    private Timeslot timeslot;

    public Step(){
    }

    public Step(User user, Device device, Timeslot timeslot) {
        this.user=  user;
        this.device = device;
        this.timeslot = timeslot;
    }

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public Timeslot getTimeslot() {
        return timeslot;
    }

    public void setTimeslot(Timeslot timeslot) {
        this.timeslot = timeslot;
    }
}