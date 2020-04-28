package be.uantwerpen.labplanner.Controller;

import be.uantwerpen.labplanner.Model.*;
import be.uantwerpen.labplanner.Service.*;
import be.uantwerpen.labplanner.common.model.stock.Product;
import be.uantwerpen.labplanner.common.model.users.User;
import be.uantwerpen.labplanner.common.service.stock.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.lang.Math.round;

@Controller
@SessionAttributes({"deviceCounter", "selectedYear", "selectedTypeOfGraph", "selectedDevices", "occupancyDevicesHours",
                    "occupancyDevicesDays", "totalHours", "highestAbsoluteValueHours", "productNames"})
public class StatisticsController {

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private DeviceTypeService deviceTypeService;

    @Autowired
    private StepService stepService;

    @Autowired
    private CompositionService compositionService;

    @Autowired
    private PieceOfMixtureService pieceOfMixtureService;

    @Autowired
    private ExperimentService experimentService;

    @Autowired
    private OwnProductService productService;

    @ModelAttribute("deviceCounter")
    private int getdeviceCounter(){
        return 0;
    }
    
    @ModelAttribute("selectedTypeOfGraph")
    private String graphType(){
        return "Device hours by month";
    }

    @ModelAttribute("selectedYear")
    private String selectYear(){
        return getCurrentYear();
    }

    @ModelAttribute("selectedDevices")
    private List<Device> selectDev(){
        return new ArrayList<>(Arrays.asList(new Device(),new Device(),new Device(),new Device(),new Device()));
    }

    @ModelAttribute("occupancyDevicesHours")
    private List<Float> occupancyDevHours(){
        return new ArrayList<Float>(Arrays.asList(new Float(0.00),new Float(0.00),new Float(0.00),new Float(0.00),new Float(0.00)));
    }

    @ModelAttribute("occupancyDevicesDays")
    private List<Float> occupancyDevDays(){
        return new ArrayList<Float>(Arrays.asList(new Float(0.00),new Float(0.00),new Float(0.00),new Float(0.00),new Float(0.00)));
    }

    @ModelAttribute("totalHours")
    private List<int[]> totalDeviceHours(){
        return new ArrayList<int[]>(Arrays.asList(
                new int[]{0,0,0,0,0,0,0,0,0,0,0,0},
                new int[]{0,0,0,0,0,0,0,0,0,0,0,0},
                new int[]{0,0,0,0,0,0,0,0,0,0,0,0},
                new int[]{0,0,0,0,0,0,0,0,0,0,0,0},
                new int[]{0,0,0,0,0,0,0,0,0,0,0,0}));
    }

    @ModelAttribute("highestAbsoluteValueHours")
    private int highestAbsValue(){
        return 0;
    }

    @ModelAttribute("productNames")
    private List<String> prodNames(){
        return new ArrayList<>();
    }

    @ModelAttribute("selectableYears")
    private List<String> selectableYears(){
        return new ArrayList<>(Arrays.asList("2019","2020","2021", "2022"));
    }

    @ModelAttribute("selectableGraphTypes")
    public List<String> selectableGraphs() {
        return new ArrayList<>(Arrays.asList("Device hours by month","Device occupancy rate in hours","Device occupancy rate in days"));
    }

    float amountOfWorkDaysInYear = 200;
    float labOpeningTime = 8;
    float labClosingTime = 20;
    float labOpeningHoursInYear = amountOfWorkDaysInYear*(labClosingTime-labOpeningTime);

    @PreAuthorize("hasAnyAuthority('Statistics Access')")
    @RequestMapping(value = "/statistics/statistics", method = RequestMethod.GET)
    public String showStatisticsPage(final ModelMap model) {
        List<Device> devices = deviceService.findAll();
        List<DeviceType> deviceTypes = deviceTypeService.findAll();

        model.addAttribute("allDevices", devices);
        model.addAttribute("allDeviceTypes", deviceTypes);
        model.addAttribute("selectedDev",new Device());

        List<Device> listSelectedDevices = (List) model.getAttribute("selectedDevices");
        model.addAttribute("dev1",listSelectedDevices.get(0));
        model.addAttribute("dev2",listSelectedDevices.get(1));
        model.addAttribute("dev3",listSelectedDevices.get(2));
        model.addAttribute("dev4",listSelectedDevices.get(3));
        model.addAttribute("dev5",listSelectedDevices.get(4));
        //Absolute hours of each device by year and month
        List<int[]> totalHours = (List) model.getAttribute("totalHours");
        model.addAttribute("totalHours1",totalHours.get(0));
        model.addAttribute("totalHours2",totalHours.get(1));
        model.addAttribute("totalHours3",totalHours.get(2));
        model.addAttribute("totalHours4",totalHours.get(3));
        model.addAttribute("totalHours5",totalHours.get(4));
        //Occupancy rate of devices by year in hours
        List<Float> occupancyDevicesHours = (List) model.getAttribute("occupancyDevicesHours");
        model.addAttribute("occupancyDevices1", occupancyDevicesHours.get(0));
        model.addAttribute("occupancyDevices2", occupancyDevicesHours.get(1));
        model.addAttribute("occupancyDevices3", occupancyDevicesHours.get(2));
        model.addAttribute("occupancyDevices4", occupancyDevicesHours.get(3));
        model.addAttribute("occupancyDevices5", occupancyDevicesHours.get(4));
        //Occupancy rate of devices by year in days
        List<Float> occupancyDevicesDays = (List) model.getAttribute("occupancyDevicesDays");
        model.addAttribute("occupancyDevicesHours1", occupancyDevicesDays.get(0));
        model.addAttribute("occupancyDevicesHours2", occupancyDevicesDays.get(1));
        model.addAttribute("occupancyDevicesHours3", occupancyDevicesDays.get(2));
        model.addAttribute("occupancyDevicesHours4", occupancyDevicesDays.get(3));
        model.addAttribute("occupancyDevicesHours5", occupancyDevicesDays.get(4));

        //model.addAttribute("deviceCounter", deviceCounter);
        model.addAttribute("deviceCounter");
        model.addAttribute("selectableYears");
        model.addAttribute("selectedYear");

        model.addAttribute("selectableGraphTypes");
        model.addAttribute("selectedTypeOfGraph");

        // value to scale the y-axis
        model.addAttribute("highestAbsoluteValueHours");

        return "Statistics/statistics";
    }

    @PreAuthorize("hasAnyAuthority('Statistics Access')")
    @RequestMapping(value = "/statistics/stockStatistics", method = RequestMethod.GET)
    public String showStatisticsStockPage(final ModelMap model) {

        List<OwnProduct> products = productService.findAll();
        List<Double> currentStockLevel = new ArrayList<>();
        List<String> productNames = (List) model.getAttribute("productNames");
        //get all the product names
        for(OwnProduct product: products){
            productNames.add(product.getName());
        }

        for(OwnProduct product: products){
            currentStockLevel.add(product.getStockLevel());
        }

        //get all the stock levels
        model.addAttribute("products",products);
        model.addAttribute("productNames",productNames);
        model.addAttribute("currentStockLevel",currentStockLevel);

        for(int j=0; j<products.size();j++) {
            currentStockLevel.add(products.get(j).getStockLevel());
        }

        return "Statistics/stockStatistics";
    }


    @PreAuthorize("hasAnyAuthority('Statistics Access')")
    @RequestMapping(value ="/statistics/statistics/submit")
    public String submit(final ModelMap model, Device selectedDev, RedirectAttributes redAttr){

        List<Device> listSelectedDevices = (List) model.getAttribute("selectedDevices");
        List<Float> occupancyDevicesHours = (List) model.getAttribute("occupancyDevicesHours");
        List<Float> occupancyDevicesDays = (List) model.getAttribute("occupancyDevicesDays");
        List<int[]> totalHours = (List) model.getAttribute("totalHours");
        List<Step> allSteps = stepService.findAll();
        int[] totalHoursSelectedDevice;
        float occupancyHours;
        float occupancyDays;
        float totalDeviceHoursYear=0;
        float totalDeviceDaysYear=0;
        boolean duplicate = false;
        //highestAbsoluteValueHours=10;
        //model.addAttribute("highestAbsoluteValueHours", 10);
        for (int i = 0; i < listSelectedDevices.size(); i++) {
            Device dev = listSelectedDevices.get(i);
            if(dev.getDevicename().matches(selectedDev.getDevicename())){
                duplicate = true;
            }
        }
            if(!duplicate) {
                if ((int) model.getAttribute("deviceCounter") < 5) {
                    listSelectedDevices.set((int) model.getAttribute("deviceCounter"), selectedDev);
                    //calculate occupancy of device by hours and year per year + total of device hours by year and month absolute
                    for (int i = 0; i < listSelectedDevices.size(); i++) {
                        Device dev = listSelectedDevices.get(i);
                        List<Step> selectedDeviceSteps = filterSelectedDeviceSteps(dev, allSteps);
                        occupancyHours = calculateOccupancyHours(model, selectedDeviceSteps, totalDeviceHoursYear);
                        occupancyDevicesHours.set(i, occupancyHours);
                        occupancyDays = calculateOccupancyDays(model, selectedDeviceSteps, totalDeviceDaysYear);
                        occupancyDevicesDays.set(i, occupancyDays);
                        totalHoursSelectedDevice = calculateTotalHoursDeviceByYearAndMonth(model, selectedDeviceSteps);
                        totalHours.set(i, totalHoursSelectedDevice);
                        //get highest absolute value to scale the y axis
                        for (int j = 0; j < totalHoursSelectedDevice.length; j++) {
                            if (totalHoursSelectedDevice[j] >= (int) model.getAttribute("highestAbsoluteValueHours")) {
                                model.addAttribute("highestAbsoluteValueHours", totalHoursSelectedDevice[j]);
                            }
                        }
                    }
                    int dc = (int) model.get("deviceCounter") + 1;
                    model.addAttribute("deviceCounter", dc);
                } else {
                    redAttr.addFlashAttribute("Status", "deviceLimit");
                }
            }
            else{
                redAttr.addFlashAttribute("Status", "deviceDuplicate");
            }
        return "redirect:/statistics/statistics";
    }

    @PreAuthorize("hasAnyAuthority('Statistics Access')")
    @RequestMapping("/statistics/statistics/clearList")
    public String clearList(final ModelMap model) {

        List<int[]> totalHours = (List) model.getAttribute("totalHours");
        int[] emptyHoursArray =new int[]{0,0,0,0,0,0,0,0,0,0,0,0};
        for(int j=0; j<totalHours.size();j++) {
            totalHours.set(j, emptyHoursArray);
        }
        model.addAttribute("occupancyDeviceDays",new ArrayList<Float>(Arrays.asList(new Float(0.00),new Float(0.00),new Float(0.00),new Float(0.00),new Float(0.00))));
        model.addAttribute("occupancyDevicesHours", new ArrayList<Float>(Arrays.asList(new Float(0.00),new Float(0.00),new Float(0.00),new Float(0.00),new Float(0.00))));
        model.addAttribute("deviceCounter",0);
        model.addAttribute("highestAbsoluteValueHours",10);
        model.addAttribute("selectedDevices", new ArrayList<>(Arrays.asList(new Device(),new Device(),new Device(),new Device(),new Device())));

        return "redirect:/statistics";
    }

    @PreAuthorize("hasAnyAuthority('Statistics Access')")
    @RequestMapping("/statistics/statistics/getSelectedYear")
    public String getSelectedYear(final ModelMap model, String selectedYear){
        setSelectedYear(model, selectedYear);
        return "redirect:/statistics/statistics/refreshYear";
    }

    @PreAuthorize("hasAnyAuthority('Statistics Access')")
    @RequestMapping("/statistics/statistics/getSelectedGraphType")
    public String getSelectedGraphType(final ModelMap model, String selectedTypeOfGraph){
        //this.selectedTypeOfGraph=selectedTypeOfGraph;
        model.addAttribute("selectedTypeOfGraph", selectedTypeOfGraph);
        return "redirect:/statistics/statistics/refreshYear";
    }

    @PreAuthorize("hasAnyAuthority('Statistics Access')")
    @RequestMapping("/statistics/statistics/refreshYear")
    public String refreshYear(final ModelMap model){
        List<Device> listSelectedDevices = (List) model.getAttribute("selectedDevices");
        List<Float> occupancyDevicesHours = (List) model.getAttribute("occupancyDevicesHours");
        List<Float> occupancyDevicesDays = (List) model.getAttribute("occupancyDevicesDays");
        List<int[]> totalHours = (List) model.getAttribute("totalHours");
        List<Step> allSteps = stepService.findAll();
        int[] totalHoursSelectedDevice;
        float occupancyHours;
        float occupancyDays;
        float totalDeviceHoursYear=0;
        float totalDeviceDaysYear=0;
        model.addAttribute("highestAbsoluteValueHours", 10);

        //calculate occupancy of device by hours and year per year + total of device hours by year and month absolute
        for(int i=0;i<listSelectedDevices.size();i++){
            Device dev = listSelectedDevices.get(i);
            List<Step> selectedDeviceSteps = filterSelectedDeviceSteps(dev,allSteps);
            occupancyHours = calculateOccupancyHours(model, selectedDeviceSteps, totalDeviceHoursYear);
            occupancyDevicesHours.set(i,occupancyHours);
            occupancyDays = calculateOccupancyDays(model, selectedDeviceSteps, totalDeviceDaysYear);
            occupancyDevicesDays.set(i,occupancyDays);
            totalHoursSelectedDevice= calculateTotalHoursDeviceByYearAndMonth(model, selectedDeviceSteps);
            totalHours.set(i,totalHoursSelectedDevice);
            //get highest absolute value to scale the y axis
            for(int j=0; j<totalHoursSelectedDevice.length;j++){
                if(totalHoursSelectedDevice[j] >= (int) model.getAttribute("highestAbsoluteValueHours")){
                    model.addAttribute("highestAbsoluteValueHours", totalHoursSelectedDevice[j]);
                }
            }
        }
        return "redirect:/statistics/statistics";
    }


    public int calculateHourDiff(Step step){
        String startTime = step.getStartHour();
        String stopTime = step.getEndHour();
        String[] startTimeParts = startTime.split(":");
        String[] stopTimeParts = stopTime.split(":");
        String startTimeHour = startTimeParts[0];
        String startTimeMinutes = startTimeParts[1];
        String stopTimeHour = stopTimeParts[0];
        String stopTimeMinutes = stopTimeParts[1];
        int hourDiff = Integer.parseInt(stopTimeHour) - Integer.parseInt(startTimeHour);
        return hourDiff;
    }

    public List<Step> filterSelectedDeviceSteps(Device selectedDev, List<Step> allSteps){

        List<Step> selectedDeviceSteps = new ArrayList<>();
        for(int i=0;i<allSteps.size();i++){
            Step tempStep = allSteps.get(i);
            if(selectedDev.getDevicename().equals(tempStep.getDevice().getDevicename())){
                selectedDeviceSteps.add(tempStep);
            }
        }
        return selectedDeviceSteps;
    }

    public int[] calculateTotalHoursDeviceByYearAndMonth(final ModelMap model, List<Step> selectedDeviceSteps){
        int[] totalHoursByMonth = new int[12];
        String[] months = new String[]{"01","02","03","04","05","06","07","08","09","10","11","12"};
        for(int j=0;j<selectedDeviceSteps.size();j++){
            Step devStep = selectedDeviceSteps.get(j);
            String startMonth = getStepMonthStart(devStep);
            String endMonth = getStepMonthEnd(devStep);
            String startDay = getStepDayStart(devStep);
            String endDay = getStepDayEnd(devStep);
            String startHour = getStepHourStart(devStep);
            String endHour = getStepHourEnd(devStep);
            String yearStep = getStepYearStart(devStep);
            for(int i = 0; i<months.length;i++) {
                if(yearStep.matches((String) model.getAttribute("selectedYear"))) {
                    //calculate for month i if same month
                    if ((startMonth.matches(months[i])) && (startMonth.matches(endMonth)==true)) {
                        if(startDay.matches(endDay)==true) {
                            totalHoursByMonth[i] = totalHoursByMonth[i] + calculateHourDiff(selectedDeviceSteps.get(j));
                        }
                        else if (startDay.matches(endDay)==false){
                            int dayDiff = ((Integer.parseInt(endDay) - Integer.parseInt(startDay)))-1;
                            totalHoursByMonth[i] = (int) (totalHoursByMonth[i] + (labClosingTime-Integer.parseInt(startHour)) + (dayDiff*(labClosingTime-labOpeningTime)) + (Integer.parseInt(endHour)-labOpeningTime));
                        }
                    }
                    //not same month same year
                    if ((startMonth.matches(months[i])) && (startMonth.matches(endMonth)==false)) {
                        float monthsDifference = (Integer.parseInt(endMonth))-(Integer.parseInt(startMonth));
                        if(monthsDifference==1) {
                            // If february -> march
                            if (startMonth.matches("02")) {
                                //check leap year
                                int startYearInt = Integer.parseInt(yearStep);
                                boolean leap = checkLeapYear(startYearInt);
                                //if leap +29
                                if (leap) {
                                    int fullDaysThisMonth= 29 - Integer.parseInt(startDay);
                                    int fullDaysNextMonth= Integer.parseInt(endDay)-1;
                                    totalHoursByMonth[i] = (int) (totalHoursByMonth[i] + (labClosingTime-Integer.parseInt(startHour)) + (fullDaysThisMonth*(labClosingTime-labOpeningTime)));
                                    totalHoursByMonth[i+1] = (int) (totalHoursByMonth[i+1] + (fullDaysNextMonth*(labClosingTime-labOpeningTime)) + (Integer.parseInt(endHour)-labOpeningTime));

                                }
                                //if not leap +28
                                else {
                                    int fullDaysThisMonth= 28 - Integer.parseInt(startDay);
                                    int fullDaysNextMonth= Integer.parseInt(endDay)-1;
                                    totalHoursByMonth[i] = (int) (totalHoursByMonth[i] + (labClosingTime-Integer.parseInt(startHour)) + (fullDaysThisMonth*(labClosingTime-labOpeningTime)));
                                    totalHoursByMonth[i+1] = (int) (totalHoursByMonth[i+1] + (fullDaysNextMonth*(labClosingTime-labOpeningTime)) + (Integer.parseInt(endHour)-labOpeningTime));

                                }
                            }
                            //if even month -> odd month => +30
                            else if (Integer.parseInt(startMonth) % 2 == 0) {
                                int fullDaysThisMonth= 30 - Integer.parseInt(startDay);
                                int fullDaysNextMonth= Integer.parseInt(endDay)-1;
                                totalHoursByMonth[i] = (int) (totalHoursByMonth[i] + (labClosingTime-Integer.parseInt(startHour)) + (fullDaysThisMonth*(labClosingTime-labOpeningTime)));
                                totalHoursByMonth[i+1] = (int) (totalHoursByMonth[i+1] + (fullDaysNextMonth*(labClosingTime-labOpeningTime)) + (Integer.parseInt(endHour)-labOpeningTime));
                            }
                            // if odd month -> even month => +31
                            else {
                                int fullDaysThisMonth= 31 - Integer.parseInt(startDay);
                                int fullDaysNextMonth= Integer.parseInt(endDay)-1;
                                totalHoursByMonth[i] = (int) (totalHoursByMonth[i] + (labClosingTime-Integer.parseInt(startHour)) + (fullDaysThisMonth*(labClosingTime-labOpeningTime)));
                                totalHoursByMonth[i+1] = (int) (totalHoursByMonth[i+1] + (fullDaysNextMonth*(labClosingTime-labOpeningTime)) + (Integer.parseInt(endHour)-labOpeningTime));
                            }
                        }
                        //If more than one month we take 30.5 as average and don't take into account februari or leap years, to reduce complexity....
                        else{
                            int fullDaysThisMonth;
                            if(Integer.parseInt(startMonth) % 2 == 0) {
                                fullDaysThisMonth = 30 - Integer.parseInt(startDay);
                            }
                            else{
                                fullDaysThisMonth=31- Integer.parseInt(startDay);
                            }
                            int fullDaysLastMonth= Integer.parseInt(endDay)-1;
                            float fullDaysMonthsInbetween = (float)30.5;
                            totalHoursByMonth[i] = (int) (totalHoursByMonth[i] + (labClosingTime-Integer.parseInt(startHour)) + (fullDaysThisMonth*(labClosingTime-labOpeningTime)));
                            totalHoursByMonth[i+(int)monthsDifference] = (int) (totalHoursByMonth[i+(int)monthsDifference] + (fullDaysLastMonth*(labClosingTime-labOpeningTime)) + (Integer.parseInt(endHour)-labOpeningTime));
                            // Calculate months in between
                            for(int z=1; z < monthsDifference;z++){
                                totalHoursByMonth[i+z] = (int) (totalHoursByMonth[i+z] + (fullDaysMonthsInbetween*(labClosingTime-labOpeningTime)));
                            }
                        }
                    }
                }
            }
        }
        return totalHoursByMonth;
    }

    public float calculateOccupancyHours(final ModelMap model, List<Step> selectedDeviceSteps, float totalDeviceHoursYear){
        float occupancySelectedYearHours=0;
        for(int j=0;j<selectedDeviceSteps.size();j++){
            Step devStep = selectedDeviceSteps.get(j);
            String yearStep = getStepYearStart(selectedDeviceSteps.get(j));
            if(yearStep.matches((String)model.getAttribute("selectedYear"))) {
                String startDay = getStepDayStart(devStep);
                String endDay = getStepDayEnd(devStep);
                String startMonth = getStepMonthStart(devStep);
                String endMonth = getStepMonthEnd(devStep);
                String startHour = getStepHourStart(devStep);
                String endHour = getStepHourEnd(devStep);
                String startYear = getStepYearStart(devStep);
                // if step only takes one day
                if((startDay.matches(endDay)==true) && (startMonth.matches(endMonth)==true)) {
                    totalDeviceHoursYear = totalDeviceHoursYear + calculateHourDiff(selectedDeviceSteps.get(j));
                }
                // if step is accros multiple days in same month
                else if((startDay.matches(endDay)==false) && (startMonth.matches(endMonth)==true)) {
                    int dayDiff = ((Integer.parseInt(endDay) - Integer.parseInt(startDay)))-1;
                    totalDeviceHoursYear = totalDeviceHoursYear + (labClosingTime-Integer.parseInt(startHour)) + (dayDiff*(labClosingTime-labOpeningTime)) + (Integer.parseInt(endHour)-labOpeningTime);
                }
                //if step over multiple months in same year
                else if(startMonth.matches(endMonth)==false){
                    float monthsDifference = (Integer.parseInt(endMonth))-(Integer.parseInt(startMonth));
                    if(monthsDifference==1) {
                        // If february -> march
                        if (startMonth.matches("02")) {
                            //check leap year
                            int startYearInt = Integer.parseInt(startYear);
                            boolean leap = checkLeapYear(startYearInt);
                            //if leap +29
                            if (leap) {
                                float dayDiff = ((Integer.parseInt(endDay) + 29) - Integer.parseInt(startDay)) - 1;
                                totalDeviceHoursYear = totalDeviceHoursYear + (labClosingTime-Integer.parseInt(startHour)) + (dayDiff*(labClosingTime-labOpeningTime)) + (Integer.parseInt(endHour)-labOpeningTime);
                            }
                            //if not leap +28
                            else {
                                float dayDiff = ((Integer.parseInt(endDay) + 28) - Integer.parseInt(startDay)) - 1;
                                totalDeviceHoursYear = totalDeviceHoursYear + (labClosingTime-Integer.parseInt(startHour)) + (dayDiff*(labClosingTime-labOpeningTime)) + (Integer.parseInt(endHour)-labOpeningTime);
                            }
                        }
                        //if even month -> odd month => +30
                        else if (Integer.parseInt(startMonth) % 2 == 0) {
                            float dayDiff = ((Integer.parseInt(endDay) + 30) - Integer.parseInt(startDay)) - 1;
                            totalDeviceHoursYear = totalDeviceHoursYear + (labClosingTime-Integer.parseInt(startHour)) + (dayDiff*(labClosingTime-labOpeningTime)) + (Integer.parseInt(endHour)-labOpeningTime);
                        }
                        // if odd month -> even month => +31
                        else {
                            float dayDiff = ((Integer.parseInt(endDay) + 31) - Integer.parseInt(startDay)) - 1;
                            totalDeviceHoursYear = totalDeviceHoursYear + (labClosingTime-Integer.parseInt(startHour)) + (dayDiff*(labClosingTime-labOpeningTime)) + (Integer.parseInt(endHour)-labOpeningTime);
                        }
                    }
                    //If more than one month we take 30.5 as average and don't take into account februari or leap years, to reduce complexity....
                    else{
                        float extraDaysMonths = monthsDifference*((float)(30.5));
                        float dayDiff = ((Integer.parseInt(endDay) + extraDaysMonths) - Integer.parseInt(startDay)) - 1;
                        totalDeviceHoursYear = totalDeviceHoursYear + (labClosingTime-Integer.parseInt(startHour)) + (dayDiff*(labClosingTime-labOpeningTime)) + (Integer.parseInt(endHour)-labOpeningTime);
                    }
                }
            }
        }
        occupancySelectedYearHours = (totalDeviceHoursYear/labOpeningHoursInYear)*100;
        return occupancySelectedYearHours;
    }

    public float calculateOccupancyDays(final ModelMap model, List<Step> selectedDeviceSteps, float totalDeviceDaysYear){
        float occupancySelectedYearDays=0;
        List<String> bookedDaysStart = new ArrayList<>();
        List<String> bookedDaysEnd = new ArrayList<>();
        for(int j=0;j<selectedDeviceSteps.size();j++){
            Step devStep = selectedDeviceSteps.get(j);
            String yearStep = getStepYearStart(selectedDeviceSteps.get(j));
            if(yearStep.matches((String) model.getAttribute("selectedYear"))) {
                String stepDateStart = devStep.getStart();
                String stepDateEnd = devStep.getEnd();
                String startDay = getStepDayStart(devStep);
                String endDay = getStepDayEnd(devStep);
                String startMonth = getStepMonthStart(devStep);
                String endMonth = getStepMonthEnd(devStep);
                String startYear = getStepYearStart(devStep);
                String endYear = getStepYearEnd(devStep);


                //If not same startDate and not same endDate as other step and same month just do (end - start)+1
                if((bookedDaysEnd.contains(stepDateStart)==false) && (bookedDaysStart.contains(stepDateEnd)==false) && (startMonth.matches(endMonth)==true)){
                    int dayDiff = (Integer.parseInt(endDay) - Integer.parseInt(startDay))+1;
                    totalDeviceDaysYear = totalDeviceDaysYear + dayDiff;
                }
                // if same startDate but not same endDate as other step and same month do (end - start) OR not same startDate but same endDate
                else if(((bookedDaysEnd.contains(stepDateStart)==true) && (bookedDaysStart.contains(stepDateEnd)==false) && (startMonth.matches(endMonth)==true))
                        || ((bookedDaysEnd.contains(stepDateStart)==false) && (bookedDaysStart.contains(stepDateEnd)==true) && (startMonth.matches(endMonth)==true))){
                    float dayDiff = (Integer.parseInt(endDay) - Integer.parseInt(startDay));
                    totalDeviceDaysYear = totalDeviceDaysYear + dayDiff;
                }
                // if same startDate and same endDate as other step and same month do (end- start)-1 BUT look out for values smaller than 0 (occurs when step duration one day)
                else if((bookedDaysEnd.contains(stepDateStart)==true) && (bookedDaysStart.contains(stepDateEnd)==true) && (startMonth.matches(endMonth)==true)){
                    float dayDiff = (Integer.parseInt(endDay) - Integer.parseInt(startDay))-1;
                    if(dayDiff >=0) {
                        totalDeviceDaysYear = totalDeviceDaysYear + dayDiff;
                    }
                    else{
                    }
                }
                // If not same startDate and not same endDate as other step and different month just do (end - start)+1
                else if((bookedDaysEnd.contains(stepDateStart)==false) && (bookedDaysStart.contains(stepDateEnd)==false) && (startMonth.matches(endMonth)==false)){
                    float monthsDifference = (Integer.parseInt(endMonth))-(Integer.parseInt(startMonth));
                    if(monthsDifference==1) {
                        // If february -> march
                        if (startMonth.matches("02")) {
                            //check leap year
                            int startYearInt = Integer.parseInt(startYear);
                            boolean leap = checkLeapYear(startYearInt);
                            //if leap +29
                            if (leap) {
                                float dayDiff = ((Integer.parseInt(endDay) + 29) - Integer.parseInt(startDay)) + 1;
                                totalDeviceDaysYear = totalDeviceDaysYear + dayDiff;
                            }
                            //if not leap +28
                            else {
                                float dayDiff = ((Integer.parseInt(endDay) + 28) - Integer.parseInt(startDay)) + 1;
                                totalDeviceDaysYear = totalDeviceDaysYear + dayDiff;
                            }
                        }
                        //if even month -> odd month => +30
                        else if (Integer.parseInt(startMonth) % 2 == 0) {
                            float dayDiff = ((Integer.parseInt(endDay) + 30) - Integer.parseInt(startDay)) + 1;
                            totalDeviceDaysYear = totalDeviceDaysYear + dayDiff;
                        }
                        // if odd month -> even month => +31
                        else {
                            float dayDiff = ((Integer.parseInt(endDay) + 31) - Integer.parseInt(startDay)) + 1;
                            totalDeviceDaysYear = totalDeviceDaysYear + dayDiff;
                        }
                    }
                    //If more than one month we take 30.5 as average and don't take into account februari or leap years, to reduce complexity....
                    else{
                        float extraDaysMonths = monthsDifference*((float)(30.5));
                        float dayDiff = ((Integer.parseInt(endDay) + extraDaysMonths) - Integer.parseInt(startDay)) + 1;
                        totalDeviceDaysYear = totalDeviceDaysYear + dayDiff;
                    }
                }
                // if same startDate but not same endDate as other step and not same month do (end - start) OR not same startDate but same endDate
                else if(((bookedDaysEnd.contains(stepDateStart)==true) && (bookedDaysStart.contains(stepDateEnd)==false) && (startMonth.matches(endMonth)==false))
                        || ((bookedDaysEnd.contains(stepDateStart)==false) && (bookedDaysStart.contains(stepDateEnd)==true) && (startMonth.matches(endMonth)==false))){
                    float monthsDifference = (Integer.parseInt(endMonth))-(Integer.parseInt(startMonth));
                    if(monthsDifference==1) {
                        // If february -> march
                        if (startMonth.matches("02")) {
                            //check leap year
                            int startYearInt = Integer.parseInt(startYear);
                            boolean leap = checkLeapYear(startYearInt);
                            //if leap +29
                            if (leap) {
                                float dayDiff = ((Integer.parseInt(endDay) + 29) - Integer.parseInt(startDay));
                                totalDeviceDaysYear = totalDeviceDaysYear + dayDiff;
                            }
                            //if not leap +28
                            else {
                                float dayDiff = ((Integer.parseInt(endDay) + 28) - Integer.parseInt(startDay));
                                totalDeviceDaysYear = totalDeviceDaysYear + dayDiff;
                            }
                        }
                        //if even month -> odd month => +30
                        else if (Integer.parseInt(startMonth) % 2 == 0) {
                            float dayDiff = ((Integer.parseInt(endDay) + 30) - Integer.parseInt(startDay));
                            totalDeviceDaysYear = totalDeviceDaysYear + dayDiff;
                        }
                        // if odd month -> even month => +31
                        else {
                            float dayDiff = ((Integer.parseInt(endDay) + 31) - Integer.parseInt(startDay));
                            totalDeviceDaysYear = totalDeviceDaysYear + dayDiff;
                        }
                    }
                    //If more than one month we take 30.5 as average and don't take into account februari or leap years, to reduce complexity....
                    else{
                        float extraDaysMonths = monthsDifference*((float)(30.5));
                        float dayDiff = ((Integer.parseInt(endDay) + extraDaysMonths) - Integer.parseInt(startDay));
                        totalDeviceDaysYear = totalDeviceDaysYear + dayDiff;
                    }
                }
                // If same startDate and same endDate as other step and different month just do (end - start)-1
                else if((bookedDaysEnd.contains(stepDateStart)==true) && (bookedDaysStart.contains(stepDateEnd)==true) && (startMonth.matches(endMonth)==false)){
                    float monthsDifference = (Integer.parseInt(endMonth))-(Integer.parseInt(startMonth));
                    if(monthsDifference==1) {
                        // If february -> march
                        if (startMonth.matches("02")) {
                            //check leap year
                            int startYearInt = Integer.parseInt(startYear);
                            boolean leap = checkLeapYear(startYearInt);
                            //if leap +29
                            if (leap) {
                                float dayDiff = ((Integer.parseInt(endDay) + 29) - Integer.parseInt(startDay)) - 1;
                                if(dayDiff >=0) {
                                    totalDeviceDaysYear = totalDeviceDaysYear + dayDiff;
                                }
                            }
                            //if not leap +28
                            else {
                                float dayDiff = ((Integer.parseInt(endDay) + 28) - Integer.parseInt(startDay)) - 1;
                                if(dayDiff >=0) {
                                    totalDeviceDaysYear = totalDeviceDaysYear + dayDiff;
                                }
                            }
                        }
                        //if even month -> odd month => +30
                        else if (Integer.parseInt(startMonth) % 2 == 0) {
                            float dayDiff = ((Integer.parseInt(endDay) + 30) - Integer.parseInt(startDay)) - 1;
                            if(dayDiff >=0) {
                                totalDeviceDaysYear = totalDeviceDaysYear + dayDiff;
                            }
                        }
                        // if odd month -> even month => +31
                        else {
                            float dayDiff = ((Integer.parseInt(endDay) + 31) - Integer.parseInt(startDay)) - 1;
                            if(dayDiff >=0) {
                                totalDeviceDaysYear = totalDeviceDaysYear + dayDiff;
                            }
                        }
                    }
                    //If more than one month we take 30.5 as average and don't take into account februari or leap years, to reduce complexity....
                    else{
                        float extraDaysMonths = monthsDifference*((float)(30.5));
                        float dayDiff = ((Integer.parseInt(endDay) + extraDaysMonths) - Integer.parseInt(startDay)) - 1;
                        totalDeviceDaysYear = totalDeviceDaysYear + dayDiff;
                    }
                }

                if(bookedDaysStart.contains(stepDateStart) == false){
                    bookedDaysStart.add(stepDateStart);
                }
                if(bookedDaysEnd.contains(stepDateEnd)==false) {
                    bookedDaysEnd.add(stepDateEnd);
                }
            }
        }
        occupancySelectedYearDays = (totalDeviceDaysYear/amountOfWorkDaysInYear)*100;
        return occupancySelectedYearDays;
    }

    public boolean checkLeapYear(int startYear){
        boolean leap = false;
        if(startYear%4==0){
            if(startYear%100==0){
                if(startYear%400==0){
                    leap = true;
                }
                else{
                    leap = false;
                }
            }
            else {
                leap = true;
            }
        }
        else{
            leap = false;
        }
        return leap;
    }

    public String getStepMonthStart(Step step){
        String totalDate = step.getStart();
        String dateSplit[] = totalDate.split("-");
        String month = dateSplit[1];
        return month;
    }

    public String getStepMonthEnd(Step step){
        String totalDate = step.getEnd();
        String dateSplit[] = totalDate.split("-");
        String month = dateSplit[1];
        return month;
    }

    public String getStepYearStart(Step step){
        String totalDate = step.getStart();
        String dateSplit[] = totalDate.split("-");
        String year = dateSplit[0];
        return year;
    }

    public String getStepYearEnd(Step step){
        String totalDate = step.getEnd();
        String dateSplit[] = totalDate.split("-");
        String year = dateSplit[0];
        return year;
    }

    public String getStepDayStart(Step step){
        String totalDate = step.getStart();
        String dataSplit[] = totalDate.split("-");
        String day = dataSplit[2];
        return day;
    }

    public String getStepDayEnd(Step step){
        String totalDate = step.getEnd();
        String dataSplit[] = totalDate.split("-");
        String day = dataSplit[2];
        return day;
    }

    public String getStepHourStart(Step step){
        String totalHour = step.getStartHour();
        String dataSplit[] = totalHour.split(":");
        String hour = dataSplit[0];
        return hour;
    }

    public String getStepHourEnd(Step step){
        String totalHour = step.getEndHour();
        String dataSplit[] = totalHour.split(":");
        String hour = dataSplit[0];
        return hour;
    }

    public static String getCurrentYear() {
        Date date = new Date();
        String strDateFormat = "yyyy";
        DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
        String formattedDate = dateFormat.format(date);
        return formattedDate;
    }

    public void setSelectedYear(final ModelMap model, String year) {
         model.addAttribute("selectedYear",year);
    }

    public float getLabOpeningHoursInYear(){
        return labOpeningHoursInYear;
    }

    public float getAmountOfWorkDaysInYear(){
        return amountOfWorkDaysInYear;
    }
}
