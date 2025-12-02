package org.example.ecommerceorderandinventory.mappers;

import org.example.ecommerceorderandinventory.dto.in.CreateItemDTO;
import org.example.ecommerceorderandinventory.dto.in.UpdateItemDTO;
import org.example.ecommerceorderandinventory.dto.out.ItemOutDTO;
import org.example.ecommerceorderandinventory.entity.Item;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    Item itemFromDTO(CreateItemDTO updateCreateItemDTO);

    ItemOutDTO outDTOFromItem(Item item);

    List<ItemOutDTO> outDTOFromItem(List<Item> item);

    void setPropertiesFromDTO(@MappingTarget Item item, UpdateItemDTO updateItemDTO);
}
