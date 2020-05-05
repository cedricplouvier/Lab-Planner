package be.uantwerpen.labplanner.Service;

import be.uantwerpen.labplanner.Model.Continuity;
import be.uantwerpen.labplanner.Repository.ContinuityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ContinuityService {
    @Autowired
    private ContinuityRepository continuityRepository;

    public Optional<Continuity> findById(Long id){
        return continuityRepository.findById(id);
    }

    public void save(Continuity continuity){
        Continuity tempContinuity = continuity.getId() == null ? null : continuityRepository.findById(continuity.getId()).orElse(null);
        if(tempContinuity!=null) {
            tempContinuity.setType(continuity.getType());
            tempContinuity.setDirectionType(continuity.getDirectionType());
            tempContinuity.setHours(continuity.getHours());
            tempContinuity.setMinutes(continuity.getMinutes());
            continuityRepository.save(tempContinuity);
        }
        else {
            continuityRepository.save(continuity);
        }
    }
    public void delete(Long id){
        continuityRepository.deleteById(id);
    }
}
