package com.barney.delivery.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AddressTest {

    private Address address;
    private Customer customer;

    @BeforeEach
    void setUp() {
        address = new Address();
        customer = new Customer();

        address.setCity("Nairobi");
        address.setCustomer(customer);
        customer.setFirstName("test");
    }

    @Test
    void getCity() {
        assertThat(address.getCity()).isEqualTo("Nairobi");
    }

    @Test
    void getCustomer() {
        assertThat(address.getCustomer()).isNotNull();
        assertThat(address.getCustomer().getFirstName()).isEqualTo("test");
    }
}