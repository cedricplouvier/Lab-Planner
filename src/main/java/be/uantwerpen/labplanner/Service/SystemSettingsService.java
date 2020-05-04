package be.uantwerpen.labplanner.Service;

import be.uantwerpen.labplanner.Model.OfficeHours;
import be.uantwerpen.labplanner.Model.SystemSettings;
import be.uantwerpen.labplanner.Repository.OfficeHoursRepository;
import be.uantwerpen.labplanner.Repository.SystemSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class SystemSettingsService {
    @Autowired
    private SystemSettingsRepository systemSettingsRepository;

    public SystemSettingsService() {

    }

    public List<SystemSettings> findAll() {
        return this.systemSettingsRepository.findAll();
    }

    public Optional<SystemSettings> findById(Long id) {
        return this.systemSettingsRepository.findById(id);
    }


    public void save(SystemSettings systemSettings) {
        //if there are no officeHours, save it as a new instance
        if (systemSettingsRepository.findAll().size() == 0) {
            systemSettingsRepository.save(systemSettings);
        }
        //If there are already officeHours, use only 1st instance
        else {
            SystemSettings tmpSystemSettings = systemSettingsRepository.findAll().get(0);
            tmpSystemSettings.setCurrentOfficeHours(systemSettings.getCurrentOfficeHours());
            systemSettingsRepository.save(systemSettings);
        }
    }

    public void delete(SystemSettings systemSettings) {
        this.systemSettingsRepository.delete(systemSettings);
    }

    public Boolean deleteById(Long id) {
        if (this.exists(id)) {
            this.systemSettingsRepository.deleteById(id);
            return !this.exists(id);
        } else {
            return false;
        }
    }

    private Boolean exists(Long id) {
        SystemSettings systemSettings = (SystemSettings) this.systemSettingsRepository.findById(id).orElse(null);
        return systemSettings != null;
    }

}
