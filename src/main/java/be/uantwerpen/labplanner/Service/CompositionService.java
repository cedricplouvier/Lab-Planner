package be.uantwerpen.labplanner.Service;

import be.uantwerpen.labplanner.Model.Composition;
import be.uantwerpen.labplanner.Model.Mixture;
import be.uantwerpen.labplanner.Repository.CompositionRepository;
import be.uantwerpen.labplanner.Repository.MixtureRepository;
import be.uantwerpen.labplanner.common.model.stock.Tag;
import com.zaxxer.hikari.metrics.micrometer.MicrometerMetricsTracker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.util.List;
import java.util.Optional;


@Service
public class CompositionService {
    @Autowired
    private CompositionRepository compositionRepository;

    public CompositionService(){

    }

    public List<Composition> findAll(){
        return  this.compositionRepository.findAll();
    }

    public Optional<Composition> findById(Long id){
        return  this.compositionRepository.findById(id);
    }


    public Composition save(Composition composition){
        return this.compositionRepository.save(composition);
    }

    public void delete(Composition composition){
        this.compositionRepository.delete(composition);
    }

    public Boolean deleteById(Long id) {
        if (this.exists(id)) {
            this.compositionRepository.deleteById(id);
            return !this.exists(id);
        } else {
            return false;
        }
    }

    private Boolean exists(Long id) {
        Composition composition = (Composition) this.compositionRepository.findById(id).orElse(null);
        return composition != null;
    }

}
