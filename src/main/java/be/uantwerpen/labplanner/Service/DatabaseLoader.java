package be.uantwerpen.labplanner.Service;

import be.uantwerpen.labplanner.Model.Device;
import be.uantwerpen.labplanner.Model.DeviceType;
import be.uantwerpen.labplanner.Repository.DeviceRepository;
import be.uantwerpen.labplanner.Repository.DeviceTypeRepository;
import be.uantwerpen.labplanner.common.model.users.Privilege;
import be.uantwerpen.labplanner.common.model.users.Role;
import be.uantwerpen.labplanner.common.model.users.User;
import be.uantwerpen.labplanner.common.repository.users.PrivilegeRepository;
import be.uantwerpen.labplanner.common.repository.users.RoleRepository;
import be.uantwerpen.labplanner.common.repository.users.UserRepository;
import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.util.*;

@Service
@Profile("default")
public class DatabaseLoader {
    private final PrivilegeRepository privilegeRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final DeviceTypeRepository deviceTypeRepository;
    private final DeviceRepository deviceRepository;
    @Autowired
    public DatabaseLoader(PrivilegeRepository privilegeRepository, RoleRepository roleRepository, UserRepository userRepository,DeviceTypeRepository deviceTypeRepository, DeviceRepository deviceRepository) {
        this.privilegeRepository = privilegeRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        //DeviceRepositories
        this.deviceTypeRepository = deviceTypeRepository;
        this.deviceRepository = deviceRepository;
    }

    @PostConstruct
    private void initDatabase() {

        Privilege p1 = new Privilege("logon");
        privilegeRepository.save(p1);
        Privilege p2 = new Privilege("secret-message"); privilegeRepository.save(p2);
        Role administrator = new Role("Administrator");
        Role tester = new Role("Tester");
        List<Privilege> privileges = new ArrayList<Privilege>();
        privileges.add(p1);
        tester.setPrivileges(privileges);
        roleRepository.save(tester);
        privileges = new ArrayList<Privilege>();
        privileges.add(p1);
        privileges.add(p2);
        administrator.setPrivileges(privileges);
        roleRepository.save(administrator);
        User u1 = new User("jaimie","test");
        userRepository.save(u1);
        Set<Role> roles = new HashSet<Role>();

        roles.add(administrator);
        roles.add(tester);
        u1.setRoles(roles);

        //Create all Device Types
        DeviceType t1 = new DeviceType("Autosaw",true);
        deviceTypeRepository.save(t1);
        DeviceType t2 = new DeviceType("Balance",false);
        deviceTypeRepository.save(t2);
        DeviceType t3 = new DeviceType("Big Mixer",true);
        deviceTypeRepository.save(t3);
        DeviceType t4 = new DeviceType("Caliper",true);
        deviceTypeRepository.save(t4);
        DeviceType t5 = new DeviceType("Cooling chamber",true);
        deviceTypeRepository.save(t5);
        DeviceType t6 = new DeviceType("Gyratory",false);
        deviceTypeRepository.save(t6);
        DeviceType t7 = new DeviceType("Oven",true);
        deviceTypeRepository.save(t7);
        DeviceType t8 = new DeviceType("Plate Compactor",false);
        deviceTypeRepository.save(t8);
        DeviceType t9 = new DeviceType("Small Mixer",true);
        deviceTypeRepository.save(t9);
        DeviceType t10 = new DeviceType("SVM Setup",false);
        deviceTypeRepository.save(t10);
        DeviceType t11 = new DeviceType("Uniframe",false);
        deviceTypeRepository.save(t11);
        DeviceType t12 = new DeviceType("Vacuum Setup",true);
        deviceTypeRepository.save(t12);
        DeviceType t13 = new DeviceType("Water Bath",false);
        deviceTypeRepository.save(t13);
        DeviceType t14 = new DeviceType("Wheel Tracking Test",true);
        deviceTypeRepository.save(t14);

        //Add random information for default information types
        //Lorem ipsum generator for random information blocks
        Lorem lorem = LoremIpsum.getInstance();

        for (DeviceType devicetype : deviceTypeRepository.findAll()) {
            for (int current = 0; current < devicetype.getDefaultInformationtypes().size(); current++) {
                devicetype.addDeviceInformation(devicetype.getDefaultInformationtypes().get(current), lorem.getWords(20));
            }
        }

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
        deviceRepository.save(d7);
        Device d8 = new Device("Oven 2",t8);
        deviceRepository.save(d8);
        Device d9 = new Device("Oven 3",t9);
        deviceRepository.save(d9);
    }
}
