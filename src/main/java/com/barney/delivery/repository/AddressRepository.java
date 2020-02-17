package com.barney.delivery.repository;

import com.barney.delivery.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> findAllByCity(String city);
    Optional<Address> findByIdAndCustomerId(Long id, Long customerId);
}
