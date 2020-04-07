package be.uantwerpen.labplanner.Repository;


import be.uantwerpen.labplanner.LabplannerApplication;
import be.uantwerpen.labplanner.common.model.users.Privilege;
import be.uantwerpen.labplanner.common.repository.users.PrivilegeRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = LabplannerApplication.class)
@WebAppConfiguration
public class PrivilegeRepositoryTests {

    @Autowired
    private PrivilegeRepository privilegeRepository;


    @Test
    public void testPrivilegeSave(){
        //create privilege
        Privilege privilege = new Privilege();
        privilege.setName("logon");

        long precount = privilegeRepository.count();
        //save product & varify id
        assertNull(privilege.getId());
        privilegeRepository.save(privilege);
        assertNotNull(privilege.getId());

        //retrieve privilege from database.
        Privilege fetchedPrivilege = privilegeRepository.findById(privilege.getId()).orElse(null);
        assertNotNull(fetchedPrivilege);

        //and the fetched privilege should equal the real privilege
        assertEquals(fetchedPrivilege.getName(),privilege.getName());
        assertEquals(fetchedPrivilege.getId(),privilege.getId());
        assertEquals(fetchedPrivilege.getDescription(),privilege.getDescription());

        //update name & desciption
        fetchedPrivilege.setName("logon_update");
        fetchedPrivilege.setDescription("This is the description");
        privilegeRepository.save(fetchedPrivilege);

        Privilege fetchedUpdated = privilegeRepository.findById(fetchedPrivilege.getId()).orElse(null);

        assertEquals(fetchedUpdated.getName(),fetchedPrivilege.getName());
        assertEquals(fetchedUpdated.getDescription(),fetchedPrivilege.getDescription());


        assertEquals(privilegeRepository.count(),precount+1);

        int count = 0;
        for (Privilege p : privilegeRepository.findAll()) {
            count++;
        }

        assertEquals(count,precount+1);

        //delete user and check if still in database
        privilegeRepository.deleteById(fetchedUpdated.getId());

        //check for delete if nnot exists
        assertThrows(EmptyResultDataAccessException.class,()->{privilegeRepository.deleteById(fetchedUpdated.getId());});


        assertEquals(privilegeRepository.count(),precount);
        assertNull(privilegeRepository.findById(fetchedUpdated.getId()).orElse(null));
    }




}
