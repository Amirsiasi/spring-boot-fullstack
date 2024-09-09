package at.spengergasse.springbootexample.customer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import static org.mockito.Mockito.verify;


class CustomerJPADataAccessServiceTest {

      private CustomerJPADataAccessService underTest;

       @Mock
        private  CustomerRepository customerRepository;

       private AutoCloseable autoCloseable;






    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new CustomerJPADataAccessService(customerRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void itShouldSelectAllCustomers() {

        underTest.selectAllCustomers();

        verify(customerRepository).findAll();
    }

    @Test
    void itShouldSelectCustomerById() {
        //Given
        var id = 1l;
        underTest.selectCustomerById(id);
        //When
        verify(customerRepository).findById(id);
        //Then
    }

    @Test
    void itShouldInsertCustomer() {
        //Given
        //When
        Customer customer = new Customer("amir", "amir@gmail.com", 23);
        underTest.insertCustomer(customer);
        //Then
        verify(customerRepository).save(customer);
    }

    @Test
    void itShouldExistsCustomerWithEmail() {
        //Given
        var email = "amir@gmail.com";
        underTest.existsCustomerWithEmail(email);
        //When
        //Then
        verify(customerRepository).existsByEmail(email);
    }

    @Test
    void itShouldExistsCustomerWithId() {
        //Given
        var id = 1l;
        underTest.existsCustomerWithId(id);
        //When
        verify(customerRepository).existsById(id);
        //Then
    }

    @Test
    void itShouldDeleteCustomerById() {
        //Given
        long id = 1l;
        underTest.deleteCustomerById(id);
        //When
        verify(customerRepository).deleteById(id);
        //Then
    }

    @Test
    void itShouldUpdateCustomer() {
        //Given
        Customer customer = new Customer("amir", "amir@gmail.com", 23);
        underTest.updateCustomer(customer);
        verify(customerRepository).save(customer);
        //When
        //Then
    }
}