package steps;

import config.SpringConfig;
import org.springframework.test.context.ContextConfiguration;
import io.cucumber.spring.CucumberContextConfiguration;

@CucumberContextConfiguration
@ContextConfiguration(classes = {SpringConfig.class})
public class CucumberSpringConfiguration {
}