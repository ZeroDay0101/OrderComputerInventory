package org.example.ecommerceorderandinventory.mappers;

import org.example.ecommerceorderandinventory.dto.out.AddressOutDTO;
import org.example.ecommerceorderandinventory.entity.user.Address;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    AddressOutDTO fromAddressToDTO(Address user);

}
