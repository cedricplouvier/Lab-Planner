package be.uantwerpen.labplanner.Service;


import be.uantwerpen.labplanner.LabplannerApplication;
import be.uantwerpen.labplanner.Model.Composition;
import be.uantwerpen.labplanner.Model.Mixture;
import be.uantwerpen.labplanner.Repository.CompositionRepository;
import be.uantwerpen.labplanner.common.model.stock.Product;
import org.junit.jupiter.api.Test;
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
public class CompositionServiceTests {

    @Autowired
    private CompositionService compositionService;

    @Autowired
    private CompositionRepository compositionRepository;

    @Test
    public void testGetCompositionById(){
        Composition composition = new Composition();
        compositionRepository.save(composition);
        Composition fetched = compositionService.findById(composition.getId()).orElse(null);
        assertNotNull(fetched);
    }

    @Test
    public void testDeleteById(){
        Composition composition = new Composition();
        compositionService.save(composition );
        List<Composition> fetchedList  = compositionService.findAll();
        compositionService.deleteById(composition.getId());
        List<Composition> fetchedList2  = compositionService.findAll();
        assertEquals(fetchedList.size()-1, fetchedList2.size());
    }

    @Test
    public void testDeleteByNonexistingId(){
        List<Composition> fetchedList  = compositionService.findAll();
        Long id = 5974631L;
        compositionService.deleteById(id);
        List<Composition> fetchedList2  = compositionService.findAll();
        assertEquals(fetchedList.size(), fetchedList2.size());
    }


    @Test
    public void testDeleteByComposition(){
        Composition composition = new Composition();
        compositionService.save(composition );
        List<Composition> fetchedList  = compositionService.findAll();
        compositionService.delete(composition);
        List<Composition> fetchedList2  = compositionService.findAll();
        assertEquals(fetchedList.size()-1, fetchedList2.size());
    }

    @Test
    public void testGetAll(){
        List<Composition> list = compositionService.findAll();
        assertNotNull(list);
    }


}
