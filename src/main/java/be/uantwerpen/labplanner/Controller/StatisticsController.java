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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static java.lang.Math.round;

@Controller
@SessionAttributes({"deviceCounter","productCounter", "selectedYear", "selectedTypeOfGraph", "selectedDevices", "occupancyDevicesHours",
                    "occupancyDevicesHoursPast", "occupancyDevicesHoursFuture", "occupancyDevicesDays", "occupancyDevicesDaysPast",
                    "occupancyDevicesDaysFuture","totalHours", "totalHoursPast", "totalHoursFuture", "highestAbsoluteValueHours",
                    "selectedTimePeriod", "selectedStartMonthStockHistory","selectedMonthStock", "selectedProducts",
                    "stockLevelStartMonthHistory"})
public class StatisticsController {

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private StepService stepService;

    @Autowired
    private OwnProductService productService;

    @ModelAttribute("deviceCounter")
    private int getdeviceCounter(){
        return 0;
    }

    @ModelAttribute("productCounter")
    private int getProductCounterCounter(){
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

    @ModelAttribute("selectedTimePeriod")
    private String selectTimePeriod(){
        return "Started";
    }

    @ModelAttribute("selectedDevices")
    private List<Device> selectDev(){
        return new ArrayList<>(Arrays.asList(new Device(),new Device(),new Device(),new Device(),new Device()));
    }

    @ModelAttribute("occupancyDevicesHours")
    private List<Float> occupancyDevHours(){
        return new ArrayList<Float>(Arrays.asList(new Float(0.00),new Float(0.00),new Float(0.00),new Float(0.00),new Float(0.00)));
    }

    @ModelAttribute("occupancyDevicesHoursPast")
    private List<Float> occupancyDevHoursPast(){
        return new ArrayList<Float>(Arrays.asList(new Float(0.00),new Float(0.00),new Float(0.00),new Float(0.00),new Float(0.00)));
    }

    @ModelAttribute("occupancyDevicesFuture")
    private List<Float> occupancyDevHoursFuture(){
        return new ArrayList<Float>(Arrays.asList(new Float(0.00),new Float(0.00),new Float(0.00),new Float(0.00),new Float(0.00)));
    }

    @ModelAttribute("occupancyDevicesDays")
    private List<Float> occupancyDevDays(){
        return new ArrayList<Float>(Arrays.asList(new Float(0.00),new Float(0.00),new Float(0.00),new Float(0.00),new Float(0.00)));
    }
    @ModelAttribute("occupancyDevicesDaysPast")
    private List<Float> occupancyDevDaysPast(){
        return new ArrayList<Float>(Arrays.asList(new Float(0.00),new Float(0.00),new Float(0.00),new Float(0.00),new Float(0.00)));
    }
    @ModelAttribute("occupancyDevicesDaysFuture")
    private List<Float> occupancyDevDaysFuture(){
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

    @ModelAttribute("totalHoursPast")
    private List<int[]> totalDeviceHoursPast(){
        return new ArrayList<int[]>(Arrays.asList(
                new int[]{0,0,0,0,0,0,0,0,0,0,0,0},
                new int[]{0,0,0,0,0,0,0,0,0,0,0,0},
                new int[]{0,0,0,0,0,0,0,0,0,0,0,0},
                new int[]{0,0,0,0,0,0,0,0,0,0,0,0},
                new int[]{0,0,0,0,0,0,0,0,0,0,0,0}));
    }

    @ModelAttribute("totalHoursFuture")
    private List<int[]> totalDeviceHoursFuture(){
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

    @ModelAttribute("selectableYears")
    private List<String> selectableYears(){
        return new ArrayList<>(Arrays.asList("2019","2020","2021", "2022"));
    }

    @ModelAttribute("selectableGraphTypes")
    private List<String> selectableGraphs() {
        return new ArrayList<>(Arrays.asList("Device hours by month","Device occupancy rate in hours","Device occupancy rate in days"));
    }

    @ModelAttribute("selectableTimePeriods")
    private List<String> selectableTimePeriods() {
        return new ArrayList<>(Arrays.asList("Started","All","Future"));
    }

    @ModelAttribute("selectedStartMonthStockHistory")
    private String selectedStartMonthStockHistory(){
        //the calendarshould always start 6 month behind us as standard
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        cal.add(Calendar.MONTH, -5);
        sdf.format(cal.getTime());
        return sdf.format(cal.getTime());
    }

    @ModelAttribute("selectedMonthStock")
    private String selectedMonthStock(){
        return new SimpleDateFormat("yyyy-MM").format(new Date());
    }

    @ModelAttribute("selectedProducts")
    private List<OwnProduct> selectProd(){
        return new ArrayList<>(Arrays.asList(new OwnProduct(),new OwnProduct(),new OwnProduct(),new OwnProduct(),new OwnProduct()));
    }

    @ModelAttribute("stockLevelStartMonthHistory")
    private List<Double[]> stockLevelStartMonthHis(){
        return new ArrayList<Double[]>(Arrays.asList(
                new Double[]{0.0,0.0,0.0,0.0,0.0,0.0},
                new Double[]{0.0,0.0,0.0,0.0,0.0,0.0},
                new Double[]{0.0,0.0,0.0,0.0,0.0,0.0},
                new Double[]{0.0,0.0,0.0,0.0,0.0,0.0},
                new Double[]{0.0,0.0,0.0,0.0,0.0,0.0}));
    }

    float amountOfWorkDaysInYear = 200;
    float labOpeningTime = 8;
    float labClosingTime = 20;
    float labOpeningHoursInYear = amountOfWorkDaysInYear*(labClosingTime-labOpeningTime);


    /**
     *
     * Method that adds all needed model attributes for the device statistics to the modelmap.
     *
     * @param model ModelMap that holds all model attributes
     * @return path to html page for device statistics
     * @throws ParseException if Date object is badly parsed
     */
    @PreAuthorize("hasAnyAuthority('Statistics Access')")
    @RequestMapping(value = "/statistics/statistics", method = RequestMethod.GET)
    public String showStatisticsPage(final ModelMap model) throws ParseException {
        List<Device> listSelectedDevices = (List) model.getAttribute("selectedDevices");
        List<int[]> totalHours = (List) model.getAttribute("totalHours");
        List<Float> occupancyDevicesHours = (List) model.getAttribute("occupancyDevicesHours");
        List<Float> occupancyDevicesDays = (List) model.getAttribute("occupancyDevicesDays");

        List<Device> devices = deviceService.findAll();
        calculateDataGraphs(model);

        model.addAttribute("allDevices", devices);
        model.addAttribute("selectedDev",new Device());

        model.addAttribute("dev1",listSelectedDevices.get(0));
        model.addAttribute("dev2",listSelectedDevices.get(1));
        model.addAttribute("dev3",listSelectedDevices.get(2));
        model.addAttribute("dev4",listSelectedDevices.get(3));
        model.addAttribute("dev5",listSelectedDevices.get(4));
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

        //model.addAttribute("deviceCounter", deviceCounter);
        model.addAttribute("deviceCounter");
        model.addAttribute("selectableYears");
        model.addAttribute("selectedYear");
        model.addAttribute("selectedTimePeriod");

        model.addAttribute("selectableGraphTypes");
        model.addAttribute("selectedTypeOfGraph");
        model.addAttribute("selectableTimePeriods");

        return "Statistics/statistics";
    }

    /**
     *
     * Method add the device that the user selected on the webpage to the graph.
     * It checks if the device has already been added and amount of devices in the graph isn't exceeded
     *
     * @param model ModelMap that holds all the model attributes
     * @param selectedDev The device the user selected from the devicelist on the webpage to be added to the graph
     * @param redAttr Attributes that are passed to the redirected method
     * @return path to the redirection method showStatisticsPage()
     * @throws ParseException if date object is badly parsed
     */
    @PreAuthorize("hasAnyAuthority('Statistics Access')")
    @RequestMapping(value ="/statistics/statistics/submit")
    public String submit(final ModelMap model, Device selectedDev, RedirectAttributes redAttr) throws ParseException {

        List<Device> listSelectedDevices = (List) model.getAttribute("selectedDevices");
        boolean duplicate = false;
        for (int i = 0; i < listSelectedDevices.size(); i++) {
            Device dev = listSelectedDevices.get(i);
            if(dev.getDevicename().matches(selectedDev.getDevicename())){
                duplicate = true;
            }
        }
            if(!duplicate) {
                if ((int) model.getAttribute("deviceCounter") < 5) {
                    listSelectedDevices.set((int) model.getAttribute("deviceCounter"), selectedDev);
                    calculateDataGraphs(model);
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

    /**
     *
     * Method that clears all the submitted devices from the graphs. It also resets the y scale of the graph,
     * resets all data to 0 and sets the device counter to 0.
     *
     * @param model ModelMap that holds the model attributes
     * @return path to the redirected method, showStatisticsPage.
     */
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

    /**
     *
     * Method changes the year to be shown in the graph chosen by the user.
     *
     * @param model ModelMap that holds the model attributes
     * @param selectedYear The year the user selected on the webpage to be shown in the graph
     * @return path to the redirection method, showStatisticsPage().
     */
    @PreAuthorize("hasAnyAuthority('Statistics Access')")
    @RequestMapping("/statistics/statistics/getSelectedYear")
    public String getSelectedYear(final ModelMap model, String selectedYear){
        setSelectedYear(model, selectedYear);
        return "redirect:/statistics/statistics";
    }

    /**
     *
     * Method changes the wanted graph type chosen by the user.
     *
     * @param model ModelMap that holds the model attributes
     * @param selectedTypeOfGraph The wanted graph type the user selected on the webpage
     * @return path to the redirection method, showStatisticsPage().
     */
    @PreAuthorize("hasAnyAuthority('Statistics Access')")
    @RequestMapping("/statistics/statistics/getSelectedGraphType")
    public String getSelectedGraphType(final ModelMap model, String selectedTypeOfGraph){
        model.addAttribute("selectedTypeOfGraph", selectedTypeOfGraph);
        return "redirect:/statistics/statistics";
    }

    /**
     *
     * Method that sets the time period the user wants to be shown in the graph.
     * Passed, All or Future are the 3 choices.
     *
     * @param model ModelMap that holds the model attributes
     * @param selectedTimePeriod Users chosen time period
     * @return path the the redirection method, showStatisticsPage()
     */
    @PreAuthorize("hasAnyAuthority('Statistics Access')")
    @RequestMapping("/statistics/statistics/getSelectedTimePeriod")
    public String getSelectedTimePeriod(final ModelMap model, String selectedTimePeriod){
        model.addAttribute("selectedTimePeriod", selectedTimePeriod);
        return "redirect:/statistics/statistics";
    }

    /**
     *
     * method that add all correct attributes to the model map for the stock statistics
     *
     * @param model MolelMap that holds all the model attributes
     * @return path to the html page of the stock statistics
     */
    @PreAuthorize("hasAnyAuthority('Statistics Access')")
    @RequestMapping(value = "/statistics/stockStatistics", method = RequestMethod.GET)
    public String showStatisticsStockPage(final ModelMap model) throws ParseException{

        List<OwnProduct> listSelectedProducts = (List) model.getAttribute("selectedProducts");
        List<Double[]> stockLevelStartMonthHis = (List) model.getAttribute("stockLevelStartMonthHistory");
        List<OwnProduct> products = productService.findAll();
        List<Double> stockLevelMonth = new ArrayList<>();
        List<String> shownMonthsHistory = new ArrayList<>();
        List<String> productNames = new ArrayList<>();
        List<String> selectedProductNames = new ArrayList<>();
        model.addAttribute("selectedProd",new OwnProduct());


        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");

        //get all the product names
        for(OwnProduct product: products){
            productNames.add(product.getName());
        }

        for(OwnProduct product: products){
            String selectedMonth = (String) model.getAttribute("selectedMonthStock");
            Map<String, Double> stockHistoryMap = product.getProductStockHistory();
            stockLevelMonth.add(stockHistoryMap.get(selectedMonth));
        }

        String selectedStartMonth = (String) model.getAttribute("selectedStartMonthStockHistory");
        cal.setTime(sdf.parse(selectedStartMonth));
        //Get selected months strings for x-axis
        String shownMonth = sdf.format(cal.getTime());
        shownMonthsHistory.add(shownMonth);
        for(int j=0; j<5;j++){
            cal.add(Calendar.MONTH, 1);
            shownMonth = sdf.format(cal.getTime());
            shownMonthsHistory.add(shownMonth);
        }
        //Calculate data points for graph
        if((int)model.getAttribute("productCounter")>0) {
            //for (OwnProduct product : listSelectedProducts) {
            for(int z=0;z<(int)model.getAttribute("productCounter");z++){
                OwnProduct product = listSelectedProducts.get(z);
                selectedProductNames.add(product.getName());
                cal.setTime(sdf.parse(selectedStartMonth));
                String month = sdf.format(cal.getTime());
                Double[] data = stockLevelStartMonthHis.get(z);

                Map<String, Double> stockHistoryMap = product.getProductStockHistory();
                data[0] = stockHistoryMap.get(selectedStartMonth);

                for (int i = 1; i < 6; i++) {
                    cal.setTime(sdf.parse(month));
                    cal.add(Calendar.MONTH, 1);
                    month = sdf.format(cal.getTime());
                    data[i] = stockHistoryMap.get(month);
                }
            }
        }

        //calculate highest stock level for y-axis of current stock graph
        double highestDataPointStock = 100.0;
        for(int j=0; j<stockLevelMonth.size();j++){
            Double data = stockLevelMonth.get(j);
            if (data >= highestDataPointStock) {
                highestDataPointStock = data;
            }
        }

        //get all the stock levels
        model.addAttribute("products",products);
        model.addAttribute("selectedProducts",listSelectedProducts);
        model.addAttribute("productNames",productNames);
        model.addAttribute("selectedProductNames", selectedProductNames);
        model.addAttribute("productCounter");
        model.addAttribute("highestDataPointStock",highestDataPointStock);
        model.addAttribute("shownMonthsHistory",shownMonthsHistory);
        model.addAttribute("stockLevelMonth",stockLevelMonth);
        model.addAttribute("maxDateSelect",new SimpleDateFormat("yyyy-MM").format(new Date()));

        return "Statistics/stockStatistics";
    }

    @PreAuthorize("hasAnyAuthority('Statistics Access')")
    @RequestMapping(value = "/statistics/stockStatistics/resetGraphStockHistory")
    public String resetGraphStockHistory(final ModelMap model){
        model.addAttribute("productCounter",0);
        model.addAttribute("selectedProducts", new ArrayList<>(Arrays.asList(new OwnProduct(),new OwnProduct(),new OwnProduct(),new OwnProduct(),new OwnProduct())));
        return "redirect:/statistics/stockStatistics";
    }

    @PreAuthorize("hasAnyAuthority('Statistics Access')")
    @RequestMapping(value = "/statistics/stockStatistics/getSelectedStartStockHistory")
    public String getSelectedStartStockHistory(final ModelMap model, String selectedStartMonthStockHistory){
        model.addAttribute("selectedStartMonthStockHistory", selectedStartMonthStockHistory);
        return "redirect:/statistics/stockStatistics";
    }

    @PreAuthorize("hasAnyAuthority('Statistics Access')")
    @RequestMapping(value = "/statistics/stockStatistics/getSelectedMonthStock")
    public String getSelectedMonthStock(final ModelMap model, String selectedMonthStock){
        model.addAttribute("selectedMonthStock", selectedMonthStock);
        return "redirect:/statistics/stockStatistics";
    }

    @PreAuthorize("hasAnyAuthority('Statistics Access')")
    @RequestMapping(value = "/statistics/stockStatistics/getSelectedProduct")
    public String getSelectedProduct(final ModelMap model, OwnProduct selectedProd, RedirectAttributes redAttr){

        List<OwnProduct> listSelectedProducts = (List) model.getAttribute("selectedProducts");
        List<OwnProduct> allProducts = productService.findAll();

        boolean duplicate = false;
        for (int i = 0; i < listSelectedProducts.size(); i++) {
            OwnProduct prod = listSelectedProducts.get(i);
            if(prod.getName().matches(selectedProd.getName())){
                duplicate = true;
            }
        }
        if(!duplicate) {
            if ((int) model.getAttribute("productCounter") < 5) {
                for(OwnProduct prod : allProducts){
                    if(selectedProd.getName().equals(prod.getName())){
                        listSelectedProducts.set((int) model.getAttribute("productCounter"), prod);
                    }
                }

                int dc = (int) model.get("productCounter") + 1;
                model.addAttribute("productCounter", dc);
            } else {
                redAttr.addFlashAttribute("ProdStatus", "productLimit");
            }
        }
        else{
            redAttr.addFlashAttribute("ProdStatus", "productDuplicate");
        }
        model.addAttribute("selectedProducts",listSelectedProducts);
        return "redirect:/statistics/stockStatistics";
    }

    /**
     *
     * Method that calculates of data depending on the model attributes the user selected like year, graph type
     * and time period.
     *
     * @param model ModelMap that holds all the model attributes
     * @throws ParseException if the date object is badly parsed
     */
    public void calculateDataGraphs(final ModelMap model) throws ParseException{

        List<Device> listSelectedDevices = (List) model.getAttribute("selectedDevices");
        List<int[]> totalHours = (List) model.getAttribute("totalHours");
        List<Float> occupancyDevicesHours = (List) model.getAttribute("occupancyDevicesHours");
        List<Float> occupancyDevicesDays = (List) model.getAttribute("occupancyDevicesDays");
        List<Step> allSteps = stepService.findAll();
        int[] totalHoursSelectedDevice;
        float occupancyHours;
        float occupancyDays;
        float totalDeviceHoursYear=0;
        float totalDeviceDaysYear=0;

        model.addAttribute("highestAbsoluteValueHours", 10);
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
    }

    /**
     *
     * Method that calculates the hours of difference of a step based on the start and end date
     *
     * @param step a reserved step
     * @return the duration of the step
     */
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

    /**
     *
     * Method that iterates over all existing steps to filter out all steps specific to a device the user selected.
     *
     * @param selectedDev device selected when submitting on the httml page
     * @param allSteps Allsteps that are booked. Single steps as well as experiment steps
     * @return a list of steps that correspond to the selected device.
     */
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

    /**
     *
     * Method calculates hours a device is used by month for a specific year chosen by the user.
     * Data also depends on the time period the user selected.
     * Takes into account leap year, odd and even months, februari.
     *
     * @param model ModelMap that holds the model attributes
     * @param selectedDeviceSteps The steps of the selected device the user submitted
     * @return an array with a corresponding amount of hours for each month in a year
     * @throws ParseException if date object is badly parsed
     */
    public int[] calculateTotalHoursDeviceByYearAndMonth(final ModelMap model, List<Step> selectedDeviceSteps) throws ParseException {
        int[] totalHoursByMonth = new int[12];
        String[] months = new String[]{"01","02","03","04","05","06","07","08","09","10","11","12"};
        String selectedTimePeriod = (String) model.getAttribute("selectedTimePeriod");
        SimpleDateFormat formatDateHourMin = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date todaysDate = new Date();
        for(int j=0;j<selectedDeviceSteps.size();j++) {
            Step devStep = selectedDeviceSteps.get(j);
            String startMonth = getStepMonthStart(devStep);
            String endMonth = getStepMonthEnd(devStep);
            String startDay = getStepDayStart(devStep);
            String endDay = getStepDayEnd(devStep);
            String startHour = getStepHourStart(devStep);
            String endHour = getStepHourEnd(devStep);
            String yearStep = getStepYearStart(devStep);

            Date thisStepDateStart = formatDateHourMin.parse(devStep.getStart() + " " + devStep.getStartHour());
            Date thisStepDateEnd = formatDateHourMin.parse(devStep.getEnd() + " " + devStep.getEndHour());

            if(selectedTimePeriod.matches("Started")) {
                if (thisStepDateStart.before(todaysDate)) {
                    for (int i = 0; i < months.length; i++) {
                        if (yearStep.matches((String) model.getAttribute("selectedYear"))) {
                            //calculate for month i if same month
                            if ((startMonth.matches(months[i])) && (startMonth.matches(endMonth) == true)) {
                                if (startDay.matches(endDay) == true) {
                                    //Problem: this code to calculate days only take into account days longer than 24h
                                    //long diff = thisStepDateEnd.getDate() - thisStepDateStart.getDate();
                                    //System.out.println ("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.HOURS));
                                    totalHoursByMonth[i] = totalHoursByMonth[i] + calculateHourDiff(selectedDeviceSteps.get(j));
                                } else if (startDay.matches(endDay) == false) {
                                    int dayDiff = ((Integer.parseInt(endDay) - Integer.parseInt(startDay))) - 1;
                                    totalHoursByMonth[i] = (int) (totalHoursByMonth[i] + (labClosingTime - Integer.parseInt(startHour)) + (dayDiff * (labClosingTime - labOpeningTime)) + (Integer.parseInt(endHour) - labOpeningTime));
                                }
                            }
                            //not same month same year
                            if ((startMonth.matches(months[i])) && (startMonth.matches(endMonth) == false)) {
                                float monthsDifference = (Integer.parseInt(endMonth)) - (Integer.parseInt(startMonth));
                                if (monthsDifference == 1) {
                                    // If february -> march
                                    if (startMonth.matches("02")) {
                                        //check leap year
                                        int startYearInt = Integer.parseInt(yearStep);
                                        boolean leap = checkLeapYear(startYearInt);
                                        //if leap +29
                                        if (leap) {
                                            int fullDaysThisMonth = 29 - Integer.parseInt(startDay);
                                            int fullDaysNextMonth = Integer.parseInt(endDay) - 1;
                                            totalHoursByMonth[i] = (int) (totalHoursByMonth[i] + (labClosingTime - Integer.parseInt(startHour)) + (fullDaysThisMonth * (labClosingTime - labOpeningTime)));
                                            totalHoursByMonth[i + 1] = (int) (totalHoursByMonth[i + 1] + (fullDaysNextMonth * (labClosingTime - labOpeningTime)) + (Integer.parseInt(endHour) - labOpeningTime));

                                        }
                                        //if not leap +28
                                        else {
                                            int fullDaysThisMonth = 28 - Integer.parseInt(startDay);
                                            int fullDaysNextMonth = Integer.parseInt(endDay) - 1;
                                            totalHoursByMonth[i] = (int) (totalHoursByMonth[i] + (labClosingTime - Integer.parseInt(startHour)) + (fullDaysThisMonth * (labClosingTime - labOpeningTime)));
                                            totalHoursByMonth[i + 1] = (int) (totalHoursByMonth[i + 1] + (fullDaysNextMonth * (labClosingTime - labOpeningTime)) + (Integer.parseInt(endHour) - labOpeningTime));

                                        }
                                    }
                                    //if even month -> odd month => +30
                                    else if (Integer.parseInt(startMonth) % 2 == 0) {
                                        int fullDaysThisMonth = 30 - Integer.parseInt(startDay);
                                        int fullDaysNextMonth = Integer.parseInt(endDay) - 1;
                                        totalHoursByMonth[i] = (int) (totalHoursByMonth[i] + (labClosingTime - Integer.parseInt(startHour)) + (fullDaysThisMonth * (labClosingTime - labOpeningTime)));
                                        totalHoursByMonth[i + 1] = (int) (totalHoursByMonth[i + 1] + (fullDaysNextMonth * (labClosingTime - labOpeningTime)) + (Integer.parseInt(endHour) - labOpeningTime));
                                    }
                                    // if odd month -> even month => +31
                                    else {
                                        int fullDaysThisMonth = 31 - Integer.parseInt(startDay);
                                        int fullDaysNextMonth = Integer.parseInt(endDay) - 1;
                                        totalHoursByMonth[i] = (int) (totalHoursByMonth[i] + (labClosingTime - Integer.parseInt(startHour)) + (fullDaysThisMonth * (labClosingTime - labOpeningTime)));
                                        totalHoursByMonth[i + 1] = (int) (totalHoursByMonth[i + 1] + (fullDaysNextMonth * (labClosingTime - labOpeningTime)) + (Integer.parseInt(endHour) - labOpeningTime));
                                    }
                                }
                                //If more than one month we take 30.4375 as average and don't take into account februari or leap years, to reduce complexity
                                else {
                                    int fullDaysThisMonth;
                                    if (Integer.parseInt(startMonth) % 2 == 0) {
                                        fullDaysThisMonth = 30 - Integer.parseInt(startDay);
                                    } else {
                                        fullDaysThisMonth = 31 - Integer.parseInt(startDay);
                                    }
                                    int fullDaysLastMonth = Integer.parseInt(endDay) - 1;
                                    float fullDaysMonthsInbetween = (float) 30.4375;
                                    totalHoursByMonth[i] = (int) (totalHoursByMonth[i] + (labClosingTime - Integer.parseInt(startHour)) + (fullDaysThisMonth * (labClosingTime - labOpeningTime)));
                                    totalHoursByMonth[i + (int) monthsDifference] = (int) (totalHoursByMonth[i + (int) monthsDifference] + (fullDaysLastMonth * (labClosingTime - labOpeningTime)) + (Integer.parseInt(endHour) - labOpeningTime));
                                    // Calculate months in between
                                    for (int z = 1; z < monthsDifference; z++) {
                                        totalHoursByMonth[i + z] = (int) (totalHoursByMonth[i + z] + (fullDaysMonthsInbetween * (labClosingTime - labOpeningTime)));
                                    }
                                }
                            }
                        }
                    }
                }
            }
            else if (selectedTimePeriod.matches("All")) {
                for (int i = 0; i < months.length; i++) {
                    if (yearStep.matches((String) model.getAttribute("selectedYear"))) {
                        //calculate for month i if same month
                        if ((startMonth.matches(months[i])) && (startMonth.matches(endMonth) == true)) {
                            if (startDay.matches(endDay) == true) {
                                //long diff = thisStepDateEnd.getDate() - thisStepDateStart.getDate();
                                //System.out.println ("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.HOURS));
                                totalHoursByMonth[i] = totalHoursByMonth[i] + calculateHourDiff(selectedDeviceSteps.get(j));
                            } else if (startDay.matches(endDay) == false) {
                                int dayDiff = ((Integer.parseInt(endDay) - Integer.parseInt(startDay))) - 1;
                                totalHoursByMonth[i] = (int) (totalHoursByMonth[i] + (labClosingTime - Integer.parseInt(startHour)) + (dayDiff * (labClosingTime - labOpeningTime)) + (Integer.parseInt(endHour) - labOpeningTime));
                            }
                        }
                        //not same month same year
                        if ((startMonth.matches(months[i])) && (startMonth.matches(endMonth) == false)) {
                            float monthsDifference = (Integer.parseInt(endMonth)) - (Integer.parseInt(startMonth));
                            if (monthsDifference == 1) {
                                // If february -> march
                                if (startMonth.matches("02")) {
                                    //check leap year
                                    int startYearInt = Integer.parseInt(yearStep);
                                    boolean leap = checkLeapYear(startYearInt);
                                    //if leap +29
                                    if (leap) {
                                        int fullDaysThisMonth = 29 - Integer.parseInt(startDay);
                                        int fullDaysNextMonth = Integer.parseInt(endDay) - 1;
                                        totalHoursByMonth[i] = (int) (totalHoursByMonth[i] + (labClosingTime - Integer.parseInt(startHour)) + (fullDaysThisMonth * (labClosingTime - labOpeningTime)));
                                        totalHoursByMonth[i + 1] = (int) (totalHoursByMonth[i + 1] + (fullDaysNextMonth * (labClosingTime - labOpeningTime)) + (Integer.parseInt(endHour) - labOpeningTime));

                                    }
                                    //if not leap +28
                                    else {
                                        int fullDaysThisMonth = 28 - Integer.parseInt(startDay);
                                        int fullDaysNextMonth = Integer.parseInt(endDay) - 1;
                                        totalHoursByMonth[i] = (int) (totalHoursByMonth[i] + (labClosingTime - Integer.parseInt(startHour)) + (fullDaysThisMonth * (labClosingTime - labOpeningTime)));
                                        totalHoursByMonth[i + 1] = (int) (totalHoursByMonth[i + 1] + (fullDaysNextMonth * (labClosingTime - labOpeningTime)) + (Integer.parseInt(endHour) - labOpeningTime));

                                    }
                                }
                                //if even month -> odd month => +30
                                else if (Integer.parseInt(startMonth) % 2 == 0) {
                                    int fullDaysThisMonth = 30 - Integer.parseInt(startDay);
                                    int fullDaysNextMonth = Integer.parseInt(endDay) - 1;
                                    totalHoursByMonth[i] = (int) (totalHoursByMonth[i] + (labClosingTime - Integer.parseInt(startHour)) + (fullDaysThisMonth * (labClosingTime - labOpeningTime)));
                                    totalHoursByMonth[i + 1] = (int) (totalHoursByMonth[i + 1] + (fullDaysNextMonth * (labClosingTime - labOpeningTime)) + (Integer.parseInt(endHour) - labOpeningTime));
                                }
                                // if odd month -> even month => +31
                                else {
                                    int fullDaysThisMonth = 31 - Integer.parseInt(startDay);
                                    int fullDaysNextMonth = Integer.parseInt(endDay) - 1;
                                    totalHoursByMonth[i] = (int) (totalHoursByMonth[i] + (labClosingTime - Integer.parseInt(startHour)) + (fullDaysThisMonth * (labClosingTime - labOpeningTime)));
                                    totalHoursByMonth[i + 1] = (int) (totalHoursByMonth[i + 1] + (fullDaysNextMonth * (labClosingTime - labOpeningTime)) + (Integer.parseInt(endHour) - labOpeningTime));
                                }
                            }
                            //If more than one month we take 30.4375 as average and don't take into account februari or leap years, to reduce complexity....
                            else {
                                int fullDaysThisMonth;
                                if (Integer.parseInt(startMonth) % 2 == 0) {
                                    fullDaysThisMonth = 30 - Integer.parseInt(startDay);
                                } else {
                                    fullDaysThisMonth = 31 - Integer.parseInt(startDay);
                                }
                                int fullDaysLastMonth = Integer.parseInt(endDay) - 1;
                                float fullDaysMonthsInbetween = (float) 30.4375;
                                totalHoursByMonth[i] = (int) (totalHoursByMonth[i] + (labClosingTime - Integer.parseInt(startHour)) + (fullDaysThisMonth * (labClosingTime - labOpeningTime)));
                                totalHoursByMonth[i + (int) monthsDifference] = (int) (totalHoursByMonth[i + (int) monthsDifference] + (fullDaysLastMonth * (labClosingTime - labOpeningTime)) + (Integer.parseInt(endHour) - labOpeningTime));
                                // Calculate months in between
                                for (int z = 1; z < monthsDifference; z++) {
                                    totalHoursByMonth[i + z] = (int) (totalHoursByMonth[i + z] + (fullDaysMonthsInbetween * (labClosingTime - labOpeningTime)));
                                }
                            }
                        }
                    }
                }
            }
            else if (selectedTimePeriod.matches("Future")){
                if (thisStepDateStart.after(todaysDate)) {
                    for (int i = 0; i < months.length; i++) {
                        if (yearStep.matches((String) model.getAttribute("selectedYear"))) {
                            //calculate for month i if same month
                            if ((startMonth.matches(months[i])) && (startMonth.matches(endMonth) == true)) {
                                if (startDay.matches(endDay) == true) {
                                    //long diff = thisStepDateEnd.getDate() - thisStepDateStart.getDate();
                                    //System.out.println ("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.HOURS));
                                    totalHoursByMonth[i] = totalHoursByMonth[i] + calculateHourDiff(selectedDeviceSteps.get(j));
                                } else if (startDay.matches(endDay) == false) {
                                    int dayDiff = ((Integer.parseInt(endDay) - Integer.parseInt(startDay))) - 1;
                                    totalHoursByMonth[i] = (int) (totalHoursByMonth[i] + (labClosingTime - Integer.parseInt(startHour)) + (dayDiff * (labClosingTime - labOpeningTime)) + (Integer.parseInt(endHour) - labOpeningTime));
                                }
                            }
                            //not same month same year
                            if ((startMonth.matches(months[i])) && (startMonth.matches(endMonth) == false)) {
                                float monthsDifference = (Integer.parseInt(endMonth)) - (Integer.parseInt(startMonth));
                                if (monthsDifference == 1) {
                                    // If february -> march
                                    if (startMonth.matches("02")) {
                                        //check leap year
                                        int startYearInt = Integer.parseInt(yearStep);
                                        boolean leap = checkLeapYear(startYearInt);
                                        //if leap +29
                                        if (leap) {
                                            int fullDaysThisMonth = 29 - Integer.parseInt(startDay);
                                            int fullDaysNextMonth = Integer.parseInt(endDay) - 1;
                                            totalHoursByMonth[i] = (int) (totalHoursByMonth[i] + (labClosingTime - Integer.parseInt(startHour)) + (fullDaysThisMonth * (labClosingTime - labOpeningTime)));
                                            totalHoursByMonth[i + 1] = (int) (totalHoursByMonth[i + 1] + (fullDaysNextMonth * (labClosingTime - labOpeningTime)) + (Integer.parseInt(endHour) - labOpeningTime));

                                        }
                                        //if not leap +28
                                        else {
                                            int fullDaysThisMonth = 28 - Integer.parseInt(startDay);
                                            int fullDaysNextMonth = Integer.parseInt(endDay) - 1;
                                            totalHoursByMonth[i] = (int) (totalHoursByMonth[i] + (labClosingTime - Integer.parseInt(startHour)) + (fullDaysThisMonth * (labClosingTime - labOpeningTime)));
                                            totalHoursByMonth[i + 1] = (int) (totalHoursByMonth[i + 1] + (fullDaysNextMonth * (labClosingTime - labOpeningTime)) + (Integer.parseInt(endHour) - labOpeningTime));

                                        }
                                    }
                                    //if even month -> odd month => +30
                                    else if (Integer.parseInt(startMonth) % 2 == 0) {
                                        int fullDaysThisMonth = 30 - Integer.parseInt(startDay);
                                        int fullDaysNextMonth = Integer.parseInt(endDay) - 1;
                                        totalHoursByMonth[i] = (int) (totalHoursByMonth[i] + (labClosingTime - Integer.parseInt(startHour)) + (fullDaysThisMonth * (labClosingTime - labOpeningTime)));
                                        totalHoursByMonth[i + 1] = (int) (totalHoursByMonth[i + 1] + (fullDaysNextMonth * (labClosingTime - labOpeningTime)) + (Integer.parseInt(endHour) - labOpeningTime));
                                    }
                                    // if odd month -> even month => +31
                                    else {
                                        int fullDaysThisMonth = 31 - Integer.parseInt(startDay);
                                        int fullDaysNextMonth = Integer.parseInt(endDay) - 1;
                                        totalHoursByMonth[i] = (int) (totalHoursByMonth[i] + (labClosingTime - Integer.parseInt(startHour)) + (fullDaysThisMonth * (labClosingTime - labOpeningTime)));
                                        totalHoursByMonth[i + 1] = (int) (totalHoursByMonth[i + 1] + (fullDaysNextMonth * (labClosingTime - labOpeningTime)) + (Integer.parseInt(endHour) - labOpeningTime));
                                    }
                                }
                                //If more than one month we take 30.4375 as average and don't take into account februari or leap years, to reduce complexity....
                                else {
                                    int fullDaysThisMonth;
                                    if (Integer.parseInt(startMonth) % 2 == 0) {
                                        fullDaysThisMonth = 30 - Integer.parseInt(startDay);
                                    } else {
                                        fullDaysThisMonth = 31 - Integer.parseInt(startDay);
                                    }
                                    int fullDaysLastMonth = Integer.parseInt(endDay) - 1;
                                    float fullDaysMonthsInbetween = (float) 30.4375;
                                    totalHoursByMonth[i] = (int) (totalHoursByMonth[i] + (labClosingTime - Integer.parseInt(startHour)) + (fullDaysThisMonth * (labClosingTime - labOpeningTime)));
                                    totalHoursByMonth[i + (int) monthsDifference] = (int) (totalHoursByMonth[i + (int) monthsDifference] + (fullDaysLastMonth * (labClosingTime - labOpeningTime)) + (Integer.parseInt(endHour) - labOpeningTime));
                                    // Calculate months in between
                                    for (int z = 1; z < monthsDifference; z++) {
                                        totalHoursByMonth[i + z] = (int) (totalHoursByMonth[i + z] + (fullDaysMonthsInbetween * (labClosingTime - labOpeningTime)));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return totalHoursByMonth;
    }

    /**
     *
     * Method calculates the occupancy rate in hours of the selected devices for a specific year and time period.
     * The occupancy rate in hours are the amount of hours a device was booked compared to the amount of hours the lab
     * was able to be booked.
     *
     * @param model ModelMap that holds the model attributes
     * @param selectedDeviceSteps List of steps of one of the devices that are selected for the graph
     * @param totalDeviceHoursYear Total hours the device has already been booked, initialised at 0.
     * @return the occupancy rate of a device for a specific year and time period
     * @throws ParseException if date object is badly parsed
     */
    public float calculateOccupancyHours(final ModelMap model, List<Step> selectedDeviceSteps, float totalDeviceHoursYear) throws ParseException {
        float occupancySelectedYearHours=0;
        String selectedTimePeriod = (String) model.getAttribute("selectedTimePeriod");
        SimpleDateFormat formatDateHourMin = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date todaysDate = new Date();

        for(int j=0;j<selectedDeviceSteps.size();j++){
            Step devStep = selectedDeviceSteps.get(j);
            String yearStep = getStepYearStart(selectedDeviceSteps.get(j));

            Date thisStepDateStart = formatDateHourMin.parse(devStep.getStart() + " " + devStep.getStartHour());

            if(selectedTimePeriod.matches("Started")) {
                if(thisStepDateStart.before(todaysDate)) {
                    if (yearStep.matches((String) model.getAttribute("selectedYear"))) {
                        String startDay = getStepDayStart(devStep);
                        String endDay = getStepDayEnd(devStep);
                        String startMonth = getStepMonthStart(devStep);
                        String endMonth = getStepMonthEnd(devStep);
                        String startHour = getStepHourStart(devStep);
                        String endHour = getStepHourEnd(devStep);
                        String startYear = getStepYearStart(devStep);

                        // if step only takes one day
                        if ((startDay.matches(endDay) == true) && (startMonth.matches(endMonth) == true)) {
                            totalDeviceHoursYear = totalDeviceHoursYear + calculateHourDiff(selectedDeviceSteps.get(j));
                        }
                        // if step is accros multiple days in same month
                        else if ((startDay.matches(endDay) == false) && (startMonth.matches(endMonth) == true)) {
                            int dayDiff = ((Integer.parseInt(endDay) - Integer.parseInt(startDay))) - 1;
                            totalDeviceHoursYear = totalDeviceHoursYear + (labClosingTime - Integer.parseInt(startHour)) + (dayDiff * (labClosingTime - labOpeningTime)) + (Integer.parseInt(endHour) - labOpeningTime);
                        }
                        //if step over multiple months in same year
                        else if (startMonth.matches(endMonth) == false) {
                            float monthsDifference = (Integer.parseInt(endMonth)) - (Integer.parseInt(startMonth));
                            if (monthsDifference == 1) {
                                // If february -> march
                                if (startMonth.matches("02")) {
                                    //check leap year
                                    int startYearInt = Integer.parseInt(startYear);
                                    boolean leap = checkLeapYear(startYearInt);
                                    //if leap +29
                                    if (leap) {
                                        float dayDiff = ((Integer.parseInt(endDay) + 29) - Integer.parseInt(startDay)) - 1;
                                        totalDeviceHoursYear = totalDeviceHoursYear + (labClosingTime - Integer.parseInt(startHour)) + (dayDiff * (labClosingTime - labOpeningTime)) + (Integer.parseInt(endHour) - labOpeningTime);
                                    }
                                    //if not leap +28
                                    else {
                                        float dayDiff = ((Integer.parseInt(endDay) + 28) - Integer.parseInt(startDay)) - 1;
                                        totalDeviceHoursYear = totalDeviceHoursYear + (labClosingTime - Integer.parseInt(startHour)) + (dayDiff * (labClosingTime - labOpeningTime)) + (Integer.parseInt(endHour) - labOpeningTime);
                                    }
                                }
                                //if even month -> odd month => +30
                                else if (Integer.parseInt(startMonth) % 2 == 0) {
                                    float dayDiff = ((Integer.parseInt(endDay) + 30) - Integer.parseInt(startDay)) - 1;
                                    totalDeviceHoursYear = totalDeviceHoursYear + (labClosingTime - Integer.parseInt(startHour)) + (dayDiff * (labClosingTime - labOpeningTime)) + (Integer.parseInt(endHour) - labOpeningTime);
                                }
                                // if odd month -> even month => +31
                                else {
                                    float dayDiff = ((Integer.parseInt(endDay) + 31) - Integer.parseInt(startDay)) - 1;
                                    totalDeviceHoursYear = totalDeviceHoursYear + (labClosingTime - Integer.parseInt(startHour)) + (dayDiff * (labClosingTime - labOpeningTime)) + (Integer.parseInt(endHour) - labOpeningTime);
                                }
                            }
                            //If more than one month we take 30.4375 as average and don't take into account februari or leap years, to reduce complexity....
                            else {
                                float extraDaysMonths = monthsDifference * ((float) (30.4375));
                                float dayDiff = ((Integer.parseInt(endDay) + extraDaysMonths) - Integer.parseInt(startDay)) - 1;
                                totalDeviceHoursYear = totalDeviceHoursYear + (labClosingTime - Integer.parseInt(startHour)) + (dayDiff * (labClosingTime - labOpeningTime)) + (Integer.parseInt(endHour) - labOpeningTime);
                            }
                        }
                    }
                }
            }
            if(selectedTimePeriod.matches("All")) {
                if (yearStep.matches((String) model.getAttribute("selectedYear"))) {
                    String startDay = getStepDayStart(devStep);
                    String endDay = getStepDayEnd(devStep);
                    String startMonth = getStepMonthStart(devStep);
                    String endMonth = getStepMonthEnd(devStep);
                    String startHour = getStepHourStart(devStep);
                    String endHour = getStepHourEnd(devStep);
                    String startYear = getStepYearStart(devStep);

                    // if step only takes one day
                    if ((startDay.matches(endDay) == true) && (startMonth.matches(endMonth) == true)) {
                        totalDeviceHoursYear = totalDeviceHoursYear + calculateHourDiff(selectedDeviceSteps.get(j));
                    }
                    // if step is accros multiple days in same month
                    else if ((startDay.matches(endDay) == false) && (startMonth.matches(endMonth) == true)) {
                        int dayDiff = ((Integer.parseInt(endDay) - Integer.parseInt(startDay))) - 1;
                        totalDeviceHoursYear = totalDeviceHoursYear + (labClosingTime - Integer.parseInt(startHour)) + (dayDiff * (labClosingTime - labOpeningTime)) + (Integer.parseInt(endHour) - labOpeningTime);
                    }
                    //if step over multiple months in same year
                    else if (startMonth.matches(endMonth) == false) {
                        float monthsDifference = (Integer.parseInt(endMonth)) - (Integer.parseInt(startMonth));
                        if (monthsDifference == 1) {
                            // If february -> march
                            if (startMonth.matches("02")) {
                                //check leap year
                                int startYearInt = Integer.parseInt(startYear);
                                boolean leap = checkLeapYear(startYearInt);
                                //if leap +29
                                if (leap) {
                                    float dayDiff = ((Integer.parseInt(endDay) + 29) - Integer.parseInt(startDay)) - 1;
                                    totalDeviceHoursYear = totalDeviceHoursYear + (labClosingTime - Integer.parseInt(startHour)) + (dayDiff * (labClosingTime - labOpeningTime)) + (Integer.parseInt(endHour) - labOpeningTime);
                                }
                                //if not leap +28
                                else {
                                    float dayDiff = ((Integer.parseInt(endDay) + 28) - Integer.parseInt(startDay)) - 1;
                                    totalDeviceHoursYear = totalDeviceHoursYear + (labClosingTime - Integer.parseInt(startHour)) + (dayDiff * (labClosingTime - labOpeningTime)) + (Integer.parseInt(endHour) - labOpeningTime);
                                }
                            }
                            //if even month -> odd month => +30
                            else if (Integer.parseInt(startMonth) % 2 == 0) {
                                float dayDiff = ((Integer.parseInt(endDay) + 30) - Integer.parseInt(startDay)) - 1;
                                totalDeviceHoursYear = totalDeviceHoursYear + (labClosingTime - Integer.parseInt(startHour)) + (dayDiff * (labClosingTime - labOpeningTime)) + (Integer.parseInt(endHour) - labOpeningTime);
                            }
                            // if odd month -> even month => +31
                            else {
                                float dayDiff = ((Integer.parseInt(endDay) + 31) - Integer.parseInt(startDay)) - 1;
                                totalDeviceHoursYear = totalDeviceHoursYear + (labClosingTime - Integer.parseInt(startHour)) + (dayDiff * (labClosingTime - labOpeningTime)) + (Integer.parseInt(endHour) - labOpeningTime);
                            }
                        }
                            //If more than one month we take 30.4375 as average and don't take into account februari or leap years, to reduce complexity....
                            else {
                                float extraDaysMonths = monthsDifference * ((float) (30.4375));
                                float dayDiff = ((Integer.parseInt(endDay) + extraDaysMonths) - Integer.parseInt(startDay)) - 1;
                                totalDeviceHoursYear = totalDeviceHoursYear + (labClosingTime - Integer.parseInt(startHour)) + (dayDiff * (labClosingTime - labOpeningTime)) + (Integer.parseInt(endHour) - labOpeningTime);
                            }
                    }
                }
            }
            if(selectedTimePeriod.matches("Future")) {
                if(thisStepDateStart.after(todaysDate)) {
                    if (yearStep.matches((String) model.getAttribute("selectedYear"))) {
                        String startDay = getStepDayStart(devStep);
                        String endDay = getStepDayEnd(devStep);
                        String startMonth = getStepMonthStart(devStep);
                        String endMonth = getStepMonthEnd(devStep);
                        String startHour = getStepHourStart(devStep);
                        String endHour = getStepHourEnd(devStep);
                        String startYear = getStepYearStart(devStep);

                        // if step only takes one day
                        if ((startDay.matches(endDay) == true) && (startMonth.matches(endMonth) == true)) {
                            totalDeviceHoursYear = totalDeviceHoursYear + calculateHourDiff(selectedDeviceSteps.get(j));
                        }
                        // if step is accros multiple days in same month
                        else if ((startDay.matches(endDay) == false) && (startMonth.matches(endMonth) == true)) {
                            int dayDiff = ((Integer.parseInt(endDay) - Integer.parseInt(startDay))) - 1;
                            totalDeviceHoursYear = totalDeviceHoursYear + (labClosingTime - Integer.parseInt(startHour)) + (dayDiff * (labClosingTime - labOpeningTime)) + (Integer.parseInt(endHour) - labOpeningTime);
                        }
                        //if step over multiple months in same year
                        else if (startMonth.matches(endMonth) == false) {
                            float monthsDifference = (Integer.parseInt(endMonth)) - (Integer.parseInt(startMonth));
                            if (monthsDifference == 1) {
                                // If february -> march
                                if (startMonth.matches("02")) {
                                    //check leap year
                                    int startYearInt = Integer.parseInt(startYear);
                                    boolean leap = checkLeapYear(startYearInt);
                                    //if leap +29
                                    if (leap) {
                                        float dayDiff = ((Integer.parseInt(endDay) + 29) - Integer.parseInt(startDay)) - 1;
                                        totalDeviceHoursYear = totalDeviceHoursYear + (labClosingTime - Integer.parseInt(startHour)) + (dayDiff * (labClosingTime - labOpeningTime)) + (Integer.parseInt(endHour) - labOpeningTime);
                                    }
                                    //if not leap +28
                                    else {
                                        float dayDiff = ((Integer.parseInt(endDay) + 28) - Integer.parseInt(startDay)) - 1;
                                        totalDeviceHoursYear = totalDeviceHoursYear + (labClosingTime - Integer.parseInt(startHour)) + (dayDiff * (labClosingTime - labOpeningTime)) + (Integer.parseInt(endHour) - labOpeningTime);
                                    }
                                }
                                //if even month -> odd month => +30
                                else if (Integer.parseInt(startMonth) % 2 == 0) {
                                    float dayDiff = ((Integer.parseInt(endDay) + 30) - Integer.parseInt(startDay)) - 1;
                                    totalDeviceHoursYear = totalDeviceHoursYear + (labClosingTime - Integer.parseInt(startHour)) + (dayDiff * (labClosingTime - labOpeningTime)) + (Integer.parseInt(endHour) - labOpeningTime);
                                }
                                // if odd month -> even month => +31
                                else {
                                    float dayDiff = ((Integer.parseInt(endDay) + 31) - Integer.parseInt(startDay)) - 1;
                                    totalDeviceHoursYear = totalDeviceHoursYear + (labClosingTime - Integer.parseInt(startHour)) + (dayDiff * (labClosingTime - labOpeningTime)) + (Integer.parseInt(endHour) - labOpeningTime);
                                }
                            }
                            //If more than one month we take 30.4375 as average and don't take into account februari or leap years, to reduce complexity....
                            else {
                                float extraDaysMonths = monthsDifference * ((float) (30.4375));
                                float dayDiff = ((Integer.parseInt(endDay) + extraDaysMonths) - Integer.parseInt(startDay)) - 1;
                                totalDeviceHoursYear = totalDeviceHoursYear + (labClosingTime - Integer.parseInt(startHour)) + (dayDiff * (labClosingTime - labOpeningTime)) + (Integer.parseInt(endHour) - labOpeningTime);
                            }
                        }
                    }
                }
            }
        }
        occupancySelectedYearHours = (totalDeviceHoursYear/labOpeningHoursInYear)*100;
        return occupancySelectedYearHours;
    }

    /**
     *
     * Method calculates the occupancy rate of a device in a specific year and time period by iterating over every step
     * the device is used in.
     * The occupancy rate in days is the amount of days a device is used compared to the amount of days the device could
     * be used.
     * Important is to notice that by a day is meant the date and not the duration of 24 hours. 1 hour on a day is counted
     * as 1 day.
     *
     * @param model ModelMap that holds the model attributes
     * @param selectedDeviceSteps steps of pne of the selected devices for the graph
     * @param totalDeviceDaysYear days a device has been used, initialised to 0.
     * @return occupancy rate in days (not by 24 hours) in a specific year and time period.
     * @throws ParseException when date object is badly parsed
     */
    public float calculateOccupancyDays(final ModelMap model, List<Step> selectedDeviceSteps, float totalDeviceDaysYear) throws ParseException {
        float occupancySelectedYearDays=0;
        List<String> bookedDaysStart = new ArrayList<>();
        List<String> bookedDaysEnd = new ArrayList<>();
        String selectedTimePeriod = (String) model.getAttribute("selectedTimePeriod");
        SimpleDateFormat formatDateHourMin = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date todaysDate = new Date();

        for(int j=0;j<selectedDeviceSteps.size();j++){
            Step devStep = selectedDeviceSteps.get(j);

            Date thisStepDateStart = formatDateHourMin.parse(devStep.getStart() + " " + devStep.getStartHour());

            if(selectedTimePeriod.matches("Started")) {
                if(thisStepDateStart.before(todaysDate)) {
                    String yearStep = getStepYearStart(selectedDeviceSteps.get(j));
                    if (yearStep.matches((String) model.getAttribute("selectedYear"))) {
                        String stepDateStart = devStep.getStart();
                        String stepDateEnd = devStep.getEnd();
                        String startDay = getStepDayStart(devStep);
                        String endDay = getStepDayEnd(devStep);
                        String startMonth = getStepMonthStart(devStep);
                        String endMonth = getStepMonthEnd(devStep);
                        String startYear = getStepYearStart(devStep);

                        //If not same startDate and not same endDate as other step and same month just do (end - start)+1
                        if ((bookedDaysEnd.contains(stepDateStart) == false) && (bookedDaysStart.contains(stepDateEnd) == false) && (startMonth.matches(endMonth) == true)) {
                            int dayDiff = (Integer.parseInt(endDay) - Integer.parseInt(startDay)) + 1;
                            totalDeviceDaysYear = totalDeviceDaysYear + dayDiff;
                        }
                        // if same startDate but not same endDate as other step and same month do (end - start) OR not same startDate but same endDate
                        else if (((bookedDaysEnd.contains(stepDateStart) == true) && (bookedDaysStart.contains(stepDateEnd) == false) && (startMonth.matches(endMonth) == true))
                                || ((bookedDaysEnd.contains(stepDateStart) == false) && (bookedDaysStart.contains(stepDateEnd) == true) && (startMonth.matches(endMonth) == true))) {
                            float dayDiff = (Integer.parseInt(endDay) - Integer.parseInt(startDay));
                            totalDeviceDaysYear = totalDeviceDaysYear + dayDiff;
                        }
                        // if same startDate and same endDate as other step and same month do (end- start)-1 BUT look out for values smaller than 0 (occurs when step duration one day)
                        else if ((bookedDaysEnd.contains(stepDateStart) == true) && (bookedDaysStart.contains(stepDateEnd) == true) && (startMonth.matches(endMonth) == true)) {
                            float dayDiff = (Integer.parseInt(endDay) - Integer.parseInt(startDay)) - 1;
                            if (dayDiff >= 0) {
                                totalDeviceDaysYear = totalDeviceDaysYear + dayDiff;
                            } else {
                            }
                        }
                        // If not same startDate and not same endDate as other step and different month just do (end - start)+1
                        else if ((bookedDaysEnd.contains(stepDateStart) == false) && (bookedDaysStart.contains(stepDateEnd) == false) && (startMonth.matches(endMonth) == false)) {
                            float monthsDifference = (Integer.parseInt(endMonth)) - (Integer.parseInt(startMonth));
                            if (monthsDifference == 1) {
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
                            //If more than one month we take 30.4375 as average and don't take into account februari or leap years, to reduce complexity....
                            else {
                                float extraDaysMonths = monthsDifference * ((float) (30.4375));
                                float dayDiff = ((Integer.parseInt(endDay) + extraDaysMonths) - Integer.parseInt(startDay)) + 1;
                                totalDeviceDaysYear = totalDeviceDaysYear + dayDiff;
                            }
                        }
                        // if same startDate but not same endDate as other step and not same month do (end - start) OR not same startDate but same endDate
                        else if (((bookedDaysEnd.contains(stepDateStart) == true) && (bookedDaysStart.contains(stepDateEnd) == false) && (startMonth.matches(endMonth) == false))
                                || ((bookedDaysEnd.contains(stepDateStart) == false) && (bookedDaysStart.contains(stepDateEnd) == true) && (startMonth.matches(endMonth) == false))) {
                            float monthsDifference = (Integer.parseInt(endMonth)) - (Integer.parseInt(startMonth));
                            if (monthsDifference == 1) {
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
                            //If more than one month we take 30.4375 as average and don't take into account februari or leap years, to reduce complexity....
                            else {
                                float extraDaysMonths = monthsDifference * ((float) (30.4375));
                                float dayDiff = ((Integer.parseInt(endDay) + extraDaysMonths) - Integer.parseInt(startDay));
                                totalDeviceDaysYear = totalDeviceDaysYear + dayDiff;
                            }
                        }
                        // If same startDate and same endDate as other step and different month just do (end - start)-1
                        else if ((bookedDaysEnd.contains(stepDateStart) == true) && (bookedDaysStart.contains(stepDateEnd) == true) && (startMonth.matches(endMonth) == false)) {
                            float monthsDifference = (Integer.parseInt(endMonth)) - (Integer.parseInt(startMonth));
                            if (monthsDifference == 1) {
                                // If february -> march
                                if (startMonth.matches("02")) {
                                    //check leap year
                                    int startYearInt = Integer.parseInt(startYear);
                                    boolean leap = checkLeapYear(startYearInt);
                                    //if leap +29
                                    if (leap) {
                                        float dayDiff = ((Integer.parseInt(endDay) + 29) - Integer.parseInt(startDay)) - 1;
                                        if (dayDiff >= 0) {
                                            totalDeviceDaysYear = totalDeviceDaysYear + dayDiff;
                                        }
                                    }
                                    //if not leap +28
                                    else {
                                        float dayDiff = ((Integer.parseInt(endDay) + 28) - Integer.parseInt(startDay)) - 1;
                                        if (dayDiff >= 0) {
                                            totalDeviceDaysYear = totalDeviceDaysYear + dayDiff;
                                        }
                                    }
                                }
                                //if even month -> odd month => +30
                                else if (Integer.parseInt(startMonth) % 2 == 0) {
                                    float dayDiff = ((Integer.parseInt(endDay) + 30) - Integer.parseInt(startDay)) - 1;
                                    if (dayDiff >= 0) {
                                        totalDeviceDaysYear = totalDeviceDaysYear + dayDiff;
                                    }
                                }
                                // if odd month -> even month => +31
                                else {
                                    float dayDiff = ((Integer.parseInt(endDay) + 31) - Integer.parseInt(startDay)) - 1;
                                    if (dayDiff >= 0) {
                                        totalDeviceDaysYear = totalDeviceDaysYear + dayDiff;
                                    }
                                }
                            }
                            //If more than one month we take 30.4375 as average and don't take into account februari or leap years, to reduce complexity....
                            else {
                                float extraDaysMonths = monthsDifference * ((float) (30.4375));
                                float dayDiff = ((Integer.parseInt(endDay) + extraDaysMonths) - Integer.parseInt(startDay)) - 1;
                                totalDeviceDaysYear = totalDeviceDaysYear + dayDiff;
                            }
                        }

                        if (bookedDaysStart.contains(stepDateStart) == false) {
                            bookedDaysStart.add(stepDateStart);
                        }
                        if (bookedDaysEnd.contains(stepDateEnd) == false) {
                            bookedDaysEnd.add(stepDateEnd);
                        }
                    }
                }
            }
            else if (selectedTimePeriod.matches("All")){
                String yearStep = getStepYearStart(selectedDeviceSteps.get(j));
                if (yearStep.matches((String) model.getAttribute("selectedYear"))) {
                    String stepDateStart = devStep.getStart();
                    String stepDateEnd = devStep.getEnd();
                    String startDay = getStepDayStart(devStep);
                    String endDay = getStepDayEnd(devStep);
                    String startMonth = getStepMonthStart(devStep);
                    String endMonth = getStepMonthEnd(devStep);
                    String startYear = getStepYearStart(devStep);

                    //If not same startDate and not same endDate as other step and same month just do (end - start)+1
                    if ((bookedDaysEnd.contains(stepDateStart) == false) && (bookedDaysStart.contains(stepDateEnd) == false) && (startMonth.matches(endMonth) == true)) {
                        int dayDiff = (Integer.parseInt(endDay) - Integer.parseInt(startDay)) + 1;
                        totalDeviceDaysYear = totalDeviceDaysYear + dayDiff;
                    }
                    // if same startDate but not same endDate as other step and same month do (end - start) OR not same startDate but same endDate
                    else if (((bookedDaysEnd.contains(stepDateStart) == true) && (bookedDaysStart.contains(stepDateEnd) == false) && (startMonth.matches(endMonth) == true))
                            || ((bookedDaysEnd.contains(stepDateStart) == false) && (bookedDaysStart.contains(stepDateEnd) == true) && (startMonth.matches(endMonth) == true))) {
                        float dayDiff = (Integer.parseInt(endDay) - Integer.parseInt(startDay));
                        totalDeviceDaysYear = totalDeviceDaysYear + dayDiff;
                    }
                    // if same startDate and same endDate as other step and same month do (end- start)-1 BUT look out for values smaller than 0 (occurs when step duration one day)
                    else if ((bookedDaysEnd.contains(stepDateStart) == true) && (bookedDaysStart.contains(stepDateEnd) == true) && (startMonth.matches(endMonth) == true)) {
                        float dayDiff = (Integer.parseInt(endDay) - Integer.parseInt(startDay)) - 1;
                        if (dayDiff >= 0) {
                            totalDeviceDaysYear = totalDeviceDaysYear + dayDiff;
                        } else {
                        }
                    }
                    // If not same startDate and not same endDate as other step and different month just do (end - start)+1
                    else if ((bookedDaysEnd.contains(stepDateStart) == false) && (bookedDaysStart.contains(stepDateEnd) == false) && (startMonth.matches(endMonth) == false)) {
                        float monthsDifference = (Integer.parseInt(endMonth)) - (Integer.parseInt(startMonth));
                        if (monthsDifference == 1) {
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
                        //If more than one month we take 30.4375 as average and don't take into account februari or leap years, to reduce complexity....
                        else {
                            float extraDaysMonths = monthsDifference * ((float) (30.4375));
                            float dayDiff = ((Integer.parseInt(endDay) + extraDaysMonths) - Integer.parseInt(startDay)) + 1;
                            totalDeviceDaysYear = totalDeviceDaysYear + dayDiff;
                        }
                    }
                    // if same startDate but not same endDate as other step and not same month do (end - start) OR not same startDate but same endDate
                    else if (((bookedDaysEnd.contains(stepDateStart) == true) && (bookedDaysStart.contains(stepDateEnd) == false) && (startMonth.matches(endMonth) == false))
                            || ((bookedDaysEnd.contains(stepDateStart) == false) && (bookedDaysStart.contains(stepDateEnd) == true) && (startMonth.matches(endMonth) == false))) {
                        float monthsDifference = (Integer.parseInt(endMonth)) - (Integer.parseInt(startMonth));
                        if (monthsDifference == 1) {
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
                        //If more than one month we take 30.4375 as average and don't take into account februari or leap years, to reduce complexity....
                        else {
                            float extraDaysMonths = monthsDifference * ((float) (30.4375));
                            float dayDiff = ((Integer.parseInt(endDay) + extraDaysMonths) - Integer.parseInt(startDay));
                            totalDeviceDaysYear = totalDeviceDaysYear + dayDiff;
                        }
                    }
                    // If same startDate and same endDate as other step and different month just do (end - start)-1
                    else if ((bookedDaysEnd.contains(stepDateStart) == true) && (bookedDaysStart.contains(stepDateEnd) == true) && (startMonth.matches(endMonth) == false)) {
                        float monthsDifference = (Integer.parseInt(endMonth)) - (Integer.parseInt(startMonth));
                        if (monthsDifference == 1) {
                            // If february -> march
                            if (startMonth.matches("02")) {
                                //check leap year
                                int startYearInt = Integer.parseInt(startYear);
                                boolean leap = checkLeapYear(startYearInt);
                                //if leap +29
                                if (leap) {
                                    float dayDiff = ((Integer.parseInt(endDay) + 29) - Integer.parseInt(startDay)) - 1;
                                    if (dayDiff >= 0) {
                                        totalDeviceDaysYear = totalDeviceDaysYear + dayDiff;
                                    }
                                }
                                //if not leap +28
                                else {
                                    float dayDiff = ((Integer.parseInt(endDay) + 28) - Integer.parseInt(startDay)) - 1;
                                    if (dayDiff >= 0) {
                                        totalDeviceDaysYear = totalDeviceDaysYear + dayDiff;
                                    }
                                }
                            }
                            //if even month -> odd month => +30
                            else if (Integer.parseInt(startMonth) % 2 == 0) {
                                float dayDiff = ((Integer.parseInt(endDay) + 30) - Integer.parseInt(startDay)) - 1;
                                if (dayDiff >= 0) {
                                    totalDeviceDaysYear = totalDeviceDaysYear + dayDiff;
                                }
                            }
                            // if odd month -> even month => +31
                            else {
                                float dayDiff = ((Integer.parseInt(endDay) + 31) - Integer.parseInt(startDay)) - 1;
                                if (dayDiff >= 0) {
                                    totalDeviceDaysYear = totalDeviceDaysYear + dayDiff;
                                }
                            }
                        }
                        //If more than one month we take 30.4375 as average and don't take into account februari or leap years, to reduce complexity....
                        else {
                            float extraDaysMonths = monthsDifference * ((float) (30.4375));
                            float dayDiff = ((Integer.parseInt(endDay) + extraDaysMonths) - Integer.parseInt(startDay)) - 1;
                            totalDeviceDaysYear = totalDeviceDaysYear + dayDiff;
                        }
                    }

                    if (bookedDaysStart.contains(stepDateStart) == false) {
                        bookedDaysStart.add(stepDateStart);
                    }
                    if (bookedDaysEnd.contains(stepDateEnd) == false) {
                        bookedDaysEnd.add(stepDateEnd);
                    }
                }
            }
            if(selectedTimePeriod.matches("Future")) {
                if(thisStepDateStart.after(todaysDate)) {
                    String yearStep = getStepYearStart(selectedDeviceSteps.get(j));
                    if (yearStep.matches((String) model.getAttribute("selectedYear"))) {
                        String stepDateStart = devStep.getStart();
                        String stepDateEnd = devStep.getEnd();
                        String startDay = getStepDayStart(devStep);
                        String endDay = getStepDayEnd(devStep);
                        String startMonth = getStepMonthStart(devStep);
                        String endMonth = getStepMonthEnd(devStep);
                        String startYear = getStepYearStart(devStep);

                        //If not same startDate and not same endDate as other step and same month just do (end - start)+1
                        if ((bookedDaysEnd.contains(stepDateStart) == false) && (bookedDaysStart.contains(stepDateEnd) == false) && (startMonth.matches(endMonth) == true)) {
                            int dayDiff = (Integer.parseInt(endDay) - Integer.parseInt(startDay)) + 1;
                            totalDeviceDaysYear = totalDeviceDaysYear + dayDiff;
                        }
                        // if same startDate but not same endDate as other step and same month do (end - start) OR not same startDate but same endDate
                        else if (((bookedDaysEnd.contains(stepDateStart) == true) && (bookedDaysStart.contains(stepDateEnd) == false) && (startMonth.matches(endMonth) == true))
                                || ((bookedDaysEnd.contains(stepDateStart) == false) && (bookedDaysStart.contains(stepDateEnd) == true) && (startMonth.matches(endMonth) == true))) {
                            float dayDiff = (Integer.parseInt(endDay) - Integer.parseInt(startDay));
                            totalDeviceDaysYear = totalDeviceDaysYear + dayDiff;
                        }
                        // if same startDate and same endDate as other step and same month do (end- start)-1 BUT look out for values smaller than 0 (occurs when step duration one day)
                        else if ((bookedDaysEnd.contains(stepDateStart) == true) && (bookedDaysStart.contains(stepDateEnd) == true) && (startMonth.matches(endMonth) == true)) {
                            float dayDiff = (Integer.parseInt(endDay) - Integer.parseInt(startDay)) - 1;
                            if (dayDiff >= 0) {
                                totalDeviceDaysYear = totalDeviceDaysYear + dayDiff;
                            } else {
                            }
                        }
                        // If not same startDate and not same endDate as other step and different month just do (end - start)+1
                        else if ((bookedDaysEnd.contains(stepDateStart) == false) && (bookedDaysStart.contains(stepDateEnd) == false) && (startMonth.matches(endMonth) == false)) {
                            float monthsDifference = (Integer.parseInt(endMonth)) - (Integer.parseInt(startMonth));
                            if (monthsDifference == 1) {
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
                            //If more than one month we take 30.4375 as average and don't take into account februari or leap years, to reduce complexity....
                            else {
                                float extraDaysMonths = monthsDifference * ((float) (30.4375));
                                float dayDiff = ((Integer.parseInt(endDay) + extraDaysMonths) - Integer.parseInt(startDay)) + 1;
                                totalDeviceDaysYear = totalDeviceDaysYear + dayDiff;
                            }
                        }
                        // if same startDate but not same endDate as other step and not same month do (end - start) OR not same startDate but same endDate
                        else if (((bookedDaysEnd.contains(stepDateStart) == true) && (bookedDaysStart.contains(stepDateEnd) == false) && (startMonth.matches(endMonth) == false))
                                || ((bookedDaysEnd.contains(stepDateStart) == false) && (bookedDaysStart.contains(stepDateEnd) == true) && (startMonth.matches(endMonth) == false))) {
                            float monthsDifference = (Integer.parseInt(endMonth)) - (Integer.parseInt(startMonth));
                            if (monthsDifference == 1) {
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
                            //If more than one month we take 30.4375 as average and don't take into account februari or leap years, to reduce complexity....
                            else {
                                float extraDaysMonths = monthsDifference * ((float) (30.4375));
                                float dayDiff = ((Integer.parseInt(endDay) + extraDaysMonths) - Integer.parseInt(startDay));
                                totalDeviceDaysYear = totalDeviceDaysYear + dayDiff;
                            }
                        }
                        // If same startDate and same endDate as other step and different month just do (end - start)-1
                        else if ((bookedDaysEnd.contains(stepDateStart) == true) && (bookedDaysStart.contains(stepDateEnd) == true) && (startMonth.matches(endMonth) == false)) {
                            float monthsDifference = (Integer.parseInt(endMonth)) - (Integer.parseInt(startMonth));
                            if (monthsDifference == 1) {
                                // If february -> march
                                if (startMonth.matches("02")) {
                                    //check leap year
                                    int startYearInt = Integer.parseInt(startYear);
                                    boolean leap = checkLeapYear(startYearInt);
                                    //if leap +29
                                    if (leap) {
                                        float dayDiff = ((Integer.parseInt(endDay) + 29) - Integer.parseInt(startDay)) - 1;
                                        if (dayDiff >= 0) {
                                            totalDeviceDaysYear = totalDeviceDaysYear + dayDiff;
                                        }
                                    }
                                    //if not leap +28
                                    else {
                                        float dayDiff = ((Integer.parseInt(endDay) + 28) - Integer.parseInt(startDay)) - 1;
                                        if (dayDiff >= 0) {
                                            totalDeviceDaysYear = totalDeviceDaysYear + dayDiff;
                                        }
                                    }
                                }
                                //if even month -> odd month => +30
                                else if (Integer.parseInt(startMonth) % 2 == 0) {
                                    float dayDiff = ((Integer.parseInt(endDay) + 30) - Integer.parseInt(startDay)) - 1;
                                    if (dayDiff >= 0) {
                                        totalDeviceDaysYear = totalDeviceDaysYear + dayDiff;
                                    }
                                }
                                // if odd month -> even month => +31
                                else {
                                    float dayDiff = ((Integer.parseInt(endDay) + 31) - Integer.parseInt(startDay)) - 1;
                                    if (dayDiff >= 0) {
                                        totalDeviceDaysYear = totalDeviceDaysYear + dayDiff;
                                    }
                                }
                            }
                            //If more than one month we take 30.4375 as average and don't take into account februari or leap years, to reduce complexity....
                            else {
                                float extraDaysMonths = monthsDifference * ((float) (30.4375));
                                float dayDiff = ((Integer.parseInt(endDay) + extraDaysMonths) - Integer.parseInt(startDay)) - 1;
                                totalDeviceDaysYear = totalDeviceDaysYear + dayDiff;
                            }
                        }

                        if (bookedDaysStart.contains(stepDateStart) == false) {
                            bookedDaysStart.add(stepDateStart);
                        }
                        if (bookedDaysEnd.contains(stepDateEnd) == false) {
                            bookedDaysEnd.add(stepDateEnd);
                        }
                    }
                }
            }
        }
        occupancySelectedYearDays = (totalDeviceDaysYear/amountOfWorkDaysInYear)*100;
        return occupancySelectedYearDays;
    }

    /**
     * method checks if year is leap year or not
     * @param startYear year a step starts
     * @return boolen true if the year is leap, false if it's not a leap year
     */
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

    /**
     * Method to get the startMonth of a step if the step date is defined as a string in the form "yyyy-mm-dd"
     * @param step step we want the start month of
     * @return month the step starts
     */
    public String getStepMonthStart(Step step){
        String totalDate = step.getStart();
        String dateSplit[] = totalDate.split("-");
        String month = dateSplit[1];
        return month;
    }

    /**
     * Method to get the end month of a step if the step date is defined as a string in the form "yyyy-mm-dd"
     * @param step step we want the end month of
     * @return month the step end
     */
    public String getStepMonthEnd(Step step){
        String totalDate = step.getEnd();
        String dateSplit[] = totalDate.split("-");
        String month = dateSplit[1];
        return month;
    }

    /**
     * Method to get the start year of a step if the step date is defined as a string in the form "yyyy-mm-dd"
     * @param step step we want the start year of
     * @return year the step starts
     */
    public String getStepYearStart(Step step){
        String totalDate = step.getStart();
        String dateSplit[] = totalDate.split("-");
        String year = dateSplit[0];
        return year;
    }

    /**
     * Method to get the end year of a step if the step date is defined as a string in the form "yyyy-mm-dd"
     * @param step step we want the end year of
     * @return year the step end
     */
    public String getStepYearEnd(Step step){
        String totalDate = step.getEnd();
        String dateSplit[] = totalDate.split("-");
        String year = dateSplit[0];
        return year;
    }

    /**
     * Method to get the start day of a step if the step date is defined as a string in the form "yyyy-mm-dd"
     * @param step step we want the start day of
     * @return day the step starts
     */
    public String getStepDayStart(Step step){
        String totalDate = step.getStart();
        String dataSplit[] = totalDate.split("-");
        String day = dataSplit[2];
        return day;
    }

    /**
     * Method to get the end day of a step if the step date is defined as a string in the form "yyyy-mm-dd"
     * @param step step we want the end day of
     * @return day the step ends
     */
    public String getStepDayEnd(Step step){
        String totalDate = step.getEnd();
        String dataSplit[] = totalDate.split("-");
        String day = dataSplit[2];
        return day;
    }

    /**
     * Method to get the start hour of a step if the step date is defined as a string in the form "hh-mm"
     * @param step step we want the start hour of
     * @return hour the step starts
     */
    public String getStepHourStart(Step step){
        String totalHour = step.getStartHour();
        String dataSplit[] = totalHour.split(":");
        String hour = dataSplit[0];
        return hour;
    }

    /**
     * Method to get the end hour of a step if the step date is defined as a string in the form "hh-mm"
     * @param step step we want the end hour of
     * @return hour the step ends
     */
    public String getStepHourEnd(Step step){
        String totalHour = step.getEndHour();
        String dataSplit[] = totalHour.split(":");
        String hour = dataSplit[0];
        return hour;
    }

    /**
     * Method that returns the current year as a string in the format 'yyyy'
     * @return current year as a string
     */
    public static String getCurrentYear() {
        Date date = new Date();
        String strDateFormat = "yyyy";
        DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
        String formattedDate = dateFormat.format(date);
        return formattedDate;
    }

    /**
     *
     * Method used to change the selected year model attribute in the modelMap
     * method is a bit overkill since the line could be immediatly integrated in the method it's called from
     *
     * @param model ModelMap that holds the model attributes
     * @param year year we want to set the selectyear model attribute to
     */
    public void setSelectedYear(final ModelMap model, String year) {
         model.addAttribute("selectedYear",year);
    }

    /**
     * getter method for the lab opening hours in a year
     * @return opening hour of the lab in a year
     */
    public float getLabOpeningHoursInYear(){
        return labOpeningHoursInYear;
    }

    /**
     * getter method for the amount of work days in a year
     * @return opening days of the lab in a year
     */
    public float getAmountOfWorkDaysInYear(){
        return amountOfWorkDaysInYear;
    }
}
