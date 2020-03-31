package be.uantwerpen.labplanner;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LabplannerApplicationTests {
    @Test
    void contextLoads() {
    }

    @Test
    void extraCheck(){
        int id = 0;
        assertEquals(id,0);
    }
}