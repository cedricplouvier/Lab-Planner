package be.uantwerpen.labplanner.Model;

import be.uantwerpen.labplanner.common.model.users.User;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Entity;
import javax.persistence.*;
import java.util.Set;


@Entity
public class Relation extends AbstractPersistable<Long>
{

    @Column
    private String description;

    @Column(nullable = false)
    private Long researcherID;

    @OneToMany(
            fetch = FetchType.EAGER
    )
    private Set<User> students;
    
    public Relation() {
    }

    public Relation(String description, Long researcherID, Set<User> students){
        this.description = description;
        this.researcherID = researcherID;
        this.students = students;
    }

    public Relation(String description){
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getResearcherID() {
        return researcherID;
    }

    public void setResearcherID(Long researcher) {
        this.researcherID = researcher;
    }

    public Set<User> getStudents() {
        return students;
    }

    public void setStudents(Set<User> students) {
        this.students = students;
    }

    public void deleteStudent(User student){
        this.students.remove(student);
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
