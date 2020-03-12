package be.uantwerpen.labplanner.Service;


import be.uantwerpen.labplanner.common.model.stock.Product;
import be.uantwerpen.labplanner.common.model.stock.Tag;
import be.uantwerpen.labplanner.common.model.stock.Unit;
import be.uantwerpen.labplanner.common.repository.stock.ProductRepository;
import be.uantwerpen.labplanner.common.repository.stock.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Profile("default")
public class DatabaseLoader {
    private final ProductRepository productRepository;
    private final TagRepository tagRepository;



    @Autowired
    public DatabaseLoader(ProductRepository productRepository,
                          TagRepository tagRepository) {
        this.productRepository = productRepository;
        this.tagRepository = tagRepository;
    }
    @PostConstruct
    private void initDatabase() {
        Tag t1 = new Tag("Beton");
        List<Tag> tags1 = new ArrayList<>();
        tags1.add(t1);
        tagRepository.save(t1);
        Tag t2 = new Tag("Asfalt");
        List<Tag> tags2 = new ArrayList<>();
        tags2.add(t2);
        tagRepository.save(t2);
        Product p1 = new Product("Zand","Elon Musk",1.0, 1.0, 1.0, 1.0, Unit.KILOGRAM, "locatie2", "properties", 5L,5L, LocalDateTime.now(), LocalDateTime.now(), tags1);
        productRepository.save(p1);
        Product p2 = new Product("Cement","cemeeeenntttt",1.0, 1.0, 1.0, 1.0, Unit.KILOGRAM, "locatie2", "properties", 5L,5L, LocalDateTime.now(), LocalDateTime.now(), tags2);
        productRepository.save(p2);

    }
}
