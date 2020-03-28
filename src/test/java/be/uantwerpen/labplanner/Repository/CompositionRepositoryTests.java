package be.uantwerpen.labplanner.Repository;

import be.uantwerpen.labplanner.LabplannerApplication;
import be.uantwerpen.labplanner.Model.Composition;
import be.uantwerpen.labplanner.common.model.stock.Product;
import be.uantwerpen.labplanner.common.model.stock.Tag;
import be.uantwerpen.labplanner.common.repository.stock.ProductRepository;
import be.uantwerpen.labplanner.common.repository.stock.TagRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.AutoConfigureDataJdbc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = LabplannerApplication.class)
@WebAppConfiguration
public class CompositionRepositoryTests {

    @Autowired
    private CompositionRepository compositionRepository;

    @Autowired
    private ProductRepository productRepository;


    @Test
    public void testSaveCompositions(){
        //Setup composition
        Composition composition  = new Composition();
        Product prod = new Product();
        prod.setName("test");
        productRepository.save(prod);
        composition.setProduct(prod);

        //Save tag, verify it has ID value after save
        assertNull(composition.getId()); //make sure it is null before save
        compositionRepository.save(composition);
        assertNotNull(composition.getId());

        //fetch from db
        Composition fetchedComp = compositionRepository.findById(composition.getId()).orElse(null);
        //should not be null
        assertNotNull(fetchedComp);

        //should be equal
        assertEquals(composition.getId(), fetchedComp.getId());
        assertEquals(composition.getProduct(), fetchedComp.getProduct());

        //update amount
        fetchedComp.setAmount(2.0);
        compositionRepository.save(fetchedComp);
        Composition fetchedComp2 = compositionRepository.findById(fetchedComp.getId()).orElse(null);
        assertEquals(fetchedComp.getAmount(), fetchedComp2.getAmount());

        //get all tag, list should only have one more then initial
        Iterable<Composition> comps = compositionRepository.findAll();
        int count = 0;
        for(Composition p : comps){
            count++;
        }
        assertEquals(count, 20); //omdat er al 22 compositions zijn in database

    }
}
