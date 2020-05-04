package be.uantwerpen.labplanner.Service;

import be.uantwerpen.labplanner.LabplannerApplication;
import be.uantwerpen.labplanner.Model.OwnTag;
import be.uantwerpen.labplanner.Model.Report;
import be.uantwerpen.labplanner.Repository.OwnTagRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest(classes = LabplannerApplication.class)
@WebAppConfiguration
public class TagServiceTests {

    @Autowired
    private OwnTagService tagService;

    @Autowired
    private OwnTagRepository tagRepository;

    @Test
    public void testGetTagById(){
        OwnTag tag = new OwnTag("testID");
        tagRepository.save(tag);
        OwnTag fetched = tagService.findById(tag.getId()).orElse(null);
        assertEquals(fetched.getName(), "testID");

    }

    @Test
    public void testGetTagByName(){
        OwnTag tag = new OwnTag("testTagForThisTest");
        tagRepository.save(tag);
        OwnTag fetched = tagService.findByName("testTagForThisTest").orElse(null);
        assertEquals(fetched.getId(), tag.getId());

    }

    @Test
    public void testGetAllTags(){
        List<OwnTag> fetchedList  = tagService.findAll();
        assertNotNull(fetchedList);
    }

    @Test
    public void testDelete(){
        OwnTag tag = new OwnTag("testID");
        tagService.save(tag);
        List<OwnTag> fetchedList  =tagService.findAll();
        tagService.delete(tag);
        List<OwnTag> fetchedList2 = tagService.findAll();
        assertEquals(fetchedList.size()-1, fetchedList2.size());

    }

    @Test
    public void testDeleteById(){
        OwnTag tag = new OwnTag("testID");
        tagService.save(tag );
        List<OwnTag> fetchedList  =tagService.findAll();
        tagService.deleteById(tag.getId());
        List<OwnTag> fetchedList2 = tagService.findAll();
        assertEquals(fetchedList.size()-1, fetchedList2.size());
    }

    @Test
    public void testDeleteByNonexistingId(){
        List<OwnTag> fetchedList  =tagService.findAll();
        Long id = 5974631L;
        tagService.deleteById(id);
        List<OwnTag> fetchedList2 = tagService.findAll();
        assertEquals(fetchedList.size(), fetchedList2.size());
    }

}
