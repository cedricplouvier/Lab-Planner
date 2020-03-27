package be.uantwerpen.labplanner.Model;

import be.uantwerpen.labplanner.common.model.stock.Product;
import be.uantwerpen.labplanner.common.model.stock.Tag;
import be.uantwerpen.labplanner.common.model.users.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import java.util.*;


@Entity
public class Mixture extends AbstractPersistable<Long> {
    @Column
    private String name;

    @ManyToMany
    @JoinTable(
            name = "MIX_TAG",
            joinColumns = {@JoinColumn(
                    name = "MIX_ID",
                    referencedColumnName = "ID"
            )},
            inverseJoinColumns = {@JoinColumn(
                    name = "COM_ID",
                    referencedColumnName = "ID"
            )}
    )
    private List<Composition> compositions;

    Mixture(String name, List<Composition> compositions){
        this.name = name;
        this.compositions = compositions;
    }

    public Mixture(){
        this.name = "";
        this.compositions = new ArrayList();
    }

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
