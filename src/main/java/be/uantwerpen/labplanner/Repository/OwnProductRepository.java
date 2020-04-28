package be.uantwerpen.labplanner.Repository;


import be.uantwerpen.labplanner.Model.OwnProduct;
import java.util.List;

import be.uantwerpen.labplanner.Model.OwnTag;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnProductRepository extends CrudRepository<OwnProduct, Long> {
    List<OwnProduct> findAll();

    List<OwnProduct> findAllByTagsContaining(OwnTag tag);
}
