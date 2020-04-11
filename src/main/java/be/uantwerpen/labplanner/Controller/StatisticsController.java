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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    Device dev1 = new Device();
    Device dev2 = new Device();
    Device dev3 = new Device();
    Device dev4 = new Device();
    Device dev5 = new Device();
    List<Device> selectedDevices = new ArrayList<>(Arrays.asList(dev1,dev2,dev3,dev4,dev5));
    int deviceCounter=0;

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
        model.addAttribute("totalHours1",totalHours.get(0));
        model.addAttribute("totalHours2",totalHours.get(1));
        model.addAttribute("totalHours3",totalHours.get(2));
        model.addAttribute("totalHours4",totalHours.get(3));
        model.addAttribute("totalHours5",totalHours.get(4));
        model.addAttribute("deviceCounter", deviceCounter);

        return "/Statistics/statistics";
    }

    @PreAuthorize("hasAnyAuthority('Statistics Access')")
    @RequestMapping("/statistics/statistics/submit")
    public String submit(final ModelMap model, Device selectedDev){

        List<Step> allSteps = stepService.findAll();
        int[] totalHoursSelectedDevice;

        selectedDevices.set(deviceCounter,selectedDev);

        for(int i=0;i<selectedDevices.size();i++){
            Device dev = selectedDevices.get(i);
            List<Step> selectedDeviceSteps = filterSelectedDeviceSteps(dev,allSteps);
            totalHoursSelectedDevice=calculateTotalHoursDeviceByMonth(selectedDeviceSteps);
            totalHours.set(i,totalHoursSelectedDevice);
        }
        deviceCounter++;
        model.addAttribute("selectedDev",selectedDevices.get(deviceCounter));
        return "redirect:/statistics/statistics";
    }

    @PreAuthorize("hasAnyAuthority('Statistics Access')")
    @RequestMapping("statistics/statistics/clearList")
    public String clearList() {
        deviceCounter = 0;
        selectedDevices = Arrays.asList(new Device(),new Device(),new Device(),new Device(),new Device());
        int[] emptyHoursArray =new int[]{0,0,0,0,0,0,0,0,0,0,0,0};

        for(int j=0; j<totalHours.size();j++) {
            totalHours.set(j, emptyHoursArray);
        }
        return "redirect:/statistics/statistics";
    }

    @PreAuthorize("hasAnyAuthority('Statistics Access')")
    @RequestMapping("statistics/statistics/updateGraph")
    public String updateGraph(final ModelMap model){
        /*
        List<Step> allSteps = stepService.findAll();

        for(int i=0;i<selectedDevices.size();i++){
            System.out.println("TEST");
            Device dev = selectedDevices.get(i);
            List<Step> selectedDeviceSteps = filterSelectedDeviceSteps(dev,allSteps);
            totalHours1=calculateTotalHoursDeviceByMonth(selectedDeviceSteps);
            totalHours.set(i,totalHours1);
        }*/
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
        System.out.println("StartTime "+startTime+" StopTime " + stopTime+ " => hours used: "+hourDiff);
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
        System.out.println(" Amount of steps of "+selectedDev.getDevicename()+" found: "+selectedDeviceSteps.size());
        return selectedDeviceSteps;
    }

    public int[] calculateTotalHoursDeviceByMonth(List<Step> selectedDeviceSteps){
        int[] totalHoursByMonth = new int[12];
        String[] months = new String[]{"01","02","03","04","05","06","07","08","09","10","11","12"};
        for(int j=0;j<selectedDeviceSteps.size();j++){
            String monthStep = getStopMonth(selectedDeviceSteps.get(j));
            for(int i = 0; i<months.length;i++) {
                if (monthStep.matches(months[i])) {
                    totalHoursByMonth[i] = totalHoursByMonth[i] + calculateHourDiff(selectedDeviceSteps.get(j));;
                }
            }
        }
        return totalHoursByMonth;
    }

    public String getStopMonth(Step step){
        String totalDate = step.getStart();
        String dateSplit[] = totalDate.split("-");
        String month = dateSplit[1];
        return month;
    }

}
