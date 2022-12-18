package ch.onlu.interview.api;

import ch.onlu.interview.api.model.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("person")
public class PersonController {

    private static final Logger log = LoggerFactory.getLogger(PersonController.class);

    List<Person> people = new ArrayList<>();

    public PersonController() {
        people.add(new Person("Max", "Musterich"));
        people.add(new Person("Peter", "Lustig"));
    }

    @GetMapping("/")
    public List<Person> getAll() {
        return people;
    }

    @PostMapping("/")
    public ResponseEntity<Void> add(Person person) {
        people.add(person);
        log.info("New person added: {}", person);

        return ResponseEntity.status(HttpStatusCode.valueOf(201)).build();
    }
}
