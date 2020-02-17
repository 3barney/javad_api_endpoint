package com.barney.delivery.services;

import com.barney.delivery.model.Address;
import com.barney.delivery.model.Customer;
import com.barney.delivery.model.CustomerForm;

import java.util.List;
import java.util.Optional;

public interface CustomerService {

    CustomerForm saveCustomer(CustomerForm customerForm);

    List<Customer> getCustomers();

    Optional<Customer> getCustomerById(Long id);

    Optional<Address> createCustomerAddress(Long customerId, Address address);

    Optional<Boolean> deleteCustomerAddress(Long addressId, Long customerId);

    List<Customer> getAllCustomersFromCity(String city);

    List<Customer> getCustomersWithPhonePrefix(String prefix);
}
