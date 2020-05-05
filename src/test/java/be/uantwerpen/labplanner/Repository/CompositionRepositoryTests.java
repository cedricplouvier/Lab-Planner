package be.uantwerpen.labplanner.Repository;

import be.uantwerpen.labplanner.LabplannerApplication;
import be.uantwerpen.labplanner.Model.Composition;
import be.uantwerpen.labplanner.Model.OwnProduct;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = LabplannerApplication.class)
@WebAppConfiguration
public class CompositionRepositoryTests {

    @Autowired
    private CompositionRepository compositionRepository;

    @Autowired
    private OwnProductRepository productRepository;


    @Test
    public void testSaveCompositions(){

        long precount = compositionRepository.count();


        //Setup composition
        Composition composition  = new Composition();
        OwnProduct prod = new OwnProduct();
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

        //get all compositions, list should only have one more then initial
        Iterable<Composition> comps = compositionRepository.findAll();
        int count = 0;
        for(Composition p : comps){
            count++;
        }
        assertEquals(count, precount+1);

    }
}
