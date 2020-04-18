package be.uantwerpen.labplanner.Service;

import be.uantwerpen.labplanner.Model.PieceOfMixture;
import be.uantwerpen.labplanner.Repository.PieceOfMixtureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class PieceOfMixtureService {
    @Autowired
    private PieceOfMixtureRepository pieceOfMixtureRepository;

    public PieceOfMixtureService(){

    }

    public List<PieceOfMixture> findAll(){
        return  this.pieceOfMixtureRepository.findAll();
    }

    public Optional<PieceOfMixture> findById(Long id){
        return  this.pieceOfMixtureRepository.findById(id);
    }


    public PieceOfMixture save(PieceOfMixture pieceOfMixtureRepository){
        return this.pieceOfMixtureRepository.save(pieceOfMixtureRepository);
    }

    public void delete(PieceOfMixture pieceOfMixtureRepository){
        this.pieceOfMixtureRepository.delete(pieceOfMixtureRepository);
    }

    public Boolean deleteById(Long id) {
        if (this.exists(id)) {
            this.pieceOfMixtureRepository.deleteById(id);
            return !this.exists(id);
        } else {
            return false;
        }
    }

    private Boolean exists(Long id) {
        PieceOfMixture pieceOfMixture = (PieceOfMixture)this.pieceOfMixtureRepository.findById(id).orElse(null);
        return pieceOfMixture != null;
    }

}
