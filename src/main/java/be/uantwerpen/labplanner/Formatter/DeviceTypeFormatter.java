package be.uantwerpen.labplanner.Formatter;


import be.uantwerpen.labplanner.Model.DeviceType;
import be.uantwerpen.labplanner.Repository.DeviceTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

@Component
public class DeviceTypeFormatter implements Formatter<DeviceType> {
    @Autowired
    private DeviceTypeRepository deviceTypeRepository;

    public DeviceType parse(final String text, final Locale locale)
            throws ParseException {
        if (text != null && !text.isEmpty())
            return deviceTypeRepository.findById(new Long(text)).orElse(null);
        else return null;
    }

    public String print(final DeviceType object, final Locale locale)
    { return (object != null ? object.getId().toString() : "");
    }

}