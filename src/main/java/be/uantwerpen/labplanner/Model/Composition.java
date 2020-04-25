package be.uantwerpen.labplanner.Model;

import be.uantwerpen.labplanner.common.model.stock.Product;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class Composition extends AbstractPersistable<Long> {

    @Column
    private Double amount;

    @JsonBackReference
    @ManyToOne
    private Product product;

    public Composition(Double amount, Product prod){
        this.amount = amount;
        this.product = prod;
    }

    public  Composition(){
        this.amount = 0.0;
        this.product = null;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        if(amount>=0) {
            this.amount = amount;
        }
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
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
