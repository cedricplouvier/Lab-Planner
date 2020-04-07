package be.uantwerpen.labplanner.Service;

import be.uantwerpen.labplanner.LabplannerApplication;
import be.uantwerpen.labplanner.common.model.stock.Product;
import be.uantwerpen.labplanner.common.model.stock.Tag;
import be.uantwerpen.labplanner.common.repository.stock.TagRepository;
import be.uantwerpen.labplanner.common.service.stock.TagService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest(classes = LabplannerApplication.class)
@WebAppConfiguration
public class TagServiceTests {

    @Autowired
    private TagService tagService;

    @Autowired
    private TagRepository tagRepository;

    @Test
    public void testGetTagById(){
        Tag tag = new Tag("test");
        tagRepository.save(tag);
        Tag fetched = tagService.findById(tag.getId()).orElse(null);
        assertEquals(fetched.getName(), "test");

    }

    @Test
    public void testGetTagByName(){
        Tag tag = new Tag("test");
        tagRepository.save(tag);
        Tag fetched = tagService.findByName("test").orElse(null);
        assertEquals(fetched.getId(), tag.getId());

    }

    @Test
    public void testGetAllTags(){
        List<Tag> fetchedList  = tagService.findAll();
        assertNotNull(fetchedList);
    }

}
