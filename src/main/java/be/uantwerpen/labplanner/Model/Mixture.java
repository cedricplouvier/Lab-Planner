package be.uantwerpen.labplanner.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Entity
public class Mixture extends AbstractPersistable<Long> {
    @Column
    private String name;

    @Column
    private String description;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "Mix_COMP",
            joinColumns = {@JoinColumn(
                    name = "PROD_ID",
                    referencedColumnName = "ID"
            )},
            inverseJoinColumns = {@JoinColumn(
                    name = "COMP_ID",
                    referencedColumnName = "ID"
            )}
    )
    private List <Composition> compositions;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "Mix_TAG",
            joinColumns = {@JoinColumn(
                    name = "PROD_ID",
                    referencedColumnName = "ID"
            )},
            inverseJoinColumns = {@JoinColumn(
                    name = "TAG_ID",
                    referencedColumnName = "ID"
            )}
    )
    private List<OwnTag> tags;

    public Mixture(String name, List<Composition> compositions, String description, List<OwnTag> tags){
        this.name = name;
        this.compositions = compositions;
        this.description = description;
        this.tags = tags;

    }

    public Mixture(){
        this.name = "";
        this.description = "";
        this.compositions = new ArrayList<Composition>();

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<OwnTag> getTags() {
        return tags;
    }

    public void setTags(List<OwnTag> tags) {
        this.tags = tags;
    }

    public List<Composition> getCompositions() {
        return compositions;
    }

    public void deleteComposition(Composition comp){
        for (int i = 0; i < this.compositions.size(); i++) {
            if (Objects.equals(comp, this.compositions.get(i))) {
                this.compositions.remove(i);
            }
        }    }

    public void setCompositions(List<Composition> compositions) {
        this.compositions = compositions;
    }

    public void addComposition(Composition comp){
        this.compositions.add(comp);
    }
}
