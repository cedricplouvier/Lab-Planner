package be.uantwerpen.labplanner.Repository;

import be.uantwerpen.labplanner.LabplannerApplication;
import be.uantwerpen.labplanner.Model.Composition;
import be.uantwerpen.labplanner.common.model.stock.Product;
import be.uantwerpen.labplanner.common.model.stock.Tag;
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


    @Test
    public void testSaveCompositions(){
        //Setup composition
        Composition composition  = new Composition();
        composition.setProduct(new Product());

        //Save tag, verify it has ID value after save
        assertNull(composition.getId()); //make sure it is null before save
        compositionRepository.save(composition);
        assertNotNull(composition.getId());



    }
}
