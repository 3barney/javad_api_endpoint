package com.barney.delivery.controller;

import com.barney.delivery.exception.ResourceNotFoundException;
import com.barney.delivery.model.Address;
import com.barney.delivery.model.Customer;
import com.barney.delivery.model.CustomerForm;
import com.barney.delivery.services.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/customer")
    public List<Customer> getAllCustomers() {
        return customerService.getCustomers();
    }

    @GetMapping("/customer/{id}")
    public Customer getCustomerById(@PathVariable (value = "id") Long customerId) {
        return customerService
                .getCustomerById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer".concat(String.valueOf(customerId)).concat(" not found")));
    }

    @PostMapping("/customer")
    public CustomerForm createCustomer(@Valid @RequestBody CustomerForm customerForm) {
        return customerService
                .saveCustomer(customerForm);
    }

    @PostMapping("/customer/{id}/address")
    public Address createCustomerAddress(@PathVariable (value = "id") Long id, @Valid @RequestBody Address address) {
        return customerService
                .createCustomerAddress(id, address)
                .orElseThrow(() -> new ResourceNotFoundException("Customer".concat(String.valueOf(id)).concat(" not found")));
    }

    @DeleteMapping("/customer/{id}/address/{addressId}")
    public ResponseEntity<?> deleteAddress(@PathVariable (value = "id") Long id, @PathVariable (value = "addressId") Long addressId) {
        return customerService
                .deleteCustomerAddress(addressId, id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id " + addressId + " Customer id " + id));
    }

    @GetMapping("/city/{name}")
    public List<Customer> getAllCustomersFromCity(@PathVariable (value = "name") String cityName) {
        return customerService
                .getAllCustomersFromCity(cityName);
    }

    @GetMapping("/phone/{prefix}")
    public List<Customer> getAllCustomersWithPhone(@PathVariable (value = "prefix") String phonePrefix) {
        return customerService
                .getCustomersWithPhonePrefix(phonePrefix);
    }
}
