package be.uantwerpen.labplanner.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class EmailController   {

    @Autowired
    public JavaMailSender emailSender;


    @RequestMapping(value = "/mail",method = RequestMethod.GET)
    public String sendSimpleMessage(final ModelMap model) {
        String to = "ruben.joosen@student.uantwerpen.be";
        String subject = "test subject";
        String text = "test text";
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);

        return "redirect:/";

    }
}