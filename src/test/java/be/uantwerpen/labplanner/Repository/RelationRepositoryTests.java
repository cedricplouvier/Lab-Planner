package be.uantwerpen.labplanner.Repository;


import be.uantwerpen.labplanner.LabplannerApplication;
import be.uantwerpen.labplanner.Model.Relation;
import be.uantwerpen.labplanner.common.model.users.User;
import be.uantwerpen.labplanner.common.repository.users.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration
@SpringBootTest(classes = LabplannerApplication.class)
@WebAppConfiguration
public class RelationRepositoryTests {

    @Autowired
    private RelationRepository relationRepository;

    @Autowired
    private UserRepository userRepository;


    @Test
    public void testRelationSave(){
        //create relation
        Relation relation = new Relation();
        relation.setDescription("logon_test");
        //relation.setResearcher((long) 1);

        long precount = relationRepository.count();
        //save product & varify id
        assertNull(relation.getId());
        relationRepository.save(relation);
        assertNotNull(relation.getId());

        //retrieve relation from database.
        Relation fetchedRelation = relationRepository.findById(relation.getId()).orElse(null);
        Relation test = relationRepository.findById((long) 42).orElse(null);
        assertNotNull(fetchedRelation);

        //and the fetched relation should equal the real relation
        assertEquals(fetchedRelation.getId(),relation.getId());
        assertEquals(fetchedRelation.getDescription(),relation.getDescription());

        //update name & desciption
        
        fetchedRelation.setDescription("This is the description");

        relationRepository.save(fetchedRelation);

        Relation fetchedUpdated = relationRepository.findById(fetchedRelation.getId()).orElse(null);

        assertEquals(fetchedUpdated.getDescription(),fetchedRelation.getDescription());
        assertEquals(fetchedUpdated.getStudents().size(),0);


        assertEquals(relationRepository.count(),precount+1);

        int count = 0;
        for (Relation p : relationRepository.findAll()) {
            count++;
        }

        assertEquals(count,precount+1);

        //delete user and check if still in database
        relationRepository.deleteById(fetchedUpdated.getId());

        //check for delete if nnot exists
        assertThrows(EmptyResultDataAccessException.class,()->{relationRepository.deleteById(fetchedUpdated.getId());});


        assertEquals(relationRepository.count(),precount);
        assertNull(relationRepository.findById(fetchedUpdated.getId()).orElse(null));
    }




}
