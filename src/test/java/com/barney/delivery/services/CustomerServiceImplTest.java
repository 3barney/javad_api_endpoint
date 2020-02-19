package com.barney.delivery.services;

import com.barney.delivery.model.Address;
import com.barney.delivery.model.Customer;
import com.barney.delivery.model.CustomerForm;
import com.barney.delivery.repository.AddressRepository;
import com.barney.delivery.repository.CustomerRepository;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import static org.junit.Assert.*;

public class CustomerServiceImplTest {

    @InjectMocks
    CustomerServiceImpl customerService;

    @Mock
    CustomerRepository customerRepository;

    @Mock
    AddressRepository addressRepository;

    private Customer customer;
    private Address address;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        customerService = new CustomerServiceImpl(customerRepository, addressRepository);
        customer = new Customer(
                "Last",
                "First",
                "25467878",
                "test@mail.com"
        );
        address = new Address(
                "type",
                "Nairobi",
                "Kenya",
                "line"
        );
    }

    @Test
    public void when_save_customer_it_should_return_customer_and_address() {
        CustomerForm customerForm = new CustomerForm();
        customerForm.setFirstName(customer.getFirstName());
        customerForm.setLastName(customer.getLastName());
        customerForm.setPhoneNumber(customer.getPhoneNumber());
        customerForm.setEmail(customer.getEmail());
        customerForm.setAddress(new ArrayList(Collections.singleton(address)));

        when(customerRepository.save(customer)).thenReturn(new Customer());
        when(addressRepository.save(address)).thenReturn(new Address());

        CustomerForm createdCustomer = customerService.saveCustomer(customerForm);
        assertThat(createdCustomer).isNotNull();
        assertThat(createdCustomer.getAddress().size()).isEqualTo(1);

        verify(customerRepository, times(1)).save(any());
        verify(addressRepository, times(1)).save(address);
    }

    @Test
    public void when_fetch_customers_it_should_return_customer_list() {
        List<Customer> customerList = Collections.singletonList(customer);
        when(customerRepository.findAll()).thenReturn(customerList);

        List<Customer> savedCustomers = customerService.getCustomers();
        assertThat(savedCustomers.size()).isEqualTo(1);
        verify(customerRepository, times(1)).findAll();
    }

    @Test
    public void when_fetch_customer_by_id_customer_is_valid() {
        Optional<Customer> optionalCustomer = Optional.of(customer);
        when(customerRepository.findById(anyLong())).thenReturn(optionalCustomer);

        Optional<Customer> found = customerService.getCustomerById(anyLong());
        assertThat(found.isPresent());
        verify(customerRepository, times(1)).findById(anyLong());
    }

    @Test
    public void when_existing_customer_adds_an_address_its_saved() {
        Optional<Address> addressOptional = Optional.of(address);
        Optional<Customer> optionalCustomer = Optional.of(customer);
        when(customerRepository.findById(anyLong())).thenReturn(optionalCustomer);

        Address add = addressOptional.get();
        add.setCustomer(optionalCustomer.get());

        when(addressRepository.save(add)).thenReturn(address);
        Optional<Address> saved = customerService.createCustomerAddress(anyLong(), addressOptional.get());
        assertNotNull(saved);
        verify(customerRepository, times(1)).findById(anyLong());
        verify(addressRepository, times(1)).save(addressOptional.get());
    }

    @Test
    public void when_customer_address_is_deleted_returns_true() {
        Optional<Address> addressOptional = Optional.of(address);
        when(addressRepository.findByIdAndCustomerId(anyLong(), anyLong())).thenReturn(addressOptional);

        Optional<Boolean> valid = customerService.deleteCustomerAddress(anyLong(), anyLong());
        assertNotNull(valid);
        verify(addressRepository, times(1)).findByIdAndCustomerId(anyLong(), anyLong());
        verify(addressRepository, times(1)).delete(addressOptional.get());
    }

    @Test
    public void when_fetch_all_customers_from_a_city_returns_customer_list() {
        address.setCustomer(customer);
        List<Address> addressList = Collections.singletonList(address);
        when(addressRepository.findAllByCity(anyString())).thenReturn(addressList);

        List<Customer> customerList = customerService.getAllCustomersFromCity(anyString());
        assertNotNull(customerList);
        assertEquals(customerList.size(), 1);
        verify(addressRepository, times(1)).findAllByCity(anyString());
    }

    @Test
    public void getCustomersWithPhonePrefix() {
        List<Customer> customerList = Collections.singletonList(customer);
        when(customerRepository.findAllByPhoneNumberStartingWith(anyString()))
                .thenReturn(customerList);
        List<Customer> found = customerService.getCustomersWithPhonePrefix(anyString());
        assertNotNull(found);
        verify(customerRepository, times(1)).findAllByPhoneNumberStartingWith(anyString());
    }
}