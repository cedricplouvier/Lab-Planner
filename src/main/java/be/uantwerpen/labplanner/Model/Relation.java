package be.uantwerpen.labplanner.Model;

import be.uantwerpen.labplanner.common.model.users.Privilege;
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

    @OneToOne
    @JoinColumn(name = "user_id")
    private User researcher;

    @OneToMany(
            fetch = FetchType.EAGER
    )
    private List<User> students;

    public Relation() {
    }

    public Relation(String description, User researcher, List<User> students){
        this.description = description;
        this.researcher = researcher;
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

    public User getResearcher() {
        return researcher;
    }

    public void setResearcher(User researcher) {
        this.researcher = researcher;
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
