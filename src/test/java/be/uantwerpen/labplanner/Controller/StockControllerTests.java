package be.uantwerpen.labplanner.Controller;

import be.uantwerpen.labplanner.LabplannerApplication;
import be.uantwerpen.labplanner.Model.Composition;
import be.uantwerpen.labplanner.Model.DatabaseLoader;
import be.uantwerpen.labplanner.Service.CompositionService;
import be.uantwerpen.labplanner.Service.MixtureService;
import be.uantwerpen.labplanner.common.model.stock.Product;
import be.uantwerpen.labplanner.common.model.stock.Tag;
import be.uantwerpen.labplanner.common.model.stock.Unit;
import be.uantwerpen.labplanner.common.service.stock.ProductService;
import be.uantwerpen.labplanner.common.service.stock.TagService;
import be.uantwerpen.labplanner.common.service.users.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.swing.text.html.Option;
import javax.swing.tree.ExpandVetoException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = LabplannerApplication.class)
@WebAppConfiguration
public class StockControllerTests {


    @Mock
    private UserService userService;

    @Mock
    private ProductService productService;

    @Mock
    private TagService tagService;

    @Mock
    private MixtureService mixtureService;

    @Mock
    private CompositionService compositionService;

    @InjectMocks
    private StockController stockController;

    private MockMvc mockMvc;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(stockController).build();
    }

    @Test
    public void viewProductListTest() throws Exception{
        mockMvc.perform(get("/products")).andExpect(view().name("/Stock/products-list"));
    }

    @Test
    public void viewProductInfoTest() throws Exception{
        mockMvc.perform(get("/products/info/127")).andExpect(view().name("/Stock/products-info"));
    }

    @Test
    public void viewProductManageTest() throws Exception{
        mockMvc.perform(get("/products/127")).andExpect(view().name("/Stock/products-manage"));
    }

    @Test
    public void viewProductCreateTest() throws Exception{
        mockMvc.perform(get("/products/put")).andExpect(view().name("/Stock/products-manage"));
    }

    @Test
    public void viewTagListTest() throws Exception{
        mockMvc.perform(get("/tags")).andExpect(view().name("/Tags/tags-list"));
    }

    @Test
    public void viewTagManageTest() throws Exception{
        mockMvc.perform(get("/tags/122")).andExpect(view().name("/Tags/tags-manage"));
    }

    @Test
    public void viewTagCreateTest() throws Exception{
        mockMvc.perform(get("/tags/put")).andExpect(view().name("/Tags/tags-manage"));
    }

    @Test
    public void viewMixturesListTest() throws Exception{
        mockMvc.perform(get("/mixtures")).andExpect(view().name("/Mixtures/mixtures-list"));
    }

    @Test
    public void viewMixturesManageTest() throws Exception{
        mockMvc.perform(get("/mixtures/158")).andExpect(view().name("/Mixtures/mixtures-manage"));
    }

    @Test
    public void viewMixtureCreateTest() throws Exception{
        mockMvc.perform(get("/mixtures/put")).andExpect(view().name("/Mixtures/mixtures-manage"));
    }

    @Test
    public void viewCompositionsListTest() throws Exception{
        mockMvc.perform(get("/compositions")).andExpect(view().name("/Mixtures/compositions-list"));
    }

    @Test
    public void viewCompositionsManageTest() throws Exception{
        mockMvc.perform(get("/compositions/139")).andExpect(view().name("/Mixtures/compositions-manage"));
    }

    @Test
    public void viewCompositionsCreateTest() throws Exception{
        mockMvc.perform(get("/compositions/put")).andExpect(view().name("/Mixtures/compositions-manage"));
    }


    @Test
    public void addNonValidProduct() throws Exception{

        Product prod = new Product();
        long id = 555;
        prod.setId(id);

        //empty name String
        prod.setName("");
        mockMvc.perform(post("/products/").flashAttr("product",prod))
                .andExpect(model().attribute("errormessage", notNullValue()))
                .andExpect(view().name("/Stock/products-manage"))
                .andDo(print());

        //empty  description
        prod.setName("testname");
        prod.setDescription("");
        mockMvc.perform(post("/products/").flashAttr("product",prod))
                .andExpect(model().attribute("errormessage", notNullValue()))
                .andExpect(view().name("/Stock/products-manage"))
                .andDo(print());

        //empty properties
        prod.setDescription("blablabla");
        prod.setProperties("");
        mockMvc.perform(post("/products/").flashAttr("product",prod))
                .andExpect(model().attribute("errormessage", notNullValue()))
                .andExpect(view().name("/Stock/products-manage"))
                .andDo(print());

        //invalid stock
        prod.setProperties("thisareproperties");
        prod.setStockLevel(-1.0);
        prod.setLowStockLevel(-1.0);
        prod.setReservedStockLevel(-1.0);
        mockMvc.perform(post("/products/").flashAttr("product",prod))
                .andExpect(model().attribute("errormessage", notNullValue()))
                .andExpect(view().name("/Stock/products-manage"))
                .andDo(print());

        //empty tag
        prod.setProperties("thisareproperties");
        prod.setStockLevel(5.0);
        prod.setLowStockLevel(1.0);
        prod.setReservedStockLevel(1.0);
        List<Tag > tags = new ArrayList<>();
        //tags is empty list
        prod.setTags(tags);
        mockMvc.perform(post("/products/").flashAttr("product",prod))
                .andExpect(model().attribute("errormessage", notNullValue()))
                .andExpect(view().name("/Stock/products-manage"))
                .andDo(print());

    }

    @Test
    public void addValidProduct() throws  Exception{
        Tag t1 = new Tag("test");
        List<Tag > tags = new ArrayList<>();
        tags.add(t1);
        Product prod = new Product("placeholder1","description",1.0, 2000.0, 200.0, 1.0, Unit.KILOGRAM, "locatie2", "props", 5L,5L, LocalDateTime.now(), LocalDateTime.now(), tags);
        mockMvc.perform(post("/products/").flashAttr("product",prod))
                .andExpect(view().name("redirect:/products"))
                .andDo(print());
    }



    @Test
    public void addValidTag() throws Exception{
        Tag t1 = new Tag("test");
        mockMvc.perform(post("/tags/").flashAttr("tag",t1))
                .andExpect(view().name("redirect:/tags"))
                .andDo(print());
    }

    @Test
    public void addNonValidTag() throws Exception{

        //name is empty
        Tag t1 = new Tag("");
        mockMvc.perform(post("/tags/").flashAttr("tag",t1))
                .andExpect(model().attribute("errormessage", notNullValue()))
                .andExpect(view().name("Tags/tags-manage"))
                .andDo(print());

    }

    @Test
    public void addDuplicateNameTag() throws Exception{

        Tag tag = new Tag("test1");
        long id = 5;
        tag.setId(id);

        Tag tag2 = new Tag("test2");

        when(tagService.findById(id)).thenReturn(Optional.of(tag2));
        when(tagService.findByName("test1")).thenReturn(Optional.of(tag));

        mockMvc.perform(post("/tags/{id", "5").flashAttr("tag",tag))
                .andExpect(model().attribute("errormessage", notNullValue()))
                .andExpect(view().name("Tags/tags-manage"))
                .andDo(print());

    }


}
