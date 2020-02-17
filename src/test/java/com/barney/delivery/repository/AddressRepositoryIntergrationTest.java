package com.barney.delivery.repository;

import com.barney.delivery.model.Address;
import com.barney.delivery.model.Customer;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AddressRepositoryIntergrationTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private AddressRepository addressRepository;

    @Test
    void whenFindAllByCity_thenReturnsCustomerInfo() {
        Customer customer = new Customer();
        Address address = new Address();

        customer.setFirstName("test");
        customer.setLastName("last");
        customer.setEmail("test@mail.com");
        customer.setPhoneNumber("25467");

        address.setCustomer(customer);
        address.setCity("Nairobi");
        address.setCountry("Kenya");

        testEntityManager.persist(customer);
        testEntityManager.persist(address);
        testEntityManager.flush();

        List<Address> found = addressRepository.findAllByCity("Nairobi");

        assertThat(found.size()).isEqualTo(1);
        assertThat(found.get(0).getCustomer()).isNotNull();
        assertThat(found.get(0).getCustomer().getFirstName()).isEqualTo("test");
    }

    @Test
    void whenFindByIdAndCustomerId_thenReturnCustomerInfo() {
        Customer customer = new Customer();
        Address address = new Address();

        customer.setFirstName("test");
        customer.setLastName("last");
        customer.setEmail("test@mail.com");
        customer.setPhoneNumber("25467");

        address.setCustomer(customer);
        address.setCity("Nairobi");
        address.setCountry("Kenya");

        testEntityManager.persist(customer);
        testEntityManager.persist(address);
        testEntityManager.flush();

        List<Address> addressList = addressRepository.findAllByCity("Nairobi");
        Long addressId = addressList.get(0).getId();
        Long customerId = addressList.get(0).getCustomer().getId();

        Optional<Address> item = addressRepository.findByIdAndCustomerId(addressId, customerId);
        assertThat(item.get().getId()).isEqualTo(customerId);
    }
}