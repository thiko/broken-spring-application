package ch.onlu.interview;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PersonIT {

    @Autowired
    private TestRestTemplate template;

    @Test
    public void getPerson() throws Exception {
        ResponseEntity<String> response = template.getForEntity("/person/", String.class);
        assertThat(response.getBody()).isEqualTo("Definitely correct response!");
    }

    @Test
    public void addPerson() throws Exception {
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(new LinkedMultiValueMap<>(), new HttpHeaders());

        ResponseEntity<String> response = template.postForEntity("/person/manual?firstname=hans&lastname=meier", request, String.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNull();
    }
}
