package be.uantwerpen.labplanner.Formatter;


import be.uantwerpen.labplanner.Model.DeviceType;
import be.uantwerpen.labplanner.Model.Mixture;
import be.uantwerpen.labplanner.Repository.DeviceTypeRepository;
import be.uantwerpen.labplanner.Repository.MixtureRepository;
import be.uantwerpen.labplanner.common.model.stock.Tag;
import be.uantwerpen.labplanner.common.repository.stock.TagRepository;
import com.zaxxer.hikari.metrics.micrometer.MicrometerMetricsTracker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

@Component
public class MixtureFormatter implements Formatter<Mixture> {
    @Autowired
    private MixtureRepository mixtureRepository;

    public Mixture parse(final String text, final Locale locale)
            throws ParseException {
        if (text != null && !text.isEmpty())
            return mixtureRepository.findById(new Long(text)).orElse(null);
        else return null;
    }

    public String print(final Mixture object, final Locale locale)
    { return (object != null ? object.getId().toString() : "");
    }

}