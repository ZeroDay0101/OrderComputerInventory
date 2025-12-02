package org.example.ecommerceorderandinventory.unit.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.ecommerceorderandinventory.dto.in.CreateItemDTO;
import org.example.ecommerceorderandinventory.dto.in.UpdateItemDTO;
import org.example.ecommerceorderandinventory.dto.out.ItemOutDTO;
import org.example.ecommerceorderandinventory.entity.Item;
import org.example.ecommerceorderandinventory.entity.ItemStatus;
import org.example.ecommerceorderandinventory.entity.ItemType;
import org.example.ecommerceorderandinventory.mappers.ItemMapper;
import org.example.ecommerceorderandinventory.repository.ItemRepository;
import org.example.ecommerceorderandinventory.service.ItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

public class ItemServiceTest {
    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ItemMapper itemMapper;

    @InjectMocks
    private ItemService itemService;

    private Item sampleItem;
    private ItemOutDTO sampleDTO;

    @BeforeEach
    void setup() {

        sampleItem = new Item();
        sampleItem.setItemStatus(ItemStatus.ACTIVE);
        sampleItem.setId(1L);
        sampleItem.setItemType(ItemType.MOUSE);
        sampleItem.setModel("Lenovo");
        sampleItem.setPrice(1000.0);
        sampleItem.setQuantity(10);

        sampleDTO = new ItemOutDTO(
                1L,
                ItemType.MOUSE,
                ItemStatus.ACTIVE,
                "Lenovo",
                1000.0,
                10
        );
    }

    @Test
    void testGetAllItems() {
        when(itemRepository.findAll()).thenReturn(List.of(sampleItem));
        when(itemMapper.outDTOFromItem(List.of(sampleItem))).thenReturn(List.of(sampleDTO));

        List<ItemOutDTO> result = itemService.getAllItems();

        assertEquals(1, result.size());
        assertEquals(sampleDTO.getId(), result.get(0).getId());
        verify(itemRepository).findAll();
        verify(itemMapper).outDTOFromItem(List.of(sampleItem));
    }

    @Test
    void testGetItemById() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(sampleItem));
        when(itemMapper.outDTOFromItem(sampleItem)).thenReturn(sampleDTO);

        ItemOutDTO result = itemService.getItemById(1L);

        assertEquals(sampleDTO.getId(), result.getId());
        verify(itemRepository).findById(1L);
        verify(itemMapper).outDTOFromItem(sampleItem);
    }

    @Test
    void testGetItemById_NotFound() {
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> itemService.getItemById(1L));
    }

    @Test
    void testAddItem() {
        CreateItemDTO createDTO = new CreateItemDTO(ItemType.MOUSE, "Lenovo", 1000.0, 10);

        when(itemMapper.itemFromDTO(createDTO)).thenReturn(sampleItem);
        when(itemMapper.outDTOFromItem(sampleItem)).thenReturn(sampleDTO);

        ItemOutDTO result = itemService.addItem(createDTO);

        assertEquals(sampleDTO.getId(), result.getId());
        assertEquals(ItemStatus.ACTIVE, sampleItem.getItemStatus()); // ensure status set
        verify(itemRepository).save(sampleItem);
        verify(itemMapper).itemFromDTO(createDTO);
        verify(itemMapper).outDTOFromItem(sampleItem);
    }

    @Test
    void testSetItemAsDeleted() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(sampleItem));

        itemService.setItemAsDeleted(1L);

        assertEquals(ItemStatus.DELETED, sampleItem.getItemStatus());
        verify(itemRepository).findById(1L);
    }

    @Test
    void testSetItemAsDeleted_NotFound() {
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> itemService.setItemAsDeleted(1L));
    }

    @Test
    void testUpdateItem() {
        UpdateItemDTO updateDTO = new UpdateItemDTO(1L, ItemType.MOUSE, "Updated", 1200, 5);

        when(itemRepository.findById(1L)).thenReturn(Optional.of(sampleItem));
        doNothing().when(itemMapper).setPropertiesFromDTO(sampleItem, updateDTO);
        when(itemRepository.save(sampleItem)).thenReturn(sampleItem);
        when(itemMapper.outDTOFromItem(sampleItem)).thenReturn(sampleDTO);

        ItemOutDTO result = itemService.updateItem(updateDTO);

        assertEquals(sampleDTO.getId(), result.getId());
        verify(itemRepository).findById(1L);
        verify(itemMapper).setPropertiesFromDTO(sampleItem, updateDTO);
        verify(itemRepository).save(sampleItem);
        verify(itemMapper).outDTOFromItem(sampleItem);
    }

    @Test
    void testUpdateItem_NotFound() {
        UpdateItemDTO updateDTO = new UpdateItemDTO(1L, null, null, 0, 0);
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> itemService.updateItem(updateDTO));
    }
}
