package com.barney.delivery.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setFirstName("test");
        customer.setEmail("test@mail.com");
    }

    @Test
    void getLastName() {
        assertThat(customer.getFirstName()).isEqualTo("test");
    }

    @Test
    void setLastName() {
        customer.setLastName("last");
    }

    @Test
    void getEmail() {
        assertThat(customer.getEmail()).isEqualTo("test@mail.com");
    }

}