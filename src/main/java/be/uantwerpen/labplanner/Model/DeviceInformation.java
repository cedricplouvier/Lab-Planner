package be.uantwerpen.labplanner.Model;

import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
public class DeviceInformation extends AbstractPersistable<Long> {
    //Default Variables
    private static String DEFAULT_INFORMATION_NAME = "DEFAULT_INFORMATION";

    //Variables
    @Column(
            name = "devicename",
            unique = true,
            nullable = false
    )
    private String informationName;
    //Device
    @ElementCollection
    private List<String> informationList =new ArrayList<String>();

    //Constructors
    public DeviceInformation(){
        this.informationList = Arrays.asList(new String[]{});
        this.informationName = DEFAULT_INFORMATION_NAME;
    }

    public DeviceInformation(String informationName,String information){
        this.informationName = informationName;
        this.informationList.add(information);
    }

    //Add
    private void AddInformationBlock(String information){
        this.informationList.add(information);
    }

    //Get and Set
    public String getInformationName() {
        return informationName;
    }

    public void setInformationName(String informationName) {
        this.informationName = informationName;
    }


    public void setInformationList(List<String> informationList) {
        this.informationList = informationList;
    }
}
