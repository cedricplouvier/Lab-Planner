package be.uantwerpen.labplanner.Repository;


import be.uantwerpen.labplanner.LabplannerApplication;
import be.uantwerpen.labplanner.common.model.users.Role;
import be.uantwerpen.labplanner.common.model.users.User;
import be.uantwerpen.labplanner.common.model.users.User;
import be.uantwerpen.labplanner.common.repository.users.*;
import be.uantwerpen.labplanner.common.repository.users.UserRepository;
import be.uantwerpen.labplanner.common.repository.users.UserRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = LabplannerApplication.class)
@WebAppConfiguration
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testWrongUserSave(){
        // A test wchich checks for wrong input in the repository. Only non Null & unique names can be saved.
        //create User
        User user = new User(null,null,"","","","","","",null,null,null);
        assertThrows(DataIntegrityViolationException.class, () ->{userRepository.save(user);});
        user.setUsername("test");
        assertThrows(DataIntegrityViolationException.class, () ->{userRepository.save(user);});
        user.setUsername(null);
        user.setPassword("test");
        assertThrows(DataIntegrityViolationException.class, () ->{userRepository.save(user);});
        user.setUsername("test");
        assertDoesNotThrow(() ->{userRepository.save(user);});

        //non unique name
        User u2 = new User("test","pw");
        assertThrows(DataIntegrityViolationException.class, () ->{userRepository.save(u2);});

        //unique name can be saved.
        u2.setUsername("test2");
        assertDoesNotThrow(()->{userRepository.save(u2);});

        User user3 = new User("u3","u3","","","","","","",null,null,null);

        userRepository.save(user3);

        //delete once, is ok
        assertDoesNotThrow(()->{userRepository.deleteById(user3.getId());});

        //delete twice gives error
        assertThrows(EmptyResultDataAccessException.class,()->{userRepository.deleteById(user3.getId());});
    }

    @Test
    public void testUserSave(){
        //create User
        User user = new User(null,null,"","","","","","",null,null,null);
        long precount = userRepository.count();

        Role p1 = new Role("tester");
        user.setPassword("test");
        user.setUsername("test");
        assertNull(user.getId());
        roleRepository.save(p1);
        Set<Role> roles = new HashSet<Role>();
        roles.add(p1);
        user.setRoles(roles);

        //save product & varify id

        userRepository.save(user);
        assertNotNull(user.getId());

        //retrieve User from database.
        User fetchedUser = userRepository.findById(user.getId()).orElse(null);
        assertNotNull(fetchedUser);

        //and the fetched User should equal the real User
        assertEquals(fetchedUser.getUsername(),user.getUsername());
        assertEquals(fetchedUser.getId(),user.getId());
        assertEquals(fetchedUser.getRoles().size(),user.getRoles().size());
        assertEquals(fetchedUser.getRoles().size(),1);
        assertEquals(fetchedUser.getPassword(),user.getPassword());

        //update name & desciption
        fetchedUser.setUsername("Test_updated");
        fetchedUser.setPassword("pw_updted");
        Role p2 = new Role("admin");
        roleRepository.save(p2);
        roles.add(p2);
        fetchedUser.setRoles(roles);


        userRepository.save(fetchedUser);

        User fetchedUpdated = userRepository.findById(fetchedUser.getId()).orElse(null);

        assertEquals(fetchedUpdated.getUsername(),fetchedUser.getUsername());
        assertEquals(fetchedUser.getRoles().size(),2);
        assertEquals(fetchedUpdated.getRoles().size(),fetchedUser.getRoles().size());


        assertEquals(userRepository.count(),precount+1);

        int count = 0;
        for (User p : userRepository.findAll()) {
            count++;
        }

        assertEquals(count,precount+1);

        //delete user and check if still in database
        userRepository.deleteById(fetchedUpdated.getId());

        assertEquals(userRepository.count(),precount);
        assertNull(userRepository.findById(fetchedUpdated.getId()).orElse(null));
    }




}
