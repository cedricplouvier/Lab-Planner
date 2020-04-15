package be.uantwerpen.labplanner.Service;

import be.uantwerpen.labplanner.Model.Report;
import be.uantwerpen.labplanner.Repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReportService {

    @Autowired
    ReportRepository reportRepository;

    public  ReportService(){

    }

    public List<Report> findAll(){
        return this.reportRepository.findAll();
    }

    public Optional<Report> findById(Long id){
        return this.reportRepository.findById(id);
    }

    public Report save(Report report){
        return this.reportRepository.save(report);
    }

    public void delete(Report report){
        this.reportRepository.delete(report);
    }

    public Boolean deleteById(Long id) {
        if (this.exists(id)) {
            this.reportRepository.deleteById(id);
            return !this.exists(id);
        } else {
            return false;
        }
    }
    private Boolean exists(Long id) {
        Report report = (Report) this.reportRepository.findById(id).orElse(null);
        return report != null;
    }




}
