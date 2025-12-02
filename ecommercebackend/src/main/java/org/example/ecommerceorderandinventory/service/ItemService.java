package org.example.ecommerceorderandinventory.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.example.ecommerceorderandinventory.dto.in.CreateItemDTO;
import org.example.ecommerceorderandinventory.dto.in.UpdateItemDTO;
import org.example.ecommerceorderandinventory.dto.out.ItemOutDTO;
import org.example.ecommerceorderandinventory.entity.Item;
import org.example.ecommerceorderandinventory.entity.ItemStatus;
import org.example.ecommerceorderandinventory.mappers.ItemMapper;
import org.example.ecommerceorderandinventory.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ItemService {

    private final ItemRepository itemRepository;

    private final ItemMapper itemMapper;


    public ItemService(ItemRepository itemRepository, ItemMapper itemMapper) {
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
    }


    public List<ItemOutDTO> getAllItems() {
        List<Item> items = itemRepository.findAll();
        return itemMapper.outDTOFromItem(items);

    }

    public ItemOutDTO getItemById(long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new EntityNotFoundException("Item not found"));
        return itemMapper.outDTOFromItem(item);
    }


    public ItemOutDTO addItem(CreateItemDTO createItemDTO) {
        Item item = itemMapper.itemFromDTO(createItemDTO);

        item.setItemStatus(ItemStatus.ACTIVE);
        itemRepository.save(item);

        return itemMapper.outDTOFromItem(item);
    }


    /**
     * @param itemId Because items are often fk in other tables (like order history which we want to persist even if we delete the item), we mark the item as DELETED instead of removing it from the database completely.
     */
    public void setItemAsDeleted(long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new EntityNotFoundException("Item not found"));
        item.setItemStatus(ItemStatus.DELETED);
    }

    public ItemOutDTO updateItem(UpdateItemDTO updateItemDTO) {
        long itemId = updateItemDTO.getItemId();


        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Item not found"));


        itemMapper.setPropertiesFromDTO(item, updateItemDTO);
        itemRepository.save(item);

        return itemMapper.outDTOFromItem(item);
    }
}
