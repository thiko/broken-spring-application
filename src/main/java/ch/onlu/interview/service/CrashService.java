package ch.onlu.interview.service;

import ch.onlu.interview.api.PersonController;
import ch.onlu.interview.config.AppConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

public class CrashService {

    private static final Logger log = LoggerFactory.getLogger(PersonController.class);

    @Autowired
    AppConfig appConfig;

    @Scheduled(fixedDelayString = "#{appConfig.minutesUntilCrash}")
    public void scheduledCrash() {
        log.warn("Self destroy mechanism executed. Ciao!");
        System.exit(1);
    }


}
