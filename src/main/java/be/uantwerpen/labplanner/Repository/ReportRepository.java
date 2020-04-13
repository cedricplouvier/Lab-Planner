package be.uantwerpen.labplanner.Repository;

import be.uantwerpen.labplanner.Model.Report;
import org.springframework.data.repository.CrudRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface ReportRepository extends CrudRepository<Report, Long> {

    List<Report> findAll();

    Optional<Report> findById(long aLong);


}
