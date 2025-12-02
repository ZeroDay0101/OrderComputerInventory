package org.example.ecommerceorderandinventory.controller;

import jakarta.validation.Valid;
import org.example.ecommerceorderandinventory.dto.in.CreateItemDTO;
import org.example.ecommerceorderandinventory.dto.in.UpdateItemDTO;
import org.example.ecommerceorderandinventory.dto.out.ItemOutDTO;
import org.example.ecommerceorderandinventory.service.ItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public ResponseEntity<List<ItemOutDTO>> getItems() {
        List<ItemOutDTO> items = new ArrayList<>(itemService.getAllItems());
        return ResponseEntity.ok().body(items);
    }

    @GetMapping("/item")
    public ResponseEntity<ItemOutDTO> getItem(@RequestParam int itemId) {
        ItemOutDTO item = itemService.getItemById(itemId);

        return ResponseEntity.ok().body(item);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ItemOutDTO> addItem(@RequestBody @Valid CreateItemDTO createItemDTO) {
        ItemOutDTO item = itemService.addItem(createItemDTO);

        return ResponseEntity.status(HttpStatus.OK).body(item);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping
    public ResponseEntity<Void> deleteItem(@RequestParam long itemId) {
        itemService.setItemAsDeleted(itemId);

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping
    public ResponseEntity<ItemOutDTO> updateItem(@RequestBody UpdateItemDTO itemDTO) {
        ItemOutDTO item = itemService.updateItem(itemDTO);

        return ResponseEntity.ok().body(item);
    }


}
