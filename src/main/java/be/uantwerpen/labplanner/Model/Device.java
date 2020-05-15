package be.uantwerpen.labplanner.Model;

import be.uantwerpen.labplanner.Exception.StorageException;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Entity
public class Device extends AbstractPersistable<Long> {
    //Default variables
    private static String DEFAULT_DEVICENAME = "default_devicename";
    private static List<String> DEFAULT_INFORMATIONTYPES = Arrays.asList(new String[]{"Manual","Maintenance", "Calibration","Safety instruction card"});

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
    public Device() {
        this(DEFAULT_DEVICENAME,null);
        initDirectory();
    }

    public Device(String devicename,DeviceType deviceType) {
        this.devicename = devicename;
        this.deviceType = deviceType;
        initDirectory();

    }
    private void initDirectory(){
        try {
            Files.createDirectories(Paths.get("upload-dir/" + devicename));
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
    //Get and Set
    public static String getDefaultDevicename() {
        return DEFAULT_DEVICENAME;
    }
    public List<DeviceInformation> getDeviceInformation() { return deviceInformations; }
    public void setDeviceInformation(List<DeviceInformation> deviceInformations) { this.deviceInformations = deviceInformations; }

    @Override
    public Long getId() {
        return super.getId();
    }

    @Override
    public void setId(Long id) {
        super.setId(id);
    }
    public void setDeviceInformations(List<DeviceInformation> deviceInformations) {
        this.deviceInformations = deviceInformations;
    }
    public List<DeviceInformation> getDeviceInformations() {
        return deviceInformations;
    }

    public String getDevicename() {
        return devicename;
    }

    public void setDevicename(String devicename) {
        //Change updload dir name if the type name changes
        if(!this.devicename.equals(devicename)){
            File dir = new File("upload-dir/"+this.devicename);
            if (!dir.isDirectory()) {
                //
            } else {
                String newDirName = devicename;
                File newDir = new File(dir.getParent() + "/" + newDirName);
                dir.renameTo(newDir);
            }
            if (this.devicePictureName!=null){
                String rootlocation = "upload-dir/images/";
                File f1 = new File(rootlocation+this.devicePictureName);
                File f2 = new File(rootlocation+devicename + "."+this.devicePictureName.substring(this.devicePictureName.lastIndexOf(".") + 1));
                f1.renameTo(f2);
                this.devicePictureName =devicename + "."+this.devicePictureName.substring(this.devicePictureName.lastIndexOf(".") + 1);
            }
        }
        this.devicename = devicename;
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
    public static List<String> getDefaultInformationtypes() {
        return DEFAULT_INFORMATIONTYPES;
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
