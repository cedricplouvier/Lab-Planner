package be.uantwerpen.labplanner.Model;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import be.uantwerpen.labplanner.common.model.stock.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
public class OwnTag extends AbstractPersistable<Long> {
    @NotBlank(
            message = "{invalid.notblank}"
    )
    @Pattern(
            regexp = "[a-zA-Z0-9-_]+",
            message = "{tag.invalid.name}"
    )
    private String name;

    @ManyToMany(
            mappedBy = "tags"
    )
    private List<OwnProduct> products;

    public OwnTag() {
    }

    public OwnTag(String name) {
        this.name = name;
    }

    public List<OwnProduct> getProducts() {
        return this.products;
    }

    public void setProducts(List<OwnProduct> products) {
        this.products = products;
    }

    public void setId(Long id) {
        super.setId(id);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
