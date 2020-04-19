package be.uantwerpen.labplanner.Controller;


import be.uantwerpen.labplanner.LabplannerApplication;
import be.uantwerpen.labplanner.Model.Relation;
import be.uantwerpen.labplanner.Model.Step;
import be.uantwerpen.labplanner.Service.RelationService;
import be.uantwerpen.labplanner.Service.StepService;
import be.uantwerpen.labplanner.common.model.users.Role;
import be.uantwerpen.labplanner.common.model.users.User;
import be.uantwerpen.labplanner.common.service.users.RoleService;
import be.uantwerpen.labplanner.common.service.users.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get; //belangrijke imports
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


import java.util.*;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = LabplannerApplication.class)
@WebAppConfiguration
public class RelationControllerTests {

    @Mock
    private RelationService relationService;

    @Mock
    private UserService userService;

    @InjectMocks
    private RelationController relationController;
    private MockMvc mockMvc;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(this.relationController).build();
    }


    @Test
    //View User list
    public void ViewRelationListTest() throws Exception{
        Relation relation = new Relation("test");
        List<Relation> relations = new ArrayList<Relation>();
        relations.add(relation);

        when(relationService.findAll()).thenReturn(relations);
        mockMvc.perform(get("/usermanagement/users/relations"))
                .andExpect(status().isOk())
                .andExpect(view().name("Users/relation-list"))
                .andExpect(model().attribute("allRelations", hasSize(1)));

    }



    @Test
    // Show user manage page test
    public void ViewCreateRelationTest() throws Exception{
        Relation rel = new Relation("admin");
        long id = 10;
        rel.setId(id);

        mockMvc.perform(get("/usermanagement/users/relations/put"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("relation",instanceOf(Relation.class)))
                .andExpect(view().name("Users/relation-manage"));

    }


    @Test
    //TEst the validity of editing the user page
    public void viewEditRelationTest() throws Exception {
        Relation rel = new Relation("admin");
        long id = 10;
        rel.setId(id);
        List<Relation> rels = new ArrayList<Relation>();
        rels.add(rel);


        when(relationService.findById(id)).thenReturn(Optional.of(rel));

        //editing with existing id as input
        mockMvc.perform(get("/usermanagement/users/relations/{id}",10))
                .andExpect(status().isOk())
                .andExpect(view().name("Users/relation-manage"))
                .andDo(print());

        //wrong input
        mockMvc.perform(get("/usermanagement/users/relations/{id}","fff"))
                .andExpect(status().is4xxClientError())
                .andDo(print());

    }



    @Test
    public void addNonValidNewRelation() throws Exception{
        Relation relation = new Relation("test");
        User res = new User("res","res");
        Set<Role> roles = new HashSet<>();
        roles.add(new Role("Student"));
        res.setRoles(roles);

        relation.setResearcher(res);

        mockMvc.perform(post("/usermanagement/users/relations/").flashAttr("relation",relation))
                .andExpect(status().is(200))
                .andExpect(model().attribute("RelationError",notNullValue()))
                .andExpect(view().name("Users/relation-manage"))
                .andDo(print());

    }

    @Test
    public void addValidNewRelation() throws Exception{
        Relation relation = new Relation("test");
        User res = new User("res","res");
        Set<Role> roles = new HashSet<>();
        roles.add(new Role("Researcher"));
        res.setRoles(roles);

        relation.setResearcher(res);

        mockMvc.perform(post("/usermanagement/users/relations/").flashAttr("relation",relation))
                .andExpect(status().is(302))
                .andExpect(model().attribute("RelationError",nullValue()))
                .andExpect(view().name("redirect:/usermanagement/users/relations"))
                .andDo(print());

    }







    @Test
    //test for deleting
    public void DeleteUserTest() throws Exception{
        Relation rel = new Relation("delete");
        long id = 10;
        rel.setId(id);
        //User is not in Use
        mockMvc.perform(get("/usermanagement/users/relations/{id}/delete","11"))
                .andExpect(status().is(302))
                .andDo(print())
                .andExpect(model().attributeDoesNotExist())
                .andExpect(view().name("redirect:/usermanagement/users/relations"));

        //wrong url input
        mockMvc.perform(get("/usermanagement/users/relations/{id}/delete","ff"))
                .andExpect(status().is4xxClientError())
                .andDo(print());




    }



}
