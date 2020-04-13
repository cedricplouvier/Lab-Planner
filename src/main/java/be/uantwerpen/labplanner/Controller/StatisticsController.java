package be.uantwerpen.labplanner.Controller;

import be.uantwerpen.labplanner.Model.Device;
import be.uantwerpen.labplanner.Model.DeviceType;
import be.uantwerpen.labplanner.Model.Step;
import be.uantwerpen.labplanner.Service.DeviceService;
import be.uantwerpen.labplanner.Service.DeviceTypeService;
import be.uantwerpen.labplanner.Service.StepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static java.lang.Math.round;

@Controller
public class StatisticsController {

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private DeviceTypeService deviceTypeService;

    @Autowired
    private StepService stepService;

    int[] totalHoursEmpty = new int[]{0,0,0,0,0,0,0,0,0,0,0,0};
    List<int[]> totalHours = new ArrayList<int[]>(Arrays.asList(totalHoursEmpty,totalHoursEmpty,totalHoursEmpty,totalHoursEmpty,totalHoursEmpty));
    List<Float> occupancyDevicesHours = new ArrayList<Float>(Arrays.asList(new Float(0.00),new Float(0.00),new Float(0.00),new Float(0.00),new Float(0.00)));
    List<Float> occupancyDevicesDays = new ArrayList<Float>(Arrays.asList(new Float(0.00),new Float(0.00),new Float(0.00),new Float(0.00),new Float(0.00)));
    List<Device> selectedDevices = new ArrayList<>(Arrays.asList(new Device(),new Device(),new Device(),new Device(),new Device()));
    List<String> selectableYears = new ArrayList<>(Arrays.asList("2019","2020","2021", "2021"));
    List<String> selectableGraphTypes = new ArrayList<>(Arrays.asList("Device hours by month","Device occupancy rate in hours","Device occupancy rate in days"));
    int deviceCounter=0;
    String selectedYear = getCurrentYear();
    String selectedTypeOfGraph= "Device hours by month";
    float amountOfWorkDaysInYear = 200;
    float labOpeningTime = 8;
    float labClosingTime = 20;
    float labOpeningHoursInYear = amountOfWorkDaysInYear*(labClosingTime-labOpeningTime);
    int highestAbsoluteValueHours=0;

    @PreAuthorize("hasAnyAuthority('Statistics Access')")
    @RequestMapping(value = "/statistics/statistics", method = RequestMethod.GET)
    public String showStatisticsPage(final ModelMap model) {
        List<Device> devices = deviceService.findAll();
        List<DeviceType> deviceTypes = deviceTypeService.findAll();

        model.addAttribute("allDevices", devices);
        model.addAttribute("allDeviceTypes", deviceTypes);
        model.addAttribute("selectedDev",new Device());
        model.addAttribute("dev1",selectedDevices.get(0));
        model.addAttribute("dev2",selectedDevices.get(1));
        model.addAttribute("dev3",selectedDevices.get(2));
        model.addAttribute("dev4",selectedDevices.get(3));
        model.addAttribute("dev5",selectedDevices.get(4));
        //Absolute hours of each device by year and month
        model.addAttribute("totalHours1",totalHours.get(0));
        model.addAttribute("totalHours2",totalHours.get(1));
        model.addAttribute("totalHours3",totalHours.get(2));
        model.addAttribute("totalHours4",totalHours.get(3));
        model.addAttribute("totalHours5",totalHours.get(4));
        //Occupancy rate of devices by year in hours
        model.addAttribute("occupancyDevices1", occupancyDevicesHours.get(0));
        model.addAttribute("occupancyDevices2", occupancyDevicesHours.get(1));
        model.addAttribute("occupancyDevices3", occupancyDevicesHours.get(2));
        model.addAttribute("occupancyDevices4", occupancyDevicesHours.get(3));
        model.addAttribute("occupancyDevices5", occupancyDevicesHours.get(4));
        //Occupancy rate of devices by year in days
        model.addAttribute("occupancyDevicesHours1", occupancyDevicesDays.get(0));
        model.addAttribute("occupancyDevicesHours2", occupancyDevicesDays.get(1));
        model.addAttribute("occupancyDevicesHours3", occupancyDevicesDays.get(2));
        model.addAttribute("occupancyDevicesHours4", occupancyDevicesDays.get(3));
        model.addAttribute("occupancyDevicesHours5", occupancyDevicesDays.get(4));

        model.addAttribute("deviceCounter", deviceCounter);
        model.addAttribute("selectableYears",selectableYears);
        model.addAttribute("selectedYear", selectedYear);

        model.addAttribute("selectableGraphTypes",selectableGraphTypes);
        model.addAttribute("selectedTypeOfGraph",selectedTypeOfGraph);

        // value to scale the y-axis
        model.addAttribute("highestAbsoluteValueHours",highestAbsoluteValueHours);

        return "/Statistics/statistics";
    }

    @PreAuthorize("hasAnyAuthority('Statistics Access')")
    @RequestMapping(value ="/statistics/statistics/submit")
    public String submit(Device selectedDev){

        List<Step> allSteps = stepService.findAll();
        int[] totalHoursSelectedDevice;
        float occupancyHours;
        float occupancyDays;
        float totalDeviceHoursYear=0;
        float totalDeviceDaysYear=0;

        selectedDevices.set(deviceCounter,selectedDev);

        //calculate occupancy of device by hours and year per year + total of device hours by year and month absolute
        for(int i=0;i<selectedDevices.size();i++){
            Device dev = selectedDevices.get(i);
            List<Step> selectedDeviceSteps = filterSelectedDeviceSteps(dev,allSteps);
            occupancyHours = calculateOccupancyHours(selectedDeviceSteps, totalDeviceHoursYear);
            occupancyDevicesHours.set(i,occupancyHours);
            occupancyDays = calculateOccupancyDays(selectedDeviceSteps, totalDeviceDaysYear);
            occupancyDevicesDays.set(i,occupancyDays);
            totalHoursSelectedDevice= calculateTotalHoursDeviceByYearAndMonth(selectedDeviceSteps);
            totalHours.set(i,totalHoursSelectedDevice);
            //get highest absolute value to scale the y axis
            for(int j=0; j<totalHoursSelectedDevice.length;j++){
                if(totalHoursSelectedDevice[j] > highestAbsoluteValueHours){
                    highestAbsoluteValueHours = totalHoursSelectedDevice[j];
                }
            }
        }

        deviceCounter++;
        return "redirect:/statistics/statistics";
    }


    @PreAuthorize("hasAnyAuthority('Statistics Access')")
    @RequestMapping("/statistics/statistics/clearList")
    public String clearList() {
        deviceCounter = 0;
        selectedDevices = Arrays.asList(new Device(),new Device(),new Device(),new Device(),new Device());
        int[] emptyHoursArray =new int[]{0,0,0,0,0,0,0,0,0,0,0,0};

        for(int j=0; j<totalHours.size();j++) {
            totalHours.set(j, emptyHoursArray);
        }
        return "redirect:/statistics";
    }

    @PreAuthorize("hasAnyAuthority('Statistics Access')")
    @RequestMapping("/statistics/statistics/getSelectedYear")
    public String getSelectedYear(String selectedYear){
        this.selectedYear=selectedYear;
        return "redirect:/statistics/statistics/refreshYear";
    }

    @PreAuthorize("hasAnyAuthority('Statistics Access')")
    @RequestMapping("/statistics/statistics/getSelectedGraphType")
    public String getSelectedGraphType(String selectedTypeOfGraph){
        this.selectedTypeOfGraph=selectedTypeOfGraph;
        return "redirect:/statistics/statistics/refreshYear";
    }

    @PreAuthorize("hasAnyAuthority('Statistics Access')")
    @RequestMapping("/statistics/statistics/refreshYear")
    public String refreshYear(){

        List<Step> allSteps = stepService.findAll();
        int[] totalHoursSelectedDevice;
        float occupancyHours;
        float occupancyDays;
        float totalDeviceHoursYear=0;
        float totalDeviceDaysYear=0;

        //calculate occupancy of device by hours and year per year + total of device hours by year and month absolute
        for(int i=0;i<selectedDevices.size();i++){
            Device dev = selectedDevices.get(i);
            List<Step> selectedDeviceSteps = filterSelectedDeviceSteps(dev,allSteps);
            occupancyHours = calculateOccupancyHours(selectedDeviceSteps, totalDeviceHoursYear);
            occupancyDevicesHours.set(i,occupancyHours);
            occupancyDays = calculateOccupancyDays(selectedDeviceSteps, totalDeviceDaysYear);
            occupancyDevicesDays.set(i,occupancyDays);
            totalHoursSelectedDevice= calculateTotalHoursDeviceByYearAndMonth(selectedDeviceSteps);
            totalHours.set(i,totalHoursSelectedDevice);
            //get highest absolute value to scale the y axis
            for(int j=0; j<totalHoursSelectedDevice.length;j++){
                if(totalHoursSelectedDevice[j] > highestAbsoluteValueHours){
                    highestAbsoluteValueHours = totalHoursSelectedDevice[j];
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

    public int[] calculateTotalHoursDeviceByYearAndMonth(List<Step> selectedDeviceSteps){
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
                if(yearStep.matches(selectedYear)) {
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
                            int fullDaysThisMonth= 31 - Integer.parseInt(startDay);
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

    public float calculateOccupancyHours(List<Step> selectedDeviceSteps, float totalDeviceHoursYear){
        float occupancySelectedYearHours=0;
        for(int j=0;j<selectedDeviceSteps.size();j++){
            Step devStep = selectedDeviceSteps.get(j);
            String yearStep = getStepYearStart(selectedDeviceSteps.get(j));
            if(yearStep.matches(selectedYear)) {
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

    public float calculateOccupancyDays(List<Step> selectedDeviceSteps, float totalDeviceDaysYear){
        float occupancySelectedYearDays=0;
        List<String> bookedDaysStart = new ArrayList<>();
        List<String> bookedDaysEnd = new ArrayList<>();
        for(int j=0;j<selectedDeviceSteps.size();j++){
            Step devStep = selectedDeviceSteps.get(j);
            String yearStep = getStepYearStart(selectedDeviceSteps.get(j));
            if(yearStep.matches(selectedYear)) {
                String stepDateStart = devStep.getStart();
                String stepDateEnd = devStep.getEnd();
                String startDay = getStepDayStart(devStep);
                String endDay = getStepDayEnd(devStep);
                String startMonth = getStepMonthStart(devStep);
                String endMonth = getStepMonthEnd(devStep);
                String startYear = getStepYearStart(devStep);
                String endYear = getStepYearEnd(devStep);


                //If not same startDate and not same endDate as other step and same month just do (end - start)+1
                if((bookedDaysStart.contains(stepDateStart)==false) && (bookedDaysEnd.contains(stepDateEnd)==false) && (startMonth.matches(endMonth)==true)){
                    int dayDiff = (Integer.parseInt(endDay) - Integer.parseInt(startDay))+1;
                    totalDeviceDaysYear = totalDeviceDaysYear + dayDiff;
                }
                // if same startDate but not same endDate as other step and same month do (end - start) OR not same startDate but same endDate
                else if(((bookedDaysStart.contains(stepDateStart)==true) && (bookedDaysEnd.contains(stepDateEnd)==false) && (startMonth.matches(endMonth)==true))
                        || ((bookedDaysStart.contains(stepDateStart)==false) && (bookedDaysEnd.contains(stepDateEnd)==true) && (startMonth.matches(endMonth)==true))){
                    float dayDiff = (Integer.parseInt(endDay) - Integer.parseInt(startDay));
                    totalDeviceDaysYear = totalDeviceDaysYear + dayDiff;
                }
                // if same startDate and same endDate as other step and same month do (end- start)-1 BUT look out for values smaller than 0 (occurs when step duration one day)
                else if((bookedDaysStart.contains(stepDateStart)==true) && (bookedDaysEnd.contains(stepDateEnd)==true) && (startMonth.matches(endMonth)==true)){
                    float dayDiff = (Integer.parseInt(endDay) - Integer.parseInt(startDay))-1;
                    if(dayDiff >=0) {
                        totalDeviceDaysYear = totalDeviceDaysYear + dayDiff;
                    }
                    else{
                    }
                }
                // If not same startDate and not same endDate as other step and different month just do (end - start)+1
                else if((bookedDaysStart.contains(stepDateStart)==false) && (bookedDaysEnd.contains(stepDateEnd)==false) && (startMonth.matches(endMonth)==false)){
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
                else if(((bookedDaysStart.contains(stepDateStart)==true) && (bookedDaysEnd.contains(stepDateEnd)==false) && (startMonth.matches(endMonth)==false))
                        || ((bookedDaysStart.contains(stepDateStart)==false) && (bookedDaysEnd.contains(stepDateEnd)==true) && (startMonth.matches(endMonth)==false))){
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
                else if((bookedDaysStart.contains(stepDateStart)==true) && (bookedDaysEnd.contains(stepDateEnd)==true) && (startMonth.matches(endMonth)==false)){
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

    public static void getCurrentTimeUsingDate() {
        Date date = new Date();
        String strDateFormat = "yyyy-MM-dd";
        DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
        String formattedDate = dateFormat.format(date);
    }

    public static String getCurrentYear() {
        Date date = new Date();
        String strDateFormat = "yyyy";
        DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
        String formattedDate = dateFormat.format(date);
        return formattedDate;
    }

}
