package be.uantwerpen.labplanner.Repository;


import be.uantwerpen.labplanner.LabplannerApplication;
import be.uantwerpen.labplanner.common.model.users.Privilege;
import be.uantwerpen.labplanner.common.model.users.Role;
import be.uantwerpen.labplanner.common.model.users.Role;
import be.uantwerpen.labplanner.common.repository.users.PrivilegeRepository;
import be.uantwerpen.labplanner.common.repository.users.RoleRepository;
import be.uantwerpen.labplanner.common.repository.users.RoleRepository;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = LabplannerApplication.class)
@WebAppConfiguration
public class RoleRepositoryTests {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;



    @Test
    public void testRoleSave(){
        //create Role
        Role role = new Role();
        role.setName("Tester");

        long precount = roleRepository.count();

        Privilege p1 = new Privilege("logon");
        privilegeRepository.save(p1);
        List<Privilege> privs = new ArrayList<Privilege>();
        privs.add(p1);
        role.setPrivileges(privs);

        //save product & varify id
        assertNull(role.getId());
        roleRepository.save(role);
        assertNotNull(role.getId());

        //retrieve Role from database.
        Role fetchedRole = roleRepository.findById(role.getId()).orElse(null);
        assertNotNull(fetchedRole);

        //and the fetched Role should equal the real Role
        assertEquals(fetchedRole.getName(),role.getName());
        assertEquals(fetchedRole.getId(),role.getId());
        assertEquals(fetchedRole.getPrivileges().size(),role.getPrivileges().size());

        //update name & desciption
        fetchedRole.setName("Test_updated");
        Privilege p2 = new Privilege("test");
        privilegeRepository.save(p2);
        privs.add(p2);
        fetchedRole.setPrivileges(privs);


        roleRepository.save(fetchedRole);

        Role fetchedUpdated = roleRepository.findById(fetchedRole.getId()).orElse(null);

        assertEquals(fetchedUpdated.getName(),fetchedRole.getName());
        assertEquals(fetchedRole.getPrivileges().size(),2);
        assertEquals(fetchedUpdated.getPrivileges().size(),fetchedRole.getPrivileges().size());
        assertEquals(fetchedUpdated.getPrivileges().size(),2);


        assertEquals(roleRepository.count(),precount+1);

        int count = 0;
        for (Role p : roleRepository.findAll()) {
            count++;
        }

        assertEquals(count,precount+1);

        //delete user and check if still in database
        roleRepository.deleteById(fetchedUpdated.getId());

        assertEquals(roleRepository.count(),precount);
        assertNull(roleRepository.findById(fetchedUpdated.getId()).orElse(null));

        //check for delete if nnot exists
        assertThrows(EmptyResultDataAccessException.class,()->{roleRepository.deleteById(fetchedUpdated.getId());});

    }




}
