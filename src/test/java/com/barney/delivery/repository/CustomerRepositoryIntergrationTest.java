package com.barney.delivery.repository;

import com.barney.delivery.model.Customer;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryIntergrationTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void whenFindAllByPhoneNumberStartingWith_thenReturnCustomerInfo() {
        Customer customer = new Customer();
        customer.setFirstName("test");
        customer.setLastName("last");
        customer.setEmail("test@mail.com");
        customer.setPhoneNumber("25467");
        testEntityManager.persist(customer);
        testEntityManager.flush();

        List<Customer> found = customerRepository.findAllByPhoneNumberStartingWith("254");

        assertThat(found.size()).isEqualTo(1);
        assertThat(found.get(0).getPhoneNumber()).isEqualTo("25467");
    }

}