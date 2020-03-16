package be.uantwerpen.labplanner.Formatter;

import be.uantwerpen.labplanner.common.model.users.Role;

import be.uantwerpen.labplanner.common.repository.users.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

@Component
public class RoleFormatter implements Formatter<Role> {
    @Autowired
    private RoleRepository roleRepository;
    public Role parse(final String text, final Locale locale)
            throws ParseException {
        if (text != null && !text.isEmpty())
            return roleRepository.findById(new Long(text)).orElse(null);
        else return null;
    }
    public String print(final Role object, final Locale locale)
    { return (object != null ? object.getId().toString() : "");
    }
}