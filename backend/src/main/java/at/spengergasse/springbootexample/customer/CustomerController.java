package at.spengergasse.springbootexample.customer;

import at.spengergasse.springbootexample.exception.ResourceNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService, CustomerRepository customerRepository) {
        this.customerService = customerService;
    }



    //@RequestMapping(path = "api/v1/customers",method = RequestMethod.GET)
    @GetMapping
    public List<Customer> getCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("{customerId}")
    public Customer getCustomers(@PathVariable("customerId") Long customerId) throws ResourceNotFound {
        return customerService.getCustomerById(customerId);
    }
    @PostMapping
    public void registerCustomer(@RequestBody CustomerRegistrationRequest request) {
        customerService.addCustomer(request);
    }

    @DeleteMapping("{customerId}")
    public void deleteCustomer(@PathVariable(name = "customerId") Long customerId) throws ResourceNotFound {
        if (!customerService.existsCustomerWithId(customerId)) {
            throw new ResourceNotFound("customerId cannot be null");
        }
        customerService.deleteCustomer(customerId);
    }
    @PutMapping("{customerId}")
    public void updateCustomer(
            @PathVariable("customerId") Long customerId,
            @RequestBody CustomerUpdateRequest updateRequest) throws ResourceValidationException, ResourceNotFound {
        customerService.updateCustomer(customerId,updateRequest);

    }


}
