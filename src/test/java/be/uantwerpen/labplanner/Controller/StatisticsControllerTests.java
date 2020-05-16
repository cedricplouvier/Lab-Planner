package be.uantwerpen.labplanner.Controller;

import be.uantwerpen.labplanner.LabplannerApplication;
import be.uantwerpen.labplanner.Model.*;
import be.uantwerpen.labplanner.Service.*;
import be.uantwerpen.labplanner.common.model.stock.Product;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get; //belangrijke imports
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = LabplannerApplication.class)
@WebAppConfiguration
public class StatisticsControllerTests {

    @Autowired
    private FilterChainProxy springSecurityFilterChain;


    @Mock
    private DeviceService deviceService;

    @Mock
    private DeviceTypeService deviceTypeService;

    @Mock
    private StepService stepService;

    @Mock
    private OwnProductService productService;

    @Mock
    private OfficeHoursService officeHoursService;

    @InjectMocks
    private StatisticsController statisticsController;

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(statisticsController)
                .apply(springSecurity(springSecurityFilterChain)).build();

        /*mockMvc = MockMvcBuilders
                    .webAppContextSetup(context)
                    .apply(springSecurity())
                    .build();*/
    }

    @Test
    public void showStatisticsPageTest() throws Exception{

        List<Device> devices = new ArrayList<>();
        List<Step> steps = new ArrayList<>();
        List<OfficeHours> officeHours = new ArrayList<>();
        OfficeHours currentOfficeHours = new OfficeHours(8,20,0,0);
        officeHours.add(currentOfficeHours);
        when(officeHoursService.findAll()).thenReturn(officeHours);
        when(deviceService.findAll()).thenReturn(devices);
        when(stepService.findAll()).thenReturn(steps);
        mockMvc.perform(get("/statistics/statistics").with(user("test").password("test")
                .authorities(new SimpleGrantedAuthority("Statistics Access"))))
                .andExpect(status().isOk())
                .andExpect(model().attribute("dev1", notNullValue()))
                .andExpect(model().attribute("selectedDev",instanceOf(Device.class)))
                .andExpect(model().attribute("dev2", notNullValue()))
                .andExpect(model().attribute("dev3", notNullValue()))
                .andExpect(model().attribute("dev4", notNullValue()))
                .andExpect(model().attribute("dev5", notNullValue()))
                .andExpect(view().name("Statistics/statistics"))
                .andDo(print());
    }

    @Test
    public void showStatisticsStockPageTest() throws Exception{

        List<OwnProduct> products = new ArrayList<>();
        OwnProduct product = new OwnProduct();
        product.setId((long) 10);
        Map<String, Double> stockHistoryMap1 = new HashMap<>();
        stockHistoryMap1.put(new SimpleDateFormat("yyyy-MM").format(new Date()),(double) 2000);
        product.setProductStockHistory(stockHistoryMap1);
        product.setName("prod1");
        products.add(product);
        OwnProduct product2 = new OwnProduct();
        product2.setId((long) 20);
        Map<String, Double> stockHistoryMap2 = new HashMap<>();
        stockHistoryMap2.put(new SimpleDateFormat("yyyy-MM").format(new Date()),(double) 2000);
        product2.setProductStockHistory(stockHistoryMap2);
        product2.setName("prod2");
        products.add(product2);

        List<OwnProduct> listSelectedProducts = new ArrayList<>();
        listSelectedProducts.add(product);
        listSelectedProducts.add(product2);

        List<Double> stocklvl = new ArrayList<>();
        stocklvl.add((double)2000);
        stocklvl.add((double)2000);

        when(productService.findAll()).thenReturn(products);

        mockMvc.perform(get("/statistics/stockStatistics").with(user("test").password("test")
                .authorities(new SimpleGrantedAuthority("Statistics Access")))
                .flashAttr("selectedMonthStock",new SimpleDateFormat("yyyy-MM").format(new Date()))
                .flashAttr("selectedStartMonthStockHistory",new SimpleDateFormat("yyyy-MM").format(new Date()))
                .flashAttr("selectedProducts",listSelectedProducts)
                .flashAttr("productCounter",2))
                .andExpect(model().attribute("highestDataPointStock",2000.0))
                .andExpect(model().attribute("stockLevelMonth",stocklvl))
                .andExpect(status().isOk())
                .andExpect(view().name("Statistics/stockStatistics"));
    }

    @Test
    public void resetStockGraphTest() throws Exception {
        mockMvc.perform(get("/statistics/stockStatistics/resetGraphStockHistory")
                .with(user("test").password("test").authorities(new SimpleGrantedAuthority("Statistics Access"))))
                .andExpect(model().attribute("productCounter",0))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/statistics/stockStatistics"))
                .andDo(print());
    }

    @Test
    public void getSelectedStartStockHistoryTest() throws Exception {
        mockMvc.perform(get("/statistics/stockStatistics/getSelectedStartStockHistory")
                .with(user("test").password("test").authorities(new SimpleGrantedAuthority("Statistics Access"))))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/statistics/stockStatistics"))
                .andDo(print());
    }

    @Test
    public void getSelectedMonthStockTest() throws Exception {
        mockMvc.perform(get("/statistics/stockStatistics/getSelectedMonthStock")
                .with(user("test").password("test").authorities(new SimpleGrantedAuthority("Statistics Access"))))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/statistics/stockStatistics"))
                .andDo(print());
    }

    @Test
    public void getSelectedProductTest() throws Exception {

        List<OwnProduct> products = new ArrayList<>();
        OwnProduct product = new OwnProduct();
        product.setId((long) 10);
        Map<String, Double> stockHistoryMap1 = new HashMap<>();
        stockHistoryMap1.put(new SimpleDateFormat("yyyy-MM").format(new Date()),(double) 2000);
        product.setProductStockHistory(stockHistoryMap1);
        product.setName("prod1");
        products.add(product);
        OwnProduct product2 = new OwnProduct();
        product2.setId((long) 20);
        Map<String, Double> stockHistoryMap2 = new HashMap<>();
        stockHistoryMap2.put(new SimpleDateFormat("yyyy-MM").format(new Date()),(double) 2000);
        product2.setProductStockHistory(stockHistoryMap2);
        product2.setName("prod2");
        products.add(product2);

        List<OwnProduct> listSelectedProducts = new ArrayList<>();
        listSelectedProducts.add(product);
        listSelectedProducts.add(product2);

        when(productService.findAll()).thenReturn(products);

        mockMvc.perform(get("/statistics/stockStatistics/getSelectedProduct")
                .with(user("test").password("test").authorities(new SimpleGrantedAuthority("Statistics Access")))
                .flashAttr("productCounter",2)
                .flashAttr("selectedProducts",listSelectedProducts))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/statistics/stockStatistics"))
                .andDo(print());
    }

    @Test
    public void updateStockMapTest() throws Exception {
        List<OwnProduct> products = new ArrayList<>();
        OwnProduct product = new OwnProduct();
        product.setId((long) 10);
        Map<String, Double> stockHistoryMap1 = new HashMap<>();
        stockHistoryMap1.put(new SimpleDateFormat("yyyy-MM").format(new Date()),(double) 2000);
        product.setProductStockHistory(stockHistoryMap1);
        product.setName("prod1");
        products.add(product);
        OwnProduct product2 = new OwnProduct();
        product2.setId((long) 20);
        Map<String, Double> stockHistoryMap2 = new HashMap<>();
        stockHistoryMap2.put(new SimpleDateFormat("yyyy-MM").format(new Date()),(double) 2000);
        product2.setProductStockHistory(stockHistoryMap2);
        product2.setName("prod2");
        products.add(product2);
        when(productService.findAll()).thenReturn(products);

        statisticsController.upDateStockMap();
    }

    @Test
    public void submitTest() throws Exception{
        long id = 10;
        List<Step> steps = new ArrayList<>();
        int[] totalHoursSelectedDevice = new int[]{10,10,10,10,10,10,10,10,10,10,10,10};;

        List<OfficeHours> officeHours = new ArrayList<>();
        OfficeHours currentOfficeHours = new OfficeHours(8,20,0,0);
        officeHours.add(currentOfficeHours);
        when(officeHoursService.findAll()).thenReturn(officeHours);
        Device dev = new Device();
        dev.setId(id);
        dev.setDevicename("selectedDev");
        List<Device> devices = new ArrayList<>();
        devices.add(dev);

        when(stepService.findAll()).thenReturn(steps);

        mockMvc.perform(get("/statistics/statistics/submit")
                .flashAttr("totalHoursSelectedDevice", totalHoursSelectedDevice)
                .flashAttr("selectedDev",dev)
                .flashAttr("selectedDevices", devices)
                .with(user("test").password("test")))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/statistics/statistics"))
                .andDo(print());
    }

    @Test
    public void submitTestToManyDevices() throws Exception{
        List<Step> steps = new ArrayList<>();
        int[] totalHoursSelectedDevice = new int[]{10,10,10,10,10,10,10,10,10,10,10,10};;

        List<OfficeHours> officeHours = new ArrayList<>();
        OfficeHours currentOfficeHours = new OfficeHours(8,20,0,0);
        officeHours.add(currentOfficeHours);
        when(officeHoursService.findAll()).thenReturn(officeHours);
        Device dev = new Device();
        dev.setId((long) 10);
        dev.setDevicename("selectedDev");
        int dc = 5;
        List<Device> devices = new ArrayList<>();
        devices.add(dev);

        when(stepService.findAll()).thenReturn(steps);

        mockMvc.perform(get("/statistics/statistics/submit")
                .flashAttr("totalHoursSelectedDevice", totalHoursSelectedDevice)
                .flashAttr("selectedDev",dev)
                .flashAttr("selectedDevices", devices)
                .flashAttr("deviceCounter",dc)
                .with(user("test").password("test")))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/statistics/statistics"))
                .andDo(print());
    }

    @Test
    public void testSubmitDuplicate() throws Exception {
        ModelMap model = new ModelMap();
        RedirectAttributes ra = new RedirectAttributes() {
            @Override
            public RedirectAttributes addAttribute(String s, Object o) {
                return null;
            }

            @Override
            public RedirectAttributes addAttribute(Object o) {
                return null;
            }

            @Override
            public RedirectAttributes addAllAttributes(Collection<?> collection) {
                return null;
            }

            @Override
            public Model addAllAttributes(Map<String, ?> map) {
                return null;
            }

            @Override
            public RedirectAttributes mergeAttributes(Map<String, ?> map) {
                return null;
            }

            @Override
            public boolean containsAttribute(String s) {
                return false;
            }

            @Override
            public Object getAttribute(String s) {
                return null;
            }

            @Override
            public Map<String, Object> asMap() {
                return null;
            }

            @Override
            public RedirectAttributes addFlashAttribute(String s, Object o) {
                return null;
            }

            @Override
            public RedirectAttributes addFlashAttribute(Object o) {
                return null;
            }

            @Override
            public Map<String, ?> getFlashAttributes() {
                return null;
            }
        };
        Step step1 = new Step();
        List<Device> devices = new ArrayList<>();
        Device d1 = new Device();
        d1.setDevicename("devNameDuplicate");
        Device d2 = new Device();
        d2.setDevicename("devNameDuplicate");
        devices.add(d1);

        model.addAttribute("selectedDevices",devices);
        statisticsController.submit(model,d2, ra);

        /*mockMvc.perform(get("/statistics/statistics/submit")
                .sessionAttr("selectedDevices",devices)
                .with(user("test").password("test")))
                .andExpect(status().isFound())
                .andExpect(model().attribute("selectedDevices",hasSize(1)))
                .andExpect(view().name("redirect:/statistics/statistics"))
                .andDo(print());

         */
    }

    @Test
    public void ClearListTest() throws Exception{
        mockMvc.perform(get("/statistics/statistics/clearList")
                .with(user("test").password("test")
                        .authorities(new SimpleGrantedAuthority("Statistics Access"))))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/statistics"))
                .andDo(print());
    }

    @Test
    public void getSelectedyearTest() throws Exception {
        mockMvc.perform(get("/statistics/statistics/getSelectedYear")
                .with(user("test").password("test")
                        .authorities(new SimpleGrantedAuthority("Statistics Access"))))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/statistics/statistics"))
                .andDo(print());
    }

    @Test
    public void getSelectedGraphTypeTest() throws Exception {
        mockMvc.perform(get("/statistics/statistics/getSelectedGraphType")
                .with(user("test").password("test")
                        .authorities(new SimpleGrantedAuthority("Statistics Access"))))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/statistics/statistics"))
                .andDo(print());
    }

    @Test
    public void getSelectedTimePeriodTest() throws Exception {
        mockMvc.perform(get("/statistics/statistics/getSelectedTimePeriod")
                .with(user("test").password("test")
                .authorities(new SimpleGrantedAuthority("Statistics Access"))))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/statistics/statistics"));
    }

    @Test
    public void getMonthStartAndEndTest(){
        Step testStep = new Step();
        testStep.setStart("2020-05-10");
        testStep.setEnd("2020-06-10");
        assert(statisticsController.getStepMonthStart(testStep).matches("05"));
        assert statisticsController.getStepMonthEnd(testStep).matches("06");
    }

    @Test
    public void getYearStartAndEndTest(){
        Step testStep = new Step();
        testStep.setStart("2020-05-10");
        testStep.setEnd("2021-06-10");
        assert statisticsController.getStepYearStart(testStep).matches("2020");
        assert statisticsController.getStepYearEnd(testStep).matches("2021");
    }

    @Test
    public void getDayStartAndEndTest(){
        Step testStep = new Step();
        testStep.setStart("2020-05-10");
        testStep.setEnd("2020-06-11");
        assert statisticsController.getStepDayStart(testStep).matches("10");
        assert statisticsController.getStepDayEnd(testStep).matches("11");
    }

    @Test
    public void getHourStartAndEndTest(){
        Step testStep = new Step();
        testStep.setStartHour("20:55");
        testStep.setEndHour("23:10");
        assert statisticsController.getStepHourStart(testStep).matches("20");
        assert statisticsController.getStepHourEnd(testStep).matches("23");
    }

    @Test
    public void checkLeapyearTest(){
        int testYearNotLeap = 2010;
        statisticsController.checkLeapYear(testYearNotLeap);
        int testYearNotLeap2 = 2100;
        statisticsController.checkLeapYear(testYearNotLeap2);
        assert(!statisticsController.checkLeapYear(testYearNotLeap));
        int testyearLeap=2016;
        assert (statisticsController.checkLeapYear(testyearLeap));
        int testyearLeap2=2000;
        assert (statisticsController.checkLeapYear(testyearLeap2));
    }

    @Test
    public void calculateHourDiffTest(){
        Step testStep = new Step();
        testStep.setStartHour("20:55");
        testStep.setEndHour("23:40");
        Assert.assertEquals(statisticsController.calculateHourDiff(testStep),3);
        Assert.assertNotEquals(statisticsController.calculateHourDiff(testStep), 10);
    }

    @Test
    public void filterSelectedDeviceStepsMatchStepTest(){
        Device testDevice = new Device();
        testDevice.setDevicename("testDevice");
        Step testStep1 = new Step();
        testStep1.setDevice(testDevice);
        List<Step> steps = new ArrayList<>();
        steps.add(testStep1);

        Assert.assertEquals(statisticsController.filterSelectedDeviceSteps(testDevice,steps).get(0),testStep1);
        Assert.assertEquals(statisticsController.filterSelectedDeviceSteps(testDevice,steps).size(),1);
    }

    @Test
    public void filterSelectedDeviceStepsNoMatchStepTest(){
        Device testDevice = new Device();
        testDevice.setDevicename("testDevice");
        Device testDevice2 = new Device();
        testDevice2.setDevicename("testDevice2");

        Step testStep1 = new Step();
        testStep1.setDevice(testDevice);
        Step testStep2 = new Step();
        testStep2.setDevice(testDevice2);
        List<Step> steps = new ArrayList<>();
        steps.add(testStep2);

        Assert.assertEquals(statisticsController.filterSelectedDeviceSteps(testDevice,steps).size(),0);
    }

    //Should probably be divided in different test for each possible outcome
    @Test
    public void calculateDeviceHoursByYearAndMonthAllTest() throws ParseException {

        ModelMap model = new ModelMap();
        model.addAttribute("selectedYear","2020");
        model.addAttribute("selectedTimePeriod","All");
        Step step1 = new Step();
        List<Step> steps = new ArrayList<>();

        List<OfficeHours> officeHours = new ArrayList<>();
        OfficeHours currentOfficeHours = new OfficeHours(0,8,0,20);
        officeHours.add(currentOfficeHours);
        when(officeHoursService.findAll()).thenReturn(officeHours);

        //calculate for month i if same month & same day
        step1.setStart("2020-01-10");
        step1.setEnd("2020-01-10");
        step1.setStartHour("10:00");
        step1.setEndHour("17:00");
        steps.add(step1);
        Assert.assertEquals(7,statisticsController.calculateTotalHoursDeviceByYearAndMonth(model,steps)[0]);
        //calculate for month i if same month & not same day
        steps.clear();
        step1.setEnd("2020-01-11");
        steps.add(step1);
        Assert.assertEquals(31,statisticsController.calculateTotalHoursDeviceByYearAndMonth(model,steps)[0]);

        //not same month February => march leap
        steps.clear();
        statisticsController.setSelectedYear(model,"2016");
        step1.setStart("2016-02-28");
        step1.setEnd("2016-03-01");
        steps.add(step1);
        Assert.assertEquals(38,statisticsController.calculateTotalHoursDeviceByYearAndMonth(model,steps)[1]);
        Assert.assertEquals(17,statisticsController.calculateTotalHoursDeviceByYearAndMonth(model,steps)[2]);
        //not same month February => march not leap
        steps.clear();
        statisticsController.setSelectedYear(model,"2017");
        step1.setStart("2017-02-28");
        step1.setEnd("2017-03-01");
        steps.add(step1);
        Assert.assertEquals(14,statisticsController.calculateTotalHoursDeviceByYearAndMonth(model,steps)[1]);
        Assert.assertEquals(17,statisticsController.calculateTotalHoursDeviceByYearAndMonth(model,steps)[2]);

        //if even month -> odd month => +30
        steps.clear();
        statisticsController.setSelectedYear(model,"2020");
        step1.setStart("2020-04-30");
        step1.setEnd("2020-05-01");
        steps.add(step1);
        Assert.assertEquals(14,statisticsController.calculateTotalHoursDeviceByYearAndMonth(model,steps)[3]);
        Assert.assertEquals(17,statisticsController.calculateTotalHoursDeviceByYearAndMonth(model,steps)[4]);
        //if odd month -> even month => +31
        steps.clear();
        step1.setStart("2020-05-30");
        step1.setEnd("2020-06-01");
        steps.add(step1);
        Assert.assertEquals(38,statisticsController.calculateTotalHoursDeviceByYearAndMonth(model,steps)[4]);
        Assert.assertEquals(17,statisticsController.calculateTotalHoursDeviceByYearAndMonth(model,steps)[5]);

        //more than one month start even month
        steps.clear();
        step1.setStart("2020-04-30");
        step1.setEnd("2020-06-01");
        steps.add(step1);
        Assert.assertEquals(14,statisticsController.calculateTotalHoursDeviceByYearAndMonth(model,steps)[3]);
        Assert.assertEquals(730,statisticsController.calculateTotalHoursDeviceByYearAndMonth(model,steps)[4]);
        Assert.assertEquals(17,statisticsController.calculateTotalHoursDeviceByYearAndMonth(model,steps)[5]);
        //more than one month start odd month
        steps.clear();
        step1.setStart("2020-09-30");
        step1.setEnd("2020-11-01");
        steps.add(step1);
        Assert.assertEquals(38,statisticsController.calculateTotalHoursDeviceByYearAndMonth(model,steps)[8]);
        Assert.assertEquals(730,statisticsController.calculateTotalHoursDeviceByYearAndMonth(model,steps)[9]);
        Assert.assertEquals(17,statisticsController.calculateTotalHoursDeviceByYearAndMonth(model,steps)[10]);
    }

    @Test
    public void calculateDeviceHoursByYearAndMonthStartedTest() throws ParseException {

        ModelMap model = new ModelMap();
        model.addAttribute("selectedYear","2019");
        model.addAttribute("selectedTimePeriod","Started");
        Step step1 = new Step();
        List<Step> steps = new ArrayList<>();

        List<OfficeHours> officeHours = new ArrayList<>();
        OfficeHours currentOfficeHours = new OfficeHours(0,8,0,20);
        officeHours.add(currentOfficeHours);
        when(officeHoursService.findAll()).thenReturn(officeHours);

        //calculate for month i if same month & same day
        step1.setStart("2019-01-10");
        step1.setEnd("2019-01-10");
        step1.setStartHour("10:00");
        step1.setEndHour("17:00");
        steps.add(step1);
        Assert.assertEquals(7,statisticsController.calculateTotalHoursDeviceByYearAndMonth(model,steps)[0]);
        //calculate for month i if same month & not same day
        steps.clear();
        step1.setEnd("2019-01-11");
        steps.add(step1);
        Assert.assertEquals(31,statisticsController.calculateTotalHoursDeviceByYearAndMonth(model,steps)[0]);

        //not same month February => march leap
        steps.clear();
        statisticsController.setSelectedYear(model,"2016");
        step1.setStart("2016-02-28");
        step1.setEnd("2016-03-01");
        steps.add(step1);
        Assert.assertEquals(38,statisticsController.calculateTotalHoursDeviceByYearAndMonth(model,steps)[1]);
        Assert.assertEquals(17,statisticsController.calculateTotalHoursDeviceByYearAndMonth(model,steps)[2]);
        //not same month February => march not leap
        steps.clear();
        statisticsController.setSelectedYear(model,"2017");
        step1.setStart("2017-02-28");
        step1.setEnd("2017-03-01");
        steps.add(step1);
        Assert.assertEquals(14,statisticsController.calculateTotalHoursDeviceByYearAndMonth(model,steps)[1]);
        Assert.assertEquals(17,statisticsController.calculateTotalHoursDeviceByYearAndMonth(model,steps)[2]);

        //if even month -> odd month => +30
        steps.clear();
        statisticsController.setSelectedYear(model,"2019");
        step1.setStart("2019-04-30");
        step1.setEnd("2019-05-01");
        steps.add(step1);
        Assert.assertEquals(14,statisticsController.calculateTotalHoursDeviceByYearAndMonth(model,steps)[3]);
        Assert.assertEquals(17,statisticsController.calculateTotalHoursDeviceByYearAndMonth(model,steps)[4]);
        //if odd month -> even month => +31
        steps.clear();
        step1.setStart("2019-05-30");
        step1.setEnd("2019-06-01");
        steps.add(step1);
        Assert.assertEquals(38,statisticsController.calculateTotalHoursDeviceByYearAndMonth(model,steps)[4]);
        Assert.assertEquals(17,statisticsController.calculateTotalHoursDeviceByYearAndMonth(model,steps)[5]);

        //more than one month start even month
        steps.clear();
        step1.setStart("2019-04-30");
        step1.setEnd("2019-06-01");
        steps.add(step1);
        Assert.assertEquals(14,statisticsController.calculateTotalHoursDeviceByYearAndMonth(model,steps)[3]);
        Assert.assertEquals(730,statisticsController.calculateTotalHoursDeviceByYearAndMonth(model,steps)[4]);
        Assert.assertEquals(17,statisticsController.calculateTotalHoursDeviceByYearAndMonth(model,steps)[5]);
        //more than one month start odd month
        steps.clear();
        step1.setStart("2019-09-30");
        step1.setEnd("2019-11-01");
        steps.add(step1);
        Assert.assertEquals(38,statisticsController.calculateTotalHoursDeviceByYearAndMonth(model,steps)[8]);
        Assert.assertEquals(730,statisticsController.calculateTotalHoursDeviceByYearAndMonth(model,steps)[9]);
        Assert.assertEquals(17,statisticsController.calculateTotalHoursDeviceByYearAndMonth(model,steps)[10]);


        // Test if not started step gets rejected
        steps.clear();
        statisticsController.setSelectedYear(model,"3020");
        step1.setStart("3020-01-10");
        step1.setEnd("3020-01-10");
        step1.setStartHour("10:00");
        step1.setEndHour("17:00");
        steps.add(step1);
        Assert.assertEquals(0,statisticsController.calculateTotalHoursDeviceByYearAndMonth(model,steps)[0]);
        Assert.assertNotEquals(7,statisticsController.calculateTotalHoursDeviceByYearAndMonth(model,steps)[0]);
    }

    @Test
    public void calculateDeviceHoursByYearAndMonthFutureTest() throws ParseException {

        ModelMap model = new ModelMap();
        model.addAttribute("selectedYear","3021");
        model.addAttribute("selectedTimePeriod","Future");
        Step step1 = new Step();
        List<Step> steps = new ArrayList<>();

        List<OfficeHours> officeHours = new ArrayList<>();
        OfficeHours currentOfficeHours = new OfficeHours(0,8,0,20);
        officeHours.add(currentOfficeHours);
        when(officeHoursService.findAll()).thenReturn(officeHours);

        //calculate for month i if same month & same day
        step1.setStart("3021-01-10");
        step1.setEnd("3021-01-10");
        step1.setStartHour("10:00");
        step1.setEndHour("17:00");
        steps.add(step1);
        Assert.assertEquals(7,statisticsController.calculateTotalHoursDeviceByYearAndMonth(model,steps)[0]);
        //calculate for month i if same month & not same day
        steps.clear();
        step1.setEnd("3021-01-11");
        steps.add(step1);
        Assert.assertEquals(31,statisticsController.calculateTotalHoursDeviceByYearAndMonth(model,steps)[0]);

        //not same month February => march leap
        steps.clear();
        statisticsController.setSelectedYear(model,"3020");
        step1.setStart("3020-02-28");
        step1.setEnd("3020-03-01");
        steps.add(step1);
        Assert.assertEquals(38,statisticsController.calculateTotalHoursDeviceByYearAndMonth(model,steps)[1]);
        Assert.assertEquals(17,statisticsController.calculateTotalHoursDeviceByYearAndMonth(model,steps)[2]);
        //not same month February => march not leap
        steps.clear();
        statisticsController.setSelectedYear(model,"3021");
        step1.setStart("3021-02-28");
        step1.setEnd("3021-03-01");
        steps.add(step1);
        Assert.assertEquals(14,statisticsController.calculateTotalHoursDeviceByYearAndMonth(model,steps)[1]);
        Assert.assertEquals(17,statisticsController.calculateTotalHoursDeviceByYearAndMonth(model,steps)[2]);

        //if even month -> odd month => +30
        steps.clear();
        statisticsController.setSelectedYear(model,"3021");
        step1.setStart("3021-04-30");
        step1.setEnd("3021-05-01");
        steps.add(step1);
        Assert.assertEquals(14,statisticsController.calculateTotalHoursDeviceByYearAndMonth(model,steps)[3]);
        Assert.assertEquals(17,statisticsController.calculateTotalHoursDeviceByYearAndMonth(model,steps)[4]);
        //if odd month -> even month => +31
        steps.clear();
        step1.setStart("3021-05-30");
        step1.setEnd("3021-06-01");
        steps.add(step1);
        Assert.assertEquals(38,statisticsController.calculateTotalHoursDeviceByYearAndMonth(model,steps)[4]);
        Assert.assertEquals(17,statisticsController.calculateTotalHoursDeviceByYearAndMonth(model,steps)[5]);

        //more than one month start even month
        steps.clear();
        step1.setStart("3021-04-30");
        step1.setEnd("3021-06-01");
        steps.add(step1);
        Assert.assertEquals(14,statisticsController.calculateTotalHoursDeviceByYearAndMonth(model,steps)[3]);
        Assert.assertEquals(730,statisticsController.calculateTotalHoursDeviceByYearAndMonth(model,steps)[4]);
        Assert.assertEquals(17,statisticsController.calculateTotalHoursDeviceByYearAndMonth(model,steps)[5]);
        //more than one month start odd month
        steps.clear();
        step1.setStart("3021-09-30");
        step1.setEnd("3021-11-01");
        steps.add(step1);
        Assert.assertEquals(38,statisticsController.calculateTotalHoursDeviceByYearAndMonth(model,steps)[8]);
        Assert.assertEquals(730,statisticsController.calculateTotalHoursDeviceByYearAndMonth(model,steps)[9]);
        Assert.assertEquals(17,statisticsController.calculateTotalHoursDeviceByYearAndMonth(model,steps)[10]);

        // Test if not future step gets rejected
        steps.clear();
        statisticsController.setSelectedYear(model,"1020");
        step1.setStart("1020-01-10");
        step1.setEnd("1020-01-10");
        step1.setStartHour("10:00");
        step1.setEndHour("17:00");
        steps.add(step1);
        Assert.assertEquals(0,statisticsController.calculateTotalHoursDeviceByYearAndMonth(model,steps)[0]);
        Assert.assertNotEquals(7,statisticsController.calculateTotalHoursDeviceByYearAndMonth(model,steps)[0]);
    }


    //Should probably be divided in different test for each possible outcome
    @Test
    public void calculateOccupancyHoursAllTest() throws ParseException{

        ModelMap model = new ModelMap();
        model.addAttribute("selectedYear","2020");
        model.addAttribute("selectedTimePeriod","All");
        Step step1 = new Step();
        List<Step> steps = new ArrayList<>();

        List<OfficeHours> officeHours = new ArrayList<>();
        OfficeHours currentOfficeHours = new OfficeHours(0,8,0,20);
        officeHours.add(currentOfficeHours);
        float labClosingTime = currentOfficeHours.getEndHour();
        float labOpeningTime = currentOfficeHours.getStartHour();
        float labOpeningHoursInYear = (statisticsController.getAmountOfWorkDaysInYear()*(labClosingTime-labOpeningTime));
        when(officeHoursService.findAll()).thenReturn(officeHours);

        //calculate if same month & same day
        step1.setStart("2020-01-10");
        step1.setEnd("2020-01-10");
        step1.setStartHour("10:00");
        step1.setEndHour("17:00");
        steps.add(step1);
        Assert.assertEquals((7/labOpeningHoursInYear)*100, statisticsController.calculateOccupancyHours(model,steps, 0),0.001);
        //calculate for month i if same month & not same day
        steps.clear();
        step1.setEnd("2020-01-11");
        steps.add(step1);
        Assert.assertEquals((31/labOpeningHoursInYear)*100, statisticsController.calculateOccupancyHours(model,steps, 0),0.001);


        //not same month February => march leap
        steps.clear();
        statisticsController.setSelectedYear(model,"2016");
        step1.setStart("2016-02-28");
        step1.setEnd("2016-03-01");
        steps.add(step1);
        Assert.assertEquals((55/labOpeningHoursInYear)*100, statisticsController.calculateOccupancyHours(model, steps, 0),0.001);

        //not same month February => march not leap
        steps.clear();
        statisticsController.setSelectedYear(model,"2017");
        step1.setStart("2017-02-28");
        step1.setEnd("2017-03-01");
        steps.add(step1);
        Assert.assertEquals((31/labOpeningHoursInYear)*100, statisticsController.calculateOccupancyHours(model, steps, 0),0.001);

        //if even month -> odd month => +30
        steps.clear();
        statisticsController.setSelectedYear(model,"2020");
        step1.setStart("2020-04-30");
        step1.setEnd("2020-05-01");
        steps.add(step1);
        Assert.assertEquals((31/labOpeningHoursInYear)*100, statisticsController.calculateOccupancyHours(model, steps, 0),0.001);
        //if odd month -> even month => +31
        steps.clear();
        step1.setStart("2020-05-30");
        step1.setEnd("2020-06-01");
        steps.add(step1);
        Assert.assertEquals((55/labOpeningHoursInYear)*100, statisticsController.calculateOccupancyHours(model, steps, 0),0.001);

        // multiple months
        steps.clear();
        step1.setStart("2020-09-30");
        step1.setEnd("2020-11-01");
        steps.add(step1);
        Assert.assertEquals((761.5/labOpeningHoursInYear)*100, statisticsController.calculateOccupancyHours(model, steps, 0),0.5);
    }

    @Test
    public void calculateOccupancyHoursStartedTest() throws ParseException{

        ModelMap model = new ModelMap();
        model.addAttribute("selectedYear","2019");
        model.addAttribute("selectedTimePeriod","Started");
        Step step1 = new Step();
        List<Step> steps = new ArrayList<>();

        List<OfficeHours> officeHours = new ArrayList<>();
        OfficeHours currentOfficeHours = new OfficeHours(0,8,0,20);
        officeHours.add(currentOfficeHours);
        float labClosingTime = currentOfficeHours.getEndHour();
        float labOpeningTime = currentOfficeHours.getStartHour();
        float labOpeningHoursInYear = (statisticsController.getAmountOfWorkDaysInYear()*(labClosingTime-labOpeningTime));
        when(officeHoursService.findAll()).thenReturn(officeHours);

        //calculate if same month & same day
        step1.setStart("2019-01-10");
        step1.setEnd("2019-01-10");
        step1.setStartHour("10:00");
        step1.setEndHour("17:00");
        steps.add(step1);
        Assert.assertEquals((7/labOpeningHoursInYear)*100, statisticsController.calculateOccupancyHours(model,steps, 0),0.001);
        //calculate for month i if same month & not same day
        steps.clear();
        step1.setEnd("2019-01-11");
        steps.add(step1);
        Assert.assertEquals((31/labOpeningHoursInYear)*100, statisticsController.calculateOccupancyHours(model,steps, 0),0.001);


        //not same month February => march leap
        steps.clear();
        statisticsController.setSelectedYear(model,"2016");
        step1.setStart("2016-02-28");
        step1.setEnd("2016-03-01");
        steps.add(step1);
        Assert.assertEquals((55/labOpeningHoursInYear)*100, statisticsController.calculateOccupancyHours(model, steps, 0),0.001);

        //not same month February => march not leap
        steps.clear();
        statisticsController.setSelectedYear(model,"2017");
        step1.setStart("2017-02-28");
        step1.setEnd("2017-03-01");
        steps.add(step1);
        Assert.assertEquals((31/labOpeningHoursInYear)*100, statisticsController.calculateOccupancyHours(model, steps, 0),0.001);

        //if even month -> odd month => +30
        steps.clear();
        statisticsController.setSelectedYear(model,"2019");
        step1.setStart("2019-04-30");
        step1.setEnd("2019-05-01");
        steps.add(step1);
        Assert.assertEquals((31/labOpeningHoursInYear)*100, statisticsController.calculateOccupancyHours(model, steps, 0),0.001);
        //if odd month -> even month => +31
        steps.clear();
        step1.setStart("2019-05-30");
        step1.setEnd("2019-06-01");
        steps.add(step1);
        Assert.assertEquals((55/labOpeningHoursInYear)*100, statisticsController.calculateOccupancyHours(model, steps, 0),0.001);

        // multiple months
        steps.clear();
        step1.setStart("2019-09-30");
        step1.setEnd("2019-11-01");
        steps.add(step1);
        Assert.assertEquals((761.5/labOpeningHoursInYear)*100, statisticsController.calculateOccupancyHours(model, steps, 0),0.5);

        //exepct rejection for future steps
        steps.clear();
        step1.setStart("3019-01-10");
        step1.setEnd("3019-01-10");
        step1.setStartHour("10:00");
        step1.setEndHour("17:00");
        Assert.assertNotEquals((31/labOpeningHoursInYear)*100, statisticsController.calculateOccupancyHours(model,steps, 0),0.001);
        Assert.assertEquals(0, statisticsController.calculateOccupancyHours(model, steps, 0),0.001);
    }

    @Test
    public void calculateOccupancyHoursFutureTest() throws ParseException{

        ModelMap model = new ModelMap();
        model.addAttribute("selectedYear","3019");
        model.addAttribute("selectedTimePeriod","Future");
        Step step1 = new Step();
        List<Step> steps = new ArrayList<>();

        List<OfficeHours> officeHours = new ArrayList<>();
        OfficeHours currentOfficeHours = new OfficeHours(0,8,0,20);
        officeHours.add(currentOfficeHours);
        float labClosingTime = currentOfficeHours.getEndHour();
        float labOpeningTime = currentOfficeHours.getStartHour();
        float labOpeningHoursInYear = (statisticsController.getAmountOfWorkDaysInYear()*(labClosingTime-labOpeningTime));
        when(officeHoursService.findAll()).thenReturn(officeHours);

        //calculate if same month & same day
        step1.setStart("3019-01-10");
        step1.setEnd("3019-01-10");
        step1.setStartHour("10:00");
        step1.setEndHour("17:00");
        steps.add(step1);
        Assert.assertEquals((7/labOpeningHoursInYear)*100, statisticsController.calculateOccupancyHours(model,steps, 0),0.001);
        //calculate for month i if same month & not same day
        steps.clear();
        step1.setEnd("3019-01-11");
        steps.add(step1);
        Assert.assertEquals((31/labOpeningHoursInYear)*100, statisticsController.calculateOccupancyHours(model,steps, 0),0.001);


        //not same month February => march leap
        steps.clear();
        statisticsController.setSelectedYear(model,"3020");
        step1.setStart("3020-02-28");
        step1.setEnd("3020-03-01");
        steps.add(step1);
        Assert.assertEquals((55/labOpeningHoursInYear)*100, statisticsController.calculateOccupancyHours(model, steps, 0),0.001);

        //not same month February => march not leap
        steps.clear();
        statisticsController.setSelectedYear(model,"3017");
        step1.setStart("3017-02-28");
        step1.setEnd("3017-03-01");
        steps.add(step1);
        Assert.assertEquals((31/labOpeningHoursInYear)*100, statisticsController.calculateOccupancyHours(model, steps, 0),0.001);

        //if even month -> odd month => +30
        steps.clear();
        statisticsController.setSelectedYear(model,"3019");
        step1.setStart("3019-04-30");
        step1.setEnd("3019-05-01");
        steps.add(step1);
        Assert.assertEquals((31/labOpeningHoursInYear)*100, statisticsController.calculateOccupancyHours(model, steps, 0),0.001);
        //if odd month -> even month => +31
        steps.clear();
        step1.setStart("3019-05-30");
        step1.setEnd("3019-06-01");
        steps.add(step1);
        Assert.assertEquals((55/labOpeningHoursInYear)*100, statisticsController.calculateOccupancyHours(model, steps, 0),0.001);

        // multiple months
        steps.clear();
        step1.setStart("3019-09-30");
        step1.setEnd("3019-11-01");
        steps.add(step1);
        Assert.assertEquals((761.5/labOpeningHoursInYear)*100, statisticsController.calculateOccupancyHours(model, steps, 0),0.5);

        //exepct rejection for passed steps
        steps.clear();
        step1.setStart("2019-01-10");
        step1.setEnd("2019-01-10");
        step1.setStartHour("10:00");
        step1.setEndHour("17:00");
        Assert.assertNotEquals((31/labOpeningHoursInYear)*100, statisticsController.calculateOccupancyHours(model,steps, 0),0.001);
        Assert.assertEquals(0, statisticsController.calculateOccupancyHours(model, steps, 0),0.001);
    }

    //Should probably be divided in different test for each possible outcome
    @Test
    public void occupancyRateDaysTest() throws ParseException{
        ModelMap model = new ModelMap();
        model.addAttribute("selectedYear","2020");
        model.addAttribute("selectedTimePeriod", "All");

        List<OfficeHours> officeHours = new ArrayList<>();
        OfficeHours currentOfficeHours = new OfficeHours(0,8,0,20);
        officeHours.add(currentOfficeHours);
        when(officeHoursService.findAll()).thenReturn(officeHours);
        Float amountOfWorkDaysInYear = statisticsController.getAmountOfWorkDaysInYear();
        List<Step> steps = new ArrayList<>();
        Step stepTest = new Step();
        stepTest.setStartHour("10:00");
        stepTest.setEndHour("17:00");
        Step step1 = new Step();
        step1.setStartHour("10:00");
        step1.setEndHour("17:00");
        Step step2 = new Step();
        step2.setStartHour("10:00");
        step2.setEndHour("17:00");

        //********** NO OVERLAP **********

        //same month device not booked on same start or end day
        stepTest.setStart("2020-01-10");
        stepTest.setEnd("2020-01-12");
        steps.add(stepTest);
        Assert.assertEquals((3/amountOfWorkDaysInYear)*100, statisticsController.calculateOccupancyDays(model,steps, 0),0.001);

        //same month same startdate other step ends
        steps.clear();
        step2.setStart("2020-01-08");
        step2.setEnd("2020-01-10");
        steps.add(step2);
        stepTest.setStart("2020-01-10");
        stepTest.setEnd("2020-01-12");
        steps.add(stepTest);
        Assert.assertEquals((5/amountOfWorkDaysInYear)*100, statisticsController.calculateOccupancyDays(model,steps, 0),0.001);

        //same month same enddate other step starts
        steps.clear();
        step2.setStart("2020-01-12");
        step2.setEnd("2020-01-14");
        steps.add(step2);
        stepTest.setStart("2020-01-10");
        stepTest.setEnd("2020-01-12");
        steps.add(stepTest);
        Assert.assertEquals((5/amountOfWorkDaysInYear)*100, statisticsController.calculateOccupancyDays(model,steps, 0),0.001);

        //same month steps with same start and enddate as other steps, more than one day difference
        steps.clear();
        step1.setStart("2020-01-08");
        step1.setEnd("2020-01-10");
        steps.add(step1);
        step2.setStart("2020-01-12");
        step2.setEnd("2020-01-14");
        steps.add(step2);
        stepTest.setStart("2020-01-10");
        stepTest.setEnd("2020-01-12");
        steps.add(stepTest);
        Assert.assertEquals((7/amountOfWorkDaysInYear)*100, statisticsController.calculateOccupancyDays(model,steps, 0),0.001);

        //same month steps with same start and enddate as other steps, more than one day difference
        steps.clear();
        step1.setStart("2020-01-08");
        step1.setEnd("2020-01-10");
        steps.add(step1);
        step2.setStart("2020-01-11");
        step2.setEnd("2020-01-13");
        steps.add(step2);
        stepTest.setStart("2020-01-10");
        stepTest.setEnd("2020-01-11");
        steps.add(stepTest);
        Assert.assertEquals((6/amountOfWorkDaysInYear)*100, statisticsController.calculateOccupancyDays(model,steps, 0),0.001);

        //different month device not booked on same start or end day february => march leap
        statisticsController.setSelectedYear(model,"2016");
        steps.clear();
        stepTest.setStart("2016-02-28");
        stepTest.setEnd("2016-03-01");
        steps.add(stepTest);
        Assert.assertEquals((3/amountOfWorkDaysInYear)*100, statisticsController.calculateOccupancyDays(model,steps, 0),0.001);
        //different month device not booked on same start or end day february => march not leap
        statisticsController.setSelectedYear(model,"2017");
        steps.clear();
        stepTest.setStart("2017-02-28");
        stepTest.setEnd("2017-03-01");
        steps.add(stepTest);
        Assert.assertEquals((2/amountOfWorkDaysInYear)*100, statisticsController.calculateOccupancyDays(model,steps, 0),0.001);

        //different month device not booked on same start or end day with even => odd month
        statisticsController.setSelectedYear(model,"2020");
        steps.clear();
        stepTest.setStart("2020-04-30");
        stepTest.setEnd("2020-05-01");
        steps.add(stepTest);
        Assert.assertEquals((2/amountOfWorkDaysInYear)*100, statisticsController.calculateOccupancyDays(model,steps, 0),0.001);

        //different month device not booked on same start or end day with odd => even month
        statisticsController.setSelectedYear(model,"2020");
        steps.clear();
        stepTest.setStart("2020-05-30");
        stepTest.setEnd("2020-06-01");
        steps.add(stepTest);
        Assert.assertEquals((3/amountOfWorkDaysInYear)*100, statisticsController.calculateOccupancyDays(model, steps, 0),0.001);

        //more than one month difference and no other steps on same start or end date
        steps.clear();
        stepTest.setStart("2020-05-01");
        stepTest.setEnd("2020-07-01");
        steps.add(stepTest);
        Assert.assertEquals(((61.875)/amountOfWorkDaysInYear)*100, statisticsController.calculateOccupancyDays(model, steps, 0),0.001);

        //********** 1 DAY OVERLAP ***********

        //different month device 1 step booked on same start or end day february => march leap
        statisticsController.setSelectedYear(model,"2016");
        steps.clear();
        step1.setStart("2016-02-27");
        step1.setEnd("2016-02-28");
        steps.add(step1);
        stepTest.setStart("2016-02-28");
        stepTest.setEnd("2016-03-01");
        steps.add(stepTest);
        Assert.assertEquals((4/amountOfWorkDaysInYear)*100, statisticsController.calculateOccupancyDays(model, steps, 0),0.001);

        //different month device 1 booked on same start or end day february => march not leap
        statisticsController.setSelectedYear(model,"2017");
        steps.clear();
        step1.setStart("2017-02-27");
        step1.setEnd("2017-02-28");
        steps.add(step1);
        stepTest.setStart("2017-02-28");
        stepTest.setEnd("2017-03-01");
        steps.add(stepTest);
        Assert.assertEquals((3/amountOfWorkDaysInYear)*100, statisticsController.calculateOccupancyDays(model, steps, 0),0.001);

        //different month device 1 booked on same start or end day with even => odd month
        statisticsController.setSelectedYear(model,"2020");
        steps.clear();
        step1.setStart("2020-05-01");
        step1.setEnd("2020-05-02");
        steps.add(step1);
        stepTest.setStart("2020-04-30");
        stepTest.setEnd("2020-05-01");
        steps.add(stepTest);
        Assert.assertEquals((3/amountOfWorkDaysInYear)*100, statisticsController.calculateOccupancyDays(model, steps, 0),0.001);

        //different month device 1 booked on same start or end day with odd => even month
        statisticsController.setSelectedYear(model,"2020");
        steps.clear();
        step1.setStart("2020-05-29");
        step1.setEnd("2020-05-30");
        steps.add(step1);
        stepTest.setStart("2020-05-30");
        stepTest.setEnd("2020-06-01");
        steps.add(stepTest);
        Assert.assertEquals((4/amountOfWorkDaysInYear)*100, statisticsController.calculateOccupancyDays(model,steps, 0),0.001);

        //more than one month difference and 1 other steps on same start or end date
        steps.clear();
        step1.setStart("2020-05-01");
        step1.setEnd("2020-05-02");
        steps.add(step1);
        stepTest.setStart("2020-05-02");
        stepTest.setEnd("2020-07-02");
        steps.add(stepTest);
        Assert.assertEquals((63/amountOfWorkDaysInYear)*100, statisticsController.calculateOccupancyDays(model, steps, 0),0.5);

        //******* 2 DAYS OVERLAP ************

        //different month device steps booked on same start and end day february => march leap
        statisticsController.setSelectedYear(model,"2016");
        steps.clear();
        step1.setStart("2016-02-27");
        step1.setEnd("2016-02-28");
        steps.add(step1);
        step2.setStart("2016-03-01");
        step2.setEnd("2016-03-02");
        steps.add(step2);
        stepTest.setStart("2016-02-28");
        stepTest.setEnd("2016-03-01");
        steps.add(stepTest);
        Assert.assertEquals((5/amountOfWorkDaysInYear)*100, statisticsController.calculateOccupancyDays(model, steps, 0),0.001);

        //different month device booked steps on same start and end day february => march not leap
        statisticsController.setSelectedYear(model,"2017");
        steps.clear();
        step1.setStart("2017-02-27");
        step1.setEnd("2017-02-28");
        steps.add(step1);
        step2.setStart("2017-03-01");
        step2.setEnd("2017-03-02");
        steps.add(step2);
        stepTest.setStart("2017-02-28");
        stepTest.setEnd("2017-03-01");
        steps.add(stepTest);
        Assert.assertEquals((4/amountOfWorkDaysInYear)*100, statisticsController.calculateOccupancyDays(model, steps, 0),0.001);

        //different month device booked steps on same start and end day with even => odd month
        statisticsController.setSelectedYear(model,"2020");
        steps.clear();
        step1.setStart("2020-04-29");
        step1.setEnd("2020-04-30");
        steps.add(step1);
        step2.setStart("2020-05-01");
        step2.setEnd("2020-05-02");
        steps.add(step2);
        stepTest.setStart("2020-04-30");
        stepTest.setEnd("2020-05-01");
        steps.add(stepTest);
        Assert.assertEquals((4/amountOfWorkDaysInYear)*100, statisticsController.calculateOccupancyDays(model, steps, 0),0.001);

        //different month device booked steps on same start and end day with odd => even month
        statisticsController.setSelectedYear(model,"2020");
        steps.clear();
        step1.setStart("2020-05-29");
        step1.setEnd("2020-05-30");
        steps.add(step1);
        step2.setStart("2020-06-01");
        step2.setEnd("2020-06-02");
        steps.add(step2);
        stepTest.setStart("2020-05-30");
        stepTest.setEnd("2020-06-01");
        steps.add(stepTest);
        Assert.assertEquals((5/amountOfWorkDaysInYear)*100, statisticsController.calculateOccupancyDays(model, steps, 0),0.001);

        //more than one month difference and 2 other steps on same start or end date
        steps.clear();
        step1.setStart("2020-05-01");
        step1.setEnd("2020-05-02");
        steps.add(step1);
        step2.setStart("2020-07-02");
        step2.setEnd("2020-07-03");
        steps.add(step2);
        stepTest.setStart("2020-05-02");
        stepTest.setEnd("2020-07-02");
        steps.add(stepTest);
        Assert.assertEquals((64/amountOfWorkDaysInYear)*100, statisticsController.calculateOccupancyDays(model, steps, 0),0.5);
    }

    @Test
    public void occupancyRateDaysFutureTest() throws ParseException{
        ModelMap model = new ModelMap();
        model.addAttribute("selectedYear","3019");
        model.addAttribute("selectedTimePeriod", "Future");

        List<OfficeHours> officeHours = new ArrayList<>();
        OfficeHours currentOfficeHours = new OfficeHours(0,8,0,20);
        officeHours.add(currentOfficeHours);
        when(officeHoursService.findAll()).thenReturn(officeHours);
        Float amountOfWorkDaysInYear = statisticsController.getAmountOfWorkDaysInYear();
        List<Step> steps = new ArrayList<>();
        Step stepTest = new Step();
        stepTest.setStartHour("10:00");
        stepTest.setEndHour("17:00");
        Step step1 = new Step();
        step1.setStartHour("10:00");
        step1.setEndHour("17:00");
        Step step2 = new Step();
        step2.setStartHour("10:00");
        step2.setEndHour("17:00");

        //********** NO OVERLAP **********

        //same month device not booked on same start or end day
        stepTest.setStart("3019-01-10");
        stepTest.setEnd("3019-01-12");
        steps.add(stepTest);
        Assert.assertEquals((3/amountOfWorkDaysInYear)*100, statisticsController.calculateOccupancyDays(model,steps, 0),0.001);

        //same month same startdate other step ends
        steps.clear();
        step2.setStart("3019-01-08");
        step2.setEnd("3019-01-10");
        steps.add(step2);
        stepTest.setStart("3019-01-10");
        stepTest.setEnd("3019-01-12");
        steps.add(stepTest);
        Assert.assertEquals((5/amountOfWorkDaysInYear)*100, statisticsController.calculateOccupancyDays(model,steps, 0),0.001);

        //same month same enddate other step starts
        steps.clear();
        step2.setStart("3019-01-12");
        step2.setEnd("3019-01-14");
        steps.add(step2);
        stepTest.setStart("3019-01-10");
        stepTest.setEnd("3019-01-12");
        steps.add(stepTest);
        Assert.assertEquals((5/amountOfWorkDaysInYear)*100, statisticsController.calculateOccupancyDays(model,steps, 0),0.001);

        //same month steps with same start and enddate as other steps, more than one day difference
        steps.clear();
        step1.setStart("3019-01-08");
        step1.setEnd("3019-01-10");
        steps.add(step1);
        step2.setStart("3019-01-12");
        step2.setEnd("3019-01-14");
        steps.add(step2);
        stepTest.setStart("3019-01-10");
        stepTest.setEnd("3019-01-12");
        steps.add(stepTest);
        Assert.assertEquals((7/amountOfWorkDaysInYear)*100, statisticsController.calculateOccupancyDays(model,steps, 0),0.001);

        //same month steps with same start and enddate as other steps, more than one day difference
        steps.clear();
        step1.setStart("3019-01-08");
        step1.setEnd("3019-01-10");
        steps.add(step1);
        step2.setStart("3019-01-11");
        step2.setEnd("3019-01-13");
        steps.add(step2);
        stepTest.setStart("3019-01-10");
        stepTest.setEnd("3019-01-11");
        steps.add(stepTest);
        Assert.assertEquals((6/amountOfWorkDaysInYear)*100, statisticsController.calculateOccupancyDays(model,steps, 0),0.001);

        //different month device not booked on same start or end day february => march leap
        statisticsController.setSelectedYear(model,"3020");
        steps.clear();
        stepTest.setStart("3020-02-28");
        stepTest.setEnd("3020-03-01");
        steps.add(stepTest);
        Assert.assertEquals((3/amountOfWorkDaysInYear)*100, statisticsController.calculateOccupancyDays(model,steps, 0),0.001);
        //different month device not booked on same start or end day february => march not leap
        statisticsController.setSelectedYear(model,"3017");
        steps.clear();
        stepTest.setStart("3017-02-28");
        stepTest.setEnd("3017-03-01");
        steps.add(stepTest);
        Assert.assertEquals((2/amountOfWorkDaysInYear)*100, statisticsController.calculateOccupancyDays(model,steps, 0),0.001);

        //different month device not booked on same start or end day with even => odd month
        statisticsController.setSelectedYear(model,"3019");
        steps.clear();
        stepTest.setStart("3019-04-30");
        stepTest.setEnd("3019-05-01");
        steps.add(stepTest);
        Assert.assertEquals((2/amountOfWorkDaysInYear)*100, statisticsController.calculateOccupancyDays(model,steps, 0),0.001);

        //different month device not booked on same start or end day with odd => even month
        statisticsController.setSelectedYear(model,"3019");
        steps.clear();
        stepTest.setStart("3019-05-30");
        stepTest.setEnd("3019-06-01");
        steps.add(stepTest);
        Assert.assertEquals((3/amountOfWorkDaysInYear)*100, statisticsController.calculateOccupancyDays(model, steps, 0),0.001);

        //more than one month difference and no other steps on same start or end date
        steps.clear();
        stepTest.setStart("3019-05-01");
        stepTest.setEnd("3019-07-01");
        steps.add(stepTest);
        Assert.assertEquals(((61.875)/amountOfWorkDaysInYear)*100, statisticsController.calculateOccupancyDays(model, steps, 0),0.001);

        //********** 1 DAY OVERLAP ***********

        //different month device 1 step booked on same start or end day february => march leap
        statisticsController.setSelectedYear(model,"3020");
        steps.clear();
        step1.setStart("3020-02-27");
        step1.setEnd("3020-02-28");
        steps.add(step1);
        stepTest.setStart("3020-02-28");
        stepTest.setEnd("3020-03-01");
        steps.add(stepTest);
        Assert.assertEquals((4/amountOfWorkDaysInYear)*100, statisticsController.calculateOccupancyDays(model, steps, 0),0.001);

        //different month device 1 booked on same start or end day february => march not leap
        statisticsController.setSelectedYear(model,"3017");
        steps.clear();
        step1.setStart("3017-02-27");
        step1.setEnd("3017-02-28");
        steps.add(step1);
        stepTest.setStart("3017-02-28");
        stepTest.setEnd("3017-03-01");
        steps.add(stepTest);
        Assert.assertEquals((3/amountOfWorkDaysInYear)*100, statisticsController.calculateOccupancyDays(model, steps, 0),0.001);

        //different month device 1 booked on same start or end day with even => odd month
        statisticsController.setSelectedYear(model,"3019");
        steps.clear();
        step1.setStart("3019-05-01");
        step1.setEnd("3019-05-02");
        steps.add(step1);
        stepTest.setStart("3019-04-30");
        stepTest.setEnd("3019-05-01");
        steps.add(stepTest);
        Assert.assertEquals((3/amountOfWorkDaysInYear)*100, statisticsController.calculateOccupancyDays(model, steps, 0),0.001);

        //different month device 1 booked on same start or end day with odd => even month
        statisticsController.setSelectedYear(model,"3019");
        steps.clear();
        step1.setStart("3019-05-29");
        step1.setEnd("3019-05-30");
        steps.add(step1);
        stepTest.setStart("3019-05-30");
        stepTest.setEnd("3019-06-01");
        steps.add(stepTest);
        Assert.assertEquals((4/amountOfWorkDaysInYear)*100, statisticsController.calculateOccupancyDays(model,steps, 0),0.001);

        //more than one month difference and 1 other steps on same start or end date
        steps.clear();
        step1.setStart("3019-05-01");
        step1.setEnd("3019-05-02");
        steps.add(step1);
        stepTest.setStart("3019-05-02");
        stepTest.setEnd("3019-07-02");
        steps.add(stepTest);
        Assert.assertEquals((63/amountOfWorkDaysInYear)*100, statisticsController.calculateOccupancyDays(model, steps, 0),0.5);

        //******* 2 DAYS OVERLAP ************

        //different month device steps booked on same start and end day february => march leap
        statisticsController.setSelectedYear(model,"3020");
        steps.clear();
        step1.setStart("3020-02-27");
        step1.setEnd("3020-02-28");
        steps.add(step1);
        step2.setStart("3020-03-01");
        step2.setEnd("3020-03-02");
        steps.add(step2);
        stepTest.setStart("3020-02-28");
        stepTest.setEnd("3020-03-01");
        steps.add(stepTest);
        Assert.assertEquals((5/amountOfWorkDaysInYear)*100, statisticsController.calculateOccupancyDays(model, steps, 0),0.001);

        //different month device booked steps on same start and end day february => march not leap
        statisticsController.setSelectedYear(model,"3017");
        steps.clear();
        step1.setStart("3017-02-27");
        step1.setEnd("3017-02-28");
        steps.add(step1);
        step2.setStart("3017-03-01");
        step2.setEnd("3017-03-02");
        steps.add(step2);
        stepTest.setStart("3017-02-28");
        stepTest.setEnd("3017-03-01");
        steps.add(stepTest);
        Assert.assertEquals((4/amountOfWorkDaysInYear)*100, statisticsController.calculateOccupancyDays(model, steps, 0),0.001);

        //different month device booked steps on same start and end day with even => odd month
        statisticsController.setSelectedYear(model,"3019");
        steps.clear();
        step1.setStart("3019-04-29");
        step1.setEnd("3019-04-30");
        steps.add(step1);
        step2.setStart("3019-05-01");
        step2.setEnd("3019-05-02");
        steps.add(step2);
        stepTest.setStart("3019-04-30");
        stepTest.setEnd("3019-05-01");
        steps.add(stepTest);
        Assert.assertEquals((4/amountOfWorkDaysInYear)*100, statisticsController.calculateOccupancyDays(model, steps, 0),0.001);

        //different month device booked steps on same start and end day with odd => even month
        statisticsController.setSelectedYear(model,"3019");
        steps.clear();
        step1.setStart("3019-05-29");
        step1.setEnd("3019-05-30");
        steps.add(step1);
        step2.setStart("3019-06-01");
        step2.setEnd("3019-06-02");
        steps.add(step2);
        stepTest.setStart("3019-05-30");
        stepTest.setEnd("3019-06-01");
        steps.add(stepTest);
        Assert.assertEquals((5/amountOfWorkDaysInYear)*100, statisticsController.calculateOccupancyDays(model, steps, 0),0.001);

        //more than one month difference and 2 other steps on same start or end date
        steps.clear();
        step1.setStart("3019-05-01");
        step1.setEnd("3019-05-02");
        steps.add(step1);
        step2.setStart("3019-07-02");
        step2.setEnd("3019-07-03");
        steps.add(step2);
        stepTest.setStart("3019-05-02");
        stepTest.setEnd("3019-07-02");
        steps.add(stepTest);
        Assert.assertEquals((64/amountOfWorkDaysInYear)*100, statisticsController.calculateOccupancyDays(model, steps, 0),0.5);
    }

    @Test
    public void occupancyRateDaysStartedTest() throws ParseException{
        ModelMap model = new ModelMap();
        model.addAttribute("selectedYear","2019");
        model.addAttribute("selectedTimePeriod", "Started");

        List<OfficeHours> officeHours = new ArrayList<>();
        OfficeHours currentOfficeHours = new OfficeHours(0,8,0,20);
        officeHours.add(currentOfficeHours);
        when(officeHoursService.findAll()).thenReturn(officeHours);
        Float amountOfWorkDaysInYear = statisticsController.getAmountOfWorkDaysInYear();
        List<Step> steps = new ArrayList<>();
        Step stepTest = new Step();
        stepTest.setStartHour("10:00");
        stepTest.setEndHour("17:00");
        Step step1 = new Step();
        step1.setStartHour("10:00");
        step1.setEndHour("17:00");
        Step step2 = new Step();
        step2.setStartHour("10:00");
        step2.setEndHour("17:00");

        //********** NO OVERLAP **********

        //same month device not booked on same start or end day
        stepTest.setStart("2019-01-10");
        stepTest.setEnd("2019-01-12");
        steps.add(stepTest);
        Assert.assertEquals((3/amountOfWorkDaysInYear)*100, statisticsController.calculateOccupancyDays(model,steps, 0),0.001);

        //same month same startdate other step ends
        steps.clear();
        step2.setStart("2019-01-08");
        step2.setEnd("2019-01-10");
        steps.add(step2);
        stepTest.setStart("2019-01-10");
        stepTest.setEnd("2019-01-12");
        steps.add(stepTest);
        Assert.assertEquals((5/amountOfWorkDaysInYear)*100, statisticsController.calculateOccupancyDays(model,steps, 0),0.001);

        //same month same enddate other step starts
        steps.clear();
        step2.setStart("2019-01-12");
        step2.setEnd("2019-01-14");
        steps.add(step2);
        stepTest.setStart("2019-01-10");
        stepTest.setEnd("2019-01-12");
        steps.add(stepTest);
        Assert.assertEquals((5/amountOfWorkDaysInYear)*100, statisticsController.calculateOccupancyDays(model,steps, 0),0.001);

        //same month steps with same start and enddate as other steps, more than one day difference
        steps.clear();
        step1.setStart("2019-01-08");
        step1.setEnd("2019-01-10");
        steps.add(step1);
        step2.setStart("2019-01-12");
        step2.setEnd("2019-01-14");
        steps.add(step2);
        stepTest.setStart("2019-01-10");
        stepTest.setEnd("2019-01-12");
        steps.add(stepTest);
        Assert.assertEquals((7/amountOfWorkDaysInYear)*100, statisticsController.calculateOccupancyDays(model,steps, 0),0.001);

        //same month steps with same start and enddate as other steps, more than one day difference
        steps.clear();
        step1.setStart("2019-01-08");
        step1.setEnd("2019-01-10");
        steps.add(step1);
        step2.setStart("2019-01-11");
        step2.setEnd("2019-01-13");
        steps.add(step2);
        stepTest.setStart("2019-01-10");
        stepTest.setEnd("2019-01-11");
        steps.add(stepTest);
        Assert.assertEquals((6/amountOfWorkDaysInYear)*100, statisticsController.calculateOccupancyDays(model,steps, 0),0.001);

        //different month device not booked on same start or end day february => march leap
        statisticsController.setSelectedYear(model,"2016");
        steps.clear();
        stepTest.setStart("2016-02-28");
        stepTest.setEnd("2016-03-01");
        steps.add(stepTest);
        Assert.assertEquals((3/amountOfWorkDaysInYear)*100, statisticsController.calculateOccupancyDays(model,steps, 0),0.001);
        //different month device not booked on same start or end day february => march not leap
        statisticsController.setSelectedYear(model,"2017");
        steps.clear();
        stepTest.setStart("2017-02-28");
        stepTest.setEnd("2017-03-01");
        steps.add(stepTest);
        Assert.assertEquals((2/amountOfWorkDaysInYear)*100, statisticsController.calculateOccupancyDays(model,steps, 0),0.001);

        //different month device not booked on same start or end day with even => odd month
        statisticsController.setSelectedYear(model,"2019");
        steps.clear();
        stepTest.setStart("2019-04-30");
        stepTest.setEnd("2019-05-01");
        steps.add(stepTest);
        Assert.assertEquals((2/amountOfWorkDaysInYear)*100, statisticsController.calculateOccupancyDays(model,steps, 0),0.001);

        //different month device not booked on same start or end day with odd => even month
        statisticsController.setSelectedYear(model,"2019");
        steps.clear();
        stepTest.setStart("2019-05-30");
        stepTest.setEnd("2019-06-01");
        steps.add(stepTest);
        Assert.assertEquals((3/amountOfWorkDaysInYear)*100, statisticsController.calculateOccupancyDays(model, steps, 0),0.001);

        //more than one month difference and no other steps on same start or end date
        steps.clear();
        stepTest.setStart("2019-05-01");
        stepTest.setEnd("2019-07-01");
        steps.add(stepTest);
        Assert.assertEquals(((61.875)/amountOfWorkDaysInYear)*100, statisticsController.calculateOccupancyDays(model, steps, 0),0.001);

        //********** 1 DAY OVERLAP ***********

        //different month device 1 step booked on same start or end day february => march leap
        statisticsController.setSelectedYear(model,"2016");
        steps.clear();
        step1.setStart("2016-02-27");
        step1.setEnd("2016-02-28");
        steps.add(step1);
        stepTest.setStart("2016-02-28");
        stepTest.setEnd("2016-03-01");
        steps.add(stepTest);
        Assert.assertEquals((4/amountOfWorkDaysInYear)*100, statisticsController.calculateOccupancyDays(model, steps, 0),0.001);

        //different month device 1 booked on same start or end day february => march not leap
        statisticsController.setSelectedYear(model,"2017");
        steps.clear();
        step1.setStart("2017-02-27");
        step1.setEnd("2017-02-28");
        steps.add(step1);
        stepTest.setStart("2017-02-28");
        stepTest.setEnd("2017-03-01");
        steps.add(stepTest);
        Assert.assertEquals((3/amountOfWorkDaysInYear)*100, statisticsController.calculateOccupancyDays(model, steps, 0),0.001);

        //different month device 1 booked on same start or end day with even => odd month
        statisticsController.setSelectedYear(model,"2019");
        steps.clear();
        step1.setStart("2019-05-01");
        step1.setEnd("2019-05-02");
        steps.add(step1);
        stepTest.setStart("2019-04-30");
        stepTest.setEnd("2019-05-01");
        steps.add(stepTest);
        Assert.assertEquals((3/amountOfWorkDaysInYear)*100, statisticsController.calculateOccupancyDays(model, steps, 0),0.001);

        //different month device 1 booked on same start or end day with odd => even month
        statisticsController.setSelectedYear(model,"2019");
        steps.clear();
        step1.setStart("2019-05-29");
        step1.setEnd("2019-05-30");
        steps.add(step1);
        stepTest.setStart("2019-05-30");
        stepTest.setEnd("2019-06-01");
        steps.add(stepTest);
        Assert.assertEquals((4/amountOfWorkDaysInYear)*100, statisticsController.calculateOccupancyDays(model,steps, 0),0.001);

        //more than one month difference and 1 other steps on same start or end date
        steps.clear();
        step1.setStart("2019-05-01");
        step1.setEnd("2019-05-02");
        steps.add(step1);
        stepTest.setStart("2019-05-02");
        stepTest.setEnd("2019-07-02");
        steps.add(stepTest);
        Assert.assertEquals((63/amountOfWorkDaysInYear)*100, statisticsController.calculateOccupancyDays(model, steps, 0),0.5);

        //******* 2 DAYS OVERLAP ************

        //different month device steps booked on same start and end day february => march leap
        statisticsController.setSelectedYear(model,"2016");
        steps.clear();
        step1.setStart("2016-02-27");
        step1.setEnd("2016-02-28");
        steps.add(step1);
        step2.setStart("2016-03-01");
        step2.setEnd("2016-03-02");
        steps.add(step2);
        stepTest.setStart("2016-02-28");
        stepTest.setEnd("2016-03-01");
        steps.add(stepTest);
        Assert.assertEquals((5/amountOfWorkDaysInYear)*100, statisticsController.calculateOccupancyDays(model, steps, 0),0.001);

        //different month device booked steps on same start and end day february => march not leap
        statisticsController.setSelectedYear(model,"2017");
        steps.clear();
        step1.setStart("2017-02-27");
        step1.setEnd("2017-02-28");
        steps.add(step1);
        step2.setStart("2017-03-01");
        step2.setEnd("2017-03-02");
        steps.add(step2);
        stepTest.setStart("2017-02-28");
        stepTest.setEnd("2017-03-01");
        steps.add(stepTest);
        Assert.assertEquals((4/amountOfWorkDaysInYear)*100, statisticsController.calculateOccupancyDays(model, steps, 0),0.001);

        //different month device booked steps on same start and end day with even => odd month
        statisticsController.setSelectedYear(model,"2019");
        steps.clear();
        step1.setStart("2019-04-29");
        step1.setEnd("2019-04-30");
        steps.add(step1);
        step2.setStart("2019-05-01");
        step2.setEnd("2019-05-02");
        steps.add(step2);
        stepTest.setStart("2019-04-30");
        stepTest.setEnd("2019-05-01");
        steps.add(stepTest);
        Assert.assertEquals((4/amountOfWorkDaysInYear)*100, statisticsController.calculateOccupancyDays(model, steps, 0),0.001);

        //different month device booked steps on same start and end day with odd => even month
        statisticsController.setSelectedYear(model,"2019");
        steps.clear();
        step1.setStart("2019-05-29");
        step1.setEnd("2019-05-30");
        steps.add(step1);
        step2.setStart("2019-06-01");
        step2.setEnd("2019-06-02");
        steps.add(step2);
        stepTest.setStart("2019-05-30");
        stepTest.setEnd("2019-06-01");
        steps.add(stepTest);
        Assert.assertEquals((5/amountOfWorkDaysInYear)*100, statisticsController.calculateOccupancyDays(model, steps, 0),0.001);

        //more than one month difference and 2 other steps on same start or end date
        steps.clear();
        step1.setStart("2019-05-01");
        step1.setEnd("2019-05-02");
        steps.add(step1);
        step2.setStart("2019-07-02");
        step2.setEnd("2019-07-03");
        steps.add(step2);
        stepTest.setStart("2019-05-02");
        stepTest.setEnd("2019-07-02");
        steps.add(stepTest);
        Assert.assertEquals((64/amountOfWorkDaysInYear)*100, statisticsController.calculateOccupancyDays(model, steps, 0),0.5);
    }

}
