package be.uantwerpen.labplanner.Formatter;


import be.uantwerpen.labplanner.Model.Composition;
import be.uantwerpen.labplanner.Model.DeviceType;
import be.uantwerpen.labplanner.Model.Mixture;
import be.uantwerpen.labplanner.Repository.CompositionRepository;
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
public class CompositionFormatter implements Formatter<Composition> {
    @Autowired
    private CompositionRepository compositionRepository;

    public Composition parse(final String text, final Locale locale)
            throws ParseException {
        if (text != null && !text.isEmpty())
            return compositionRepository.findById(new Long(text)).orElse(null);
        else return null;
    }

    public String print(final Composition object, final Locale locale)
    { return (object != null ? object.getId().toString() : "");
    }

}