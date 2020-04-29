package be.uantwerpen.labplanner.Service;

import be.uantwerpen.labplanner.common.model.users.User;
import be.uantwerpen.labplanner.common.repository.users.UserRepository;
import be.uantwerpen.labplanner.common.service.SecurityService;
import be.uantwerpen.labplanner.common.service.users.PrivilegeService;
import be.uantwerpen.labplanner.common.service.users.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class NewSecurityService implements UserDetailsService {
    @Autowired
    private UserService userService;
    @Autowired
    private PrivilegeService privilegeService;
    @Autowired
    private UserRepository userRepository;
    private static Logger logger = LoggerFactory.getLogger(NewSecurityService.class);

    public NewSecurityService()  {
    }

    public User loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = (User)this.userService.findByEmail(email.toLowerCase()).orElse( null);
        if (user != null) {
            this.userRepository.save(user);
            logger.info("Attempting to login user " + user.getId() + "");
            return user;
        } else {
            throw new UsernameNotFoundException("No user with email '" + email + "' found!");
        }
    }
}
