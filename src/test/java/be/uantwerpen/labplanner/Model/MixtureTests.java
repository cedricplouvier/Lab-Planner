package be.uantwerpen.labplanner.Model;

import be.uantwerpen.labplanner.LabplannerApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.management.MXBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = LabplannerApplication.class)
@WebAppConfiguration
public class MixtureTests {

    private Mixture mixture;

    @Test
    public void TestMixture() throws  Exception{
        mixture = new Mixture();

        assertEquals(mixture.getName(), "");
        assertEquals(mixture.getDescription(),"");
        assertEquals(mixture.getCompositions(), new ArrayList<Composition>());

        String name = "name";
        mixture.setName(name);
        assertEquals(mixture.getName(), name);

        String desc = "description";
        mixture.setDescription(desc);
        assertEquals(mixture.getDescription(), desc);

        String filename = "filename";
        mixture.setImage(filename);
        assertEquals(mixture.getImage(), filename);


        Long id = (long) 5;
        mixture.setId(id);
        assertEquals(mixture.getId(),id);

        List<Composition>  compositionList = new ArrayList<>();
        compositionList.add(new Composition());
        mixture.setCompositions(compositionList);
        assertEquals(mixture.getCompositions(), compositionList);



    }

}
