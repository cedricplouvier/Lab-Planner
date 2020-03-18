package be.uantwerpen.labplanner.Model;

import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;


@Entity
public class DeviceInformation extends AbstractPersistable<Long> {
    //Default Variables
    private static String DEFAULT_INFORMATION_NAME = "DEFAULT_INFORMATION";
    @Column
    private String informationName;
    @Column
    private String information;

    @ElementCollection
    private List<String> files = new ArrayList<String>();

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

    public void addFile(String file){
        files.add(file);
    }
    @Override
    public Long getId() {
        return super.getId();
    }

    @Override
    public void setId(Long id) {
        super.setId(id);
    }
    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }
}
