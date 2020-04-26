package be.uantwerpen.labplanner.Controller;

import be.uantwerpen.labplanner.LabplannerApplication;
import be.uantwerpen.labplanner.Model.Composition;
import be.uantwerpen.labplanner.Model.Mixture;
import be.uantwerpen.labplanner.Model.OwnProduct;
import be.uantwerpen.labplanner.Model.OwnTag;
import be.uantwerpen.labplanner.Service.CompositionService;
import be.uantwerpen.labplanner.Service.MixtureService;
import be.uantwerpen.labplanner.Service.OwnProductService;
import be.uantwerpen.labplanner.Service.OwnTagService;
import be.uantwerpen.labplanner.common.model.stock.Unit;
import be.uantwerpen.labplanner.common.service.users.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = LabplannerApplication.class)
@WebAppConfiguration
public class StockControllerTests {


    @Mock
    private UserService userService;

    @Mock
    private OwnProductService productService;

    @Mock
    private OwnTagService tagService;

    @Mock
    private MixtureService mixtureService;

    @Mock
    private CompositionService compositionService;

    @InjectMocks
    private StockController stockController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(stockController).build();
    }


    @Test
    public void addNonValidProduct() throws Exception{

        OwnProduct prod = new OwnProduct();
        long id = 555;
        prod.setId(id);

        //empty name String
        prod.setName("");
        mockMvc.perform(post("/products/").flashAttr("product",prod))
                .andExpect(model().attribute("errormessage", notNullValue()))
                .andExpect(view().name("Stock/products-manage"))
                .andDo(print());

        //empty  description
        prod.setName("testname");
        prod.setDescription("");
        mockMvc.perform(post("/products/").flashAttr("product",prod))
                .andExpect(model().attribute("errormessage", notNullValue()))
                .andExpect(view().name("Stock/products-manage"))
                .andDo(print());

        //empty properties
        prod.setDescription("blablabla");
        prod.setProperties("");
        mockMvc.perform(post("/products/").flashAttr("product",prod))
                .andExpect(model().attribute("errormessage", notNullValue()))
                .andExpect(view().name("Stock/products-manage"))
                .andDo(print());

        //invalid stock
        prod.setProperties("thisareproperties");
        prod.setStockLevel(-1.0);
        prod.setLowStockLevel(-1.0);
        prod.setReservedStockLevel(-1.0);
        mockMvc.perform(post("/products/").flashAttr("product",prod))
                .andExpect(model().attribute("errormessage", notNullValue()))
                .andExpect(view().name("Stock/products-manage"))
                .andDo(print());

        //empty tag
        prod.setProperties("thisareproperties");
        prod.setStockLevel(5.0);
        prod.setLowStockLevel(1.0);
        prod.setReservedStockLevel(1.0);
        List<OwnTag> tags = new ArrayList<>();
        //tags is empty list
        prod.setTags(tags);
        mockMvc.perform(post("/products/").flashAttr("product",prod))
                .andExpect(model().attribute("errormessage", notNullValue()))
                .andExpect(view().name("Stock/products-manage"))
                .andDo(print());

    }

    @Test
    public void addValidProduct() throws  Exception{
        OwnTag t1 = new OwnTag("test");
        List<OwnTag> tags = new ArrayList<>();
        tags.add(t1);
        OwnProduct prod = new OwnProduct("placeholder1","description",1.0, 2000.0, 200.0, 1.0, Unit.KILOGRAM, "locatie2", "props", 5L,5L, LocalDateTime.now(), LocalDateTime.now(), tags,null);
        mockMvc.perform(post("/products/").flashAttr("product",prod))
                .andExpect(view().name("redirect:/products"))
                .andDo(print());
    }

    @Test
    public void EditValidProduct() throws Exception{
        OwnTag t1 = new OwnTag("test");
        List<OwnTag> tags = new ArrayList<>();
        tags.add(t1);
        OwnProduct prod = new OwnProduct("placeholder1","description",1.0, 2000.0, 200.0, 1.0, Unit.KILOGRAM, "locatie2", "props", 5L,5L, LocalDateTime.now(), LocalDateTime.now(), tags,null);
        long id = 10;

        when(productService.findById(id)).thenReturn(Optional.of(prod));
        mockMvc.perform(post("/products/{id}","10").flashAttr("product",prod))
                .andExpect(view().name("redirect:/products"))
                .andDo(print());

    }

    @Test
    public void EditInvalidNameProduct() throws Exception{
        OwnTag t1 = new OwnTag("test");
        List<OwnTag> tags = new ArrayList<>();
        tags.add(t1);
        OwnProduct prod = new OwnProduct("","description",1.0, 2000.0, 200.0, 1.0, Unit.KILOGRAM, "locatie2", "props", 5L,5L, LocalDateTime.now(), LocalDateTime.now(), tags,null);
        long id = 10;

        when(productService.findById(id)).thenReturn(Optional.of(prod));
        mockMvc.perform(post("/products/{id}","10").flashAttr("product",prod))
                .andExpect(model().attribute("errormessage", notNullValue()))
                .andExpect(view().name("Stock/products-manage"))
                .andDo(print());

    }

    @Test
    public void EditInvalidDescriptionProduct() throws Exception{
        OwnTag t1 = new OwnTag("test");
        List<OwnTag> tags = new ArrayList<>();
        tags.add(t1);
        OwnProduct prod = new OwnProduct("test","",1.0, 2000.0, 200.0, 1.0, Unit.KILOGRAM, "locatie2", "props", 5L,5L, LocalDateTime.now(), LocalDateTime.now(), tags, null);
        long id = 10;

        when(productService.findById(id)).thenReturn(Optional.of(prod));
        mockMvc.perform(post("/products/{id}","10").flashAttr("product",prod))
                .andExpect(model().attribute("errormessage", notNullValue()))
                .andExpect(view().name("Stock/products-manage"))
                .andDo(print());

    }

    @Test
    public void EditInvalidTagsProduct() throws Exception{
        //Taglist is empty
        OwnTag t1 = new OwnTag("test");
        List<OwnTag> tags = new ArrayList<>();
        OwnProduct prod = new OwnProduct("test","description",1.0, 2000.0, 200.0, 1.0, Unit.KILOGRAM, "locatie2", "props", 5L,5L, LocalDateTime.now(), LocalDateTime.now(), tags, null);
        long id = 10;

        when(productService.findById(id)).thenReturn(Optional.of(prod));
        mockMvc.perform(post("/products/{id}","10").flashAttr("product",prod))
                .andExpect(model().attribute("errormessage", notNullValue()))
                .andExpect(view().name("Stock/products-manage"))
                .andDo(print());

    }

    @Test
    public void EditInvalidStockProduct() throws Exception{
        //Taglist is empty
        OwnTag t1 = new OwnTag("test");
        List<OwnTag> tags = new ArrayList<>();
        tags.add(t1);
        OwnProduct prod = new OwnProduct("test","description",1.0, -5.0, 0.0, 0.0, Unit.KILOGRAM, "locatie2", "props", 5L,5L, LocalDateTime.now(), LocalDateTime.now(), tags, null);
        long id = 10;

        when(productService.findById(id)).thenReturn(Optional.of(prod));
        mockMvc.perform(post("/products/{id}","10").flashAttr("product",prod))
                .andExpect(model().attribute("errormessage", notNullValue()))
                .andExpect(view().name("Stock/products-manage"))
                .andDo(print());

    }

    @Test
    public void EditInvalidURLProduct() throws Exception{

        OwnTag t1 = new OwnTag("test");
        List<OwnTag> tags = new ArrayList<>();
        tags.add(t1);


        //Taglist is empty
        URL url = new URL("dfwdgfvw");
        OwnProduct prod = new OwnProduct("test","description",1.0, 2000.0, 200.0, 1.0, Unit.KILOGRAM, "locatie2", "props", 5L,5L, LocalDateTime.now(), LocalDateTime.now(), tags, url);
        long id = 10;

        when(productService.findById(id)).thenReturn(Optional.of(prod));
        mockMvc.perform(post("/products/{id}","10").flashAttr("product",prod))
                .andExpect(model().attribute("errormessage", notNullValue()))
                .andExpect(view().name("Stock/products-manage"))
                .andDo(print());

    }



    @Test
    public void addValidTag() throws Exception{
        OwnTag t1 = new OwnTag("test");
        long id= 10;
        t1.setId(id);
        List<OwnTag> taglist = new ArrayList<>();
        taglist.add(t1);

        when(tagService.findAll()).thenReturn(taglist);

        mockMvc.perform(post("/tags/").flashAttr("tag",t1))
                .andExpect(view().name("redirect:/tags"))
                .andDo(print());
    }

    @Test
    public void addNonValidTag() throws Exception{

        //name is empty
        OwnTag t1 = new OwnTag("");
        mockMvc.perform(post("/tags/").flashAttr("tag",t1))
                .andExpect(model().attribute("errormessage", notNullValue()))
                .andExpect(view().name("Tags/tags-manage"))
                .andDo(print());

    }

    @Test
    public void addDuplicateNameTag() throws Exception{

        OwnTag tag = new OwnTag("test1");
        long id = 5;
        tag.setId(id);

        OwnTag tag2 = new OwnTag("test2");

        when(tagService.findById(id)).thenReturn(Optional.of(tag2));
        when(tagService.findByName("test1")).thenReturn(Optional.of(tag));

        mockMvc.perform(post("/tags/{id", "5").flashAttr("tag",tag))
                .andExpect(model().attribute("errormessage", notNullValue()))
                .andExpect(view().name("Tags/tags-manage"))
                .andDo(print());

    }

    @Test
    public void EditTag() throws Exception{
        OwnTag t1 = new OwnTag("test");
      long id = 10;

        when(tagService.findById(id)).thenReturn(Optional.of(t1));
        mockMvc.perform(post("/tags/{id}","10").flashAttr("tag",t1))
                .andExpect(view().name("redirect:/tags"))
                .andDo(print());
    }

    @Test
    public void EditInvalidTagName() throws Exception{
        //name is invalid
        OwnTag t1 = new OwnTag("");
       long id = 10;

        when(tagService.findById(id)).thenReturn(Optional.of(t1));
        mockMvc.perform(post("/tags/{id}","10").flashAttr("tag",t1))
                .andExpect(model().attribute("errormessage", notNullValue()))
                .andExpect(view().name("Tags/tags-manage"))
                .andDo(print());

    }

    @Test
    public void addValidComposition() throws Exception{
        Composition comp1 = new Composition(5.0, new OwnProduct());
        mockMvc.perform(post("/compositions/").flashAttr("composition",comp1))
                .andExpect(view().name("Mixtures/compositions-list"))
                .andDo(print());
    }

    @Test
    public void addInValidComposition() throws Exception{
        //Amount must be greater than 0
        Composition comp1 = new Composition(0.0, new OwnProduct());
        mockMvc.perform(post("/compositions/").flashAttr("composition",comp1))
                .andExpect(model().attribute("errormessage", notNullValue()))
                .andExpect(view().name("Mixtures/compositions-manage"))
                .andDo(print());
    }

    @Test
    public void EditComposition() throws Exception{
        Composition comp = new Composition(5.0, new OwnProduct());
        long id = 10;

        when(compositionService.findById(id)).thenReturn(Optional.of(comp));
        mockMvc.perform(post("/compositions/{id}","10").flashAttr("composition",comp))
                .andExpect(view().name("Mixtures/compositions-list"))
                .andDo(print());
    }

    @Test
    public void EditInvalidCompositionAmountZero() throws Exception{
        //amount is not valid, amount is zero
        Composition comp = new Composition(0.0, new OwnProduct());
        long id = 10;

        when(compositionService.findById(id)).thenReturn(Optional.of(comp));
        mockMvc.perform(post("/compositions/{id}","10").flashAttr("composition",comp))
                .andExpect(model().attribute("errormessage", notNullValue()))
                .andExpect(view().name("Mixtures/compositions-manage"))
                .andDo(print());

    }

    @Test
    public void EditInvalidCompositionAmountGreaterThanHundred() throws Exception{
        //amount is not valid, amount is >100
        Composition comp = new Composition(150.0, new OwnProduct());
        long id = 10;

        when(compositionService.findById(id)).thenReturn(Optional.of(comp));
        mockMvc.perform(post("/compositions/{id}","10").flashAttr("composition",comp))
                .andExpect(model().attribute("errormessage", notNullValue()))
                .andExpect(view().name("Mixtures/compositions-manage"))
                .andDo(print());

    }


    @Test
    public void addValidMixture() throws Exception{
        List<Composition> ingredients = new ArrayList<>();
        ingredients.add(new Composition(100.0, new OwnProduct()));
        List<OwnTag> tags = new ArrayList<>();
        tags.add(new OwnTag("test"));
        Mixture mix = new Mixture("testing", ingredients, "blablabla",tags);

        mockMvc.perform(post("/mixtures/").flashAttr("mixture",mix))
                .andExpect(view().name("Mixtures/mixtures-list"))
                .andDo(print());
    }

    @Test
    public void addInValidMixtureAmount() throws Exception{
        //total amount of ingredients must be 100
        List<Composition> ingredients = new ArrayList<>();
        ingredients.add(new Composition(50.0, new OwnProduct()));
        ingredients.add(new Composition(30.0, new OwnProduct()));

        List<OwnTag> tags = new ArrayList<>();
        tags.add(new OwnTag("test"));
        Mixture mix = new Mixture("testingC", ingredients, "blablabla",tags);

        mockMvc.perform(post("/mixtures/").flashAttr("mixture",mix))
                .andExpect(model().attribute("errormessage", notNullValue()))
                .andExpect(view().name("Mixtures/mixtures-manage"))
                .andDo(print());
    }

    @Test
    public void addInValidMixtureDescription() throws Exception{
        //total amount of ingredients must be 100
        List<Composition> ingredients = new ArrayList<>();
        ingredients.add(new Composition(50.0, new OwnProduct()));
        ingredients.add(new Composition(50.0, new OwnProduct()));

        List<OwnTag> tags = new ArrayList<>();
        tags.add(new OwnTag("test"));
        Mixture mix = new Mixture("testingC", ingredients, "",tags);

        mockMvc.perform(post("/mixtures/").flashAttr("mixture",mix))
                .andExpect(model().attribute("errormessage", notNullValue()))
                .andExpect(view().name("Mixtures/mixtures-manage"))
                .andDo(print());
    }

    @Test
    public void addInValidMixtureTags() throws Exception{
        //total amount of ingredients must be 100
        List<Composition> ingredients = new ArrayList<>();
        ingredients.add(new Composition(50.0, new OwnProduct()));
        ingredients.add(new Composition(50.0, new OwnProduct()));

        List<OwnTag> tags = new ArrayList<>();
        Mixture mix = new Mixture("testingC", ingredients, "blabla",tags);

        mockMvc.perform(post("/mixtures/").flashAttr("mixture",mix))
                .andExpect(model().attribute("errormessage", notNullValue()))
                .andExpect(view().name("Mixtures/mixtures-manage"))
                .andDo(print());
    }

    @Test
    public void EditMixture() throws Exception{
        List<Composition> ingredients = new ArrayList<>();
        ingredients.add(new Composition(50.0, new OwnProduct()));
        ingredients.add(new Composition(50.0, new OwnProduct()));

        List<OwnTag> tags = new ArrayList<>();
        tags.add(new OwnTag("test"));
        Mixture mix = new Mixture("testingC", ingredients, "blablabla",tags);

        long id = 10;

        when(mixtureService.findById(id)).thenReturn(Optional.of(mix));
        mockMvc.perform(post("/mixtures/{id}","10").flashAttr("mixture",mix))
                .andExpect(view().name("Mixtures/mixtures-list"))
                .andDo(print());
    }

    @Test
    public void EditInvalidMixtureName() throws Exception{
        //mixture name is not valid
        List<Composition> ingredients = new ArrayList<>();
        ingredients.add(new Composition(50.0, new OwnProduct()));
        ingredients.add(new Composition(50.0, new OwnProduct()));

        List<OwnTag> tags = new ArrayList<>();
        tags.add(new OwnTag("test"));
        Mixture mix = new Mixture("", ingredients, "blablabla",tags);

        long id = 10;

        when(mixtureService.findById(id)).thenReturn(Optional.of(mix));
        mockMvc.perform(post("/mixtures/{id}","10").flashAttr("mixture",mix))
                .andExpect(model().attribute("errormessage", notNullValue()))
                .andExpect(view().name("Mixtures/mixtures-manage"))
                .andDo(print());
    }

    @Test
    public void EditInvalidMixtureTags() throws Exception{
        //mixture tags is empty
        List<Composition> ingredients = new ArrayList<>();
        ingredients.add(new Composition(50.0, new OwnProduct()));
        ingredients.add(new Composition(50.0, new OwnProduct()));

        List<OwnTag> tags = new ArrayList<>();
        Mixture mix = new Mixture("test", ingredients, "blablabla",tags);

        long id = 10;

        when(mixtureService.findById(id)).thenReturn(Optional.of(mix));
        mockMvc.perform(post("/mixtures/{id}","10").flashAttr("mixture",mix))
                .andExpect(model().attribute("errormessage", notNullValue()))
                .andExpect(view().name("Mixtures/mixtures-manage"))
                .andDo(print());
    }

    @Test
    public void EditInvalidMixtureIngredients() throws Exception{
        //mixture ingredients amount doesn't add up to 100%
        List<Composition> ingredients = new ArrayList<>();
        ingredients.add(new Composition(50.0, new OwnProduct()));
        ingredients.add(new Composition(30.0, new OwnProduct()));

        List<OwnTag> tags = new ArrayList<>();
        tags.add(new OwnTag("test"));
        Mixture mix = new Mixture("test", ingredients, "blablabla",tags);

        long id = 10;

        when(mixtureService.findById(id)).thenReturn(Optional.of(mix));
        mockMvc.perform(post("/mixtures/{id}","10").flashAttr("mixture",mix))
                .andExpect(model().attribute("errormessage", notNullValue()))
                .andExpect(view().name("Mixtures/mixtures-manage"))
                .andDo(print());
    }

    @Test
    public void EditInvalidMixtureDescription() throws Exception{
        //mixture's desciption is empty
        List<Composition> ingredients = new ArrayList<>();
        ingredients.add(new Composition(50.0, new OwnProduct()));
        ingredients.add(new Composition(50.0, new OwnProduct()));

        List<OwnTag> tags = new ArrayList<>();
        tags.add(new OwnTag("test"));
        Mixture mix = new Mixture("test", ingredients, "",tags);

        long id = 10;

        when(mixtureService.findById(id)).thenReturn(Optional.of(mix));
        mockMvc.perform(post("/mixtures/{id}","10").flashAttr("mixture",mix))
                .andExpect(model().attribute("errormessage", notNullValue()))
                .andExpect(view().name("Mixtures/mixtures-manage"))
                .andDo(print());
    }









}
