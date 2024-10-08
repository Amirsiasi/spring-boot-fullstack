package at.spengergasse.springbootexample.customer;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Repository("list")
public class CustomerListDataAccessService implements CustomerDao{

    private static List<Customer> customers;
    static {
        customers = new ArrayList<>();
        Customer alex= new Customer(
                1L,"Alex","alex@gmail.com",21
        );
        Customer nilu= new Customer(
                2L,"nilu","nilu@gmail.com",23
        );

        customers.add(alex);
        customers.add(nilu);
    }


    @Override
    public List<Customer> selectAllCustomers() {
        return customers;
    }

    @Override
    public Optional<Customer> selectCustomerById(Long id) {
        return  customers.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();


    }

    @Override
    public void insertCustomer(Customer customer) {
        customers.add(customer);
    }

    @Override
    public boolean existsCustomerWithEmail(String email) {
        return customers.stream()
                .anyMatch(c -> c.getEmail().equals(email));
    }

    @Override
    public boolean existsCustomerWithId(Long id) {
        return customers.stream()
                .anyMatch(c -> c.getId().equals(id));
    }

    @Override
    public void deleteCustomerById(Long id) {
        customers.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .ifPresent(c -> customers.remove(c));
    }

    @Override
    public void updateCustomer(Customer customer) {
        customers.add(customer);
    }
}
