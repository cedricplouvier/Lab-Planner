package be.uantwerpen.labplanner.Model;

import be.uantwerpen.labplanner.common.model.stock.Product;
import be.uantwerpen.labplanner.common.service.stock.ProductService;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class Composition extends AbstractPersistable<Long> {

    @Column
    private Double amount;

    @ManyToOne
    private Product product;

    Composition(Double amount, Product prod){
        this.amount = amount;
        this.product = prod;
    }

    public  Composition(){
        this.amount = 0.0;
        this.product = null;
    }

    @Override
    public Long getId() {
        return super.getId();
    }

    @Override
    public void setId(Long id) {
        super.setId(id);
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
}
