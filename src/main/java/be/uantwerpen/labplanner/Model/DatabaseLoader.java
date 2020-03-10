package be.uantwerpen.labplanner.Model;

import be.uantwerpen.labplanner.common.model.users.Privilege;
import be.uantwerpen.labplanner.common.model.users.Role;
import be.uantwerpen.labplanner.common.model.users.User;
import be.uantwerpen.labplanner.common.repository.users.PrivilegeRepository;
import be.uantwerpen.labplanner.common.repository.users.RoleRepository;
import be.uantwerpen.labplanner.common.repository.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Profile("default")

public class DatabaseLoader {

    private final PrivilegeRepository privilegeRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Autowired
    public DatabaseLoader(PrivilegeRepository privilegeRepository,
                          RoleRepository roleRepository, UserRepository userRepository) {
        this.privilegeRepository = privilegeRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @PostConstruct
    private void initDatabase() {

        //Create all the privileges
        Privilege p1 = new Privilege("logon");
        privilegeRepository.save(p1);
        Privilege p2 = new Privilege("Planning - Book step/experiment");
        privilegeRepository.save(p2);
        Privilege p3 = new Privilege("Planning - Delete step/experiment own");
        privilegeRepository.save(p3);
        Privilege p4 = new Privilege("Planning - Delete step/experiment own/promotor");
        privilegeRepository.save(p4);
        Privilege p5 = new Privilege("Planning - Delete step/experiment all");
        privilegeRepository.save(p5);
        Privilege p6 = new Privilege("Planning - Adjust step/experiment own");
        privilegeRepository.save(p6);
        Privilege p7 = new Privilege("Planning - Adjust step/experiment own/promotor");
        privilegeRepository.save(p7);
        Privilege p8 = new Privilege("Planning - Adjust step/experiment all");
        privilegeRepository.save(p8);
        Privilege p9 = new Privilege("Planning - Make new step");
        privilegeRepository.save(p9);
        Privilege p10 = new Privilege("Planning - Make new experiment");
        privilegeRepository.save(p10);
        Privilege p11 = new Privilege("Planning - Start within office hours");
        privilegeRepository.save(p11);
        Privilege p12 = new Privilege("Planning - Start outside office hours");
        privilegeRepository.save(p12);
        Privilege p13 = new Privilege("Planning - Overview");
        privilegeRepository.save(p13);
        Privilege p14 = new Privilege("Stock - Aggregates + Bitumen Read only - Basic");
        privilegeRepository.save(p14);
        Privilege p15 = new Privilege("Stock - Aggregates + Bitumen Read only - Advanced");
        privilegeRepository.save(p15);
        Privilege p16 = new Privilege("Stock - Aggregates + Bitumen Modify - Advanced");
        privilegeRepository.save(p16);
        Privilege p17= new Privilege("Stock - Modify - All");
        privilegeRepository.save(p17);
        Privilege p18 = new Privilege("Stock - Consumables + Other Read only - Advanced");
        privilegeRepository.save(p18);
        Privilege p19 = new Privilege("User Management");
        privilegeRepository.save(p19);
        Privilege p20 = new Privilege("Database - Read only - Basic");
        privilegeRepository.save(p20);
        Privilege p21 = new Privilege("Database - Modify - Advanced");
        privilegeRepository.save(p21);
        Privilege p22 = new Privilege("Database - Modify - All");
        privilegeRepository.save(p22);
        Privilege p23 = new Privilege("Reports - Modify - Advanced");
        privilegeRepository.save(p23);
        Privilege p24 = new Privilege("Reports - Modify - All");
        privilegeRepository.save(p24);
        Privilege p25 = new Privilege("Device - Read only - Basic");
        privilegeRepository.save(p25);
        Privilege p26 = new Privilege("Device - Modify - All");
        privilegeRepository.save(p26);

        //Create all the roles
        Role bachelorstudent = new Role("Bachelorstudent");
        Role masterstudent = new Role("Masterstudent");
        Role researcher = new Role("Researcher");
        Role administrator = new Role("Administrator");

        //Create a list of privileges

        //Bachelor student
        List<Privilege> privileges = new ArrayList<Privilege>();

        //logon privilege
        privileges.add(p1);

        //planner privileges
        privileges.add(p2);
        privileges.add(p3);
        privileges.add(p6);
        privileges.add(p11);
        privileges.add(p13);

        //stock privileges
        privileges.add(p14);

        //database privileges
        privileges.add(p20);

        //device privileges
        privileges.add(p25);

        bachelorstudent.setPrivileges(privileges);
        roleRepository.save(bachelorstudent);

        //Master student
        privileges = new ArrayList<Privilege>();

        //logon privilege
        privileges.add(p1);

        //Planner privileges
        privileges.add(p2);
        privileges.add(p3);
        privileges.add(p6);
        privileges.add(p11);
        privileges.add(p12);
        privileges.add(p13);

        //Stock privileges
        privileges.add(p15);

        //database privileges
        privileges.add(p20);

        //device privileges
        privileges.add(p25);
        masterstudent.setPrivileges(privileges);
        roleRepository.save(masterstudent);

        //Researcher
        privileges = new ArrayList<Privilege>();

        //logon privilege
        privileges.add(p1);

        //Planner privileges
        privileges.add(p2);
        privileges.add(p4);
        privileges.add(p7);
        privileges.add(p9);
        privileges.add(p11);
        privileges.add(p12);
        privileges.add(p13);

        // Stock privileges
        privileges.add(p16);
        privileges.add(p18);

        // Database privileges
        privileges.add(p21);
        privileges.add(p23);

        // Device privileges
        privileges.add(p25);
        researcher.setPrivileges(privileges);
        roleRepository.save(researcher);

        // Admin
        privileges = new ArrayList<Privilege>();

        //logon privilege
        privileges.add(p1);

        //Planner privileges
        privileges.add(p2);
        privileges.add(p5);
        privileges.add(p8);
        privileges.add(p9);
        privileges.add(p10);
        privileges.add(p11);
        privileges.add(p12);
        privileges.add(p13);

        //Stock privileges
        privileges.add(p17);

        // User management privilege
        privileges.add(p19);

        // Database privileges
        privileges.add(p22);
        privileges.add(p24);

        // Device privileges
        privileges.add(p26);

        administrator.setPrivileges(privileges);
        roleRepository.save(administrator);


        User u1 = new User("Cedric","PW");
        User u2 = new User("Ruben","PW");
        User u3 = new User("Jaimie","PW");
        User u4 = new User("Ali","PW");
        User u5 = new User("Timo","PW");
        User u6 = new User("Ondrej","PW");
        User u7 = new User("Bachelor","PW");
        User u8 = new User("Master","PW");
        User u9 = new User("Researcher","PW");

        Set<Role> roles = new HashSet<>();
        roles.add(administrator);
        //set 5 admins
        u1.setRoles(roles);
        userRepository.save(u1);
        u2.setRoles(roles);
        userRepository.save(u2);
        u3.setRoles(roles);
        userRepository.save(u3);
        u4.setRoles(roles);
        userRepository.save(u4);
        u5.setRoles(roles);
        userRepository.save(u5);
        u6.setRoles(roles);
        userRepository.save(u6);

        //Bachelor student
        roles = new HashSet<>();
        roles.add(bachelorstudent);
        u7.setRoles(roles);
        userRepository.save(u7);

        // Master student
        roles = new HashSet<>();
        roles.add(masterstudent);
        u8.setRoles(roles);
        userRepository.save(u8);

        // Researcher
        roles = new HashSet<>();
        roles.add(researcher);
        u9.setRoles(roles);
        userRepository.save(u9);
    }
}
