package be.uantwerpen.labplanner.Formatter;


import be.uantwerpen.labplanner.Model.DeviceType;
import be.uantwerpen.labplanner.Repository.DeviceTypeRepository;
import be.uantwerpen.labplanner.common.model.stock.Tag;
import be.uantwerpen.labplanner.common.repository.stock.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

@Component
public class TagFormatter implements Formatter<Tag> {
    @Autowired
    private TagRepository tagRepository;

    public Tag parse(final String text, final Locale locale)
            throws ParseException {
        if (text != null && !text.isEmpty())
            return tagRepository.findById(new Long(text)).orElse(null);
        else return null;
    }

    public String print(final Tag object, final Locale locale)
    { return (object != null ? object.getId().toString() : "");
    }
}
