package be.uantwerpen.labplanner.Model;

import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Entity
public class DeviceType extends AbstractPersistable<Long> {
    //Default Variables
    private static String DEFAULT_DEVICTYPENAME = "default_devicetypename";
    private static Boolean DEFAULT_OVERNIGHTUSE = false;
    private static List<String> DEFAULT_INFORMATIONTYPES = Arrays.asList(new String[]{"Manual","Maintenance", "Calibration","Safety instruction card"});


    //Variables
    @Column(
            name = "devicetypename",
            unique = true,
            nullable = false
    )
    private String devicetypename;
    @Column
    private Boolean overnightuse;

    @OneToMany(targetEntity=DeviceInformation.class, mappedBy="informationName", fetch=FetchType.EAGER)
    private List<DeviceInformation> deviceInformation;

    //Constructors
    public DeviceType() {
        this.devicetypename = DEFAULT_DEVICTYPENAME;
        this.overnightuse = DEFAULT_OVERNIGHTUSE;
    }
    public DeviceType(String devicetypename,Boolean overnightuse) {
        this.devicetypename = devicetypename;
        this.overnightuse = overnightuse;
    }

    //Add
    public void addDeviceInformation(String name,String information){
        deviceInformation.add(new DeviceInformation(name,information));
    }

    //Get and Set
    public Boolean getOvernightuse() {
        return overnightuse;
    }

    public void setOvernightuse(Boolean overnightuse) {
        this.overnightuse = overnightuse;
    }

    public String getDevicetypename() {
        return devicetypename;
    }

    public void setDevicetypename(String devicetypename) {
        this.devicetypename = devicetypename;
    }

    public List<DeviceInformation> getDeviceInformation() {
        return deviceInformation;
    }

    public void setDeviceInformation(List<DeviceInformation> deviceInformation) {
        this.deviceInformation = deviceInformation;
    }

    public static List<String> getDefaultInformationtypes() {
        return DEFAULT_INFORMATIONTYPES;
    }

    public static void setDefaultInformationtypes(List<String> defaultInformationtypes) {
        DEFAULT_INFORMATIONTYPES = defaultInformationtypes;
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
