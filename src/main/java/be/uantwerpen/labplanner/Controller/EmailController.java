package be.uantwerpen.labplanner.Controller;

import be.uantwerpen.labplanner.Model.Device;
import be.uantwerpen.labplanner.Model.Experiment;
import be.uantwerpen.labplanner.Model.OwnProduct;
import be.uantwerpen.labplanner.Model.Step;
import be.uantwerpen.labplanner.Service.DeviceService;
import be.uantwerpen.labplanner.common.model.users.Role;
import be.uantwerpen.labplanner.common.model.users.User;
import be.uantwerpen.labplanner.common.service.users.RoleService;
import be.uantwerpen.labplanner.common.service.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import static jdk.nashorn.internal.objects.NativeMath.round;

@Controller
public class EmailController   {

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private UserService userService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private StepController stepController;

    private boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    @RequestMapping(value= "/mail/maintanance/{id}", method = RequestMethod.GET)
    public String sendMaintanceMail(@PathVariable long id, final ModelMap model, RedirectAttributes ra){
        //get current user.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        Device device = deviceService.findById(id).orElse(null);

        if (device==null){
            ra.addAttribute("deviceError", ResourceBundle.getBundle("messages", LocaleContextHolder.getLocale()).getString("device.maintananceError"));

            return "redirect:/devices";
        }

        List<String> adresses = loadAdresses();

        if (adresses.size()<1){
            ra.addAttribute("deviceError", ResourceBundle.getBundle("messages", LocaleContextHolder.getLocale()).getString("mail.empty"));
            return "redirect:/devices";
        }

        //convert to string array
        String[] adressArray = adresses.toArray(new String[0]);
        String subject = device.getDevicename() + " needs maintanance.";
        String text = user.getFirstName() + " " + user.getLastName() + " says " + device.getDevicename() + " needs maintanance.";


        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(adressArray);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);

        ra.addAttribute("MailSuccess", ResourceBundle.getBundle("messages", LocaleContextHolder.getLocale()).getString("mail.success"));
        return "redirect:/devices";
    }

    @Scheduled(cron =  "0 0 20 * * ?")
    public void sendPeriodicMail(){
        List<String> adresses = loadAdresses();

        if (adresses.size()>0){
            //convert to string array
            String[] adressArray = adresses.toArray(new String[0]);
            String subject = "Daily update of steps & experiments";
            StringBuilder text = new StringBuilder("");

            if (stepController.getAddedSteps().size()>0) {
                text.append("Added steps:\n");
                for (Map.Entry<Step, User> pair : stepController.getAddedSteps().entrySet()) {
                    text.append("Step with id " + pair.getKey().getId() + " is added by user " + pair.getValue().getFirstName() + " " + pair.getValue().getLastName() + ".\n");
                }
                text.append("\n");
            }

            if (stepController.getEditedSteps().size()>0) {

                text.append("Edited steps:\n");
                for (Map.Entry<Step, User> pair : stepController.getEditedSteps().entrySet()) {
                    text.append("Step with id " + pair.getKey().getId() + " is edited by user " + pair.getValue().getFirstName() + " " + pair.getValue().getLastName() + ".\n");
                }
                text.append("\n");
            }

            if (stepController.getDeletedSteps().size()>0) {

                text.append("Deleted steps:\n");
                for (Map.Entry<Step, User> pair : stepController.getDeletedSteps().entrySet()) {
                    text.append("Step with id " + pair.getKey().getId() + " is deleted by user " + pair.getValue().getFirstName() + " " + pair.getValue().getLastName() + ".\n");
                }
                text.append("\n");
            }

            if (stepController.getAddedExperiments().size()>0) {

                text.append("Added experiments:\n");
                for (Map.Entry<Experiment, User> pair : stepController.getAddedExperiments().entrySet()) {
                    text.append("Experiment " + pair.getKey().getExperimentname() + " with id " + pair.getKey().getId() + " is added by user " + pair.getValue().getFirstName() + " " + pair.getValue().getLastName() + ".\n");
                }
                text.append("\n");
            }

            if (stepController.getEditedExperiments().size()>0) {

                text.append("Edited experiments:\n");
                for (Map.Entry<Experiment, User> pair : stepController.getEditedExperiments().entrySet()) {
                    text.append("Experiment " + pair.getKey().getExperimentname() + " with id " + pair.getKey().getId() + " is edited by user " + pair.getValue().getFirstName() + " " + pair.getValue().getLastName() + ".\n");
                }
                text.append("\n");
            }

            if (stepController.getAddedSteps().size()>0) {

                text.append("Deleted experiments:\n");
                for (Map.Entry<Experiment, User> pair : stepController.getDeletedExperiments().entrySet()) {
                    text.append("Experiment " + pair.getKey().getExperimentname() + " with id " + pair.getKey().getId() + " is deleted by user " + pair.getValue().getFirstName() + " " + pair.getValue().getLastName() + ".\n");
                }
                text.append("\n");
            }

            stepController.clearLists();

            if (text.length()<1){
                text.append("No new updates.");
            }

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(adressArray);
            message.setSubject(subject);
            message.setText(text.toString());
            emailSender.send(message);
        }

    }

    public void sendLowStockEmail(OwnProduct product, User user, String type){
        List<String> adresses = loadAdresses();
        DecimalFormat df = new DecimalFormat("#.##");

        if (adresses.size() > 0){
            //convert to string array
            String[] adressArray = adresses.toArray(new String[0]);
            String subject = product.getName() + " has low stock";
            StringBuilder text = new StringBuilder();
            text.append("New " + type + " created by " + user.getFirstName() + " " + user.getLastName() + " and now  " + product.getName() + " has low stock.\n");
            text.append(product.getName() + " has current stock " + df.format(product.getStockLevel())  + " " + product.getUnits() + ".\n");
            text.append(product.getName() + " has low stock level " +df.format(product.getLowStockLevel())  + " " + product.getUnits() + ".\n");


            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(adressArray);
            message.setSubject(subject);
            message.setText(text.toString());
            emailSender.send(message);
        }

    }

    private List<String> loadAdresses(){
        List<String> adresses = new ArrayList<>();

        Role admin = roleService.findByName("Administrator").orElse(null);
        for (User temp : userService.findAll()){
            if ((temp.getRoles().contains(admin)) && (temp.getFirstName().equals("Ruben"))){
                if ((temp.getEmail()!=null)&&(isValidEmailAddress(temp.getEmail()))){
                    adresses.add(temp.getEmail());
                }

            }
        }

        return adresses;

    }
}