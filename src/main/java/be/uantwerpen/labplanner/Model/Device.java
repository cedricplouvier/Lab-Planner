package be.uantwerpen.labplanner.Model;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;

@Entity
public class Device extends AbstractPersistable<Long> {
    //Default variables
    private static String DEFAULT_DEVICENAME = "default_devicename";

    //Variables
    @Column(
            name = "devicename",
            unique = true,
            nullable = false
    )
    private String devicename;

    @OneToOne
    @JoinColumn(name = "deviceType", nullable = true)
    private DeviceType deviceType;
    @Column
    private String Comment;


    //Constructors
    public Device() {
        this(DEFAULT_DEVICENAME,null);
    }

    public Device(String devicename,DeviceType deviceType) {
        this.devicename = devicename;
        this.deviceType = deviceType;
    }

    //Get and Set
    public static String getDefaultDevicename() {
        return DEFAULT_DEVICENAME;
    }

    @Override
    public Long getId() {
        return super.getId();
    }

    @Override
    public void setId(Long id) {
        super.setId(id);
    }


    public String getDevicename() {
        return devicename;
    }

    public void setDevicename(String devicename) {
        this.devicename = devicename;
    }


    public DeviceType getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(DeviceType deviceType) {
        this.deviceType = deviceType;
    }


    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }
}
