package be.uantwerpen.labplanner.Formatter;

import be.uantwerpen.labplanner.Model.Step;
import be.uantwerpen.labplanner.Repository.StepRepository;
import be.uantwerpen.labplanner.common.model.users.Role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

@Component
public class StepFormatter implements Formatter<Step> {
    @Autowired
    private StepRepository stepRepository;
    public Step parse(final String text, final Locale locale)  throws ParseException {
        if (text != null && !text.isEmpty())
            return stepRepository.findById(new Long(text)).orElse(null);
        else return null;
    }

    public String print(final Step object, final Locale locale)
    { return (object != null ? object.getId().toString() : "");
    }

}
