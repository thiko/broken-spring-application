package ch.onlu.interview.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;

@Configuration
public class AppConfig {

    @Value("app.crash-in-minutes")
    public String minutesUntilCrash;

    @Bean
    public DataSource buildDatasource() {
        return new SimpleDriverDataSource(new org.h2.Driver(), "jdbc:h2:mem:testdb");
    }

}
