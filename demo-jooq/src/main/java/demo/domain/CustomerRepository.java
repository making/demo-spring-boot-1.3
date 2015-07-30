package demo.domain;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static demo.jooq.tables.Customers.CUSTOMERS;

@Repository
public class CustomerRepository {
    @Autowired
    DSLContext dslContext;

    public List<Customer> findAll() {
        return dslContext.select()
                .from(CUSTOMERS)
                .orderBy(CUSTOMERS.FIRST_NAME.asc())
                .fetchInto(Customer.class);
    }
}
