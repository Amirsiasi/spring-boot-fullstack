package at.spengergasse.springbootexample.customer;

import at.spengergasse.springbootexample.exception.DuplicateResourceNotFound;
import at.spengergasse.springbootexample.exception.ResourceNotFound;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private CustomerDao customerDao;


    public CustomerService(@Qualifier("jdbc") CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public List<Customer> getAllCustomers() {
        return customerDao.selectAllCustomers();
    }

    public Customer getCustomerById(Long id) throws ResourceNotFound {
        return customerDao.selectCustomerById(id)
                .orElseThrow(() -> new ResourceNotFound("Customer with id " + id + " not found"));
    }

    public void addCustomer(
            CustomerRegistrationRequest customerRegistrationRequest) {


        var email = customerRegistrationRequest.email();
        var name = customerRegistrationRequest.name();
        var age = customerRegistrationRequest.age();

        if(customerDao.existsCustomerWithEmail(email)){
            throw new DuplicateResourceNotFound("Customer with email " + email + " already exists");
        }

        var customer = new Customer(name, email, age);
        customerDao.insertCustomer(customer);
    }

//    private void checkEmail(String email, String x) {
//        if (customerDao.existsCustomerWithEmail(email)) {
//            throw new DuplicateResourceNotFound("Customer with email " + email + x);
//        }
//    }

    public boolean existsCustomerWithId(Long id) {
        return customerDao.existsCustomerWithId(id);
    }

    public void deleteCustomer(Long id) {
        if(!customerDao.existsCustomerWithId(id)) {
            throw new ResourceNotFound("Customer with id " + id + " not found");
        }
        customerDao.deleteCustomerById(id);
    }

    public void updateCustomer(Long customerId, CustomerUpdateRequest updateRequest) throws ResourceValidationException {

        var customer = getCustomerById(customerId);

        boolean changes = false;

        if(updateRequest.name()!=null && !updateRequest.name().equals(customer.getName())) {
            customer.setName(updateRequest.name());
            changes = true;
        }
        if(updateRequest.email()!=null && !updateRequest.email().equals(customer.getEmail())) {
           // checkEmail(updateRequest.email(), " already exists");
            if(customerDao.existsCustomerWithEmail(updateRequest.email())){
                throw new DuplicateResourceNotFound("Customer with email already exists");
            }
            customer.setEmail(updateRequest.email());
            changes = true;
        }
        if(updateRequest.age()!=null && !updateRequest.age().equals(customer.getAge())) {
            customer.setAge(updateRequest.age());
            changes = true;
        }
        if(!changes) {
            throw new ResourceValidationException("no data changes found");
        }
        customerDao.updateCustomer(customer);

    }



}
