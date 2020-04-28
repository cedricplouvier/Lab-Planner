package be.uantwerpen.labplanner.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class Composition extends AbstractPersistable<Long> {

    @Column
    private Double amount;

    @ManyToOne
    private OwnProduct product;

    public Composition(Double amount, OwnProduct prod){
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

    public OwnProduct getProduct() {
        return product;
    }

    public void setProduct(OwnProduct product) {
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
