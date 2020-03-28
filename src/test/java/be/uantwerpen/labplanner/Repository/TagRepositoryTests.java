
package be.uantwerpen.labplanner.Repository;

import be.uantwerpen.labplanner.LabplannerApplication;

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
public class TagRepositoryTests {

    @Autowired
    private TagRepository tagRepository;


    @Test
    public void testSaveTags(){
        //Setup tag
        Tag tag = new Tag();
        tag.setName("testTag");

        //Save tag, verify it has ID value after save
        assertNull(tag.getId()); //make sure it is null before save
        tagRepository.save(tag);
        assertNotNull(tag.getId());

        //fetch from db
        Tag fetchedTag = tagRepository.findById(tag.getId()).orElse(null);
        //should not be null
        assertNotNull(fetchedTag);

        //should be equal
        assertEquals(tag.getId(), fetchedTag.getId());
        assertEquals(tag.getName(), fetchedTag.getName());




        //get all tag, list should only have one more then initial
        Iterable<Tag> tags = tagRepository.findAll();
        int count = 0;
        for(Tag p : tags){
            count++;
        }
        assertEquals(count, 6); //omdat er al 4 tags zijn in database
        //NOTE: change to actual: 7 if you're running all the tests at once.

    }
}
