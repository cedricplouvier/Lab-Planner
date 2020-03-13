package be.uantwerpen.labplanner.Model;

import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import java.sql.Blob;
import java.util.List;


@Entity
public class DeviceInformation extends AbstractPersistable<Long> {
    //Default Variables
    private static String DEFAULT_INFORMATION_NAME = "DEFAULT_INFORMATION";
    @Column
    private String informationName;
    @Column
    private String information;
    @Column
    private List<String> fileURLs;

    //Constructors
    public DeviceInformation(){
        this.information = DEFAULT_INFORMATION_NAME;
        this.informationName = DEFAULT_INFORMATION_NAME;
    }

    public DeviceInformation(String informationName,String information){
        this.informationName = informationName;
        this.information = information;
    }

    public String getInformationName() { return informationName; }
    public void setInformationName(String informationName) { this.informationName = informationName; }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }
}
