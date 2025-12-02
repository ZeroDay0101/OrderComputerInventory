package org.example.ecommerceorderandinventory.repository;

import org.example.ecommerceorderandinventory.entity.user.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
