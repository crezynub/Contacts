package Skillbox.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@ComponentScan("Skillbox")
@Configuration
@PropertySource("classpath:application.properties")
public class AppConfig {

}
