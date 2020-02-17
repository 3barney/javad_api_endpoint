package com.barney.delivery.repository;

import com.barney.delivery.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> findAllByCity(String city);
}
