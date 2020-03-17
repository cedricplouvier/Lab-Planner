package be.uantwerpen.labplanner.Service;

import be.uantwerpen.labplanner.Controller.HomeController;
import be.uantwerpen.labplanner.common.model.users.Privilege;
import be.uantwerpen.labplanner.common.model.users.Role;
import be.uantwerpen.labplanner.common.model.users.User;
import be.uantwerpen.labplanner.common.repository.users.RoleRepository;
import be.uantwerpen.labplanner.common.repository.users.UserRepository;
import be.uantwerpen.labplanner.common.service.users.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class MyUserDetailsService implements UserDetailsService {

        Logger logger = LoggerFactory.getLogger(MyUserDetailsService.class);

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private UserService userService;

        @Autowired
        private RoleRepository roleRepository;

        @Override
        public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
            UserDetails ud = null;
            for (User user : userService.findAll()){
                if (userName.equals(user.getUsername())){
                    Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

                    /*for (Role role : user.getRoles()) {
                        for (Privilege perm : role.getPrivileges()){
                            authorities.add(new SimpleGrantedAuthority(perm.getName()));
                        }
                    }*/
                    for (Role role : user.getRoles()) {
                            authorities.add(new SimpleGrantedAuthority(role.getName()));
                            logger.info("authorities added");
                    }
                    ud = new be.uantwerpen.labplanner.common.model.users.User(userName, user.getPassword(), user.getEmail(),user.getFirstName(),user.getLastName(),
                            user.getUaNumber(),user.getTelephone(),user.getLocation(),user.getRoles(),user.getDateCreated(),user.getUpdateDateTime());
                            /*org.springframework.security.core.userdetails.User(userName,
                            user.getPassword(), true, true, true, true,authorities);*/
                }
            }
            if (ud == null) throw new UsernameNotFoundException("No user with username '" + userName + "' found!");
            return ud;
        }

        /*private Collection<? extends GrantedAuthority> getAuthorities(
                Collection<Role> roles) {

            return getGrantedAuthorities(getPrivileges(roles));
        }*/

        private Collection<? extends GrantedAuthority> getAuthorities(Collection<Role> roles) {
            return getGrantedAuthorities(getRoles(roles));
        }

        /*private List<String> getPrivileges(Collection<Role> roles) {

            List<String> privileges = new ArrayList<>();
            List<Privilege> collection = new ArrayList<>();
            for (Role role : roles) {
                collection.addAll(role.getPrivileges());
            }
            for (Privilege item : collection) {
                privileges.add(item.getName());
            }
            return privileges;
        }*/

        private List<String> getRoles(Collection<Role> roles) {

            List<String> roleNames = new ArrayList<>();
            for (Role role : roles) {
                roleNames.add(role.getName());
            }
            return roleNames;
        }

        /*private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
            List<GrantedAuthority> authorities = new ArrayList<>();
            for (String privilege : privileges) {
                authorities.add(new SimpleGrantedAuthority(privilege));
            }
            return authorities;
        }*/
        private List<GrantedAuthority> getGrantedAuthorities(List<String> roles) {
            List<GrantedAuthority> authorities = new ArrayList<>();
            for (String role : roles) {
                authorities.add(new SimpleGrantedAuthority(role));
            }
            return authorities;
        }
}
