package be.uantwerpen.labplanner.Model;


import be.uantwerpen.labplanner.LabplannerApplication;
import be.uantwerpen.labplanner.common.model.stock.Product;
import be.uantwerpen.labplanner.common.model.stock.Tag;
import be.uantwerpen.labplanner.common.model.stock.Unit;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = LabplannerApplication.class)
@WebAppConfiguration
public class ProductTests {

    private Product product;

    @Test
    public void TestProduct() throws Exception{
        product = new Product();

        assertNull(product.getName());
        assertNull(product.getTags());
        assertNull(product.getDescription());
        assertNull(product.getStockLevel());
        assertNull(product.getLowStockLevel());
        assertNull(product.getReservedStockLevel());
        assertNull(product.getUnits());

        String name = "name";
        product.setName(name);
        assertEquals(product.getName(), name);

        List<Tag> tags= new ArrayList<>();
        tags.add(new Tag("name"));
        product.setTags(tags);
        assertEquals(product.getTags(), tags);

        String desc = "descr";
        product.setDescription(desc);
        assertEquals(product.getDescription(), desc);

        double stocklevel = 5.0;
        product.setStockLevel(stocklevel);
        assertEquals(product.getStockLevel(),stocklevel);
        product.setLowStockLevel(stocklevel);
        assertEquals(product.getLowStockLevel(),stocklevel);
        product.setReservedStockLevel(stocklevel);
        assertEquals(product.getReservedStockLevel(),stocklevel);

        //invalid stockelvel

        Unit unit = Unit.KILOGRAM;
        product.setUnits(unit);
        assertEquals(product.getUnits(), unit);


    }
}
