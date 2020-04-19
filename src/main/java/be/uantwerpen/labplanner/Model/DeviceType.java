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
    private static List<String> DEFAULT_INFORMATIONTYPES = Arrays.asList(new String[]{"Manual","Maintenance", "Calibration","Safety instruction card"});

    @Column
    private String color;



    //Variables
    @Column
    private String deviceTypeName;
    @Column
    private Boolean overnightuse =false;
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
        this.deviceTypeName = DEFAULT_DEVICETYPENAME;
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
    public void setDeviceTypeName(String deviceTypeName) {
        //Change updload dir name if the type name changes
        if(!this.deviceTypeName.equals(deviceTypeName)){
            File dir = new File("upload-dir/"+this.deviceTypeName);
            if (!dir.isDirectory()) {
                //
            } else {
                String newDirName = deviceTypeName;
                File newDir = new File(dir.getParent() + "/" + newDirName);
                dir.renameTo(newDir);
            }
            if (this.devicePictureName!=null){
                String rootlocation = "upload-dir/images/";
                File f1 = new File(rootlocation+this.devicePictureName);
                File f2 = new File(rootlocation+deviceTypeName + "."+this.devicePictureName.substring(this.devicePictureName.lastIndexOf(".") + 1));
                f1.renameTo(f2);
                this.devicePictureName =deviceTypeName + "."+this.devicePictureName.substring(this.devicePictureName.lastIndexOf(".") + 1);
            }
        }
        this.deviceTypeName = deviceTypeName;
    }
    public static void setDefaultInformationtypes(List<String> defaultInformationtypes) {
        DEFAULT_INFORMATIONTYPES = defaultInformationtypes;
    }

    public String getDevicePictureName() {
        return devicePictureName;
    }

    public void setDevicePictureName(String devicePictureName) {
        this.devicePictureName = devicePictureName;
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
