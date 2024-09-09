package at.spengergasse.springbootexample.customer;

import at.spengergasse.springbootexample.AbstractTestcontainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest extends AbstractTestcontainer {
    @Autowired
    private  CustomerRepository underTest;




    @BeforeEach
    void setUp() {
        underTest.deleteAll();
    }

    @Test
    void itShouldExistsByEmail() {
        //Given
        String email = FAKER.internet().emailAddress() + "-" + UUID.randomUUID();

        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );
        //When
        Customer saved = underTest.save(customer);

        boolean existsed = underTest.existsByEmail(email);
        //Then
        assertThat(saved).isEqualTo(customer);
        assertThat(existsed).isTrue();

    }
}