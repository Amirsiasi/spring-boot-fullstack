package at.spengergasse.springbootexample.customer;

import at.spengergasse.springbootexample.AbstractTestcontainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CustomerJDBCDataAccessServiceTest extends AbstractTestcontainer {

    private CustomerJDBCDataAccessService underTest;
    private final CustomerRowMapper customerRowMapper = new CustomerRowMapper();

    @BeforeEach
    void setUp() {
        underTest = new CustomerJDBCDataAccessService(
                getJdbcTemplate(),
                customerRowMapper
        );
    }

    @Test
    void itShouldSelectAllCustomers() {
        //Given



   Customer customer = new Customer(
           FAKER.name().firstName(),
           FAKER.internet().emailAddress() + "-"+UUID.randomUUID(),
           20
   );

       underTest.insertCustomer(customer);


        //When
        List<Customer> customers = underTest.selectAllCustomers();
        //Then


    }

    @Test
    void itShouldSelectCustomerById() {
        //Given

        String email = FAKER.internet().emailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().firstName(),
                email,
                20
        );
        underTest.insertCustomer(customer);
        var id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();



        //When
        var actual = underTest.selectCustomerById(id);
        //Then
      assertThat(actual).isPresent()
              .hasValueSatisfying(c->{
                  assertThat(c.getEmail()).isEqualTo(customer.getEmail());
                  assertThat(c.getName()).isEqualTo(customer.getName());
                  assertThat(c.getAge()).isEqualTo(customer.getAge());
                  assertThat(c.getId()).isEqualTo(id);
              });
    }

    @Test
    void willReturnEmptyOptionalWhenCustomerDoesNotExist() {
        var id = -1L;
        //Given

       var actual= underTest.selectCustomerById(id);

        //When
        //Then
        assertThat(actual).isNotPresent();
    }

    @Test
    void itShouldInsertCustomer() {
        //Given


    }

    @Test
    void itShouldExistsCustomerWithEmail() {
        //Given

        String email = FAKER.internet().emailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().firstName(),
                email,
                20
        );

        //When
        underTest.insertCustomer(customer);


        var existsed = underTest.existsCustomerWithEmail(email);
        //Then
        assertThat(existsed).isTrue();

    }

    @Test
    void existsCustomerWithEmailReturnsFalseWhenEmailDoesNotExist() {
        //Given
        String email = FAKER.internet().emailAddress() + "-" + UUID.randomUUID();
        //When
        boolean actual = underTest.existsCustomerWithEmail(email);
        //Then
        assertThat(actual).isFalse();
    }

    @Test
    void itShouldExistsCustomerWithId() {
        //Given
        String email = FAKER.internet().emailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().firstName(),
                email,
                20
        );

        underTest.insertCustomer(customer);

        var id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        //When
        var actual = underTest.existsCustomerWithId(id);
        //Then
        assertThat(actual).isTrue();
    }

    @Test
    void itShouldDeleteCustomerById() {
        //Given
        String email = FAKER.internet().emailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().firstName(),
                email,
                20
        );

        underTest.insertCustomer(customer);
        var id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        //When
        underTest.deleteCustomerById(id);
        //Then
        var actual = underTest.existsCustomerWithId(id);
        assertThat(actual).isFalse();



    }

    @Test
    void itShouldUpdateCustomer() {
        //Given

        String email = FAKER.internet().emailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().firstName(),
                email,
                20
        );

        //When
        underTest.insertCustomer(customer);
        var id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        //Then
          var newNAme = "foo";
          Customer updatedCustomer = new Customer();
          updatedCustomer.setId(id);
          updatedCustomer.setName(newNAme);
        //When
        underTest.updateCustomer(updatedCustomer);
        var actual = underTest.existsCustomerWithId(id);
        //Then
        assertThat(updatedCustomer.getName()).isEqualTo(newNAme);
    }
}