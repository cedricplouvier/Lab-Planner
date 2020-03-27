package be.uantwerpen.labplanner.Service;


import be.uantwerpen.labplanner.LabplannerApplication;
import be.uantwerpen.labplanner.common.model.stock.Product;
import be.uantwerpen.labplanner.common.model.stock.Tag;
import be.uantwerpen.labplanner.common.repository.stock.ProductRepository;
import be.uantwerpen.labplanner.common.repository.stock.TagRepository;
import be.uantwerpen.labplanner.common.service.stock.ProductService;
import be.uantwerpen.labplanner.common.service.stock.TagService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LabplannerApplication.class)
public class ProductServiceTests {

    @Autowired
    private TagService tagService;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void testGetProductById(){
        Product product = new Product();
        product.setName("test");
        productRepository.save(product);
        Product fetched = productService.findById(product.getId()).orElse(null);
        assertEquals(fetched.getName(), "test");

    }

    @Test
    public void testGetAllProducts(){
        List<Product> fetchedList  = productService.findAll();
        assertNotNull(fetchedList);

    }

    @Test
    public void testGetAllProductsByTag(){
        List<Product> fetchedList  = productService.findAllByTag(tagService.findByName("Aggregates").orElse(null));
        assertNotNull(fetchedList);
    }

    @Test
    public void TestDeleteById(){
        Product product = new Product();
        product.setName("test");
        productRepository.save(product);
        List<Product> fetchedList  = productService.findAll();
        productService.deleteById(product.getId());
        List<Product> fetchedList2  = productService.findAll();
        assertEquals(fetchedList.size()-1, fetchedList2.size());
    }

}
