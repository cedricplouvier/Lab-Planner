package be.uantwerpen.labplanner.Service;

import be.uantwerpen.labplanner.Model.Composition;
import be.uantwerpen.labplanner.Model.Relation;
import be.uantwerpen.labplanner.Repository.RelationRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public class RelationService {

    @Autowired
    private RelationRepository relationRepository;

    public RelationService() {
    }

    public List<Relation> findAll(){
        return this.relationRepository.findAll();
    }

    public Optional<Relation> findByResearcher(long ID){
        return this.relationRepository.findByResearcherID(ID);
    }

    public void delete(long id){relationRepository.delete(relationRepository.findById(id).orElse(null));}

    public Relation save(Relation relation){
        return this.relationRepository.save(relation);
    }

    public Boolean deleteById(Long id) {
        if (this.exists(id)) {
            this.relationRepository.deleteById(id);
            return !this.exists(id);
        } else {
            return false;
        }
    }

    private Boolean exists(Long id) {
        Relation relation = (Relation) this.relationRepository.findById(id).orElse(null);
        return relation != null;
    }




}
