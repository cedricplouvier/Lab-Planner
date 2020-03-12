package be.uantwerpen.labplanner.Model;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Entity
public class DeviceType extends AbstractPersistable<Long>{
    //Default Variables
    private static String DEFAULT_DEVICTYPENAME = "default_deviceTypeName";
    private static Boolean DEFAULT_OVERNIGHTUSE = false;
    private static List<String> DEFAULT_INFORMATIONTYPES = Arrays.asList(new String[]{"Manual","Maintenance", "Calibration","Safety instruction card"});


    //Variables
    @Column
    private String deviceTypeName;
    @Column
    private Boolean overnightuse;

    @Lob
    @Column
    private byte[] devicePicture;
    @ManyToMany(
            fetch = FetchType.EAGER
    )
    @JoinTable(
            name = "DEVICE_TYPE_INFORMATION",
            joinColumns = {@JoinColumn(
                    name = "DEVICE_TYPE_ID",
                    referencedColumnName = "ID"
            )},
            inverseJoinColumns = {@JoinColumn(
                    name = "DEVICE_INFORMATION_ID",
                    referencedColumnName = "ID"
            )}
    )
    private List<DeviceInformation> deviceInformations;



    //Constructors
    public DeviceType() {
        this.deviceTypeName = DEFAULT_DEVICTYPENAME;
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
    public static List<String> getDefaultInformationtypes() {
        return DEFAULT_INFORMATIONTYPES;
    }
    public List<DeviceInformation> getDeviceInformation() { return deviceInformations; }
    public void setDeviceInformation(List<DeviceInformation> deviceInformations) { this.deviceInformations = deviceInformations; }
    public String getDeviceTypeName() { return deviceTypeName; }
    public void setDeviceTypeName(String deviceTypeName) { this.deviceTypeName = deviceTypeName; }
    public static void setDefaultInformationtypes(List<String> defaultInformationtypes) {
        DEFAULT_INFORMATIONTYPES = defaultInformationtypes;
    }

    public byte[] getDevicePicture() {
        return devicePicture;
    }

    public void setDevicePicture(byte[] devicePicture) {
        this.devicePicture = devicePicture;
    }

    public List<DeviceInformation> getDeviceInformations() {
        return deviceInformations;
    }

    public void setDeviceInformations(List<DeviceInformation> deviceInformations) {
        this.deviceInformations = deviceInformations;
    }
}
