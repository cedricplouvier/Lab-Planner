package be.uantwerpen.labplanner.Formatter;


import be.uantwerpen.labplanner.Model.DeviceInformation;
import be.uantwerpen.labplanner.Model.DeviceType;
import be.uantwerpen.labplanner.Repository.DeviceInformationRepository;
import be.uantwerpen.labplanner.Repository.DeviceTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

@Component
public class DeviceInformationFormatter implements Formatter<DeviceInformation> {
    @Autowired
    private DeviceInformationRepository deviceInformationRepository;

    public DeviceInformation parse(final String text, final Locale locale)
            throws ParseException {
        if (text != null && !text.isEmpty())
            return deviceInformationRepository.findById(new Long(text)).orElse(null);
        else return null;
    }

    public String print(final DeviceInformation object, final Locale locale)
    { return (object != null ? object.getId().toString() : "");
    }
}
