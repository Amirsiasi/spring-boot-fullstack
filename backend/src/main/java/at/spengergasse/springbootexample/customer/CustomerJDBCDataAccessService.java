package at.spengergasse.springbootexample.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jdbc")
public class CustomerJDBCDataAccessService implements CustomerDao {

    private final JdbcTemplate jdbcTemplate;
    private final CustomerRowMapper customerRowMapper;


    @Autowired
    public CustomerJDBCDataAccessService(
            JdbcTemplate jdbcTemplate,
            CustomerRowMapper customerRowMapper
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.customerRowMapper = customerRowMapper;
    }

    @Override
    public List<Customer> selectAllCustomers() {
        var sql= """
                select id,name,email,age
                from customer
                """;
        return jdbcTemplate.query(sql, customerRowMapper);

    }

    @Override
    public Optional<Customer> selectCustomerById(Long id) {

        var sql = """
                select id,name ,email,age 
                from customer where id=?
                """;
        return jdbcTemplate.query(sql,customerRowMapper,id)
                .stream()
                .findFirst();
    }

    @Override
    public void insertCustomer(Customer customer) {
        var sql = """
                insert into customer(name, email,age)
                values (?,?,?)
                """;
        int update = jdbcTemplate.update(
                sql,
                customer.getName(),
                customer.getEmail(),
                customer.getAge()
        );
        System.out.println(update);
    }

    @Override
    public boolean existsCustomerWithEmail(String email) {
        var sql = """
               select count(id) 
               from customer 
               where email=? 
                """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count !=null && count > 0;
    }

    @Override
    public boolean existsCustomerWithId(Long id) {
       var sql = """
               select count(id)
               from customer
               where id=?
               """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return  count!=null && count>0;
    }

    @Override
    public void deleteCustomerById(Long id) {
        var sql = """
                delete 
                   from customer 
                     where id=?
                         """;
        jdbcTemplate.update(sql, id);

    }

    @Override
    public void updateCustomer(Customer customer) {
        if (customer.getName()!=null && customer.getEmail()!=null && customer.getAge()!=null){
            var sql = """
                    update customer
                    set name=?,email=?,age=?
                    where id=?
                    """;
            jdbcTemplate.update(sql, customer.getName(), customer.getEmail(), customer.getAge(), customer.getId());
        }

    }
}
