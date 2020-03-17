package be.uantwerpen.labplanner.Model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;

public class Role extends be.uantwerpen.labplanner.common.model.users.Role implements GrantedAuthority {


    public Role(String name) {
        super(name);
    }

    @Override
    public String getAuthority() {
        return getName();
    }
}
