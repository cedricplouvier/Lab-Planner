package be.uantwerpen.labplanner.Model;

import be.uantwerpen.labplanner.common.model.stock.Product;

import javax.persistence.Entity;

@Entity
public class ProductExt extends Product {

    @Override
    public void setId(Long id) {
        super.setId(id);
    }

    @Override
    public Long getId() {
        return super.getId();
    }
}
