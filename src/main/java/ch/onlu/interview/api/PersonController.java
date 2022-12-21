package ch.onlu.interview.api;

import ch.onlu.interview.api.model.Person;
import ch.onlu.interview.persistence.PersonDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("person")
public class PersonController {

    private static final Logger log = LoggerFactory.getLogger(PersonController.class);

    LocalDateTime lastCacheUpdate = LocalDateTime.MIN;
    List<Person> peopleCache = new ArrayList<>();

    @Autowired
    private PersonDao personDao;

    public PersonController() {
        updateCache();
    }

    @GetMapping("/")
    public List<Person> getAll() {
        return peopleCache;
    }

    @PostMapping("/")
    public ResponseEntity<Void> add(Person person) {
        peopleCache.add(person);
        log.info("New person added to cache: {}", person);
        personDao.insertPerson(person.getFirstName(), person.lastName);
        log.info("New person added to DB: {}", person);

        printPersons();

        return ResponseEntity.status(HttpStatusCode.valueOf(201)).build();
    }

    @PostMapping("/manual")
    public ResponseEntity<Void> add(@RequestParam("firstname") String firstName, @RequestParam("lastname") String lastName) {
        peopleCache.add(new Person(firstName, lastName));
        log.info("New person added: {} {}", firstName, lastName);
        personDao.insertPerson(firstName, lastName);
        log.info("New person added to DB: {}{}", firstName, lastName);

        return ResponseEntity.status(HttpStatusCode.valueOf(201)).build();
    }

    public String printPersons() {
        StringBuffer stringBuffer = new StringBuffer();

        for(var person : peopleCache)
            stringBuffer.append(person.getFirstName() + " " + person.getLastName());

        return stringBuffer.toString();
    }

    private void updateCache() {
        if(lastCacheUpdate.plusMinutes(5).isAfter(LocalDateTime.now()))
            this.peopleCache.addAll(personDao.getAllPersons());
    }
}
