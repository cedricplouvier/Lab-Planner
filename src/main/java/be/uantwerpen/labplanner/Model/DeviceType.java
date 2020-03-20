package be.uantwerpen.labplanner.Model;

import be.uantwerpen.labplanner.Exception.StorageException;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
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
    @Column
    private String devicePictureName;
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
        initDirectory();
    }
    public DeviceType(String deviceTypeName,Boolean overnightuse) {
        this.deviceTypeName = deviceTypeName;
        this.overnightuse = overnightuse;
        initDirectory();
    }

    private void initDirectory(){
        try {
            Files.createDirectories(Paths.get("upload-dir/" + deviceTypeName));
        }
        catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
        try {
            Files.createDirectories(Paths.get("upload-dir/images"));
        }
        catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
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

    public String getDevicePictureName() {
        return devicePictureName;
    }

    public void setDevicePictureName(String devicePictureName) {
        this.devicePictureName = devicePictureName;
    }

    public List<DeviceInformation> getDeviceInformations() {
        return deviceInformations;
    }

    public void setDeviceInformations(List<DeviceInformation> deviceInformations) {
        this.deviceInformations = deviceInformations;
    }
}
