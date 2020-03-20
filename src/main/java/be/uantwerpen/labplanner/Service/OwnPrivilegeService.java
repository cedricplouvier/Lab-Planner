package be.uantwerpen.labplanner.Service;

import be.uantwerpen.labplanner.common.model.users.Privilege;
import be.uantwerpen.labplanner.common.repository.users.PrivilegeRepository;
import be.uantwerpen.labplanner.common.service.users.PrivilegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OwnPrivilegeService extends PrivilegeService {
    @Autowired
    private PrivilegeRepository privilegeRepository;

    public Optional<Privilege> findByName(String name){
        return this.privilegeRepository.findByName(name);
    }


}
