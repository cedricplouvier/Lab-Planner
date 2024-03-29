package be.uantwerpen.labplanner.Repository;

import be.uantwerpen.labplanner.LabplannerApplication;
import be.uantwerpen.labplanner.Model.Composition;
import be.uantwerpen.labplanner.Model.Mixture;
import be.uantwerpen.labplanner.Model.OwnProduct;
import be.uantwerpen.labplanner.Model.OwnTag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.transaction.Transactional;
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
    private OwnProductRepository productRepository;

    @Autowired
    private OwnTagRepository tagRepository;
    @Transactional
    @Test
    public void testSaveMixtures(){


        long precount = mixtureRepository.count();


        OwnProduct prod1 = new OwnProduct();
        prod1.setName("testproduct1");
        productRepository.save(prod1);

        OwnProduct prod2 = new OwnProduct();
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

        //update image
        fetchedMix.setImage("testImage");
        mixtureRepository.save(fetchedMix);
        Mixture fetchedMix6 = mixtureRepository.findById(fetchedMix.getId()).orElse(null);
        assertEquals(fetchedMix.getImage(), fetchedMix6.getImage());

        //update document
        fetchedMix.setDocument("testdoc");
        mixtureRepository.save(fetchedMix);
        Mixture fetchedMix9 = mixtureRepository.findById(fetchedMix.getId()).orElse(null);
        assertEquals(fetchedMix.getDocument(), fetchedMix9.getDocument());

        //update tag
        List<OwnTag> tags  = new ArrayList<>();
        OwnTag t1 = new OwnTag("test");
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
