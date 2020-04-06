package be.uantwerpen.labplanner.Model;

import be.uantwerpen.labplanner.Repository.DeviceInformationRepository;
import be.uantwerpen.labplanner.Repository.DeviceRepository;
import be.uantwerpen.labplanner.Repository.DeviceTypeRepository;

import be.uantwerpen.labplanner.Repository.StepRepository;
import be.uantwerpen.labplanner.Repository.*;
import be.uantwerpen.labplanner.common.model.stock.Product;
import be.uantwerpen.labplanner.common.model.stock.Tag;
import be.uantwerpen.labplanner.common.model.stock.Unit;
import be.uantwerpen.labplanner.common.model.users.Privilege;
import be.uantwerpen.labplanner.common.model.users.Role;
import be.uantwerpen.labplanner.common.model.users.User;
import be.uantwerpen.labplanner.common.repository.stock.ProductRepository;
import be.uantwerpen.labplanner.common.repository.stock.TagRepository;
import be.uantwerpen.labplanner.common.repository.users.PrivilegeRepository;
import be.uantwerpen.labplanner.common.repository.users.RoleRepository;
import be.uantwerpen.labplanner.common.repository.users.UserRepository;
import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service

public class DatabaseLoader {

    private final PrivilegeRepository privilegeRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final DeviceTypeRepository deviceTypeRepository;
    private final DeviceRepository deviceRepository;
    private final DeviceInformationRepository deviceInformationRepository;
    private final ProductRepository productRepository;
    private final TagRepository tagRepository;
    private final StepRepository stepRepository;
    private final CompositionRepository compositionRepository;
    private final MixtureRepository mixtureRepository;
    private final RelationRepository relationRepository;

    @Autowired
    public DatabaseLoader(PrivilegeRepository privilegeRepository, RoleRepository roleRepository, UserRepository userRepository,DeviceTypeRepository deviceTypeRepository, DeviceRepository deviceRepository, DeviceInformationRepository deviceInformationRepository, ProductRepository productRepository, TagRepository tagRepository, StepRepository stepRepository, CompositionRepository compositionRepository, MixtureRepository mixtureRepository, RelationRepository relationRepository) {



        this.privilegeRepository = privilegeRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        //DeviceRepositories
        this.deviceTypeRepository = deviceTypeRepository;
        this.deviceRepository = deviceRepository;

        this.stepRepository= stepRepository;

        this.deviceInformationRepository = deviceInformationRepository;
        //ProductRepositories
        this.productRepository = productRepository;
        this.tagRepository = tagRepository;
        //MixtureRepositories
        this.mixtureRepository = mixtureRepository;
        this.compositionRepository = compositionRepository;
        this.relationRepository = relationRepository;

    }

    @PostConstruct
    private void initDatabase() throws IOException {

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
        Privilege p27 = new Privilege("Console Access");
        privilegeRepository.save(p27);

        //Create all the roles
        Role bachelorstudent = new Role("Bachelorstudent");
        Role masterstudent = new Role("Masterstudent");
        Role researcher = new Role("Researcher");
        Role administrator = new Role("Administrator");

        //Create a list of privileges
        // ###########################//
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

        // ###########################//
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

        // ###########################//
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

        // ###########################//
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

        //Console privilege
        privileges.add(p27);

        administrator.setPrivileges(privileges);
        roleRepository.save(administrator);


        User u1 = new User("Cedric","PW","","Cedric","Plouvier","20152267","","",null,null,null);
        User u2 = new User("Ruben","PW","","Ruben","Joosen","20164473","","",null,null,null);
        User u3 = new User("Jaimie","PW","","Jaimie","Vranckx","20155797","","",null,null,null);
        User u4 = new User("Ali","PW","","Ali","Amir","20163446","","",null,null,null);
        User u5 = new User("Timo","PW","","Timo","Nelen","S0162117","","",null,null,null);
        User u6 = new User("Ondrej","PW","","Ondrej","Bures","20160002","","",null,null,null);
        User u7 = new User("Bachelor","PW","","Bach","Student","20170001","","",null,null,null);
        User u8 = new User("Master","PW","","Mas","Student","20160009","","",null,null,null);
        User u9 = new User("Researcher","PW","","Researcher","Developper","20100001","","",null,null,null);
        User admin = new User("admin","admin","","admin","admin",null,"","",null,null,null);
        //Set<Role> roles = new HashSet<>();
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
        admin.setRoles(roles);
        userRepository.save(admin);

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

//        //add relations
//        Relation relation = new Relation("testrelation");
//        relation.setResearcher(u9);
//        List<User> students = new ArrayList<>();
//        students.add(u7);
//        students.add(u8);
//
//        relation.setStudents(students);
//        Relation f =  relationRepository.save(relation);
//        Relation fetch = relationRepository.findById((long) 42).orElse(null);
//
//        relation.setResearcher(u1);
//        relationRepository.save(relation);

        //Create all Device Types
        ArrayList<DeviceType> deviceTypes = new ArrayList<>();
        DeviceType t1 = new DeviceType("Autosaw",true);
        t1.setColor("#FF00FF");
        deviceTypes.add(t1);
        DeviceType t2 = new DeviceType("Balance",false);
        t2.setColor("#FFAFAF");
        deviceTypes.add(t2);
        DeviceType t3 = new DeviceType("Big Mixer",true);
        t3.setColor("#00FF00");
        deviceTypes.add(t3);
        DeviceType t4 = new DeviceType("Caliper",true);
        t4.setColor("#000000");
        deviceTypes.add(t4);
        DeviceType t5 = new DeviceType("Cooling chamber",true);
        t5.setColor("#FFFF00");
        deviceTypes.add(t5);
        DeviceType t6 = new DeviceType("Gyratory",false);
        t6.setColor("#00FFFF");
        deviceTypes.add(t6);
        DeviceType t7 = new DeviceType("Oven",true);
        t7.setColor("#404040");
        deviceTypes.add(t7);
        DeviceType t8 = new DeviceType("Plate Compactor",false);
        t8.setColor("#FF0000");
        deviceTypes.add(t8);
        DeviceType t9 = new DeviceType("Small Mixer",true);
        t9.setColor("#FFC800");
        deviceTypes.add(t9);
        DeviceType t10 = new DeviceType("SVM Setup",false);
        t10.setColor("#808080");
        deviceTypes.add(t10);
        DeviceType t11 = new DeviceType("Uniframe",false);
        t11.setColor("#FFFFFF");
        deviceTypes.add(t11);
        DeviceType t12 = new DeviceType("Vacuum Setup",true);
        t12.setColor("#0000FF");
        deviceTypes.add(t12);
        DeviceType t13 = new DeviceType("Water Bath",false);
        t13.setColor("#222222");
        deviceTypes.add(t13);
        DeviceType t14 = new DeviceType("Wheel Tracking Test",true);
        t14.setColor("#C0C0C0");
        deviceTypes.add(t14);

        //Add random information for default information types
        //Lorem ipsum generator for random information blocks
        Lorem lorem = LoremIpsum.getInstance();

        for (DeviceType devicetype : deviceTypes) {
            List<DeviceInformation> deviceinformations = new ArrayList<DeviceInformation>();

            //Add a new block for each default information type (Maintenance , Calibration ...)
            for (int current = 0; current < DeviceType.getDefaultInformationtypes().size(); current++) {
                //Add new device information block
                DeviceInformation i = new DeviceInformation(DeviceType.getDefaultInformationtypes().get(current), lorem.getWords(20));

                //Adding some files for Oven type , files were already loaded into the correct directory
                if(devicetype.getDeviceTypeName().equals("Oven")){
                    if(current==0){
                        i.addFile("placeholder.pdf");
                    }else if(current==2){
                        i.addFile("placeholder.xlsx");
                        i.addFile("placeholder.jpg");
                    }
                }
                deviceinformations.add(i);
                deviceInformationRepository.save(i);
            }

            //Add an extra block of info for oven to show full possibilities
            if(devicetype.getDeviceTypeName().equals("Oven")) {
                DeviceInformation i = new DeviceInformation("Other", lorem.getWords(10));
                deviceinformations.add(i);
                deviceInformationRepository.save(i);
            }
                devicetype.setDeviceInformation(deviceinformations);
        }

        //Set the oven profile pic , the picture is already placed in the root directory /upload-dir/images where all device pictures are kept
        t7.setDevicePictureName("Oven.jpg");

        //Save all devicetypes
        deviceTypeRepository.save(t1);
        deviceTypeRepository.save(t2);
        deviceTypeRepository.save(t3);
        deviceTypeRepository.save(t4);
        deviceTypeRepository.save(t5);
        deviceTypeRepository.save(t6);
        deviceTypeRepository.save(t7);
        deviceTypeRepository.save(t8);
        deviceTypeRepository.save(t9);
        deviceTypeRepository.save(t10);
        deviceTypeRepository.save(t11);
        deviceTypeRepository.save(t12);
        deviceTypeRepository.save(t13);
        deviceTypeRepository.save(t14);
        //Add devices for some device types
        Device d1 = new Device("Autosaw 1",t1);
        deviceRepository.save(d1);
        Device d2 = new Device("Autosaw 2",t1);
        deviceRepository.save(d2);
        Device d3 = new Device("Uniframe 1",t11);
        deviceRepository.save(d3);
        Device d4 = new Device("Vacuum Setup 1",t12);
        deviceRepository.save(d4);
        Device d5 = new Device("Water Bath 1",t13);
        deviceRepository.save(d5);
        Device d6 = new Device("Wheel Tracking Test 1",t14);
        deviceRepository.save(d6);
        Device d7 = new Device("Oven 1",t7);
        d7.setComment("Perfect oven to bake a pizza in your spare times");
        deviceRepository.save(d7);
        Device d8 = new Device("Oven 2",t7);
        deviceRepository.save(d8);
        Device d9 = new Device("Oven 3",t7);
        deviceRepository.save(d9);



        //create some products
        Tag tag1 = new Tag("Aggregates");
        List<Tag> tags1 = new ArrayList<>();
        tags1.add(tag1);
        tagRepository.save(tag1);
        Tag tag2 = new Tag("Bitumen");
        List<Tag> tags2 = new ArrayList<>();
        tags2.add(tag2);
        tagRepository.save(tag2);
        Tag tag3 = new Tag("Consumables");
        List<Tag> tags3 = new ArrayList<>();
        tags3.add(tag3);
        tagRepository.save(tag3);
        Tag tag4 = new Tag("Other");
        List<Tag> tags4 = new ArrayList<>();
        tags4.add(tag4);
        tagRepository.save(tag4);
        Product pr1 = new Product("placeholder1",lorem.getWords(20),1.0, 2000.0, 200.0, 1.0, Unit.KILOGRAM, "locatie2", lorem.getWords(8), 5L,5L, LocalDateTime.now(), LocalDateTime.now(), tags3);
        productRepository.save(pr1);
        Product pr2 = new Product("placeholder2",lorem.getWords(20),1.0, 2000.0, 200.0, 1.0, Unit.KILOGRAM, "locatie2", lorem.getWords(8), 5L,5L, LocalDateTime.now(), LocalDateTime.now(), tags1);
        productRepository.save(pr2);
        Product pr3 = new Product("placeholder3",lorem.getWords(20),1.0, 2000.0, 200.0, 1.0, Unit.KILOGRAM, "locatie2", lorem.getWords(8), 5L,5L, LocalDateTime.now(), LocalDateTime.now(), tags1);
        productRepository.save(pr3);
        Product pr4 = new Product("placeholder4",lorem.getWords(20),1.0, 2000.0, 200.0, 1.0, Unit.KILOGRAM, "locatie2", lorem.getWords(8), 5L,5L, LocalDateTime.now(), LocalDateTime.now(), tags3);
        productRepository.save(pr4);
        Product pr5 = new Product("placeholder5",lorem.getWords(20),1.0, 1000.0, 100.0, 1.0, Unit.KILOGRAM, "locatie2", lorem.getWords(8), 5L,5L, LocalDateTime.now(), LocalDateTime.now(), tags2);
        productRepository.save(pr5);
        Product pr6 = new Product("placeholder6",lorem.getWords(20),1.0, 500.0, 100.0, 1.0, Unit.KILOGRAM, "locatie2", lorem.getWords(8), 5L,5L, LocalDateTime.now(), LocalDateTime.now(), tags4);
        productRepository.save(pr6);
        Product pr7 = new Product("placeholder7",lorem.getWords(20),1.0, 200.0, 25.0, 1.0, Unit.LITRE, "locatie2", lorem.getWords(8), 5L,5L, LocalDateTime.now(), LocalDateTime.now(), tags4);
        productRepository.save(pr7);
        Product pr8 = new Product("placeholder8",lorem.getWords(20),1.0, 90.0, 1.0, 1.0, Unit.UNIT, "locatie2", lorem.getWords(8), 5L,5L, LocalDateTime.now(), LocalDateTime.now(), tags4);
        productRepository.save(pr8);
        Step s1= new Step(u1,d1,"2020-03-18","2020-03-18","11:00","12:00");
        stepRepository.save(s1);
        Step s2= new Step(u1,d7,"2020-03-17","2020-03-17","08:00","18:00");
        stepRepository.save(s2);
        Step s3= new Step(u4,d1,"2020-03-16","2020-03-16","14:00","16:00");
        stepRepository.save(s3);
        Step s4= new Step(u4,d7,"2020-03-15","2020-03-15","16:00","18:00");
        stepRepository.save(s4);
        Step s5= new Step(u5,d9,"2020-03-19","2020-03-19","13:00","18:00");
        stepRepository.save(s5);
        //Create compositions

        //APT-C ingredients
        Composition c1 = new Composition(46.67,pr1);
        Composition c2 = new Composition(10.10, pr2);
        Composition c3 = new Composition(8.29,pr3);
        Composition c4 = new Composition(23.89, pr4);
        Composition c5 = new Composition(2.45,pr5);
        Composition c6 = new Composition(8.60, pr6);
        Composition c7 = new Composition(6.50, pr7);
        compositionRepository.save(c1);
        compositionRepository.save(c2);
        compositionRepository.save(c3);
        compositionRepository.save(c4);
        compositionRepository.save(c5);
        compositionRepository.save(c6);
        compositionRepository.save(c7);
        List<Composition> mix1= new ArrayList<>();
        mix1.add(c1);mix1.add(c2);mix1.add(c3);mix1.add(c4);mix1.add(c5);mix1.add(c6);mix1.add(c7);

        //Ab-4C ingredients
        Composition c11 = new Composition(20.72,pr1);
        Composition c12 = new Composition(14.20, pr2);
        Composition c13 = new Composition(21.24,pr3);
        Composition c14 = new Composition(23.84, pr4);
        Composition c15 = new Composition(12.91,pr5);
        Composition c16 = new Composition(7.10, pr6);
        Composition c17 = new Composition(6.50, pr7);
        compositionRepository.save(c11);
        compositionRepository.save(c12);
        compositionRepository.save(c13);
        compositionRepository.save(c14);
        compositionRepository.save(c15);
        compositionRepository.save(c16);
        compositionRepository.save(c17);
        List<Composition> mix2= new ArrayList<>();
        mix2.add(c11);mix2.add(c12);mix2.add(c13);mix2.add(c14);mix2.add(c15);mix2.add(c16);mix2.add(c17);

        //SMA ingredients
        Composition c21 = new Composition(61.70,pr1);
        Composition c22 = new Composition(11.20, pr2);
        Composition c24 = new Composition(17.80, pr4);
        Composition c25 = new Composition(9.30,pr5);
        Composition c27 = new Composition(6.90, pr7);
        compositionRepository.save(c21);
        compositionRepository.save(c22);
        compositionRepository.save(c24);
        compositionRepository.save(c25);
        compositionRepository.save(c27);
        List<Composition> mix3= new ArrayList<>();
        mix3.add(c21);mix3.add(c22);mix3.add(c24);mix3.add(c25);mix3.add(c27);


        //create mixtures and save them
        Mixture m1 = new Mixture("APT-C", mix1);
        mixtureRepository.save(m1);
        Mixture m2 = new Mixture("AB-4C", mix2);
        mixtureRepository.save(m2);
        Mixture m3 = new Mixture("SMA", mix3);
        mixtureRepository.save(m3);







    }
}
