package at.spengergasse.springbootexample.customer;

import at.spengergasse.springbootexample.exception.DuplicateResourceNotFound;
import at.spengergasse.springbootexample.exception.ResourceNotFound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {
    private CustomerService underTest;

    @Mock
    private CustomerDao customerDao;

    @Captor
    private ArgumentCaptor<Customer> customerCaptor;

    @BeforeEach
    void setUp() {

        underTest = new CustomerService(customerDao);
    }



    @Test
    void itShouldGetAllCustomers() {
        //Given
        underTest.getAllCustomers();
        //When
        verify(customerDao).selectAllCustomers();
        //Then
    }

    @Test
    void itShouldGetCustomerById() {

        //Given
        var id = 1L;
        var customer = new Customer(

                id,"amir","amir@gmail.com",19
        );
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        //When
        var actual = underTest.getCustomerById(id);

        //Then
        assertThat(actual).isEqualTo(customer);

    }

    @Test
    void WillThrowWhenGetReturnEmptyCustomer() {

        //Given
        var id = 1L;

        when(customerDao.selectCustomerById(id)).thenReturn(Optional.empty());
        //When


        //Then
        assertThatThrownBy(()->underTest.getCustomerById(id))
                .isInstanceOf(ResourceNotFound.class)
                .hasMessageContaining("Customer with id " + id + " not found");

    }

    @Test
    void itShouldAddCustomer() {
        //Given
        String email = "amir@gmail.com";

        when(customerDao.existsCustomerWithEmail(email)).thenReturn(false);
        var request = new CustomerRegistrationRequest("amir",email,19);
        //When
        underTest.addCustomer(request);
        //Then
        customerCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).insertCustomer(customerCaptor.capture());
        Customer customerCaptorValue = customerCaptor.getValue();


        assertThat(customerCaptorValue.getId()).isNull();
        assertThat(customerCaptorValue.getName()).isEqualTo(request.name());
        assertThat(customerCaptorValue.getEmail()).isEqualTo(request.email());
        assertThat(customerCaptorValue.getAge()).isEqualTo(request.age());
    }

    @Test
    void willThrowWhenEmailExistsWhileAddingAnotherCustomer() {
        //Given
        String email = "amir@gmail.com";
        when(customerDao.existsCustomerWithEmail(email)).thenReturn(true);
        var request = new CustomerRegistrationRequest("amir",email,19);

        //When
        assertThatThrownBy(()->underTest.addCustomer(request))
        .isInstanceOf(DuplicateResourceNotFound.class)
                .hasMessageContaining("Customer with email " + email + " already exists");
        //Then

        verify(customerDao,never()).insertCustomer(any(Customer.class));
    }

    @Test
    void itShouldDeleteCustomer() {
        //Given
        var id = 1L;
        when(customerDao.existsCustomerWithId(id)).thenReturn(true);
        //When
        underTest.deleteCustomer(id);
        //Then
        verify(customerDao).deleteCustomerById(id);
    }

    @Test
    void willThrowWhenCustomerDoesNotExistWhileDeletingAnotherCustomer() {
        //Given
        var id = 1L;
        when(customerDao.existsCustomerWithId(id)).thenReturn(false);

        assertThatThrownBy(()-> underTest.deleteCustomer(id))
        .isInstanceOf(ResourceNotFound.class)
                .hasMessageContaining("Customer with id " + id + " not found");
        //When
        verify(customerDao,never()).deleteCustomerById(any(Long.class));
        //Then
    }

    @Test
    void itShouldUpdateCustomer() throws ResourceValidationException {
        //Given
        var id = 1L;

        var customer = new Customer("amir","amir@gmail.com",19);
        String newEmail  = "amirsiasi@gmail.com";
        var updateRequest = new CustomerUpdateRequest("amirsiasi", newEmail ,23);
        //When
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        when(customerDao.existsCustomerWithEmail(newEmail)).thenReturn(false);
        underTest.updateCustomer(id,updateRequest);
        //Then

        verify(customerDao).updateCustomer(customerCaptor.capture());
        Customer customerCaptorValue = customerCaptor.getValue();
        assertThat(customerCaptorValue.getName()).isEqualTo(updateRequest.name());
        assertThat(customerCaptorValue.getEmail()).isEqualTo(updateRequest.email());
        assertThat(customerCaptorValue.getAge()).isEqualTo(updateRequest.age());

    }
    @Test
    void itShouldUpdateOnlyCustomerName() throws ResourceValidationException {
        //Given
        var id = 1L;

        var customer = new Customer("amir","amir@gmail.com",19);
        String newEmail  = "amirsiasi@gmail.com";
        var updateRequest = new CustomerUpdateRequest("amirsiasi", null ,null);
        //When
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        underTest.updateCustomer(id,updateRequest);
        //Then

        verify(customerDao).updateCustomer(customerCaptor.capture());
        Customer customerCaptorValue = customerCaptor.getValue();
        assertThat(customerCaptorValue.getName()).isEqualTo(updateRequest.name());
        assertThat(customerCaptorValue.getEmail()).isEqualTo(customer.getEmail());
        assertThat(customerCaptorValue.getAge()).isEqualTo(customer.getAge());

    }
    @Test
    void itShouldUpdateOnlyCustomerEmail() throws ResourceValidationException {
        //Given
        var id = 1L;

        var customer = new Customer("amir","amir@gmail.com",19);
        String newEmail  = "amirsiasi@gmail.com";
        var updateRequest = new CustomerUpdateRequest(null, newEmail ,null);
        //When
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        when(customerDao.existsCustomerWithEmail(newEmail)).thenReturn(false);

        underTest.updateCustomer(id,updateRequest);
        //Then

        verify(customerDao).updateCustomer(customerCaptor.capture());
        Customer customerCaptorValue = customerCaptor.getValue();
        assertThat(customerCaptorValue.getName()).isEqualTo(customer.getName());
        assertThat(customerCaptorValue.getEmail()).isEqualTo(updateRequest.email());
        assertThat(customerCaptorValue.getAge()).isEqualTo(customer.getAge());

    }
    @Test
    void itShouldUpdateOnlyCustomerAge() throws ResourceValidationException {
        //Given
        var id = 1L;

        var customer = new Customer("amir","amir@gmail.com",19);
        String newEmail  = "amirsiasi@gmail.com";
        var updateRequest = new CustomerUpdateRequest(null, null ,22);
        //When
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        underTest.updateCustomer(id,updateRequest);
        //Then

        verify(customerDao).updateCustomer(customerCaptor.capture());
        Customer customerCaptorValue = customerCaptor.getValue();
        assertThat(customerCaptorValue.getName()).isEqualTo(customer.getName());
        assertThat(customerCaptorValue.getEmail()).isEqualTo(customer.getEmail());
        assertThat(customerCaptorValue.getAge()).isEqualTo(updateRequest.age());

    }
    @Test
    void willThrowWhenTryingUpdateCustomerEmailWhenAlreadyTaken() throws ResourceValidationException {
        //Given
        var id = 1L;

        var customer = new Customer("amir","amir@gmail.com",19);
        String newEmail  = "amirsiasi@gmail.com";
        var updateRequest = new CustomerUpdateRequest(null, newEmail ,null);
        //When
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        when(customerDao.existsCustomerWithEmail(newEmail)).thenReturn(true);

        //Then

        assertThatThrownBy(() -> underTest.updateCustomer(id,updateRequest))
                .isInstanceOf(DuplicateResourceNotFound.class)
                .hasMessageContaining("Customer with email already exists");

        verify(customerDao,never()).updateCustomer(any());
    }
    @Test
    void willThrowWhenCustomerUpdateUpdateHasNoChanges() throws ResourceValidationException {
        //Given
        var id = 1L;

        var customer = new Customer("amir","amir@gmail.com",19);
        var updateRequest = new CustomerUpdateRequest(customer.getName(), customer.getEmail() ,customer.getAge());
        //When
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        //Then

        assertThatThrownBy(() -> underTest.updateCustomer(id,updateRequest))
                .isInstanceOf(ResourceValidationException.class)
                .hasMessageContaining("no data changes found");

        verify(customerDao,never()).updateCustomer(any());


    }
}