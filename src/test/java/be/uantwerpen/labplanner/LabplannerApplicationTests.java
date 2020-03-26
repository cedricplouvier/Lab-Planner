package be.uantwerpen.labplanner;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(classes = LabplannerApplication.class)
@WebAppConfiguration
class LabplannerApplicationTests {
    @Test
    void contextLoads() {
    }
}
