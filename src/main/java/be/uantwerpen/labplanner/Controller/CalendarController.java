package be.uantwerpen.labplanner.Controller;
import be.uantwerpen.labplanner.Model.*;
import be.uantwerpen.labplanner.Service.*;
import be.uantwerpen.labplanner.common.model.users.Role;
import be.uantwerpen.labplanner.common.model.users.User;
import be.uantwerpen.labplanner.common.service.users.RoleService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import de.jollyday.Holiday;
import de.jollyday.HolidayCalendar;
import de.jollyday.HolidayManager;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Controller
public class CalendarController {
    @Autowired
    StepService stepService;
    @Autowired
    DeviceTypeService deviceTypeService;
    @Autowired
    DeviceService deviceService;
    @Autowired
    RoleService roleService;
    @Autowired
    SystemSettingsService systemSettingsService;

    //Populate
    @ModelAttribute("allDevices")
    public Iterable<Device> populateDevices() {
        return this.deviceService.findAll();
    }
    @ModelAttribute("allDeviceTypes")
    public Iterable<DeviceType> populateDeviceTypes() { return this.deviceTypeService.findAll(); }
    @ModelAttribute("allSteps")
    public Iterable<Step> populateSteps() { return this.stepService.findAll();}

    //Mappings
    @RequestMapping(value = "/calendar/user", method = RequestMethod.GET)
    public String showUserCalendar(final ModelMap model) {
        User user = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        List<Step> userSteps = new ArrayList<Step>();
        List<Step> otherSteps = new ArrayList<Step>();
        for (Step step : stepService.findAll()) {
            if (step.getUser().getId() == user.getId()) {
                userSteps.add(step);
            } else {
                otherSteps.add(step);
            }
        }
        Role admin = roleService.findByName("Administrator").get();
        if(user.getRoles().contains(admin)) {
            model.addAttribute("otherSteps", otherSteps);
        }
        model.addAttribute("userSteps", userSteps);
        model.addAttribute("allDevices", deviceService.findAll());
        model.addAttribute("allDeviceTypes",deviceTypeService.findAll());
        model.addAttribute("Step", new Step());
        return "Calendar/userCalendar";
    }

    @RequestMapping(value="/calendar/calculateStepSuggestion",method=RequestMethod.POST, produces = "application/json", consumes = "application/json")
    @ResponseBody
    public String calculateStepSuggestion(@RequestBody SuggestionResponseBody suggestionResponseBody) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<SuggestionStep> steps = mapper.readValue(suggestionResponseBody.getSteps(), new TypeReference<ArrayList<SuggestionStep>>() {});
        Boolean success = getSuggestion(steps,suggestionResponseBody.getOverlapAllowed(),suggestionResponseBody.getWithinOfficeHours(),suggestionResponseBody.getDateTime(),suggestionResponseBody.getCurrentStep());
        System.out.println("Suggestion generated : "+success);
        if(success){
            return new Gson().toJson(steps);
        }else{
            return "failed";
        }
    }

    private Boolean getSuggestion(ArrayList<SuggestionStep> steps, Boolean overlapAllowed, Boolean withinOfficeHOurs, LocalDateTime start, int index){
        Boolean found = false;
        SuggestionStep currentStep = steps.get(index);
        SuggestionStep previousStep = null;
        if(index>0){
            previousStep = steps.get(index-1);
        }
        System.out.println(index);
        //Determine start time
        LocalDateTime currentDateTime = start;
        if(start==null){
        if(index>0&&previousStep!=null) {
            if(previousStep.getEnd()!=null) {
                currentDateTime = steps.get(index - 1).getEnd();
                if (previousStep.getContinuity().getDirectionType().equals("Before")){
                    currentDateTime = currentDateTime.minusHours(previousStep.getContinuity().getHours());
                    currentDateTime = currentDateTime.minusMinutes(previousStep.getContinuity().getMinutes());
                }
            }
        }}

        if(currentDateTime==null){
            currentDateTime = LocalDateTime.now();
        }
        currentDateTime = currentDateTime.withSecond(0).withNano(0);
        if(currentDateTime.getMinute()<30&&currentDateTime.getMinute()!=0){
            currentDateTime = currentDateTime.withMinute(30);
        }else if(currentDateTime.getMinute()<60&&currentDateTime.getMinute()>30){
            currentDateTime =currentDateTime.plusHours(1).withMinute(0);
        }
        int length = 60; //default step block length
        if(currentStep.isHasFixedLength()){
            length = currentStep.getFixedTimeHours()*60+currentStep.getFixedTimeMinutes();
        }
        if(previousStep!=null&&previousStep.getContinuity().getType().equals("Soft (at most)")) {
            if(steps.get(index).getContinuity().getDirectionType().equals("Before")) {
                steps.get(index).setStart(previousStep.getEnd().minusHours(previousStep.getFixedTimeHours()));
                steps.get(index).setStart(steps.get(index).getStart().minusMinutes(previousStep.getFixedTimeMinutes()));
                LocalDateTime endDate = previousStep.getEnd();
                found = loop(steps,withinOfficeHOurs,index,overlapAllowed,endDate,length);
            }else{
                steps.get(index).setStart(previousStep.getEnd());
                LocalDateTime endDate = steps.get(index).getStart().plusMinutes(previousStep.getContinuity().getMinutes());
                endDate = endDate.plusHours(previousStep.getContinuity().getHours());
                found = loop(steps,withinOfficeHOurs,index,overlapAllowed,endDate,length);

            }
        }else if(previousStep!=null&&previousStep.getContinuity().getType().equals("Hard")) {
            if (previousStep.getContinuity().getDirectionType().equals("Before")) {
                steps.get(index).setStart(previousStep.getEnd().minusHours(previousStep.getContinuity().getHours()));
                steps.get(index).setStart(steps.get(index).getStart().minusMinutes(previousStep.getContinuity().getMinutes()));
            } else {
                steps.get(index).setStart(previousStep.getEnd().plusHours(previousStep.getContinuity().getHours()));
                steps.get(index).setStart(steps.get(index).getStart().plusMinutes(previousStep.getContinuity().getMinutes()));
            }
            steps.get(index).setEnd(steps.get(index).getStart().plusMinutes(length));
            found = checkPossibility(steps, withinOfficeHOurs, index, overlapAllowed);
        }else if(previousStep!=null&&previousStep.getContinuity().getType().equals("Soft (at least)")){
                if(steps.get(index).getContinuity().getDirectionType().equals("Before")){
                    if(currentStep.isHasFixedLength()){
                        steps.get(index).setStart(previousStep.getEnd().minusHours(previousStep.getFixedTimeHours()));
                        steps.get(index).setStart(steps.get(index).getStart().minusMinutes(previousStep.getFixedTimeMinutes()));
                        LocalDateTime endDate = steps.get(index).getStart().plusMinutes(length%60-previousStep.getContinuity().getMinutes());
                        endDate = endDate.plusHours(length/60-previousStep.getContinuity().getHours());
                        found = loop(steps,withinOfficeHOurs,index,overlapAllowed,endDate,length);

                    }else{
                        steps.get(index).setStart(previousStep.getEnd().minusHours(previousStep.getContinuity().getHours()));
                        steps.get(index).setStart(steps.get(index).getStart().minusMinutes(previousStep.getContinuity().getMinutes()));
                        steps.get(index).setEnd(previousStep.getEnd());
                        found = checkPossibility(steps, withinOfficeHOurs, index, overlapAllowed);
                    }

                }else{
                    steps.get(index).setStart(previousStep.getEnd().plusHours(previousStep.getContinuity().getHours()));
                    steps.get(index).setStart(steps.get(index).getStart().plusMinutes(previousStep.getContinuity().getMinutes()));
                    LocalDateTime endDate = steps.get(index).getStart().plusDays(7);
                    found = loop(steps,withinOfficeHOurs,index,overlapAllowed,endDate,length);

                }
        }else{
            steps.get(index).setStart(currentDateTime);
            LocalDateTime endDate = steps.get(index).getStart().plusDays(7);
            //Find possibility within week
            found = loop(steps,withinOfficeHOurs,index,overlapAllowed,endDate,length);
        }
        return found;
    }

    private Boolean loop(ArrayList<SuggestionStep> steps, Boolean withinOfficeHOurs,int index,Boolean overlapAllowed,LocalDateTime endDate,int length){
        int defaultStep = 30; //default step between calculations in minutes
        Boolean found = false;
        while (steps.get(index).getStart().isBefore(endDate)) {
            steps.get(index).setEnd(steps.get(index).getStart().plusMinutes(length));
            if (checkPossibility(steps, withinOfficeHOurs, index, overlapAllowed)) {
                found = true;
                break;
            }
            steps.get(index).setStart(steps.get(index).getStart().plusMinutes(defaultStep));
            //check if end is in weekend, otherwise skip weekend;
            if(steps.get(index).getStart().getDayOfWeek()== DayOfWeek.SATURDAY || steps.get(index).getStart().getDayOfWeek()==DayOfWeek.SUNDAY){
                steps.get(index).setStart(steps.get(index).getStart().plusDays(2));
                if(withinOfficeHOurs) {
                    OfficeHours officeHours = systemSettingsService.getSystemSetting().getCurrentOfficeHours();
                    LocalDateTime start = steps.get(index).getStart();
                    steps.get(index).setStart(LocalDateTime.of(start.getYear(), start.getMonth(), start.getDayOfMonth(), officeHours.getStartHour(), officeHours.getStartMinute(), 0, 0));
                }
            }
            //check if holiday, then skip
            HolidayManager manager = HolidayManager.getInstance(HolidayCalendar.BELGIUM);
            Set<Holiday> holidays = manager.getHolidays(Calendar.getInstance().get(Calendar.YEAR));
            Iterator<Holiday> it = holidays.iterator();
            Boolean holiday = false;
            while(it.hasNext()){
                if(it.next().getDate().isEqual(steps.get(index).getStart().toLocalDate())){
                    holiday =true;
                    break;
                }
            }
            if(holiday){
                steps.get(index).setStart(steps.get(index).getStart().plusDays(1));
                if(withinOfficeHOurs) {
                    OfficeHours officeHours = systemSettingsService.getSystemSetting().getCurrentOfficeHours();
                    LocalDateTime start = steps.get(index).getStart();
                    steps.get(index).setStart(LocalDateTime.of(start.getYear(), start.getMonth(), start.getDayOfMonth(), officeHours.getStartHour(), officeHours.getStartMinute(), 0, 0));
                }
            }
            //check if is out of officehours
            if(withinOfficeHOurs) {
                OfficeHours officeHours = systemSettingsService.getSystemSetting().getCurrentOfficeHours();
                LocalTime officeTimeEnd = LocalTime.of(officeHours.getEndHour(), officeHours.getEndMinute());
                if (steps.get(index).getStart().toLocalTime().isAfter(officeTimeEnd)) {
                    LocalDateTime start = steps.get(index).getStart().plusDays(1);
                    steps.get(index).setStart(LocalDateTime.of(start.getYear(), start.getMonth(), start.getDayOfMonth(), officeHours.getStartHour(), officeHours.getStartMinute(), 0,0));
                }
            }
        }
        return found;
    }

    private Boolean checkPossibility(ArrayList<SuggestionStep> steps, Boolean withinOfficeHOurs,int index,Boolean overlapAllowed){
        if(checkContinuity(steps,index,withinOfficeHOurs,false)){
            List<Long> possibleDevices = checkOverlap(steps,overlapAllowed,index);
            if(possibleDevices.size()!=0){
                //Set current step
                steps.get(index).setDeviceId(possibleDevices.get(0));
                // check if other steps can still be found
                if(index==steps.size()-1){
                    return true;
                }else if(getSuggestion(steps,overlapAllowed,withinOfficeHOurs,null,index+1)){
                    return true;
                }
            }
        }
        return false;
    }
    private Boolean checkContinuity(ArrayList<SuggestionStep> steps, int currentIndex, Boolean withinOfficeHOurs, Boolean passContinuityCheck){
        SuggestionStep currentStep = steps.get(currentIndex);

        SuggestionStep previousStep = null;
        if(currentIndex>0){
            previousStep = steps.get(currentIndex-1);
        }
        //end Before previous type
        if(previousStep!=null && currentStep.getEnd().isBefore(previousStep.getEnd())){
            return false;
        }
        //book before today
        if(currentStep.getStart().isBefore(LocalDateTime.now())){
            return false;
        }

        //Weekend
//        //test end
//        if(currentStep.getEnd().getDayOfWeek()== DayOfWeek.SATURDAY || currentStep.getEnd().getDayOfWeek()==DayOfWeek.SUNDAY){
//            return false;
//        }

        //test start
        if(currentStep.getStart().getDayOfWeek()== DayOfWeek.SATURDAY || currentStep.getStart().getDayOfWeek()==DayOfWeek.SUNDAY){
            return false;
        }

        //Officehours
        if(withinOfficeHOurs) {

            OfficeHours officeHours = systemSettingsService.getSystemSetting().getCurrentOfficeHours();

            LocalTime officeTimeStart = LocalTime.of(officeHours.getStartHour(), officeHours.getStartMinute());
            LocalTime officeTimeEnd = LocalTime.of(officeHours.getEndHour(), officeHours.getEndMinute());

            if (currentStep.getStart().toLocalTime().isAfter(officeTimeEnd)) {
                return false;
            }
            if (currentStep.getStart().toLocalTime().isBefore(officeTimeStart)) {
                return false;
            }
        }

        //overnightuse
        if(currentStep.getStart().getDayOfYear()!=currentStep.getEnd().getDayOfYear()){
            return false;
        }
        //hollidays
        HolidayManager manager = HolidayManager.getInstance(HolidayCalendar.BELGIUM);
        Set<Holiday> holidays = manager.getHolidays(Calendar.getInstance().get(Calendar.YEAR));
        Iterator<Holiday> it = holidays.iterator();
        while(it.hasNext()){
            if(it.next().getDate().isEqual(currentStep.getStart().toLocalDate())){
                return false;
            }
        }

        //continuity type
        if(previousStep!=null) {
            //Hard
            if (previousStep.getContinuity().getType().equals("Hard")) {
                //Before
                if (previousStep.getContinuity().getDirectionType().equals("Before")) {
                    //start x time before end of previous step
                    LocalDateTime testTime = previousStep.getEnd();
                    testTime = testTime.minusHours(previousStep.getContinuity().getHours());
                    testTime = testTime.minusMinutes(previousStep.getContinuity().getMinutes());
                    if (!currentStep.getStart().isEqual(testTime)) {
                        return false;
                    }
                }
                //After
                if (previousStep.getContinuity().getDirectionType().equals("After")) {
                    //start x time after end of previous step
                    LocalDateTime testTime = previousStep.getEnd();
                    testTime = testTime.plusHours(previousStep.getContinuity().getHours());
                    testTime = testTime.plusMinutes(previousStep.getContinuity().getMinutes());
                    if (!currentStep.getStart().isEqual(testTime)) {
                        return false;
                    }
                }
            }

            //Soft(atleast)
            if (previousStep.getContinuity().getType().equals("Soft at least")) {
                //Before
                if (previousStep.getContinuity().getDirectionType().equals("Before")) {
                    //start atleast x time before end of previous step
                    LocalDateTime testTime = previousStep.getEnd();
                    testTime = testTime.plusHours(previousStep.getContinuity().getHours());
                    testTime = testTime.plusMinutes(previousStep.getContinuity().getMinutes());
                    if (!testTime.isBefore(previousStep.getEnd())) {
                        return false;
                    }
                }
                //After
                if (previousStep.getContinuity().getDirectionType().equals("After")) {
                    //start atleast x time after end of previous step
                    LocalDateTime testTime = previousStep.getEnd();
                    testTime = testTime.minusHours(previousStep.getContinuity().getHours());
                    testTime = testTime.minusMinutes(previousStep.getContinuity().getMinutes());
                    if (!testTime.isAfter(previousStep.getEnd())) {
                        return false;
                    }
                }
            }
            //Soft(atmost)
            if (previousStep.getContinuity().getType().equals("Soft (at most)")) {
                //Before
                if (previousStep.getContinuity().getDirectionType().equals("Before")) {
                    //start at most x time before end of previous step
                    LocalDateTime testTime = previousStep.getEnd();
                    testTime = testTime.plusHours(previousStep.getContinuity().getHours());
                    testTime = testTime.plusMinutes(previousStep.getContinuity().getMinutes());
                    if (!testTime.isAfter(previousStep.getEnd())) {
                        return false;
                    }
                }
                //After
                if (previousStep.getContinuity().getDirectionType().equals("After")) {
                    //start at most x time after end of previous step
                    LocalDateTime testTime = previousStep.getEnd();
                    testTime = testTime.minusHours(previousStep.getContinuity().getHours());
                    testTime = testTime.minusMinutes(previousStep.getContinuity().getMinutes());
                    if (!testTime.isBefore(previousStep.getEnd())) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    private List<Long> checkOverlap(ArrayList<SuggestionStep> steps, Boolean overlapAllowed, int currentIndex){
        System.out.println(overlapAllowed);
        User user = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        List<Long> idUsed = new ArrayList<>();
        SuggestionStep currentStep = steps.get(currentIndex);
        List<Step> allSteps = this.stepService.findAll();
        for(Step step:allSteps){
                if(!overlapAllowed&&step.getUser().getId()==user.getId()){
                    //Create datetime object
                    LocalDateTime stepStart = LocalDateTime.of(LocalDate.parse(step.getStart()), LocalTime.parse(step.getStartHour()));
                    LocalDateTime stepEnd = LocalDateTime.of(LocalDate.parse(step.getEnd()), LocalTime.parse(step.getEndHour()));
                    //if step starts before previous but ends after start previous

                    if(currentStep.getStart().isBefore(stepStart)&&currentStep.getEnd().isAfter(stepStart)){
                        return new ArrayList<>();
                    }else if(currentStep.getStart().isBefore(stepEnd)&&currentStep.getEnd().isAfter(stepEnd) ){
                        return new ArrayList<>();
                    }else if((currentStep.getStart().isAfter(stepStart)||currentStep.getStart().isEqual(stepStart))&&(currentStep.getEnd().isBefore(stepEnd)||currentStep.getEnd().isEqual(stepEnd))){
                        return new ArrayList<>();
                    }

                }else if(!idUsed.contains(step.getDevice().getId())){ //check if device is already used
                    //Create datetime object
                    LocalDateTime stepStart = LocalDateTime.of(LocalDate.parse(step.getStart()), LocalTime.parse(step.getStartHour()));
                    LocalDateTime stepEnd = LocalDateTime.of(LocalDate.parse(step.getEnd()), LocalTime.parse(step.getEndHour()));
                    //if step starts before previous but ends after start previous
                    if(currentStep.getStart().isBefore(stepStart)&&currentStep.getEnd().isAfter(stepStart)){
                        idUsed.add(step.getDevice().getId());
                    }else if(currentStep.getStart().isBefore(stepEnd)&&currentStep.getEnd().isAfter(stepEnd) ){
                        idUsed.add(step.getDevice().getId());
                    }else if((currentStep.getStart().isAfter(stepStart)||currentStep.getStart().isEqual(stepStart))&&(currentStep.getEnd().isBefore(stepEnd)||currentStep.getEnd().isEqual(stepEnd))){
                        idUsed.add(step.getDevice().getId());
                    }
                }
        }
        List<Device> allDevices = this.deviceService.findAll();
        List<Long> notUsedIds = new ArrayList<>();
        for(Device device:allDevices){
            if(device.getDeviceType().getId()==currentStep.getDeviceType().getId()){
                if(!idUsed.contains(device.getId())){
                    notUsedIds.add(device.getId());
                }
            }
        }
        System.out.println(notUsedIds.size());
        return notUsedIds;
    }

}
