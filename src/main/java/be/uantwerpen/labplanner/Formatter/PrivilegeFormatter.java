package be.uantwerpen.labplanner.Formatter;

import be.uantwerpen.labplanner.common.model.users.Privilege;

import be.uantwerpen.labplanner.common.repository.users.PrivilegeRepository;
import be.uantwerpen.labplanner.common.repository.users.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

@Component
public class PrivilegeFormatter implements Formatter<Privilege> {
    @Autowired
    private PrivilegeRepository privilegeRepository;
    public Privilege parse(final String text, final Locale locale)
            throws ParseException {
        if (text != null && !text.isEmpty())
            return privilegeRepository.findById(new Long(text)).orElse(null);
        else return null;
    }
    public String print(final Privilege object, final Locale locale)
    { return (object != null ? object.getId().toString() : "");
    }
}