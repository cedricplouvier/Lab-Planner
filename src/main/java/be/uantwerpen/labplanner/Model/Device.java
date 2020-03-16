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
    private static String DEFAULT_DEVICENAME = "default_username";

    //Variables
    @Column(
            name = "devicename",
            unique = true,
            nullable = false
    )
    private String devicename;

    @OneToOne
    @JoinColumn(name = "deviceType", nullable = false)
    private DeviceType deviceType;
    @Column
    private String Comment;
    @Column(
            name = "date_created",
            nullable = false,
            updatable = false
    )
    @CreationTimestamp
    private LocalDateTime dateCreated;
    @Column(
            name = "updated_on"
    )
    @UpdateTimestamp
    private LocalDateTime updateDateTime;

    //Constructors
    public Device() {
        this(DEFAULT_DEVICENAME,new DeviceType());
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

    public static void setDefaultDevicename(String defaultDevicename) {
        DEFAULT_DEVICENAME = defaultDevicename;
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

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public LocalDateTime getUpdateDateTime() {
        return updateDateTime;
    }

    public void setUpdateDateTime(LocalDateTime updateDateTime) {
        this.updateDateTime = updateDateTime;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }
}