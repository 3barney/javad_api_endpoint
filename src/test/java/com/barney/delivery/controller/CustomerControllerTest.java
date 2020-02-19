package com.barney.delivery.controller;

import com.barney.delivery.model.Address;
import com.barney.delivery.model.Customer;
import com.barney.delivery.model.CustomerForm;
import com.barney.delivery.services.CustomerService;
import com.barney.delivery.services.CustomerServiceImpl;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    private Customer customer;
    private Address address;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
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
    public void when_getAllCustomers_return_json_array() throws Exception {
        List<Customer> customerList = Collections.singletonList(customer);
        given(customerService.getCustomers()).willReturn(customerList);

        mockMvc.perform(get("/customer")
            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].lastName", is(customer.getLastName())));
    }

    @Test
    public void when_getCustomerById_returns_customer_object() throws Exception {
        Optional<Customer> cusutomerOptional = Optional.of(customer);
        given(customerService.getCustomerById(anyLong())).willReturn(cusutomerOptional);

        mockMvc.perform(get("/customer/1")
                .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.lastName", is(cusutomerOptional.get().getLastName())));
    }

    @Test
    public void when_createCustomer_returns_saved_customer() throws Exception {
        CustomerForm customerForm = new CustomerForm();
        customerForm.setFirstName(customer.getFirstName());
        customerForm.setLastName(customer.getLastName());
        customerForm.setPhoneNumber(customer.getPhoneNumber());
        customerForm.setEmail(customer.getEmail());
        customerForm.setAddress(new ArrayList(Collections.singleton(address)));

        given(customerService.saveCustomer(any())).willReturn(customerForm);
        mockMvc.perform(post("/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(customerForm)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.firstName", is(customerForm.getFirstName())))
                    .andExpect(jsonPath("$.address", hasSize(1)));

    }

    @Test
    public void when_createCustomerAddress_returns_saved_address() throws Exception {
        Optional<Address> optionalAddress = Optional.of(address);
        given(customerService.createCustomerAddress(anyLong(), any())).willReturn(optionalAddress);

        mockMvc.perform(post("/customer/1/address")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(address)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.type", is(address.getType())))
                    .andExpect(jsonPath("$.city", is(address.getCity())));

    }

    @Test
    public void when_deleteAddress_returns_success() throws Exception {
        Optional<Boolean> success = Optional.of(true);
        given(customerService.deleteCustomerAddress(anyLong(), anyLong())).willReturn(success);

        mockMvc.perform(delete("/customer/1/address/1")
                .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", is(true)));
    }

    @Test
    public void when_getAllCustomersFromCity_returns_list_of_customers() throws Exception {
        List<Customer> customerList = Collections.singletonList(customer);
        given(customerService.getAllCustomersFromCity(anyString())).willReturn(customerList);

        mockMvc.perform(get("/city/nairobi")
                .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].email", is(customer.getEmail())));

    }

    @Test
    public void when_getAllCustomersWithPhone_returns_list_of_customers() throws Exception {
        List<Customer> customerList = Collections.singletonList(customer);
        given(customerService.getCustomersWithPhonePrefix(anyString())).willReturn(customerList);

        mockMvc.perform(get("/phone/254")
                .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].phoneNumber", is(customer.getPhoneNumber())));

    }

    /**
     * Convert java object to Json
     * @param object Java object to transform
     * @return JsonBytes
     * @throws JsonProcessingException
     */
    private static byte[] convertObjectToJsonBytes(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return objectMapper.writeValueAsBytes(object);
    }
}