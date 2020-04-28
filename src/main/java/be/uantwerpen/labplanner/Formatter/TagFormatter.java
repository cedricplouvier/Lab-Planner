package be.uantwerpen.labplanner.Formatter;


import be.uantwerpen.labplanner.Model.OwnTag;
import be.uantwerpen.labplanner.Repository.OwnTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

@Component
public class TagFormatter implements Formatter<OwnTag> {
    @Autowired
    private OwnTagRepository tagRepository;

    public OwnTag parse(final String text, final Locale locale)
            throws ParseException {
        if (text != null && !text.isEmpty())
            return tagRepository.findById(new Long(text)).orElse(null);
        else return null;
    }

    public String print(final OwnTag object, final Locale locale)
    { return (object != null ? object.getId().toString() : "");
    }
}
