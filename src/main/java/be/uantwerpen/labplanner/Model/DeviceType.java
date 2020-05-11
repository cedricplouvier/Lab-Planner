package be.uantwerpen.labplanner.Model;

import be.uantwerpen.labplanner.Exception.StorageException;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;


@Entity
public class DeviceType extends AbstractPersistable<Long>{
    //Default Variables
    private static String DEFAULT_DEVICETYPENAME = "default_deviceTypeName";
    private static Boolean DEFAULT_OVERNIGHTUSE = false;

    @Column
    private String color;



    //Variables
    @Column
    private String deviceTypeName;
    @Column
    private Boolean overnightuse;


    //Constructors
    public DeviceType() {
        this.deviceTypeName = DEFAULT_DEVICETYPENAME;
        this.overnightuse = DEFAULT_OVERNIGHTUSE;
    }
    public DeviceType(String deviceTypeName,Boolean overnightuse) {
        this.deviceTypeName = deviceTypeName;
        this.overnightuse = overnightuse;
    }


    //Add


    //Get and Set
    public Boolean getOvernightuse() { return overnightuse; }
    public void setOvernightuse(Boolean overnightuse) { this.overnightuse = overnightuse; }
   public String getDeviceTypeName() { return deviceTypeName; }
    public void setDeviceTypeName(String deviceTypeName) {
        this.deviceTypeName = deviceTypeName;
    }


    @Override
    public Long getId() {
        return super.getId();
    }

    @Override
    public void setId(Long id) {
        super.setId(id);
    }
    public static String getDefaultDevicetypename() {
        return DEFAULT_DEVICETYPENAME;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
