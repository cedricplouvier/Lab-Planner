package be.uantwerpen.labplanner.Service;


import be.uantwerpen.labplanner.common.model.stock.Product;
import be.uantwerpen.labplanner.common.model.stock.Tag;
import be.uantwerpen.labplanner.common.repository.stock.ProductRepository;
import java.util.List;
import java.util.Optional;

import be.uantwerpen.labplanner.common.service.stock.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceExt extends ProductService {
    @Autowired
    private ProductRepository productRepository;

    public ProductServiceExt() {
    }

    public void saveSomeAttributes(Product product) {
        Product temp = Product.getId() == null?null: productRepository.findById( product.getId()).orElse(null);
        if (temp != null){
            temp.setName(product.getName());
            temp.setDescription((product.getDescription()));
            temp.setUnitCost(product.getUnitCost());
            temp.setStockLevel(product.getStockLevel());
            temp.setLowStockLevel(product.getLowStockLevel());
            temp.setReservedStockLevel(product.getReservedStockLevel());
            temp.setUnits(product.getUnits());
            temp.setImageUrl(product.getImageUrl());
            temp.setProperties(product.getProperties());
            temp.setProductCreatorId(product.getProductCreatorId());
            temp.setLastUpdatedById(product.getLastUpdatedById());
            temp.setCreateDateTime(product.getCreateDateTime());
            temp.setTags(product.getTags());
            productRepository.save(temp);
        }
        else{
            productRepository.save(product);
        }
    }
}
