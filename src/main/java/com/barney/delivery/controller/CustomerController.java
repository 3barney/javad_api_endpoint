package com.barney.delivery.controller;

import com.barney.delivery.exception.ResourceNotFoundException;
import com.barney.delivery.model.Address;
import com.barney.delivery.model.Customer;
import com.barney.delivery.repository.AddressRepository;
import com.barney.delivery.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
public class CustomerController {

    private final CustomerRepository customerRepository;
    private final AddressRepository addressRepository;

    public CustomerController(CustomerRepository customerRepository, AddressRepository addressRepository) {
        this.customerRepository = customerRepository;
        this.addressRepository = addressRepository;
    }

    @GetMapping("/customer")
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @GetMapping("/customer/{id}")
    public Customer getCustomerById(@PathVariable (value = "id") Long customerId) {
        Optional<Customer> customer = customerRepository.findById(customerId);
        return customer.orElseThrow(() ->
                new ResourceNotFoundException("Customer".concat(String.valueOf(customerId)).concat(" not found")));
    }

    @PostMapping("/customer")
    public Customer createCustomer(@Valid @RequestBody Customer customer) {
        return customerRepository.save(customer);
    }

    @PostMapping("/customer/{id}/address")
    public Address createCustomerAddress(@PathVariable (value = "id") Long id, @Valid @RequestBody Address address) {
        return customerRepository.findById(id).map(customer -> {
            address.setCustomer(customer);
            return addressRepository.save(address);
        }).orElseThrow(() -> new ResourceNotFoundException("Customer".concat(String.valueOf(id)).concat(" not found")));
    }

    @DeleteMapping("/customer/{id}/address/{addressId}")
    public ResponseEntity<?> deleteAddress(@PathVariable (value = "id") Long id, @PathVariable (value = "addressId") Long addressId) {
        return addressRepository.findByIdAndCustomerId(addressId, id).map(address -> {
            addressRepository.delete(address);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("Address not found with id " + addressId + " Customer id " + id));
    }

    @GetMapping("/city/{name}")
    public List<Customer> getAllCustomersFromCity(@PathVariable (value = "name") String cityName) {
        List<Customer> cityCustomers = new ArrayList<>();
        Function<Address, Object> mapper = Address::getCustomer;
        for (Address address : addressRepository.findAllByCity(cityName)) {
            Object o = mapper.apply(address);
            cityCustomers.add((Customer) o);
        }
        return cityCustomers;
    }

    @GetMapping("/phone/{prefix}")
    public List<Customer> getAllCustomersWithPhone(@PathVariable (value = "prefix") String phonePrefix) {
        return customerRepository.findAllByPhoneNumberStartingWith(phonePrefix);
    }
}
