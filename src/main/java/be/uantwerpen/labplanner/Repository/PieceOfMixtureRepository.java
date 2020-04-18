package be.uantwerpen.labplanner.Repository;

import be.uantwerpen.labplanner.Model.PieceOfMixture;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PieceOfMixtureRepository extends CrudRepository<PieceOfMixture, Long> {
    List<PieceOfMixture> findAll();

    Optional<PieceOfMixture> findById(Long aLong);



}
