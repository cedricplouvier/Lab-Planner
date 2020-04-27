package be.uantwerpen.labplanner.Service;


import be.uantwerpen.labplanner.LabplannerApplication;
import be.uantwerpen.labplanner.Model.OwnProduct;
import be.uantwerpen.labplanner.Repository.OwnProductRepository;
import be.uantwerpen.labplanner.common.repository.stock.TagRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = LabplannerApplication.class)
@WebAppConfiguration
public class ProductServiceTests {

    @Autowired
    private OwnTagService tagService;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private OwnProductService productService;

    @Autowired
    private OwnProductRepository productRepository;

    @Test
    public void testGetProductById(){
        OwnProduct product = new OwnProduct();
        product.setName("test");
        productRepository.save(product);
        OwnProduct fetched = productService.findById(product.getId()).orElse(null);
        assertEquals(fetched.getName(), "test");

    }

    @Test
    public void testGetAllProducts(){
        List<OwnProduct> fetchedList  = productService.findAll();
        assertNotNull(fetchedList);

    }

    @Test
    public void testGetAllProductsByTag(){
        List<OwnProduct> fetchedList  = productService.findAllByTag(tagService.findByName("Aggregates").orElse(null));
        assertNotNull(fetchedList);
    }

    @Test
    public void testDeleteById(){
        OwnProduct product = new OwnProduct();
        product.setName("test");
        productRepository.save(product);
        List<OwnProduct> fetchedList  = productService.findAll();
        productService.deleteById(product.getId());
        List<OwnProduct> fetchedList2  = productService.findAll();
        assertEquals(fetchedList.size()-1, fetchedList2.size());
    }

}
