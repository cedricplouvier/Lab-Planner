package be.uantwerpen.labplanner.Repository;


import be.uantwerpen.labplanner.LabplannerApplication;
import be.uantwerpen.labplanner.Model.OwnProduct;
import be.uantwerpen.labplanner.Model.OwnTag;
import be.uantwerpen.labplanner.common.model.stock.Unit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = LabplannerApplication.class)
@WebAppConfiguration
public class ProductRepositoryTests {

    @Autowired
    private OwnProductRepository productRepository;

    @Autowired
    private OwnTagRepository tagRepository;


    @Test
    public void testSaveProduct(){

        //Setup products
        OwnProduct product = new OwnProduct();
        product.setName("testproduct");

        long precount = productRepository.count();




        //Save product, verify it has ID value after save
        assertNull(product.getId()); //make sure it is null before save
        productRepository.save(product);
        assertNotNull(product.getId());

        //fetch from db
        OwnProduct fetchedproduct = productRepository.findById(product.getId()).orElse(null);
        //should not be null
        assertNotNull(fetchedproduct);

        //should be equal
        assertEquals(product.getId(), fetchedproduct.getId());
        assertEquals(product.getName(), fetchedproduct.getName());

        //update desciption
        fetchedproduct.setDescription("blabljksdghhgjdskqgh");
        productRepository.save(fetchedproduct);
        OwnProduct fetchUpdatedProduct = productRepository.findById(fetchedproduct.getId()).orElse(null);
        assertEquals(fetchedproduct.getDescription(), fetchUpdatedProduct.getDescription());

        //update unitcost
        fetchedproduct.setUnitCost(2.0);
        productRepository.save(fetchedproduct);
        OwnProduct fetchUpdatedProduct2 = productRepository.findById(fetchedproduct.getId()).orElse(null);
        assertEquals(fetchedproduct.getUnitCost(), fetchUpdatedProduct2.getUnitCost());

        //update lowstocklevel
        fetchedproduct.setLowStockLevel(5.0);
        productRepository.save(fetchedproduct);
        OwnProduct fetchUpdatedProduct3 = productRepository.findById(fetchedproduct.getId()).orElse(null);
        assertEquals(fetchedproduct.getLowStockLevel(), fetchUpdatedProduct3.getLowStockLevel());

        //update reserved stock level
        fetchedproduct.setReservedStockLevel(5.0);
        productRepository.save(fetchedproduct);
        OwnProduct fetchUpdatedProduct4 = productRepository.findById(fetchedproduct.getId()).orElse(null);
        assertEquals(fetchedproduct.getReservedStockLevel(), fetchUpdatedProduct4.getReservedStockLevel());

        //update units
        fetchedproduct.setUnits(Unit.KILOGRAM);
        productRepository.save(fetchedproduct);
        OwnProduct fetchUpdatedProduct5 = productRepository.findById(fetchedproduct.getId()).orElse(null);
        assertEquals(fetchedproduct.getUnits(), fetchUpdatedProduct5.getUnits());

        //update imageurl
        fetchedproduct.setImageUrl("thisIsAVeryFancyURL");
        productRepository.save(fetchedproduct);
        OwnProduct fetchUpdatedProduct6 = productRepository.findById(fetchedproduct.getId()).orElse(null);
        assertEquals(fetchedproduct.getImageUrl(), fetchUpdatedProduct6.getImageUrl());

        //update properties
        fetchedproduct.setProperties("testproperties");
        productRepository.save(fetchedproduct);
        OwnProduct fetchUpdatedProduct7 = productRepository.findById(fetchedproduct.getId()).orElse(null);
        assertEquals(fetchedproduct.getProperties(), fetchUpdatedProduct7.getProperties());

        //update productcreator id
        fetchedproduct.setProductCreatorId(5L);
        productRepository.save(fetchedproduct);
        OwnProduct fetchUpdatedProduct8 = productRepository.findById(fetchedproduct.getId()).orElse(null);
        assertEquals(fetchedproduct.getProductCreatorId(), fetchUpdatedProduct8.getProductCreatorId());

        //update lastupdated by id
        fetchedproduct.setLastUpdatedById(5L);
        productRepository.save(fetchedproduct);
        OwnProduct fetchUpdatedProduct9 = productRepository.findById(fetchedproduct.getId()).orElse(null);
        assertEquals(fetchedproduct.getLastUpdatedById(), fetchUpdatedProduct9.getLastUpdatedById());


        //update tags
        List<OwnTag> taglist = new ArrayList<>();
        OwnTag tag1 = new OwnTag("test1");
        tagRepository.save(tag1);
        taglist.add(tag1);
        OwnTag tag2 = new OwnTag("test2");
        tagRepository.save(tag2);
        taglist.add(tag2);
        fetchedproduct.setTags(taglist);
        productRepository.save(fetchedproduct);
        OwnProduct fetchUpdatedProduct10 = productRepository.findById(fetchedproduct.getId()).orElse(null);
        assertEquals(fetchedproduct.getTags(), fetchUpdatedProduct10.getTags());


        //get all products, list should only have one more then initial
        Iterable<OwnProduct> products = productRepository.findAll();
        int count = 0;
        for(OwnProduct p : products){
            count++;
        }
        assertEquals(count, precount+1);

    }
}
