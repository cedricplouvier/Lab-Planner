package be.uantwerpen.labplanner.Controller;

import be.uantwerpen.labplanner.LabplannerApplication;
import be.uantwerpen.labplanner.Model.DatabaseLoader;
import be.uantwerpen.labplanner.Service.CompositionService;
import be.uantwerpen.labplanner.Service.MixtureService;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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


}
