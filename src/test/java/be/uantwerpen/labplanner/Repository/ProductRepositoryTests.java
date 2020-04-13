package be.uantwerpen.labplanner.Repository;


import be.uantwerpen.labplanner.LabplannerApplication;
import be.uantwerpen.labplanner.common.model.stock.Product;
import be.uantwerpen.labplanner.common.model.stock.Tag;
import be.uantwerpen.labplanner.common.model.stock.Unit;
import be.uantwerpen.labplanner.common.repository.stock.ProductRepository;
import be.uantwerpen.labplanner.common.repository.stock.TagRepository;
import org.apache.tomcat.jni.Local;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = LabplannerApplication.class)
@WebAppConfiguration
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TagRepository tagRepository;


    @Test
    public void testSaveProduct(){

        //Setup products
        Product product = new Product();
        product.setName("testproduct");

        long precount = productRepository.count();




        //Save product, verify it has ID value after save
        assertNull(product.getId()); //make sure it is null before save
        productRepository.save(product);
        assertNotNull(product.getId());

        //fetch from db
        Product fetchedproduct = productRepository.findById(product.getId()).orElse(null);
        //should not be null
        assertNotNull(fetchedproduct);

        //should be equal
        assertEquals(product.getId(), fetchedproduct.getId());
        assertEquals(product.getName(), fetchedproduct.getName());

        //update desciption
        fetchedproduct.setDescription("blabljksdghhgjdskqgh");
        productRepository.save(fetchedproduct);
        Product fetchUpdatedProduct = productRepository.findById(fetchedproduct.getId()).orElse(null);
        assertEquals(fetchedproduct.getDescription(), fetchUpdatedProduct.getDescription());

        //update unitcost
        fetchedproduct.setUnitCost(2.0);
        productRepository.save(fetchedproduct);
        Product fetchUpdatedProduct2 = productRepository.findById(fetchedproduct.getId()).orElse(null);
        assertEquals(fetchedproduct.getUnitCost(), fetchUpdatedProduct2.getUnitCost());

        //update lowstocklevel
        fetchedproduct.setLowStockLevel(5.0);
        productRepository.save(fetchedproduct);
        Product fetchUpdatedProduct3 = productRepository.findById(fetchedproduct.getId()).orElse(null);
        assertEquals(fetchedproduct.getLowStockLevel(), fetchUpdatedProduct3.getLowStockLevel());

        //update reserved stock level
        fetchedproduct.setReservedStockLevel(5.0);
        productRepository.save(fetchedproduct);
        Product fetchUpdatedProduct4 = productRepository.findById(fetchedproduct.getId()).orElse(null);
        assertEquals(fetchedproduct.getReservedStockLevel(), fetchUpdatedProduct4.getReservedStockLevel());

        //update units
        fetchedproduct.setUnits(Unit.KILOGRAM);
        productRepository.save(fetchedproduct);
        Product fetchUpdatedProduct5 = productRepository.findById(fetchedproduct.getId()).orElse(null);
        assertEquals(fetchedproduct.getUnits(), fetchUpdatedProduct5.getUnits());

        //update imageurl
        fetchedproduct.setImageUrl("thisIsAVeryFancyURL");
        productRepository.save(fetchedproduct);
        Product fetchUpdatedProduct6 = productRepository.findById(fetchedproduct.getId()).orElse(null);
        assertEquals(fetchedproduct.getImageUrl(), fetchUpdatedProduct6.getImageUrl());

        //update properties
        fetchedproduct.setProperties("testproperties");
        productRepository.save(fetchedproduct);
        Product fetchUpdatedProduct7 = productRepository.findById(fetchedproduct.getId()).orElse(null);
        assertEquals(fetchedproduct.getProperties(), fetchUpdatedProduct7.getProperties());

        //update productcreator id
        fetchedproduct.setProductCreatorId(5L);
        productRepository.save(fetchedproduct);
        Product fetchUpdatedProduct8 = productRepository.findById(fetchedproduct.getId()).orElse(null);
        assertEquals(fetchedproduct.getProductCreatorId(), fetchUpdatedProduct8.getProductCreatorId());

        //update lastupdated by id
        fetchedproduct.setLastUpdatedById(5L);
        productRepository.save(fetchedproduct);
        Product fetchUpdatedProduct9 = productRepository.findById(fetchedproduct.getId()).orElse(null);
        assertEquals(fetchedproduct.getLastUpdatedById(), fetchUpdatedProduct9.getLastUpdatedById());


        //update tags
        List<Tag> taglist = new ArrayList<>();
        Tag tag1 = new Tag("test1");
        tagRepository.save(tag1);
        taglist.add(tag1);
        Tag tag2 = new Tag("test2");
        tagRepository.save(tag2);
        taglist.add(tag2);
        fetchedproduct.setTags(taglist);
        productRepository.save(fetchedproduct);
        Product fetchUpdatedProduct10 = productRepository.findById(fetchedproduct.getId()).orElse(null);
        assertEquals(fetchedproduct.getTags(), fetchUpdatedProduct10.getTags());


        //get all products, list should only have one more then initial
        Iterable<Product> products = productRepository.findAll();
        int count = 0;
        for(Product p : products){
            count++;
        }
        assertEquals(count, precount+1);

    }
}
