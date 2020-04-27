package be.uantwerpen.labplanner.Service;


import be.uantwerpen.labplanner.Model.OwnProduct;
import be.uantwerpen.labplanner.Model.OwnTag;
import be.uantwerpen.labplanner.Repository.OwnProductRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OwnProductService {
    @Autowired
    private OwnProductRepository productRepository;

    public OwnProductService() {
    }

    public List<OwnProduct> findAll() {
        return this.productRepository.findAll();
    }

    public Optional<OwnProduct> findById(Long id) {
        return this.productRepository.findById(id);
    }

    public List<OwnProduct> findAllByTag(OwnTag tag) {
        return this.productRepository.findAllByTagsContaining(tag);
    }

    public OwnProduct save(OwnProduct product) {
        return (OwnProduct)this.productRepository.save(product);
    }

    public Boolean deleteById(Long id) {
        if (this.exists(id)) {
            this.productRepository.deleteById(id);
            return !this.exists(id);
        } else {
            return false;
        }
    }

    private Boolean exists(Long id) {
        OwnProduct product = (OwnProduct)this.productRepository.findById(id).orElse((OwnProduct)null);
        return product != null;
    }
}