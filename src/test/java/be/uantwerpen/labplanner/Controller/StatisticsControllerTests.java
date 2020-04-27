package be.uantwerpen.labplanner.Controller;

import be.uantwerpen.labplanner.LabplannerApplication;
import be.uantwerpen.labplanner.Model.Device;
import be.uantwerpen.labplanner.Model.DeviceType;
import be.uantwerpen.labplanner.Model.OwnProduct;
import be.uantwerpen.labplanner.Model.Step;
import be.uantwerpen.labplanner.Service.DeviceService;
import be.uantwerpen.labplanner.Service.DeviceTypeService;
import be.uantwerpen.labplanner.Service.OwnProductService;
import be.uantwerpen.labplanner.Service.StepService;
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
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get; //belangrijke imports
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


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

    @InjectMocks
    private StatisticsController statisticsController;

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(this.statisticsController)
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

        when(deviceService.findAll()).thenReturn(devices);
        when(stepService.findAll()).thenReturn(steps);
        mockMvc.perform(get("/statistics/statistics").with(user("test").password("test")
                //Testing on the authorities is not working
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
        OwnProduct productTest = new OwnProduct();
        products.add(productTest);

        when(productService.findAll()).thenReturn(products);
        mockMvc.perform(get("/statistics/stockStatistics").with(user("test").password("test")))
                .andExpect(status().isOk())
                .andExpect(view().name("Statistics/stockStatistics"))
                .andExpect(model().attribute("products",hasSize(1)))
                .andDo(print());
    }

    @Test
    public void submitTest() throws Exception{
        long id = 10;
        List<Step> steps = new ArrayList<>();
        int[] totalHoursSelectedDevice = new int[]{10,10,10,10,10,10,10,10,10,10,10,10};;
        int highestAbsoluteValueHours=10;
        int test=11;
        int testValue = 1;
        Device dev = new Device();
        dev.setId(id);
        DeviceType devType = new DeviceType();
        devType.setId(id);
        dev.setDevicename("testDevive");
        dev.setDeviceType(devType);
        List<Device> devices = new ArrayList<>();
        devices.add(dev);

        when(stepService.findAll()).thenReturn(steps);

        mockMvc.perform(get("/statistics/statistics/submit").flashAttr("test",test)
                .flashAttr("totalHoursSelectedDevice", totalHoursSelectedDevice)
                .flashAttr("highestAbsoluteValueHours",highestAbsoluteValueHours)
                .flashAttr("selectedDev",dev)
                .flashAttr("selectedDevices", devices)
                .flashAttr("testValue", testValue)
                .with(user("test").password("test")))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/statistics/statistics"))
                .andDo(print());

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
                .andExpect(view().name("redirect:/statistics/statistics/refreshYear"))
                .andDo(print());
    }

    @Test
    public void getSelectedGraphTypeTest() throws Exception {
        mockMvc.perform(get("/statistics/statistics/getSelectedGraphType")
                .with(user("test").password("test")
                        .authorities(new SimpleGrantedAuthority("Statistics Access"))))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/statistics/statistics/refreshYear"))
                .andDo(print());
    }
    @Test
    public void refreshYearTest() throws Exception {
        mockMvc.perform(get("/statistics/statistics/refreshYear")
                .with(user("test").password("test")
                        .authorities(new SimpleGrantedAuthority("Statistics Access"))))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/statistics/statistics"))
                .andDo(print());
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
    public void calculateDeviceHoursByYearAndMonthTest(){

        Step step1 = new Step();
        List<Step> steps = new ArrayList<>();
        //calculate for month i if same month & same day
        step1.setStart("2020-01-10");
        step1.setEnd("2020-01-10");
        step1.setStartHour("10:00");
        step1.setEndHour("17:00");
        steps.add(step1);
        Assert.assertEquals(7,statisticsController.calculateTotalHoursDeviceByYearAndMonth(steps)[0]);
        //calculate for month i if same month & not same day
        steps.clear();
        step1.setEnd("2020-01-11");
        steps.add(step1);
        Assert.assertEquals(19,statisticsController.calculateTotalHoursDeviceByYearAndMonth(steps)[0]);

        //not same month February => march leap
        steps.clear();
        statisticsController.setSelectedYear("2016");
        step1.setStart("2016-02-28");
        step1.setEnd("2016-03-01");
        steps.add(step1);
        Assert.assertEquals(22,statisticsController.calculateTotalHoursDeviceByYearAndMonth(steps)[1]);
        Assert.assertEquals(9,statisticsController.calculateTotalHoursDeviceByYearAndMonth(steps)[2]);
        //not same month February => march not leap
        steps.clear();
        statisticsController.setSelectedYear("2017");
        step1.setStart("2017-02-28");
        step1.setEnd("2017-03-01");
        steps.add(step1);
        Assert.assertEquals(10,statisticsController.calculateTotalHoursDeviceByYearAndMonth(steps)[1]);
        Assert.assertEquals(9,statisticsController.calculateTotalHoursDeviceByYearAndMonth(steps)[2]);

        //if even month -> odd month => +30
        steps.clear();
        statisticsController.setSelectedYear("2020");
        step1.setStart("2020-04-30");
        step1.setEnd("2020-05-01");
        steps.add(step1);
        Assert.assertEquals(10,statisticsController.calculateTotalHoursDeviceByYearAndMonth(steps)[3]);
        Assert.assertEquals(9,statisticsController.calculateTotalHoursDeviceByYearAndMonth(steps)[4]);
        //if odd month -> even month => +31
        steps.clear();
        step1.setStart("2020-05-30");
        step1.setEnd("2020-06-01");
        steps.add(step1);
        Assert.assertEquals(22,statisticsController.calculateTotalHoursDeviceByYearAndMonth(steps)[4]);
        Assert.assertEquals(9,statisticsController.calculateTotalHoursDeviceByYearAndMonth(steps)[5]);

        //more than one month start even month
        steps.clear();
        step1.setStart("2020-04-30");
        step1.setEnd("2020-06-01");
        steps.add(step1);
        Assert.assertEquals(10,statisticsController.calculateTotalHoursDeviceByYearAndMonth(steps)[3]);
        Assert.assertEquals(366,statisticsController.calculateTotalHoursDeviceByYearAndMonth(steps)[4]);
        Assert.assertEquals(9,statisticsController.calculateTotalHoursDeviceByYearAndMonth(steps)[5]);
        //more than one month start odd month
        steps.clear();
        step1.setStart("2020-09-30");
        step1.setEnd("2020-11-01");
        steps.add(step1);
        Assert.assertEquals(22,statisticsController.calculateTotalHoursDeviceByYearAndMonth(steps)[8]);
        Assert.assertEquals(366,statisticsController.calculateTotalHoursDeviceByYearAndMonth(steps)[9]);
        Assert.assertEquals(9,statisticsController.calculateTotalHoursDeviceByYearAndMonth(steps)[10]);
    }

    //Should probably be divided in different test for each possible outcome
    @Test
    public void calculateOccupancyHoursTest(){
        Step step1 = new Step();
        List<Step> steps = new ArrayList<>();
        Float labOpeningHoursInYear = statisticsController.getLabOpeningHoursInYear();
        //calculate if same month & same day
        step1.setStart("2020-01-10");
        step1.setEnd("2020-01-10");
        step1.setStartHour("10:00");
        step1.setEndHour("17:00");
        steps.add(step1);
        Assert.assertEquals((7/labOpeningHoursInYear)*100, statisticsController.calculateOccupancyHours(steps, 0),0.001);
        //calculate for month i if same month & not same day
        steps.clear();
        step1.setEnd("2020-01-11");
        steps.add(step1);
        Assert.assertEquals((19/labOpeningHoursInYear)*100, statisticsController.calculateOccupancyHours(steps, 0),0.001);


        //not same month February => march leap
        steps.clear();
        statisticsController.setSelectedYear("2016");
        step1.setStart("2016-02-28");
        step1.setEnd("2016-03-01");
        steps.add(step1);
        Assert.assertEquals((31/labOpeningHoursInYear)*100, statisticsController.calculateOccupancyHours(steps, 0),0.001);

        //not same month February => march not leap
        steps.clear();
        statisticsController.setSelectedYear("2017");
        step1.setStart("2017-02-28");
        step1.setEnd("2017-03-01");
        steps.add(step1);
        Assert.assertEquals((19/labOpeningHoursInYear)*100, statisticsController.calculateOccupancyHours(steps, 0),0.001);

        //if even month -> odd month => +30
        steps.clear();
        statisticsController.setSelectedYear("2020");
        step1.setStart("2020-04-30");
        step1.setEnd("2020-05-01");
        steps.add(step1);
        Assert.assertEquals((19/labOpeningHoursInYear)*100, statisticsController.calculateOccupancyHours(steps, 0),0.001);
        //if odd month -> even month => +31
        steps.clear();
        step1.setStart("2020-05-30");
        step1.setEnd("2020-06-01");
        steps.add(step1);
        Assert.assertEquals((31/labOpeningHoursInYear)*100, statisticsController.calculateOccupancyHours(steps, 0),0.001);

        // multiple months
        steps.clear();
        step1.setStart("2020-09-30");
        step1.setEnd("2020-11-01");
        steps.add(step1);
        Assert.assertEquals((385/labOpeningHoursInYear)*100, statisticsController.calculateOccupancyHours(steps, 0),0.5);
    }

    //Should probably be divided in different test for each possible outcome
    @Test
    public void occupancyRateDaysTest(){
        List<String> bookedDaysStartTest = new ArrayList<>();
        List<String> bookedDaysEndTest = new ArrayList<>();
        Float amountOfWorkDaysInYear = statisticsController.getAmountOfWorkDaysInYear();
        List<Step> steps = new ArrayList<>();
        Step stepTest = new Step();
        Step step1 = new Step();
        Step step2 = new Step();

        //********** NO OVERLAP **********

        //same month device not booked on same start or end day
        stepTest.setStart("2020-01-10");
        stepTest.setEnd("2020-01-12");
        steps.add(stepTest);
        Assert.assertEquals((3/amountOfWorkDaysInYear)*100, statisticsController.calculateOccupancyDays(steps, 0),0.001);

        //same month same startdate other step ends
        steps.clear();
        step2.setStart("2020-01-08");
        step2.setEnd("2020-01-10");
        steps.add(step2);
        stepTest.setStart("2020-01-10");
        stepTest.setEnd("2020-01-12");
        steps.add(stepTest);
        Assert.assertEquals((5/amountOfWorkDaysInYear)*100, statisticsController.calculateOccupancyDays(steps, 0),0.001);

        //same month same enddate other step starts
        steps.clear();
        step2.setStart("2020-01-12");
        step2.setEnd("2020-01-14");
        steps.add(step2);
        stepTest.setStart("2020-01-10");
        stepTest.setEnd("2020-01-12");
        steps.add(stepTest);
        Assert.assertEquals((5/amountOfWorkDaysInYear)*100, statisticsController.calculateOccupancyDays(steps, 0),0.001);

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
        Assert.assertEquals((7/amountOfWorkDaysInYear)*100, statisticsController.calculateOccupancyDays(steps, 0),0.001);

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
        Assert.assertEquals((6/amountOfWorkDaysInYear)*100, statisticsController.calculateOccupancyDays(steps, 0),0.001);

        //different month device not booked on same start or end day february => march leap
        statisticsController.setSelectedYear("2016");
        steps.clear();
        stepTest.setStart("2016-02-28");
        stepTest.setEnd("2016-03-01");
        steps.add(stepTest);
        Assert.assertEquals((3/amountOfWorkDaysInYear)*100, statisticsController.calculateOccupancyDays(steps, 0),0.001);
        //different month device not booked on same start or end day february => march not leap
        statisticsController.setSelectedYear("2017");
        steps.clear();
        stepTest.setStart("2017-02-28");
        stepTest.setEnd("2017-03-01");
        steps.add(stepTest);
        Assert.assertEquals((2/amountOfWorkDaysInYear)*100, statisticsController.calculateOccupancyDays(steps, 0),0.001);

        //different month device not booked on same start or end day with even => odd month
        statisticsController.setSelectedYear("2020");
        steps.clear();
        stepTest.setStart("2020-04-30");
        stepTest.setEnd("2020-05-01");
        steps.add(stepTest);
        Assert.assertEquals((2/amountOfWorkDaysInYear)*100, statisticsController.calculateOccupancyDays(steps, 0),0.001);

        //different month device not booked on same start or end day with odd => even month
        statisticsController.setSelectedYear("2020");
        steps.clear();
        stepTest.setStart("2020-05-30");
        stepTest.setEnd("2020-06-01");
        steps.add(stepTest);
        Assert.assertEquals((3/amountOfWorkDaysInYear)*100, statisticsController.calculateOccupancyDays(steps, 0),0.001);

        //more than one month difference and no other steps on same start or end date
        steps.clear();
        stepTest.setStart("2020-05-01");
        stepTest.setEnd("2020-07-01");
        steps.add(stepTest);
        Assert.assertEquals((62/amountOfWorkDaysInYear)*100, statisticsController.calculateOccupancyDays(steps, 0),0.001);

        //********** 1 DAY OVERLAP ***********

        //different month device 1 step booked on same start or end day february => march leap
        statisticsController.setSelectedYear("2016");
        steps.clear();
        step1.setStart("2016-02-27");
        step1.setEnd("2016-02-28");
        steps.add(step1);
        stepTest.setStart("2016-02-28");
        stepTest.setEnd("2016-03-01");
        steps.add(stepTest);
        Assert.assertEquals((4/amountOfWorkDaysInYear)*100, statisticsController.calculateOccupancyDays(steps, 0),0.001);

        //different month device 1 booked on same start or end day february => march not leap
        statisticsController.setSelectedYear("2017");
        steps.clear();
        step1.setStart("2017-02-27");
        step1.setEnd("2017-02-28");
        steps.add(step1);
        stepTest.setStart("2017-02-28");
        stepTest.setEnd("2017-03-01");
        steps.add(stepTest);
        Assert.assertEquals((3/amountOfWorkDaysInYear)*100, statisticsController.calculateOccupancyDays(steps, 0),0.001);

        //different month device 1 booked on same start or end day with even => odd month
        statisticsController.setSelectedYear("2020");
        steps.clear();
        step1.setStart("2020-05-01");
        step1.setEnd("2020-05-02");
        steps.add(step1);
        stepTest.setStart("2020-04-30");
        stepTest.setEnd("2020-05-01");
        steps.add(stepTest);
        Assert.assertEquals((3/amountOfWorkDaysInYear)*100, statisticsController.calculateOccupancyDays(steps, 0),0.001);

        //different month device 1 booked on same start or end day with odd => even month
        statisticsController.setSelectedYear("2020");
        steps.clear();
        step1.setStart("2020-05-29");
        step1.setEnd("2020-05-30");
        steps.add(step1);
        stepTest.setStart("2020-05-30");
        stepTest.setEnd("2020-06-01");
        steps.add(stepTest);
        Assert.assertEquals((4/amountOfWorkDaysInYear)*100, statisticsController.calculateOccupancyDays(steps, 0),0.001);

        //more than one month difference and 1 other steps on same start or end date
        steps.clear();
        step1.setStart("2020-05-01");
        step1.setEnd("2020-05-02");
        steps.add(step1);
        stepTest.setStart("2020-05-02");
        stepTest.setEnd("2020-07-02");
        steps.add(stepTest);
        Assert.assertEquals((63/amountOfWorkDaysInYear)*100, statisticsController.calculateOccupancyDays(steps, 0),0.5);

        //******* 2 DAYS OVERLAP ************

        //different month device steps booked on same start and end day february => march leap
        statisticsController.setSelectedYear("2016");
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
        Assert.assertEquals((5/amountOfWorkDaysInYear)*100, statisticsController.calculateOccupancyDays(steps, 0),0.001);

        //different month device booked steps on same start and end day february => march not leap
        statisticsController.setSelectedYear("2017");
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
        Assert.assertEquals((4/amountOfWorkDaysInYear)*100, statisticsController.calculateOccupancyDays(steps, 0),0.001);

        //different month device booked steps on same start and end day with even => odd month
        statisticsController.setSelectedYear("2020");
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
        Assert.assertEquals((4/amountOfWorkDaysInYear)*100, statisticsController.calculateOccupancyDays(steps, 0),0.001);

        //different month device booked steps on same start and end day with odd => even month
        statisticsController.setSelectedYear("2020");
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
        Assert.assertEquals((5/amountOfWorkDaysInYear)*100, statisticsController.calculateOccupancyDays(steps, 0),0.001);

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
        Assert.assertEquals((64/amountOfWorkDaysInYear)*100, statisticsController.calculateOccupancyDays(steps, 0),0.5);
    }

}
