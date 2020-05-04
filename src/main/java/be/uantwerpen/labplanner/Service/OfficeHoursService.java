package be.uantwerpen.labplanner.Service;

import be.uantwerpen.labplanner.Model.OfficeHours;
import be.uantwerpen.labplanner.Model.PieceOfMixture;
import be.uantwerpen.labplanner.Model.Step;
import be.uantwerpen.labplanner.Repository.OfficeHoursRepository;
import be.uantwerpen.labplanner.Repository.PieceOfMixtureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class OfficeHoursService {
    @Autowired
    private OfficeHoursRepository officeHoursRepository;

    public OfficeHoursService() {

    }

    public List<OfficeHours> findAll() {
        return this.officeHoursRepository.findAll();
    }

    public Optional<OfficeHours> findById(Long id) {
        return this.officeHoursRepository.findById(id);
    }


    public void save(OfficeHours officeHours) {
        //if there are no officeHours, save it as a new instance
        if (officeHoursRepository.findAll().size() == 0) {
            officeHoursRepository.save(officeHours);
        }
        //If there are already officeHours, use only 1st instance
        else {
            OfficeHours tmpOfficeHours = officeHoursRepository.findAll().get(0);
            tmpOfficeHours.setStartHour(officeHours.getStartHour());
            tmpOfficeHours.setStartMinute(officeHours.getStartMinute());
            tmpOfficeHours.setEndHour(officeHours.getEndHour());
            tmpOfficeHours.setEndMinute(officeHours.getEndMinute());
            tmpOfficeHours.setOfficeHoursOn(officeHours.isOfficeHoursOn());
            tmpOfficeHours.setWeekendOn(officeHours.isWeekendOn());
            tmpOfficeHours.setHolidaysOn(officeHours.isHolidaysOn());
            officeHoursRepository.save(tmpOfficeHours);
        }
    }

    public void delete(OfficeHours officeHours) {
        this.officeHoursRepository.delete(officeHours);
    }

    public Boolean deleteById(Long id) {
        if (this.exists(id)) {
            this.officeHoursRepository.deleteById(id);
            return !this.exists(id);
        } else {
            return false;
        }
    }

    private Boolean exists(Long id) {
        OfficeHours officeHours = (OfficeHours) this.officeHoursRepository.findById(id).orElse(null);
        return officeHours != null;
    }

}
