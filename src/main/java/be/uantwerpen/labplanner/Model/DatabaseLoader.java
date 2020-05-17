package be.uantwerpen.labplanner.Model;

import be.uantwerpen.labplanner.Repository.*;
import be.uantwerpen.labplanner.common.model.stock.Unit;
import be.uantwerpen.labplanner.common.model.users.Privilege;
import be.uantwerpen.labplanner.common.model.users.Role;
import be.uantwerpen.labplanner.common.model.users.User;
import be.uantwerpen.labplanner.common.repository.users.PrivilegeRepository;
import be.uantwerpen.labplanner.common.repository.users.RoleRepository;
import be.uantwerpen.labplanner.common.repository.users.UserRepository;
import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Profile("!prod")
public class DatabaseLoader {

    private final PrivilegeRepository privilegeRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final DeviceTypeRepository deviceTypeRepository;
    private final DeviceRepository deviceRepository;
    private final DeviceInformationRepository deviceInformationRepository;
    private final OwnProductRepository productRepository;
    private final OwnTagRepository tagRepository;
    private final StepRepository stepRepository;
    private final CompositionRepository compositionRepository;
    private final MixtureRepository mixtureRepository;
    private final PieceOfMixtureRepository pieceOfMixtureRepository;
    private final ExperimentTypeRepository experimentTypeRepository;
    private final StepTypeRepository stepTypeRepository;
    private final ContinuityRepository continuityRepository;
    private final RelationRepository relationRepository;
    private final ExperimentRepository experimentRepository;
    private final ReportRepository reportRepository;
    private final OfficeHoursRepository officeHoursRepository;
    private final SystemSettingsRepository systemSettingsRepository;

    @Autowired
    public DatabaseLoader(RelationRepository relationRepository, PrivilegeRepository privilegeRepository, RoleRepository roleRepository, UserRepository userRepository, DeviceTypeRepository deviceTypeRepository, DeviceRepository deviceRepository, DeviceInformationRepository deviceInformationRepository, OwnProductRepository productRepository, OwnTagRepository tagRepository, StepRepository stepRepository, CompositionRepository compositionRepository, MixtureRepository mixtureRepository, PieceOfMixtureRepository pieceOfMixtureRepository, ExperimentTypeRepository experimentTypeRepository, StepTypeRepository stepTypeRepository, ContinuityRepository continuityRepository, ReportRepository reportRepository, ExperimentRepository experimentRepository, OfficeHoursRepository officeHoursRepository, SystemSettingsRepository systemSettingsRepository) {

        this.privilegeRepository = privilegeRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        //DeviceRepositories
        this.deviceTypeRepository = deviceTypeRepository;
        this.deviceRepository = deviceRepository;

        this.stepRepository = stepRepository;

        this.deviceInformationRepository = deviceInformationRepository;
        //ProductRepositories
        this.productRepository = productRepository;
        this.tagRepository = tagRepository;
        //MixtureRepositories
        this.mixtureRepository = mixtureRepository;
        this.compositionRepository = compositionRepository;
        this.relationRepository = relationRepository;
        this.pieceOfMixtureRepository = pieceOfMixtureRepository;


        this.experimentTypeRepository = experimentTypeRepository;
        this.stepTypeRepository = stepTypeRepository;
        this.continuityRepository = continuityRepository;
        this.experimentRepository = experimentRepository;

        this.reportRepository = reportRepository;


        this.officeHoursRepository = officeHoursRepository;
        this.systemSettingsRepository = systemSettingsRepository;


    }

    @Autowired
    private PasswordEncoder passwordEncoder;

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
        Privilege p17 = new Privilege("Stock - Modify - All");
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
        Privilege p28 = new Privilege("Statistics Access");
        privilegeRepository.save(p28);

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

        //Statistics privileges
        privileges.add(p28);

        administrator.setPrivileges(privileges);
        roleRepository.save(administrator);


        User u1 = new User("Cedric", passwordEncoder.encode("PW"), "cedric.plouvier@student.uantwerpen.be", "Cedric", "Plouvier", "20152267", "", "", null, null, null);
        User u2 = new User("Ruben", passwordEncoder.encode("PW"), "ruben.joosen@student.uantwerpen.be", "Ruben", "Joosen", "20164473", "", "", null, null, null);
        User u3 = new User("Jaimie", passwordEncoder.encode("PW"), "jaimie.vranckx@student.uantwerpen.be", "Jaimie", "Vranckx", "20155797", "", "", null, null, null);
        User u4 = new User("Ali", passwordEncoder.encode("PW"), "mohammad.amir2@student.uantwerpen.be", "Ali", "Amir", "20163446", "", "", null, null, null);
        User u5 = new User("Timo", passwordEncoder.encode("PW"), "timo.nelen@student.uantwerpen.be", "Timo", "Nelen", "S0162117", "", "", null, null, null);
        User u6 = new User("Ondrej", passwordEncoder.encode("PW"), "ondrej.bures@student.uantwerpen.be", "Ondrej", "Bures", "20160002", "", "", null, null, null);
        User u7 = new User("Bachelor", passwordEncoder.encode("PW"), "bachelor@student.uantwerpen.be", "Bach", "Student", "20170001", "", "", null, null, null);
        User u8 = new User("Master", passwordEncoder.encode("PW"), "master@student.uantwerpen.be", "Mas", "Student", "20160009", "", "", null, null, null);
        User u9 = new User("Researcher", passwordEncoder.encode("PW"), "researcher@uantwerpen.be", "Researcher", "Developper", "20100001", "", "", null, null, null);
        User admin = new User("admin", passwordEncoder.encode("admin"), "admin@uantwerpen.be", "admin", "admin", null, "", "", null, null, null);
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
        Relation relation = new Relation("testrelation");
        relation.setResearcher(u9);
        Set<User> students = new HashSet<>();
        students.add(u7);
        students.add(u8);

        relation.setStudents(students);
        Relation f = relationRepository.save(relation);
        //  Relation fetch = relationRepository.findById((long) 42).orElse(null);

        //   Relation fetchByRes = relationRepository.findByResearcher(u9).orElse(null);
        //   relation.setResearcher(u1);
        //   relationRepository.save(relation);

        //Create all Device Types
        ArrayList<DeviceType> deviceTypes = new ArrayList<>();
        DeviceType t1 = new DeviceType("Autosaw", true);
        t1.setColor("#FF00FF");
        deviceTypes.add(t1);
        DeviceType t2 = new DeviceType("Balance", false);
        t2.setColor("#FFAFAF");
        deviceTypes.add(t2);
        DeviceType t3 = new DeviceType("Big Mixer", true);
        t3.setColor("#00FF00");
        deviceTypes.add(t3);
        DeviceType t4 = new DeviceType("Caliper", true);
        t4.setColor("#000000");
        deviceTypes.add(t4);
        DeviceType t5 = new DeviceType("Cooling chamber", true);
        t5.setColor("#FFFF00");
        deviceTypes.add(t5);
        DeviceType t6 = new DeviceType("Gyratory", false);
        t6.setColor("#00FFFF");
        deviceTypes.add(t6);
        DeviceType t7 = new DeviceType("Oven", true);
        t7.setColor("#404040");
        deviceTypes.add(t7);
        DeviceType t8 = new DeviceType("Plate Compactor", false);
        t8.setColor("#FF0000");
        deviceTypes.add(t8);
        DeviceType t9 = new DeviceType("Small Mixer", true);
        t9.setColor("#FFC800");
        deviceTypes.add(t9);
        DeviceType t10 = new DeviceType("SVM Setup", false);
        t10.setColor("#808080");
        deviceTypes.add(t10);
        DeviceType t11 = new DeviceType("Uniframe", false);
        t11.setColor("#FFFFFF");
        deviceTypes.add(t11);
        DeviceType t12 = new DeviceType("Vacuum Setup", true);
        t12.setColor("#0000FF");
        deviceTypes.add(t12);
        DeviceType t13 = new DeviceType("Water Bath", false);
        t13.setColor("#222222");
        deviceTypes.add(t13);
        DeviceType t14 = new DeviceType("Wheel Tracking Test", true);
        t14.setColor("#C0C0C0");
        deviceTypes.add(t14);

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


        List<Device> devices = new ArrayList<>();
        //Add devices for some device types
        Device d1 = new Device("Autosaw 1", t1);
        devices.add(d1);
        Device d2 = new Device("Autosaw 2", t1);
        devices.add(d2);
        Device d3 = new Device("Uniframe 1", t11);
        devices.add(d3);
        Device d4 = new Device("Vacuum Setup 1", t12);
        devices.add(d4);
        Device d5 = new Device("Water Bath 1", t13);
        devices.add(d5);
        Device d6 = new Device("Wheel Tracking Test 1", t14);
        devices.add(d6);
        Device d7 = new Device("Oven 1", t7);
        d7.setComment("Perfect oven to bake a pizza in your spare times");
        d7.setDevicePictureName("Oven.png");
        devices.add(d7);
        Device d8 = new Device("Oven 2", t7);
        devices.add(d8);
        Device d9 = new Device("Oven 3", t7);
        devices.add(d9);
        Device d10 = new Device("Gyratory 1", t6);
        devices.add(d10);
        Device d11 = new Device("Caliper 1", t4);
        devices.add(d11);
        Device d12 = new Device("SVM Setup 1", t10);
        devices.add(d12);
        Device d13 = new Device("Big mixer 1", t3);
        devices.add(d13);
        Device d14 = new Device("Cooling chamber 1", t5);
        devices.add(d14);
        Device d15 = new Device("Balance 1", t2);
        devices.add(d15);

        //Add random information for default information types
        //Lorem ipsum generator for random information blocks
        Lorem lorem = LoremIpsum.getInstance();

        for (Device device : devices) {
            List<DeviceInformation> deviceinformations = new ArrayList<DeviceInformation>();

            //Add a new block for each default information type (Maintenance , Calibration ...)
            for (int current = 0; current < Device.getDefaultInformationtypes().size(); current++) {
                //Add new device information block
                DeviceInformation i = new DeviceInformation(Device.getDefaultInformationtypes().get(current), lorem.getWords(20));

                //Adding some files for Oven 1type , files were already loaded into the correct directory
                if (device.getDevicename().equals("Oven 1")) {
                    if (current == 0) {
                        i.addFile("placeholder.pdf");
                    } else if (current == 2) {
                        i.addFile("placeholder.xlsx");
                        i.addFile("placeholder.jpg");
                    }
                }
                deviceinformations.add(i);
                deviceInformationRepository.save(i);
            }

            //Add an extra block of info for oven to show full possibilities
            if (device.getDevicename().equals("Oven 1")) {
                DeviceInformation i = new DeviceInformation("Other", lorem.getWords(10));
                deviceinformations.add(i);
                deviceInformationRepository.save(i);
            }
            device.setDeviceInformation(deviceinformations);
        }

        deviceRepository.save(d1);
        deviceRepository.save(d2);
        deviceRepository.save(d3);
        deviceRepository.save(d4);
        deviceRepository.save(d5);
        deviceRepository.save(d6);
        deviceRepository.save(d7);
        deviceRepository.save(d8);
        deviceRepository.save(d9);
        deviceRepository.save(d10);
        deviceRepository.save(d11);
        deviceRepository.save(d12);
        deviceRepository.save(d13);
        deviceRepository.save(d14);
        deviceRepository.save(d15);
        //create some products
        OwnTag tag1 = new OwnTag("Aggregates");
        List<OwnTag> tags1 = new ArrayList<>();
        tags1.add(tag1);
        tagRepository.save(tag1);
        OwnTag tag2 = new OwnTag("Bitumen");
        List<OwnTag> tags2 = new ArrayList<>();
        tags2.add(tag2);
        tagRepository.save(tag2);
        OwnTag tag3 = new OwnTag("Consumables");
        List<OwnTag> tags3 = new ArrayList<>();
        tags3.add(tag3);
        tagRepository.save(tag3);
        OwnTag tag4 = new OwnTag("Other");
        List<OwnTag> tags4 = new ArrayList<>();
        tags4.add(tag4);
        tagRepository.save(tag4);

        Map<String,Double> stockHis1 = new HashMap<>();
        stockHis1.put("2019-11",(double)500);
        stockHis1.put("2019-12",(double)220);
        stockHis1.put("2020-01",(double)100);
        stockHis1.put("2020-02",(double)200);
        stockHis1.put("2020-03",(double)100);
        stockHis1.put("2020-04",(double)250);
        stockHis1.put("2020-05",(double)260);
        OwnProduct pr1 = new OwnProduct("Gebroken Porfier 6,3/10", lorem.getWords(20), 1.0, 2000.0, 200.0, 1.0, Unit.KILOGRAM, null, lorem.getWords(8), 5L, 5L, LocalDateTime.now(), LocalDateTime.now(), tags3, new URL("https://nl.wikipedia.org/wiki/Porfier"),null, new HashMap<>());
        pr1.setProductStockHistory(stockHis1);
        productRepository.save(pr1);
        Map<String,Double> stockHis2 = new HashMap<>();
        stockHis2.put("2019-11",(double)1000);
        stockHis2.put("2019-12",(double)200);
        stockHis2.put("2020-01",(double)600);
        stockHis2.put("2020-02",(double)500);
        stockHis2.put("2020-03",(double)550);
        stockHis2.put("2020-04",(double)200);
        stockHis2.put("2020-05",(double)350);
        OwnProduct pr2 = new OwnProduct("Gebroken Porfier 4/6,3", lorem.getWords(20), 1.0, 2000.0, 200.0, 1.0, Unit.KILOGRAM, null, lorem.getWords(8), 5L, 5L, LocalDateTime.now(), LocalDateTime.now(), tags1, new URL("https://nl.wikipedia.org/wiki/Porfier"),null, new HashMap<>());
        pr2.setProductStockHistory(stockHis2);
        productRepository.save(pr2);
        OwnProduct pr3 = new OwnProduct("Gebroken Profier 2/4", lorem.getWords(20), 1.0, 2000.0, 200.0, 1.0, Unit.KILOGRAM, null, lorem.getWords(8), 5L, 5L, LocalDateTime.now(), LocalDateTime.now(), tags1, new URL("https://nl.wikipedia.org/wiki/Porfier"),null, new HashMap<>());
        pr3.setProductStockHistory(stockHis1);
        productRepository.save(pr3);
        OwnProduct pr4 = new OwnProduct("Gewassen Kalksteen 0/2", lorem.getWords(20), 1.0, 2000.0, 200.0, 1.0, Unit.KILOGRAM, null, lorem.getWords(8), 5L, 5L, LocalDateTime.now(), LocalDateTime.now(), tags3, new URL("https://nl.wikipedia.org/wiki/Kalksteen"),null, new HashMap<>());
        pr4.setProductStockHistory(stockHis1);
        productRepository.save(pr4);
        OwnProduct pr5 = new OwnProduct("Alzagri Rond Zand 0/1", lorem.getWords(20), 1.0, 1000.0, 100.0, 1.0, Unit.KILOGRAM, null, lorem.getWords(8), 5L, 5L, LocalDateTime.now(), LocalDateTime.now(), tags2, new URL("https://nl.wikipedia.org/wiki/Zand"),null, new HashMap<>());
        pr5.setProductStockHistory(stockHis1);
        productRepository.save(pr5);
        OwnProduct pr6 = new OwnProduct("Vulstof duras 2a", lorem.getWords(20), 1.0, 500.0, 100.0, 1.0, Unit.KILOGRAM, null, lorem.getWords(8), 5L, 5L, LocalDateTime.now(), LocalDateTime.now(), tags4, new URL("https://nl.wikipedia.org/wiki/Duras"),null, new HashMap<>());
        pr6.setProductStockHistory(stockHis1);
        productRepository.save(pr6);
        OwnProduct pr7 = new OwnProduct("Bitumen op aggregaten", lorem.getWords(20), 1.0, 200.0, 25.0, 1.0, Unit.LITRE, null, lorem.getWords(8), 5L, 5L, LocalDateTime.now(), LocalDateTime.now(), tags4, new URL("https://nl.wikipedia.org/wiki/Bitumen"),null, new HashMap<>());
        pr7.setProductStockHistory(stockHis1);
        productRepository.save(pr7);
        OwnProduct pr8 = new OwnProduct("placeholder8", lorem.getWords(20), 1.0, 90.0, 1.0, 1.0, Unit.UNIT, null, lorem.getWords(8), 5L, 5L, LocalDateTime.now(), LocalDateTime.now(), tags4, null,null, new HashMap<>());
        pr8.setProductStockHistory(stockHis1);
        productRepository.save(pr8);


        Step s1 = new Step(u1, d1, "2020-03-18", "2020-03-18", "11:00", "12:00", "");
        stepRepository.save(s1);
        Step s2 = new Step(u1, d7, "2020-03-17", "2020-03-17", "08:00", "18:00", "");
        stepRepository.save(s2);
        Step s3 = new Step(u4, d1, "2020-03-16", "2020-03-16", "14:00", "16:00", "");
        stepRepository.save(s3);
        Step s4 = new Step(u4, d7, "2020-03-15", "2020-03-15", "16:00", "18:00", "");
        stepRepository.save(s4);
        Step s5 = new Step(u5, d9, "2020-03-19", "2020-03-19", "13:00", "18:00", "");
        stepRepository.save(s5);

        Step s6 = new Step(u7, d9, "2020-04-10", "2020-04-10", "13:00", "18:00", "");
        stepRepository.save(s6);
        Step s7 = new Step(u7, d9, "2020-04-11", "2020-04-11", "13:00", "18:00", "");
        stepRepository.save(s7);
        Step s8 = new Step(u8, d9, "2020-04-12", "2020-04-12", "13:00", "18:00", "");
        stepRepository.save(s8);
        Step s9 = new Step(u8, d9, "2020-04-13", "2020-04-13", "13:00", "18:00", "");
        stepRepository.save(s9);

        Step s10 = new Step(u1, d1, "2020-05-18", "2020-05-18", "11:00", "13:00", "");
        stepRepository.save(s10);
        Step s11 = new Step(u1, d1, "2020-06-17", "2020-06-17", "08:00", "19:00", "");
        stepRepository.save(s11);
        Step s12 = new Step(u4, d1, "2020-07-16", "2020-07-16", "11:00", "16:00", "");
        stepRepository.save(s12);
        Step s13 = new Step(u4, d1, "2020-08-15", "2020-08-15", "12:00", "18:00", "");
        stepRepository.save(s13);
        Step s14 = new Step(u5, d1, "2020-09-19", "2020-09-19", "14:00", "18:00", "");
        stepRepository.save(s14);
        Step s15 = new Step(u4, d1, "2020-10-16", "2020-10-16", "11:00", "16:00", "");
        stepRepository.save(s15);
        Step s16 = new Step(u4, d1, "2020-11-15", "2020-11-15", "12:00", "18:00", "");
        stepRepository.save(s16);
        Step s17 = new Step(u5, d1, "2020-12-19", "2020-12-19", "14:00", "18:00", "");
        stepRepository.save(s17);

        Step s18 = new Step(u7, d2, "2020-04-10", "2020-04-10", "09:00", "18:00", "");
        stepRepository.save(s18);
        Step s19 = new Step(u7, d3, "2020-04-11", "2020-04-11", "13:00", "20:00", "");
        stepRepository.save(s19);
        Step s20 = new Step(u8, d4, "2020-04-12", "2020-04-12", "13:00", "15:00", "");
        stepRepository.save(s20);
        Step s21 = new Step(u8, d5, "2020-04-13", "2020-04-13", "11:00", "18:00", "");
        stepRepository.save(s21);

        Step s22 = new Step(u1, d2, "2019-05-18", "2019-05-18", "10:00", "20:00", "");
        stepRepository.save(s22);

        Step s23 = new Step(u1, d8, "2020-01-13", "2020-01-15", "10:00", "18:00", "");
        stepRepository.save(s23);
        Step s24 = new Step(u1, d8, "2020-02-13", "2020-03-15", "10:00", "18:00", "");
        stepRepository.save(s24);
        Step s25 = new Step(u1, d8, "2020-04-13", "2020-06-15", "10:00", "18:00", "");
        stepRepository.save(s25);
        Step s26 = new Step(u1, d8, "2020-09-13", "2020-12-15", "10:00", "18:00", "");
        stepRepository.save(s26);
        Step s27 = new Step(u1, d8, "2020-05-13", "2020-07-15", "10:00", "18:00", "");
        //stepRepository.save(s27);
        Step s28 = new Step(u1, d8, "2020-09-13", "2020-11-15", "10:00", "18:00", "");
        //stepRepository.save(s28);
        Step s29 = new Step(u1, d8, "2020-05-13", "2020-07-15", "10:00", "18:00", "");
        //stepRepository.save(s29);
        Step s30 = new Step(u1, d8, "2020-09-13", "2020-11-15", "10:00", "18:00", "");
        //stepRepository.save(s30);

        Step s31 = new Step(u1, d2, "2021-01-01", "2021-12-30", "10:00", "20:00", "");
        stepRepository.save(s31);

        //Create compositions

        //APT-C ingredients
        Composition c1 = new Composition(46.67, pr1);
        Composition c2 = new Composition(10.10, pr2);
        Composition c3 = new Composition(8.29, pr3);
        Composition c4 = new Composition(23.89, pr4);
        Composition c5 = new Composition(2.45, pr5);
        Composition c6 = new Composition(8.60, pr6);
        Composition c7 = new Composition(6.50, pr7);
        compositionRepository.save(c1);
        compositionRepository.save(c2);
        compositionRepository.save(c3);
        compositionRepository.save(c4);
        compositionRepository.save(c5);
        compositionRepository.save(c6);
        compositionRepository.save(c7);
        List<Composition> mix1 = new ArrayList<>();
        mix1.add(c1);
        mix1.add(c2);
        mix1.add(c3);
        mix1.add(c4);
        mix1.add(c5);
        mix1.add(c6);
        mix1.add(c7);

        //Ab-4C ingredients
        Composition c11 = new Composition(20.72, pr1);
        Composition c12 = new Composition(14.20, pr2);
        Composition c13 = new Composition(21.24, pr3);
        Composition c14 = new Composition(23.84, pr4);
        Composition c15 = new Composition(12.91, pr5);
        Composition c16 = new Composition(7.10, pr6);
        Composition c17 = new Composition(6.50, pr7);
        compositionRepository.save(c11);
        compositionRepository.save(c12);
        compositionRepository.save(c13);
        compositionRepository.save(c14);
        compositionRepository.save(c15);
        compositionRepository.save(c16);
        compositionRepository.save(c17);
        List<Composition> mix2 = new ArrayList<>();
        mix2.add(c11);
        mix2.add(c12);
        mix2.add(c13);
        mix2.add(c14);
        mix2.add(c15);
        mix2.add(c16);
        mix2.add(c17);

        //SMA ingredients
        Composition c21 = new Composition(61.70, pr1);
        Composition c22 = new Composition(11.20, pr2);
        Composition c24 = new Composition(17.80, pr4);
        Composition c25 = new Composition(9.30, pr5);
        Composition c27 = new Composition(6.90, pr7);
        compositionRepository.save(c21);
        compositionRepository.save(c22);
        compositionRepository.save(c24);
        compositionRepository.save(c25);
        compositionRepository.save(c27);
        List<Composition> mix3 = new ArrayList<>();
        mix3.add(c21);
        mix3.add(c22);
        mix3.add(c24);
        mix3.add(c25);
        mix3.add(c27);


        //create mixtures and save them
        Mixture m1 = new Mixture("APT-C", mix1, lorem.getWords(20), tags2, null, null);
        mixtureRepository.save(m1);
        Mixture m2 = new Mixture("AB-4C", mix2, lorem.getWords(20), tags1, null, null);
        mixtureRepository.save(m2);
        Mixture m3 = new Mixture("SMA", mix3, lorem.getWords(20), tags4, null, null);
        mixtureRepository.save(m3);

        //default continuity for custom experiment
        Continuity defaultContForCustomExp = new Continuity(0, 0, "No", "After");
        continuityRepository.save(defaultContForCustomExp);
        long id =  defaultContForCustomExp.getId();

        //Continuities
        Continuity cont1 = new Continuity(0, 0, "No", "After");
        continuityRepository.save(cont1);
        Continuity cont2 = new Continuity(8, 0, "Soft (at least)", "After");
        continuityRepository.save(cont2);
        Continuity cont3 = new Continuity(24, 0, "Hard", "After");
        continuityRepository.save(cont3);
        Continuity cont4 = new Continuity(4, 0, "Soft (at least)", "After");
        continuityRepository.save(cont4);
        Continuity cont5 = new Continuity(0, 0, "Hard", "After");
        continuityRepository.save(cont5);
        Continuity cont6 = new Continuity(24, 0, "Soft (at most)", "After");
        continuityRepository.save(cont6);

        //Steptypes
        StepType styp1 = new StepType(t2, cont1, t2.getDeviceTypeName());
        stepTypeRepository.save(styp1);
        StepType styp2 = new StepType(t8, cont2, t8.getDeviceTypeName());
        stepTypeRepository.save(styp2);
        StepType styp3 = new StepType(t3, cont1, "Mixer");
        stepTypeRepository.save(styp3);
        StepType styp4 = new StepType(t6, cont3, t6.getDeviceTypeName());
        stepTypeRepository.save(styp4);
        StepType styp5 = new StepType(t1, cont4, t1.getDeviceTypeName());
        stepTypeRepository.save(styp5);
        StepType styp6 = new StepType(t4, cont1, t4.getDeviceTypeName());
        stepTypeRepository.save(styp6);
        StepType styp7 = new StepType(t10, cont1, t10.getDeviceTypeName());
        stepTypeRepository.save(styp7);
        StepType styp8 = new StepType(t12, cont5, t12.getDeviceTypeName());
        stepTypeRepository.save(styp8);
        StepType styp9 = new StepType(t13, cont1, t13.getDeviceTypeName());
        stepTypeRepository.save(styp9);
        StepType styp10 = new StepType(t14, cont1, t14.getDeviceTypeName());
        stepTypeRepository.save(styp10);
        StepType styp11 = new StepType(t8, cont6, t8.getDeviceTypeName());
        stepTypeRepository.save(styp11);
        List<StepType> ITSRStyps = new ArrayList<StepType>();
        ITSRStyps.add(styp1);
        ITSRStyps.add(styp2);
        ITSRStyps.add(styp1);
        ITSRStyps.add(styp3);
        ITSRStyps.add(styp4);
        ITSRStyps.add(styp5);
        ITSRStyps.add(styp6);
        ITSRStyps.add(styp7);
        ITSRStyps.add(styp8);
        ITSRStyps.add(styp9);
        List<StepType> WhlTrkTest = new ArrayList<StepType>();
        WhlTrkTest.add(styp1);
        WhlTrkTest.add(styp2);
        WhlTrkTest.add(styp1);
        WhlTrkTest.add(styp3);
        WhlTrkTest.add(styp11);
        WhlTrkTest.add(styp10);
        ExperimentType experimentType1 = new ExperimentType("ITSR", ITSRStyps,true);
        experimentTypeRepository.save(experimentType1);
        ExperimentType experimentType2 = new ExperimentType("Wheel Tracking Test", WhlTrkTest,true);
        experimentTypeRepository.save(experimentType2);


        //
        //Step1 - No continuity, Device: Balance
        Continuity continuity1 = new Continuity(0, 0, "No", "After");
        StepType stepType1 = new StepType(t2, continuity1, "Step" + t2.getDeviceTypeName());

        //Step2 - Continuity 4h before end, Device: Oven, Fixed time At least 12h
        Continuity continuity2 = new Continuity(4, 0, "Hard", "Before");
        StepType stepType2 = new StepType(t7, continuity2, "Step" + t7.getDeviceTypeName(), true, "At least", 12, 0);

        //Step3,4,5 should be 4 hours before end of oven
        //Step 3 - Continuity: Hard 4h before, DeviceType: Mixer, FixedTime: 4h
        Continuity continuity3 = new Continuity(4, 0, "Hard", "Before");
        StepType stepType3 = new StepType(t3, continuity3, "Step" + t3.getDeviceTypeName(), true, "Equal", 4, 0);

        //Step 4 - Continuity: Hard 4h before, DeviceType: Balance, FixedTime: 4h
        Continuity continuity4 = new Continuity(4, 0, "Hard", "Before");
        StepType stepType4 = new StepType(t2, continuity4, "Step" + t2.getDeviceTypeName(), true, "Equal", 4, 0);

        //Step 5 - Continuity: Hard 24h after, DeviceType: Gyrator, FixedTime: 4h
        Continuity continuity5 = new Continuity(20, 0, "Soft (at least)", "After");
        StepType stepType5 = new StepType(t6, continuity5, "Step" + t6.getDeviceTypeName(), true, "Equal", 4, 0);

        //Step 6 - Continuity: At least 12h after, DeviceType: Autosaw, FixedTime: 2h
        Continuity continuity6 = new Continuity(12, 0, "Soft (at least)", "After");
        StepType stepType6 = new StepType(t1, continuity6, "Step" + t1.getDeviceTypeName(), true, "Equal", 2, 0);

        //Step 7 - Continuity: At least 12h after, DeviceType: Caliper, FixedTime: 1h
        Continuity continuity7 = new Continuity(0, 0, "No", "After");
        StepType stepType7 = new StepType(t4, continuity7, "Step" + t4.getDeviceTypeName(), true, "Equal", 1, 0);

        //Step 8 - Continuity: No, DeviceType: SVM, FixedTime: 1h
        Continuity continuity8 = new Continuity(0, 0, "No", "After");
        StepType stepType8 = new StepType(t10, continuity8, "Step" + t10.getDeviceTypeName(), true, "Equal", 1, 0);

        //Step 9 - Continuity: No, DeviceType: Vacuum setup, FixedTime: 1h
        Continuity continuity9 = new Continuity(0, 0, "No", "After");
        StepType stepType9 = new StepType(t12, continuity9, "Step" + t12.getDeviceTypeName(), true, "Equal", 1, 0);

        //Step 10 - Continuity: Hard, 0h, DeviceType: Water bath, FixedTime: 70h
        Continuity continuity10 = new Continuity(0, 0, "Hard", "After");
        StepType stepType10 = new StepType(t13, continuity10, "Step" + t13.getDeviceTypeName(), true, "Equal", 3, 0);

        //Step 11 - Continuity: Hard, 0h, DeviceType: Cooling chamber, FixedTime: 4h
        Continuity continuity11 = new Continuity(0, 0, "Soft (at least)", "After");
        StepType stepType11 = new StepType(t5, continuity11, "Step" + t5.getDeviceTypeName(), true, "Equal", 4, 0);

        //Step 12 - Continuity: Hard, 0h, DeviceType: Uniframe, FixedTime: 1h
        Continuity continuity12 = new Continuity(0, 0, "Soft (at least)", "After");
        StepType stepType12 = new StepType(t11, continuity12, "Step" + t11.getDeviceTypeName(), true, "Equal", 1, 0);

        //save into database
        continuityRepository.save(continuity1);
        stepTypeRepository.save(stepType1);
        continuityRepository.save(continuity2);
        stepTypeRepository.save(stepType2);
        continuityRepository.save(continuity3);
        stepTypeRepository.save(stepType3);
        continuityRepository.save(continuity4);
        stepTypeRepository.save(stepType4);
        continuityRepository.save(continuity5);
        stepTypeRepository.save(stepType5);
        continuityRepository.save(continuity6);
        stepTypeRepository.save(stepType6);
        continuityRepository.save(continuity7);
        stepTypeRepository.save(stepType7);
        continuityRepository.save(continuity8);
        stepTypeRepository.save(stepType8);
        continuityRepository.save(continuity9);
        stepTypeRepository.save(stepType9);
        continuityRepository.save(continuity10);
        stepTypeRepository.save(stepType10);
        continuityRepository.save(continuity11);
        stepTypeRepository.save(stepType11);
        continuityRepository.save(continuity12);
        stepTypeRepository.save(stepType12);

        List<StepType> ITSRSDifficultList = new ArrayList<StepType>();
        ITSRSDifficultList.add(stepType1);
        ITSRSDifficultList.add(stepType2);
        ITSRSDifficultList.add(stepType3);
        ITSRSDifficultList.add(stepType4);
        ITSRSDifficultList.add(stepType5);
        ITSRSDifficultList.add(stepType6);
        ITSRSDifficultList.add(stepType7);
        ITSRSDifficultList.add(stepType8);
        ITSRSDifficultList.add(stepType9);
        ITSRSDifficultList.add(stepType10);
        ITSRSDifficultList.add(stepType11);
        ITSRSDifficultList.add(stepType12);

        ExperimentType experimentType3 = new ExperimentType("ITSR (Difficult)", ITSRSDifficultList, true);
        experimentTypeRepository.save(experimentType3);

        Report r1 = new Report("Autosaw is broken", lorem.getWords(25), userRepository.findByUsername("Timo").orElse(null));
        Report r2 = new Report("Fancy Title", lorem.getWords(10), userRepository.findByUsername("Ali").orElse(null));
        Report r3 = new Report("Sand mixture is not in stock", lorem.getWords(20), userRepository.findByUsername("Ruben").orElse(null));
        Report r4 = new Report("Lab was closed yesterday", lorem.getWords(20), userRepository.findByUsername("Master").orElse(null));
        Report r5 = new Report("Placeholder title", lorem.getWords(25), userRepository.findByUsername("Researcher").orElse(null));

        reportRepository.save(r1);
        reportRepository.save(r2);
        reportRepository.save(r3);
        reportRepository.save(r4);
        reportRepository.save(r5);

        //fill experimentRepository
        Step step1a = new Step(u7, d10, "2020-05-04", "2020-05-04", "11:00", "12:00", "");
        Step step2a = new Step(u7, d1, "2020-05-05", "2020-05-05", "12:00", "13:00", "");
        Step step3a = new Step(u7, d11, "2020-05-06", "2020-05-06", "11:00", "12:00", "");
        Step step4a = new Step(u7, d12, "2020-05-07", "2020-05-07", "09:00", "10:00", "");
        Step step5a = new Step(u7, d4, "2020-05-08", "2020-05-08", "10:00", "11:00", "");
        Step step6a = new Step(u7, d5, "2020-05-08", "2020-05-08", "11:00", "12:00", "");


        Step step1b = new Step(u7, d10, "2020-05-04", "2020-05-04", "13:00", "14:00", "");
        Step step2b = new Step(u7, d1, "2020-05-05", "2020-05-05", "14:00", "15:00", "");
        Step step3b = new Step(u7, d11, "2020-05-06", "2020-05-06", "13:00", "14:00", "");
        Step step4b = new Step(u7, d12, "2020-05-07", "2020-05-07", "11:00", "12:00", "");
        Step step5b = new Step(u7, d4, "2020-05-08", "2020-05-08", "12:00", "13:00", "");
        Step step6b = new Step(u7, d5, "2020-05-08", "2020-05-08", "13:00", "14:00", "");


        Step step1c = new Step(u7, d10, "2020-05-04", "2020-05-04", "14:00", "15:00", "");
        Step step2c = new Step(u7, d1, "2020-05-05", "2020-05-05", "15:00", "16:00", "");
        Step step3c = new Step(u7, d11, "2020-05-06", "2020-05-06", "15:00", "16:00", "");
        Step step4c = new Step(u7, d12, "2020-05-07", "2020-05-07", "14:00", "15:00", "");
        Step step5c = new Step(u7, d4, "2020-05-08", "2020-05-08", "14:00", "15:00", "");
        Step step6c = new Step(u7, d5, "2020-05-08", "2020-05-08", "15:00", "16:00", "");


        Step step1d = new Step(u7, d15, "2020-05-11", "2020-05-11", "10:00", "11:00", "");
        Step step2d = new Step(u7, d7, "2020-05-11", "2020-05-12", "11:00", "13:00", "");
        Step step3d = new Step(u7, d13, "2020-05-12", "2020-05-12", "09:00", "13:00", "");
        Step step4d = new Step(u7, d15, "2020-05-12", "2020-05-12", "09:00", "13:00", "");
        Step step5d = new Step(u7, d10, "2020-05-12", "2020-05-12", "09:00", "13:00", "");
        Step step6d = new Step(u7, d1, "2020-05-13", "2020-05-13", "13:00", "15:00", "");
        Step step7d = new Step(u7, d11, "2020-05-14", "2020-05-14", "10:00", "11:00", "");
        Step step8d = new Step(u7, d12, "2020-05-14", "2020-05-14", "11:00", "12:00", "");
        Step step9d = new Step(u7, d4, "2020-05-15", "2020-05-15", "12:00", "13:00", "");
        Step step10d = new Step(u7, d5, "2020-05-15", "2020-05-15", "13:00", "17:00", "");
        Step step11d = new Step(u7, d14, "2020-05-18", "2020-05-18", "11:00", "15:00", "");
        Step step12d = new Step(u7, d3, "2020-05-18", "2020-05-18", "15:00", "16:00", "");


        List<Step> stepList1 = new ArrayList<Step>();
        stepList1.add(step1a);
        stepList1.add(step2a);
        stepList1.add(step3a);
        stepList1.add(step4a);
        stepList1.add(step5a);
        stepList1.add(step6a);

        List<Step> stepList2 = new ArrayList<Step>();
        stepList2.add(step1b);
        stepList2.add(step2b);
        stepList2.add(step3b);
        stepList2.add(step4b);
        stepList2.add(step5b);
        stepList2.add(step6b);

        List<Step> stepList3 = new ArrayList<Step>();
        stepList3.add(step1c);
        stepList3.add(step2c);
        stepList3.add(step3c);
        stepList3.add(step4c);
        stepList3.add(step5c);
        stepList3.add(step6c);

        List<Step> stepList4 = new ArrayList<Step>();
        stepList4.add(step1d);
        stepList4.add(step2d);
        stepList4.add(step3d);
        stepList4.add(step4d);
        stepList4.add(step5d);
        stepList4.add(step6d);
        stepList4.add(step7d);
        stepList4.add(step8d);
        stepList4.add(step9d);
        stepList4.add(step10d);
        stepList4.add(step11d);
        stepList4.add(step12d);

        for (int i = 0; i < stepList1.size(); i++) {
            stepList1.get(i).setStepType(experimentType1.getStepTypes().get(i));
        }
        for (int i = 0; i < stepList2.size(); i++) {
            stepList2.get(i).setStepType(experimentType1.getStepTypes().get(i));
        }
        for (int i = 0; i < stepList3.size(); i++) {
            stepList3.get(i).setStepType(experimentType1.getStepTypes().get(i));
        }

        for (int i = 0; i < stepList4.size(); i++) {

            stepList4.get(i).setStepType(experimentType3.getStepTypes().get(i));

        }
        stepRepository.save(step1a);
        stepRepository.save(step2a);
        stepRepository.save(step3a);
        stepRepository.save(step4a);
        stepRepository.save(step5a);
        stepRepository.save(step6a);

        stepRepository.save(step1b);
        stepRepository.save(step2b);
        stepRepository.save(step3b);
        stepRepository.save(step4b);
        stepRepository.save(step5b);
        stepRepository.save(step6b);

        stepRepository.save(step1c);
        stepRepository.save(step2c);
        stepRepository.save(step3c);
        stepRepository.save(step4c);
        stepRepository.save(step5c);
        stepRepository.save(step6c);

        stepRepository.save(step1d);
        stepRepository.save(step2d);
        stepRepository.save(step3d);
        stepRepository.save(step4d);
        stepRepository.save(step5d);
        stepRepository.save(step6d);
        stepRepository.save(step7d);
        stepRepository.save(step8d);
        stepRepository.save(step9d);
        stepRepository.save(step10d);
        stepRepository.save(step11d);
        stepRepository.save(step12d);

        PieceOfMixture pom1 = new PieceOfMixture(m1, "comment 1", 6.6);
        pieceOfMixtureRepository.save(pom1);
        PieceOfMixture pom2 = new PieceOfMixture(m2, "comment 2", 6.6);
        pieceOfMixtureRepository.save(pom2);
        PieceOfMixture pom3 = new PieceOfMixture(m3, "comment 3", 6.6);
        pieceOfMixtureRepository.save(pom3);
        PieceOfMixture pom4 = new PieceOfMixture(m3, "comment 4", 6.6);
        pieceOfMixtureRepository.save(pom4);

        List<PieceOfMixture> pomList1 = new ArrayList<>();
        pomList1.add(pom1);

        List<PieceOfMixture> pomList2 = new ArrayList<>();
        pomList2.add(pom2);

        List<PieceOfMixture> pomList3 = new ArrayList<>();
        pomList3.add(pom3);
        pomList3.add(pom4);

        Experiment ex1 = new Experiment(experimentType1, stepList1, u7, "experiment1", pomList1, step1a.getStart(), step6a.getEnd());
        Experiment ex2 = new Experiment(experimentType1, stepList2, u7, "experiment2", pomList2, step1b.getStart(), step6b.getEnd());
        Experiment ex3 = new Experiment(experimentType1, stepList3, u7, "experiment3", pomList3, step1c.getStart(), step6c.getEnd());
        Experiment ex4 = new Experiment(experimentType3, stepList4, u7, "experiment4", null, step1d.getStart(), step12d.getEnd());
        experimentRepository.save(ex1);
        experimentRepository.save(ex2);
        experimentRepository.save(ex3);
        experimentRepository.save(ex4);
        OfficeHours oh = new OfficeHours();
        officeHoursRepository.save(oh);
        SystemSettings systemSettings = new SystemSettings(oh);
        systemSettingsRepository.save(systemSettings);
        SystemSettings.setCurrentSystemSettings(systemSettings);
    }
}
