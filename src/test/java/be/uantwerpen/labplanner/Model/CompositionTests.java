package be.uantwerpen.labplanner.Model;


import be.uantwerpen.labplanner.LabplannerApplication;
import be.uantwerpen.labplanner.common.model.stock.Product;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = LabplannerApplication.class)
@WebAppConfiguration
public class CompositionTests {

    private Composition composition;

    @Test
    public void TestComposition() throws Exception{

        composition = new Composition();

        assertNull(composition.getProduct());
        assertEquals(composition.getAmount(), 0.0);


        //setvalid amount
        double amount = 5.0;
        composition.setAmount(amount);
        assertEquals(composition.getAmount(), amount);

        //Set invalid amount
        double falseAmount = -2.0;
        composition.setAmount(falseAmount);
        assertNotEquals(composition.getAmount(), falseAmount);

        Long id = (long) 5;
        composition.setId(id);
        assertEquals(composition.getId(), id);

        Product product = new Product();
        composition.setProduct(product);
        assertEquals(composition.getProduct(), product);




    }
}
