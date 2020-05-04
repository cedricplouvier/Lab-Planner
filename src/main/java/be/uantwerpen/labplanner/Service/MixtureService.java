package be.uantwerpen.labplanner.Service;

import be.uantwerpen.labplanner.Model.Mixture;
import be.uantwerpen.labplanner.Repository.MixtureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.util.List;
import java.util.Optional;


@Service
public class MixtureService {
    @Autowired
    private MixtureRepository mixtureRepository;

    public MixtureService(){

    }

    public List<Mixture> findAll(){
        return  this.mixtureRepository.findAll();
    }

    public Optional<Mixture> findById(Long id){
        return  this.mixtureRepository.findById(id);
    }

    public Optional<Mixture> findByName(String name){
        return this.mixtureRepository.findByName(name);
    }

    public Mixture save(Mixture mixture){
        return this.mixtureRepository.save(mixture);
    }

    public void saveSome(Mixture mixture){

        Mixture tempMix = mixture.getId() ==null? null: mixtureRepository.findById(mixture.getId()).orElse(null);
        if (tempMix != null){
            tempMix.setName(mixture.getName());
            tempMix.setCompositions(mixture.getCompositions());
            tempMix.setDescription(mixture.getDescription());
            tempMix.setTags(mixture.getTags());
            if(mixture.getImage() != null){
                tempMix.setImage(mixture.getImage());
            }
            if(mixture.getDocument()!=null){
                tempMix.setDocument(mixture.getDocument());
            }

            mixtureRepository.save(tempMix);

        }
        else {
            mixtureRepository.save(mixture);
        }
    }
    

    public void delete(Mixture mixture){
        this.mixtureRepository.delete(mixture);
    }

    public Boolean deleteById(Long id) {
        if (this.exists(id)) {
            this.mixtureRepository.deleteById(id);
            return !this.exists(id);
        } else {
            return false;
        }
    }

    public Boolean exists(Long id) {
        Mixture mixture = (Mixture)this.mixtureRepository.findById(id).orElse(null);
        return mixture != null;
    }

}
