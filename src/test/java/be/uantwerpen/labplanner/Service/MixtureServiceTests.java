package be.uantwerpen.labplanner.Service;


import be.uantwerpen.labplanner.LabplannerApplication;
import be.uantwerpen.labplanner.Model.Composition;
import be.uantwerpen.labplanner.Model.Mixture;
import be.uantwerpen.labplanner.Repository.MixtureRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = LabplannerApplication.class)
@WebAppConfiguration
public class MixtureServiceTests {

    @Autowired
    MixtureService mixtureService;

    @Autowired
    MixtureRepository mixtureRepository;

    @Test
    public void testGetMixtureById(){
        Mixture mixture = new Mixture();
        mixtureRepository.save(mixture);
        Mixture fetched = mixtureService.findById(mixture.getId()).orElse(null);
        assertNotNull(fetched);
    }

    @Test
    public void testGetMixtureByName(){
        Mixture mixture = new Mixture();
        mixture.setName("test");
        mixtureRepository.save(mixture);
        Mixture fetched = mixtureService.findByName("test").orElse(null);
        assertNotNull(fetched);
    }

    @Test
    public void testDeleteById(){
        Mixture mixture = new Mixture();
        mixtureService.save(mixture );
        List<Mixture> fetchedList  = mixtureService.findAll();
        mixtureService.deleteById(mixture.getId());
        List<Mixture> fetchedList2  = mixtureService.findAll();
        assertEquals(fetchedList.size()-1, fetchedList2.size());
    }

    @Test
    public void testDeleteByComposition(){
        Mixture mixture = new Mixture();
        mixtureService.save(mixture );
        List<Mixture> fetchedList  = mixtureService.findAll();
        mixtureService.delete(mixture);
        List<Mixture> fetchedList2  = mixtureService.findAll();
        assertEquals(fetchedList.size()-1, fetchedList2.size());
    }

    @Test
    public void testGetAll(){
        List<Mixture> list = mixtureService.findAll();
         assertNotNull(list);
    }


}
