package be.uantwerpen.labplanner.Model;

import be.uantwerpen.labplanner.LabplannerApplication;
import be.uantwerpen.labplanner.common.model.users.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = LabplannerApplication.class)
@WebAppConfiguration
public class RelationTests {

    private Relation relation;

    @Test
    public void TestRelation() throws Exception{
        relation = new Relation();
        assertNull(relation.getResearcher());
        assertNull(relation.getId());
        assertNull(relation.getStudents());
        assertNull(relation.getDescription());
        String description = "test";
        relation = new Relation(description);
        assertNull(relation.getResearcher());
        assertNull(relation.getId());
        assertNull(relation.getStudents());
        assertNotNull(relation.getDescription());

        User u = new User("tester","tester");
        relation.setResearcher(u);
        Long id = (long) 1;
        relation.setId(id);

        Set<User> studs = new HashSet<>();
        studs.add(u);
        relation.setStudents(studs);

        assertEquals(relation.getResearcher(),u);
        assertEquals(relation.getId(),id);
        assertEquals(relation.getStudents(),studs);
        assertEquals(relation.getDescription(),description);



    }

}
