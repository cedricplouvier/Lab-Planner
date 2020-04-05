package be.uantwerpen.labplanner.Model;

import be.uantwerpen.labplanner.common.model.users.User;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Entity;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
public class Relation extends AbstractPersistable<Long>
{

    @Column
    private String description;

    @Column(
            name = "researcherID",
            nullable = false,
            unique = true

    )
    private long researcherID;

    @OneToMany
    private List<User> students = new ArrayList<>();

    public Relation() {
    }

    public Relation(String description, long ID, List<User> students){
        this.description = description;
        this.researcherID = ID;
        this.students = students;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getResearcherID() {
        return researcherID;
    }

    public void setResearcherID(long researcherID) {
        this.researcherID = researcherID;
    }

    public List<User> getStudents() {
        return students;
    }

    public void setStudents(List<User> students) {
        this.students = students;
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
