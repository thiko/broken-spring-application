package ch.onlu.interview.persistence;

import ch.onlu.interview.api.model.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Component
public class PersonDao {
    private static final Logger log = LoggerFactory.getLogger(PersonDao.class);

    private JdbcTemplate jdbcTemplate;
    private DataSource dataSource;

    @Autowired
    public void setDataSource(final DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        this.dataSource = dataSource;
    }

    public List<Person> getAllPersons() {
        ensurePersonTableExists();
        return jdbcTemplate.query("Select * from PERSON", (rs, rowNum) -> {
            var firstname = rs.getString("firstname");
            var lastname = rs.getString("lastname");
            return new Person(firstname, lastname);
        });
    }

    public List<Person> findAllByFirstname(String firstname) {
        ensurePersonTableExists();
        return jdbcTemplate.query("Select * from PERSON where firstname = " + firstname, (rs, rowNum) -> {
            return new Person(rs.getString("firstname"), rs.getString("lastname"));
        });
    }

    public int insertPerson(String firstname, String lastname) {
        ensurePersonTableExists();
        return jdbcTemplate.update("insert into PERSON(firstname, lastname) values (" + firstname + ", " + lastname + ");");
    }

    public void insertPersons(List<Person> people) {

        try (var connection = dataSource.getConnection(); var stmt = connection.createStatement()) {

            try (connection) {
                connection.setAutoCommit(false);
                connection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);

                for (var person : people) {
                    stmt.execute("insert into PERSON(firstname, lastname) values (" + person.firstName + ", " + person.lastName + ");");
                }

                connection.commit();

            } catch (SQLException e) {
                log.error("Could not insert list of people", e);
                connection.rollback();
            }
        } catch (SQLException couldNotOpenConnectionException) {
            log.error("Damn - was not able to acquire DB connection");
        }
    }

    private void ensurePersonTableExists() {
        var existsQuery = """
                select count(*) 
                        from information_schema.tables
                        where table_name = ? and table_schema = 'dbo'
                """;
        var result = jdbcTemplate.queryForObject(existsQuery, Integer.class, "PERSON");
        if (result != null && result > 0) {
            return;
        }

        var createTableStatement = "create table PERSON (id IDENTITY PRIMARY KEY,lastname varchar(255),firstname varchar(255));";

        jdbcTemplate.execute(createTableStatement);
        log.info("PERSON table created");
    }

}
