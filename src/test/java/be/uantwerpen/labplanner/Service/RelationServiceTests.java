package be.uantwerpen.labplanner.Service;

import be.uantwerpen.labplanner.LabplannerApplication;
import be.uantwerpen.labplanner.Model.Relation;
import be.uantwerpen.labplanner.Repository.RelationRepository;
import be.uantwerpen.labplanner.common.model.users.User;
import be.uantwerpen.labplanner.common.repository.users.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = LabplannerApplication.class)
@WebAppConfiguration
public class RelationServiceTests {
    @Mock
    private RelationRepository relationRepository;

    @InjectMocks
    private RelationService relationService;

    @BeforeEach
    public void init(){



        MockitoAnnotations.initMocks(this);

    }

    @Test
    public void findAllTest() throws Exception{

        Relation relation1 = new Relation("relatie 1");
        Relation relation2 = new Relation("relatie 1");

        User res1 = new User("res1","res1");
        User res2 = new User("res2","res2");
        User stud1 = new User("res1","res1");
        User stud2 = new User("res2","res2");
        User stud3 = new User("res1","res1");
        User stud4= new User("res2","res2");

        Set<User> students1 = new HashSet<>();
        students1.add(stud1);
        students1.add(stud2);

        Set<User> students2 = new HashSet<>();
        students2.add(stud3);
        students2.add(stud4);

        relation1.setResearcher(res1);
        relation2.setResearcher(res2);
        relation1.setStudents(students1);
        relation2.setStudents(students2);

        relationRepository.save(relation1);
        relationRepository.save(relation2);
        when(relationRepository.findAll()).thenReturn(Arrays.asList(relation1,relation2));
        List<Relation> allRelations = relationService.findAll();
        assertEquals(allRelations.size(),2);


    }

    @Test
    public void FinByResearcherTest() throws Exception{
        Relation relation1 = new Relation("relatie 1");
        Relation relation2 = new Relation("relatie 1");

        User res1 = new User("res1","res1");
        User res2 = new User("res2","res2");
        User stud1 = new User("res1","res1");
        User stud2 = new User("res2","res2");
        User stud3 = new User("res1","res1");
        User stud4= new User("res2","res2");

        Set<User> students1 = new HashSet<>();
        students1.add(stud1);
        students1.add(stud2);

        Set<User> students2 = new HashSet<>();
        students2.add(stud3);
        students2.add(stud4);

        relation1.setResearcher(res1);
        relation2.setResearcher(res2);
        relation1.setStudents(students1);
        relation2.setStudents(students2);

        relationRepository.save(relation1);
        relationRepository.save(relation2);

        when(relationRepository.findByResearcher(res1)).thenReturn(Optional.of(relation1));
        assertEquals(Optional.of(relation1),relationService.findByResearcher(res1));
    }

    @Test
    public void DeleteTest() throws Exception{
        Relation relation1 = new Relation("relatie 1");
        Relation relation2 = new Relation("relatie 1");

        User res1 = new User("res1","res1");
        User res2 = new User("res2","res2");
        User stud1 = new User("res1","res1");
        User stud2 = new User("res2","res2");
        User stud3 = new User("res1","res1");
        User stud4= new User("res2","res2");

        Set<User> students1 = new HashSet<>();
        students1.add(stud1);
        students1.add(stud2);

        Set<User> students2 = new HashSet<>();
        students2.add(stud3);
        students2.add(stud4);

        relation1.setResearcher(res1);
        relation2.setResearcher(res2);
        relation1.setStudents(students1);
        relation2.setStudents(students2);

        relationRepository.save(relation1);
        relation1.setId((long) 1);
        relationRepository.save(relation2);

        when(relationRepository.findById((long) 1)).thenReturn(Optional.of(relation1));
        relationService.delete(1);

    }

    @Test
    public void DeleteByIdFalseTest() throws Exception{
        Relation relation1 = new Relation("relatie 1");
        Relation relation2 = new Relation("relatie 1");

        User res1 = new User("res1","res1");
        User res2 = new User("res2","res2");
        User stud1 = new User("res1","res1");
        User stud2 = new User("res2","res2");
        User stud3 = new User("res1","res1");
        User stud4= new User("res2","res2");

        Set<User> students1 = new HashSet<>();
        students1.add(stud1);
        students1.add(stud2);

        Set<User> students2 = new HashSet<>();
        students2.add(stud3);
        students2.add(stud4);

        relation1.setResearcher(res1);
        relation2.setResearcher(res2);
        relation1.setStudents(students1);
        relation2.setStudents(students2);

        relationRepository.save(relation1);
        relation1.setId((long) 1);
        relationRepository.save(relation2);

        //deleted does not exist
        when(relationRepository.findById((long) 1)).thenReturn(Optional.empty());
        assertEquals(relationService.deleteById((long) 1),false);

    }

    @Test
    public void DeleteByIdCorrectTest() throws Exception{
        Relation relation1 = new Relation("relatie 1");
        Relation relation2 = new Relation("relatie 1");

        User res1 = new User("res1","res1");
        User res2 = new User("res2","res2");
        User stud1 = new User("res1","res1");
        User stud2 = new User("res2","res2");
        User stud3 = new User("res1","res1");
        User stud4= new User("res2","res2");

        Set<User> students1 = new HashSet<>();
        students1.add(stud1);
        students1.add(stud2);

        Set<User> students2 = new HashSet<>();
        students2.add(stud3);
        students2.add(stud4);

        relation1.setResearcher(res1);
        relation2.setResearcher(res2);
        relation1.setStudents(students1);
        relation2.setStudents(students2);

        relationRepository.save(relation1);
        relation1.setId((long) 1);
        relationRepository.save(relation2);

        //deleted does not exist
        when(relationRepository.findById((long) 1)).thenReturn(Optional.of(relation1));
        assertEquals(relationService.deleteById((long) 1),false);

    }


}
