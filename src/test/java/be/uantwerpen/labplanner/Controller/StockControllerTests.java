package be.uantwerpen.labplanner.Controller;

import be.uantwerpen.labplanner.LabplannerApplication;
import be.uantwerpen.labplanner.Model.Composition;
import be.uantwerpen.labplanner.Model.Mixture;
import be.uantwerpen.labplanner.Model.OwnProduct;
import be.uantwerpen.labplanner.Model.OwnTag;
import be.uantwerpen.labplanner.Service.*;
import be.uantwerpen.labplanner.common.model.stock.Unit;
import be.uantwerpen.labplanner.common.model.users.Role;
import be.uantwerpen.labplanner.common.service.users.UserService;
import ch.qos.logback.core.encoder.EchoEncoder;
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
    public void testViewCreateProducts() throws Exception{
        OwnProduct product = new OwnProduct();
        List<OwnProduct> products = new ArrayList<>();
        products.add(product);

        mockMvc.perform(get("/products/put"))
                .andExpect(status().isOk())
                .andExpect(view().name("Stock/products-manage"))
                .andDo(print());

    }

    @Test
    public void testVieweditProducts() throws Exception{
        OwnProduct product = new OwnProduct();
        long id = 555;
        product.setId(id);

        mockMvc.perform(get("/products/").flashAttr("id",product.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("Stock/overview-stock"))
                .andDo(print());

    }





    @Test
    public void addNonValidProduct() throws Exception{

        OwnProduct prod = new OwnProduct();
        long id = 555;
        prod.setId(id);

        //empty name String
        prod.setName("");
        mockMvc.perform(post("/products/").flashAttr("ownProduct",prod))
                .andExpect(model().attribute("errormessage", notNullValue()))
                .andExpect(view().name("Stock/products-manage"))
                .andDo(print());

        //empty  description
        prod.setName("testname");
        prod.setDescription("");
        mockMvc.perform(post("/products/").flashAttr("ownProduct",prod))
                .andExpect(model().attribute("errormessage", notNullValue()))
                .andExpect(view().name("Stock/products-manage"))
                .andDo(print());

        //empty properties
        prod.setDescription("blablabla");
        prod.setProperties("");
        mockMvc.perform(post("/products/").flashAttr("ownProduct",prod))
                .andExpect(model().attribute("errormessage", notNullValue()))
                .andExpect(view().name("Stock/products-manage"))
                .andDo(print());

        //invalid stocklevel
        prod.setProperties("thisareproperties");
        prod.setStockLevel(-1.0);
        prod.setLowStockLevel(1.0);
        prod.setReservedStockLevel(0.0);
        mockMvc.perform(post("/products/").flashAttr("ownProduct",prod))
                .andExpect(model().attribute("errormessage", notNullValue()))
                .andExpect(view().name("Stock/products-manage"))
                .andDo(print());

        //invalid Lowstocklevel
        prod.setProperties("thisareproperties");
        prod.setStockLevel(5.0);
        prod.setLowStockLevel(-1.0);
        prod.setReservedStockLevel(0.0);
        mockMvc.perform(post("/products/").flashAttr("ownProduct",prod))
                .andExpect(model().attribute("errormessage", notNullValue()))
                .andExpect(view().name("Stock/products-manage"))
                .andDo(print());

        //invalid reservedStocklevel
        prod.setProperties("thisareproperties");
        prod.setStockLevel(5.0);
        prod.setLowStockLevel(1.0);
        prod.setReservedStockLevel(-5.0);
        mockMvc.perform(post("/products/").flashAttr("ownProduct",prod))
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
        mockMvc.perform(post("/products/").flashAttr("ownProduct",prod))
                .andExpect(model().attribute("errormessage", notNullValue()))
                .andExpect(view().name("Stock/products-manage"))
                .andDo(print());

    }

    @Test
    public void addValidProduct() throws  Exception{
        OwnTag t1 = new OwnTag("test");
        List<OwnTag> tags = new ArrayList<>();
        tags.add(t1);
        OwnProduct prod = new OwnProduct("placeholder1","description",1.0, 2000.0, 200.0, 1.0, Unit.KILOGRAM, "locatie2", "props", 5L,5L, LocalDateTime.now(), LocalDateTime.now(), tags , null);
        prod.setId(5L);
        List<OwnProduct> products = new ArrayList<>();
        products.add(prod);
        when(productService.findAll()).thenReturn(products);
        mockMvc.perform(post("/products/").flashAttr("ownProduct",prod))
                .andExpect(view().name("redirect:/products"))
                .andDo(print());
    }

    @Test
    public void addDuplicateNameProduct() throws  Exception{
        OwnTag t1 = new OwnTag("test");
        List<OwnTag> tags = new ArrayList<>();
        tags.add(t1);
        OwnProduct prod = new OwnProduct("placeholder1","description",1.0, 2000.0, 200.0, 1.0, Unit.KILOGRAM, "locatie2", "props", 5L,5L, LocalDateTime.now(), LocalDateTime.now(), tags , null);
        OwnProduct prod2 = new OwnProduct("placeholder1","description",1.0, 2000.0, 200.0, 1.0, Unit.KILOGRAM, "locatie2", "props", 5L,5L, LocalDateTime.now(), LocalDateTime.now(), tags , null);
        prod.setId(5L);
        prod2.setId(6L);
        List<OwnProduct> products = new ArrayList<>();
        products.add(prod);
        products.add(prod2);
        when(productService.findAll()).thenReturn(products);
        mockMvc.perform(post("/products/").flashAttr("ownProduct",prod))
                .andExpect(view().name("Stock/products-manage"))
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
        mockMvc.perform(post("/products/{id}","10").flashAttr("ownProduct",prod))
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
        prod.setId(id);

        when(productService.findById(id)).thenReturn(Optional.of(prod));
        mockMvc.perform(post("/products/").flashAttr("ownProduct",prod))
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
        mockMvc.perform(post("/products/{id}","10").flashAttr("ownProduct",prod))
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
        mockMvc.perform(post("/products/{id}","10").flashAttr("ownProduct",prod))
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
        mockMvc.perform(post("/products/{id}","10").flashAttr("ownProduct",prod))
                .andExpect(model().attribute("errormessage", notNullValue()))
                .andExpect(view().name("Stock/products-manage"))
                .andDo(print());

    }

    @Test
    public void deleteProductNotInUseTest() throws Exception{
        OwnProduct product = new OwnProduct();
        Long id = 519L;
        product.setId(id);

        List<Composition> compositions = new ArrayList<>();
        when(productService.findById(id)).thenReturn(Optional.of(product));
        when(compositionService.findAll()).thenReturn((compositions));
        mockMvc.perform(get("/products/{id}/delete",product.getId()))
                .andExpect(view().name("Stock/overview-stock"))
                .andExpect(model().attribute("success", notNullValue()))
                .andDo(print());

    }

    @Test
    public void deleteProductIsInUseTest() throws Exception{
        OwnProduct product = new OwnProduct();
        Long id = 519L;
        product.setId(id);
        Composition comp = new Composition();
        comp.setProduct(product);
        List<Composition> compositions = new ArrayList<>();
        compositions.add(comp);
        when(productService.findById(id)).thenReturn(Optional.of(product));
        when(compositionService.findAll()).thenReturn((compositions));
        mockMvc.perform(get("/products/{id}/delete",product.getId()))
                .andExpect(view().name("Stock/overview-stock"))
                .andExpect(model().attribute("error", notNullValue()))
                .andDo(print());
    }

    @Test
    public void viewProductInfoTest() throws Exception{
        OwnProduct product = new OwnProduct();
        Long id = 519L;
        product.setId(id);
        Mixture mix1  = new Mixture();
        Composition comp1  = new Composition(5.0, product);
        mix1.addComposition(comp1);
        List<Mixture> mixtures = new ArrayList<>();
        mixtures.add(mix1);
        when(mixtureService.findAll()).thenReturn(mixtures);
        when(productService.findById(id)).thenReturn(Optional.of(product));
        mockMvc.perform(get("/products/info/{id}",product.getId()))
                .andExpect(view().name("Stock/products-info"))
                .andDo(print());
    }

    @Test
    public void viewEditproductTest() throws Exception{
        OwnProduct product = new OwnProduct();
        Long id = 519L;
        product.setId(id);
        mockMvc.perform(get("/products/{id}",product.getId()))
                .andExpect(view().name("Stock/products-manage"))
                .andDo(print());
    }




    @Test
    public void addValidTag() throws Exception{
        OwnTag t1 = new OwnTag("testtag");
        long id= 10;
        t1.setId(id);
        List<OwnTag> taglist = new ArrayList<>();
        taglist.add(t1);

        when(tagService.findAll()).thenReturn(taglist);

        System.out.println(t1.getName());

        mockMvc.perform(post("/tags/").flashAttr("ownTag",t1))
                .andExpect(view().name("Stock/overview-stock"))
                .andDo(print());
    }

    @Test
    public void addNonValidTag() throws Exception{

        //name is empty
        OwnTag t1 = new OwnTag("");
        mockMvc.perform(post("/tags/").flashAttr("ownTag",t1))
                .andExpect(model().attribute("errormessage", notNullValue()))
                .andExpect(view().name("Tags/tags-manage"))
                .andDo(print());

    }

    @Test
    public void addDuplicateNameTag() throws Exception{

        OwnTag tag = new OwnTag("test1");
        long id = 5;
        tag.setId(id);

        OwnTag tag2 = new OwnTag("test1");
        tag2.setId(6L);
        List<OwnTag> taglist = new ArrayList<>();
        taglist.add(tag);
        taglist.add(tag2);
        when(tagService.findAll()).thenReturn(taglist);

        mockMvc.perform(post("/tags/{id", "5").flashAttr("ownTag",tag))
                .andExpect(model().attribute("errormessage", notNullValue()))
                .andExpect(view().name("Tags/tags-manage"))
                .andDo(print());

    }

    @Test
    public void EditTag() throws Exception{
        OwnTag t1 = new OwnTag("test");
      long id = 10;

        when(tagService.findById(id)).thenReturn(Optional.of(t1));
        mockMvc.perform(post("/tags/{id}","10").flashAttr("ownTag",t1))
                .andExpect(view().name("Stock/overview-stock"))
                .andDo(print());
    }

    @Test
    public void EditInvalidTagName() throws Exception{
        //name is invalid
        OwnTag t1 = new OwnTag("");
       long id = 10;

        when(tagService.findById(id)).thenReturn(Optional.of(t1));
        mockMvc.perform(post("/tags/{id}","10").flashAttr("ownTag",t1))
                .andExpect(model().attribute("errormessage", notNullValue()))
                .andExpect(view().name("Tags/tags-manage"))
                .andDo(print());

    }

    @Test
    public void deleteNotUsedTag() throws Exception{
        OwnTag t1 = new OwnTag("");
        long id = 10;
        t1.setId(id);

        when(productService.findAll()).thenReturn(new ArrayList<OwnProduct>());
        when(mixtureService.findAll()).thenReturn(new ArrayList<Mixture>());
        when(tagService.findById(id)).thenReturn(Optional.of(t1));
        mockMvc.perform(get("/tags/{id}/delete",t1.getId()))
                .andExpect(model().attribute("success", notNullValue()))
                .andExpect(view().name("Stock/overview-stock"))
                .andDo(print());
    }

    @Test
    public void deleteIsUsedInProductTag() throws Exception{
        OwnTag t1 = new OwnTag("");
        long id = 10;
        t1.setId(id);

        OwnProduct product = new OwnProduct();
        List<OwnTag> tags = new ArrayList<>();
        tags.add(t1);
        product.setTags(tags);
        List<OwnProduct> products = new ArrayList<>();
        products.add(product);

        when(productService.findAll()).thenReturn(products);
        when(mixtureService.findAll()).thenReturn(new ArrayList<Mixture>());
        when(tagService.findById(id)).thenReturn(Optional.of(t1));
        mockMvc.perform(get("/tags/{id}/delete",t1.getId()))
                .andExpect(model().attribute("error", notNullValue()))
                .andExpect(view().name("Stock/overview-stock"))
                .andDo(print());
    }

    @Test
    public void deleteIsUsedInMixtureTag() throws Exception{
        OwnTag t1 = new OwnTag("");
        long id = 10;
        t1.setId(id);

        Mixture mix = new Mixture();
        List<OwnTag> tags = new ArrayList<>();
        tags.add(t1);
        mix.setTags(tags);
        List<Mixture> mixtures = new ArrayList<>();
        mixtures.add(mix);

        when(productService.findAll()).thenReturn(new ArrayList<>());
        when(mixtureService.findAll()).thenReturn(mixtures);
        when(tagService.findById(id)).thenReturn(Optional.of(t1));
        mockMvc.perform(get("/tags/{id}/delete",t1.getId()))
                .andExpect(model().attribute("error", notNullValue()))
                .andExpect(view().name("Stock/overview-stock"))
                .andDo(print());
    }

    @Test
    public void testViewEditTag() throws Exception{
        OwnTag tag = new OwnTag();
        tag.setId(586L);

        mockMvc.perform(get("/tags/{id}",tag.getId()))
                .andExpect(view().name("Tags/tags-manage"))
                .andDo(print());
    }



    @Test
    public void testViewCreateTag() throws Exception{
        mockMvc.perform(get("/tags/put"))
                .andExpect(view().name("Tags/tags-manage"))
                .andDo(print());

    }


    @Test
    public void addValidMixture() throws Exception{
        List<Composition> ingredients = new ArrayList<>();
        ingredients.add(new Composition(100.0, new OwnProduct()));
        List<OwnTag> tags = new ArrayList<>();
        tags.add(new OwnTag("test"));
        Mixture mix = new Mixture("testing", ingredients, "blablabla",tags, null, null);

        mockMvc.perform(post("/mixtures/").flashAttr("mixture",mix))
                .andExpect(view().name("Mixtures/mixtures-manage"))
                .andDo(print());
    }

    @Test
    public void addDuplicateNameMixture() throws Exception{
        List<Composition> ingredients = new ArrayList<>();
        ingredients.add(new Composition(100.0, new OwnProduct()));
        List<OwnTag> tags = new ArrayList<>();
        tags.add(new OwnTag("test"));
        Mixture mix = new Mixture("testing", ingredients, "blablabla",tags, null, null);
        mix.setId(2L);
        Mixture mix2 = new Mixture("testing", ingredients, "blablabla",tags, null, null);
        mix2.setId(3L);
        List<Mixture> mixtures = new ArrayList<>();
        mixtures.add(mix);
        mixtures.add(mix2);

        when(mixtureService.findAll()).thenReturn(mixtures);
        mockMvc.perform(post("/mixtures/").flashAttr("mixture",mix))
                .andExpect(model().attribute("errormessage", notNullValue()))
                .andExpect(view().name("Mixtures/mixtures-manage"))
                .andDo(print());
    }

    @Test
    public void addInvalidMixtureCompositions() throws Exception{
        List<Composition> ingredients = new ArrayList<>();
        List<OwnTag> tags = new ArrayList<>();
        tags.add(new OwnTag("test"));
        Mixture mix = new Mixture("testing", ingredients, "blablabla",tags, null, null);

        mockMvc.perform(post("/mixtures/").flashAttr("mixture",mix))
                .andExpect(model().attribute("errormessage", notNullValue()))
                .andExpect(view().name("Mixtures/mixtures-manage"))
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
        Mixture mix = new Mixture("testingC", ingredients, "blablabla",tags, null, null);

        mockMvc.perform(post("/mixtures/").flashAttr("mixture",mix))
                .andExpect(model().attribute("errormessage", notNullValue()))
                .andExpect(view().name("Mixtures/mixtures-manage"))
                .andDo(print());
    }

    @Test
    public void testViewEditMixture() throws Exception{
        Mixture mixture = new Mixture();
        mixture.setId(585L);
        mockMvc.perform(get("/mixtures/{id}",mixture.getId()))
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
        Mixture mix = new Mixture("testingC", ingredients, "",tags, null, null);

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
        Mixture mix = new Mixture("testingC", ingredients, "blabla",tags, null, null);

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
        Mixture mix = new Mixture("testingC", ingredients, "blablabla",tags, null, null);

        long id = 10;

        when(mixtureService.findById(id)).thenReturn(Optional.of(mix));
        mockMvc.perform(post("/mixtures/{id}","10").flashAttr("mixture",mix))
                .andExpect(view().name("redirect:/products"))
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
        Mixture mix = new Mixture("", ingredients, "blablabla",tags, null, null);

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
        Mixture mix = new Mixture("test", ingredients, "blablabla",tags, null, null);

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
        Mixture mix = new Mixture("test", ingredients, "blablabla",tags, null, null);

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
        Mixture mix = new Mixture("test", ingredients, "",tags, null, null);

        long id = 10;

        when(mixtureService.findById(id)).thenReturn(Optional.of(mix));
        mockMvc.perform(post("/mixtures/{id}","10").flashAttr("mixture",mix))
                .andExpect(model().attribute("errormessage", notNullValue()))
                .andExpect(view().name("Mixtures/mixtures-manage"))
                .andDo(print());
    }

    @Test
    public void ViewCreateMixtureTest() throws Exception{
        mockMvc.perform(get("/mixtures/put"))
                .andExpect(view().name("Mixtures/mixtures-manage"))
                .andDo(print());

    }

    @Test
    public void viewMixtureInfoTest() throws  Exception{
        mockMvc.perform(get("/mixtures/info/{id}","10"))
                .andExpect(view().name("Mixtures/mixtures-info"))
                .andDo(print());
    }









}
