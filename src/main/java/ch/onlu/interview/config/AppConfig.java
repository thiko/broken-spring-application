package ch.onlu.interview.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Value("app.crash-in-minutes")
    public String minutesUntilCrash;


}
