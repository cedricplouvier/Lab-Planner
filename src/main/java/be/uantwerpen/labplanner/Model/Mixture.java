package be.uantwerpen.labplanner.Model;

import be.uantwerpen.labplanner.common.model.stock.Product;
import be.uantwerpen.labplanner.common.model.users.User;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import java.util.List;
import java.util.Map;




@Entity
public class Mixture extends AbstractPersistable<Long> {
    @Column
    private String name;

    @OneToMany
    private List<Composition> compositions;

    Mixture(String name){
        this.name = name;
    }

    public Mixture(){}

    @Override
    public Long getId() {
        return super.getId();
    }

    @Override
    public void setId(Long id) {
        super.setId(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Composition> getCompositions() {
        return compositions;
    }

    public void setCompositions(List<Composition> compositions) {
        this.compositions = compositions;
    }

    public void addComposition(Composition comp){
        this.compositions.add(comp);
    }
}
