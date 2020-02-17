package com.barney.delivery.services;

import com.barney.delivery.model.Address;
import com.barney.delivery.model.Customer;
import com.barney.delivery.model.CustomerForm;
import com.barney.delivery.repository.AddressRepository;
import com.barney.delivery.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final AddressRepository addressRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository, AddressRepository addressRepository) {
        this.customerRepository = customerRepository;
        this.addressRepository = addressRepository;
    }

    /**
     * Create customer with associated customer addresses.
     * @param customerForm JSON object representing customer information.
     * @return Customer object
     */
    @Override
    public CustomerForm saveCustomer(CustomerForm customerForm) {
        Customer customer = customerRepository.save(
                new Customer(customerForm.getLastName(), customerForm.getFirstName(),
                        customerForm.getPhoneNumber(),
                        customerForm.getEmail()
                )
        );
        List<Address> addressList =  customerForm
                .getAddress()
                .stream()
                .map(address -> {
                    address.setCustomer(customer);
                    return addressRepository.save(address);
                })
                .collect(Collectors.toList());

        customerForm.setAddress(addressList);
        return customerForm;
    }

    /**
     * Fetch all customers in Db
     * @return List of customers
     */
    @Override
    public List<Customer> getCustomers() {
        return customerRepository.findAll();
    }

    /**
     * Get customer via their ID
     * @param id identity of customer
     * @return Optional
     */
    @Override
    public Optional<Customer> getCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    /**
     * Create a customer address for existing customer
     * @param customerId
     * @param address
     * @return Optional
     */
    @Override
    public Optional<Address> createCustomerAddress(Long customerId, Address address) {
        return customerRepository
                .findById(customerId)
                .map(customer -> {
                    address.setCustomer(customer);
                    return addressRepository.save(address);
                });
    }

    /**
     * Delete a customers id
     * @param addressId address to be deleted
     * @param customerId associated customer.
     * @return
     */
    @Override
    public Optional<Boolean> deleteCustomerAddress(Long addressId, Long customerId) {
        return addressRepository
                .findByIdAndCustomerId(addressId, customerId)
                .map(address -> {
                    addressRepository.delete(address);
                    return true;
                });
    }

    /**
     * Get all customers from a given city
     * @param city value to check against
     * @return customer list.
     */
    @Override
    public List<Customer> getAllCustomersFromCity(String city) {
        return addressRepository
                .findAllByCity(city)
                .stream()
                .map(address -> address.getCustomer())
                .collect(Collectors.toList());
    }

    /**
     *
     * @param prefix
     * @return
     */
    @Override
    public List<Customer> getCustomersWithPhonePrefix(String prefix) {
        return customerRepository
                .findAllByPhoneNumberStartingWith(prefix);
    }

}
