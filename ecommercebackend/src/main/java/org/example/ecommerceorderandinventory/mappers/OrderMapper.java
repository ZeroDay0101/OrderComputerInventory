package org.example.ecommerceorderandinventory.mappers;

import org.example.ecommerceorderandinventory.dto.out.OrderDTO;
import org.example.ecommerceorderandinventory.entity.order.Order;
import org.example.ecommerceorderandinventory.entity.user.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(target = "id", ignore = true)
        //DON'T DELETE THIS LINE. WE DON'T WANT ADDRESS ID ON ORDER ID
    void setAddressOnOrder(@MappingTarget Order order, Address address);

    @Mapping(target = "transactionId", expression = "java(order.getParentTransaction().getTransactionId())")
    @Mapping(target = "itemId", expression = "java(order.getItem().getId())")
    @Mapping(target = "userId", expression = "java(order.getUser().getId())")
    OrderDTO toOrderDTO(Order order);

    List<OrderDTO> toOrderDTO(List<Order> order);
}
