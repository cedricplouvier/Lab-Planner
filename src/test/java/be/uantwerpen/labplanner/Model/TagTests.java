package be.uantwerpen.labplanner.Model;


import be.uantwerpen.labplanner.LabplannerApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest(classes = LabplannerApplication.class)
@WebAppConfiguration
public class TagTests {

    private OwnTag tag;

    @Test
    public void TestTag() throws Exception{
        tag = new OwnTag();
        assertNull(tag.getName());

        tag.setName("name");
        assertEquals(tag.getName(), "name");

        Long id = (long) 1;
        tag.setId(id);
        assertEquals(tag.getId(), id);

    }
}
