package com.barney.delivery.repository;

import com.barney.delivery.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    List<Customer> findAllByPhoneNumberStartingWith(String phoneNumber);
}
