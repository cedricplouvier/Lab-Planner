package be.uantwerpen.labplanner.Repository;

import be.uantwerpen.labplanner.LabplannerApplication;
import be.uantwerpen.labplanner.Model.Composition;
import be.uantwerpen.labplanner.Model.Mixture;
import be.uantwerpen.labplanner.common.model.stock.Product;
import be.uantwerpen.labplanner.common.model.stock.Tag;
import be.uantwerpen.labplanner.common.repository.stock.ProductRepository;
import be.uantwerpen.labplanner.common.repository.stock.TagRepository;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = LabplannerApplication.class)
@WebAppConfiguration
public class MixtureRepositoryTests {

    @Autowired
    private MixtureRepository mixtureRepository;

    @Autowired
    private CompositionRepository compositionRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TagRepository tagRepository;

    @Test
    public void testSaveMixtures(){


        long precount = mixtureRepository.count();


        Product prod1 = new Product();
        prod1.setName("testproduct1");
        productRepository.save(prod1);

        Product prod2 = new Product();
        prod2.setName("testproduct2");
        productRepository.save(prod2);

        Composition comp1 = new Composition();
        comp1.setProduct(prod1);
        comp1.setAmount(2.0);
        compositionRepository.save(comp1);

        Composition comp2 = new Composition();
        comp2.setProduct(prod2);
        comp2.setAmount(5.0);
        compositionRepository.save(comp2);

        List<Composition> compositions = new ArrayList<>();
        compositions.add(comp1);
        compositions.add(comp2);

        Mixture mix = new Mixture();

        mix.setName("TestMixture");

        //Save mixture, verify it has ID value after save
        assertNull(mix.getId()); //make sure it is null before save
        mixtureRepository.save(mix);
        assertNotNull(mix.getId());

        //fetch from db
        Mixture fetchedMix = mixtureRepository.findById(mix.getId()).orElse(null);
        //should not be null
        assertNotNull(fetchedMix);

        //should be equal
        assertEquals(mix.getId(), fetchedMix.getId());
        assertEquals(mix.getName(), fetchedMix.getName());

        //update name
        fetchedMix.setName("testname2");
        mixtureRepository.save(fetchedMix);
        Mixture fetchedMix2 = mixtureRepository.findById(fetchedMix.getId()).orElse(null);
        assertEquals(fetchedMix.getName(), fetchedMix2.getName());


        //update compositions
        fetchedMix.setCompositions(compositions);
        mixtureRepository.save(fetchedMix);
        Mixture fetchedMix3 = mixtureRepository.findById(fetchedMix.getId()).orElse(null);
        assertEquals(fetchedMix.getCompositions(), fetchedMix3.getCompositions());

        //update description
        fetchedMix.setDescription("testdecription");
        mixtureRepository.save(fetchedMix);
        Mixture fetchedMix4 = mixtureRepository.findById(fetchedMix.getId()).orElse(null);
        assertEquals(fetchedMix.getDescription(), fetchedMix4.getDescription());

        //update tag
        List<Tag> tags  = new ArrayList<>();
        Tag t1 = new Tag("test");
        tagRepository.save(t1);
        tags.add(t1);
        fetchedMix.setTags(tags);
        mixtureRepository.save(fetchedMix);
        Mixture fetchedMix5 = mixtureRepository.findById(fetchedMix.getId()).orElse(null);
        assertEquals(fetchedMix.getTags(), fetchedMix5.getTags());


        //get all tag, list should only have one more then initial
        Iterable<Mixture> mixtures = mixtureRepository.findAll();
        int count = 0;
        for(Mixture p : mixtures){
            count++;
        }
        assertEquals(count, precount+1);





    }


}