package org.example.ecommerceorderandinventory.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.ecommerceorderandinventory.controller.ItemController;
import org.example.ecommerceorderandinventory.dto.in.CreateItemDTO;
import org.example.ecommerceorderandinventory.dto.in.UpdateItemDTO;
import org.example.ecommerceorderandinventory.dto.out.ItemOutDTO;
import org.example.ecommerceorderandinventory.entity.ItemStatus;
import org.example.ecommerceorderandinventory.entity.ItemType;
import org.example.ecommerceorderandinventory.service.ItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = {ItemController.class})
@ExtendWith(MockitoExtension.class)
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ItemService itemService;

    @Autowired
    private ObjectMapper objectMapper;

    private ItemOutDTO sampleItem;

    @BeforeEach
    void setup() {
        sampleItem = new ItemOutDTO(
                1L,
                ItemType.MOUSE,
                ItemStatus.ACTIVE,
                "Lenovo",
                1000.0,
                10
        );
    }

    @Test
    @WithMockUser
    void testGetItems() throws Exception {
        Mockito.when(itemService.getAllItems()).thenReturn(List.of(sampleItem));

        mockMvc.perform(get("/api/inventory").with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(sampleItem.getId()))
                .andExpect(jsonPath("$[0].model").value(sampleItem.getModel()));
    }

    @Test
    @WithMockUser
    void testGetItemById() throws Exception {
        Mockito.when(itemService.getItemById(1)).thenReturn(sampleItem);

        mockMvc.perform(get("/api/inventory/item").with(csrf())

                        .param("itemId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(sampleItem.getId()))
                .andExpect(jsonPath("$.model").value(sampleItem.getModel()));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void testAddItem() throws Exception {
        CreateItemDTO createItemDTO = new CreateItemDTO(
                ItemType.MOUSE,
                "Lenovo",
                1000.0,
                10
        );

        Mockito.when(itemService.addItem(any(CreateItemDTO.class)))
                .thenReturn(sampleItem);

        mockMvc.perform(post("/api/inventory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(createItemDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(sampleItem.getId()))
                .andExpect(jsonPath("$.model").value(sampleItem.getModel()));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void testDeleteItem() throws Exception {

        doNothing().when(itemService).setItemAsDeleted(1L);

        mockMvc.perform(delete("/api/inventory")
                        .with(csrf())
                        .param("itemId", "1"))

                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void testUpdateItem() throws Exception {
        UpdateItemDTO updateDTO = new UpdateItemDTO(
                1L,
                ItemType.MOUSE,
                "Lenovo Updated",
                1200,
                8
        );

        Mockito.when(itemService.updateItem(any(UpdateItemDTO.class)))
                .thenReturn(sampleItem);

        mockMvc.perform(patch("/api/inventory")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(sampleItem.getId()));
    }

    @Test
    void testUnauthorizedAdminEndpoints() throws Exception {
        // Without ADMIN role requests should be forbidden
        mockMvc.perform(post("/api/inventory")
                        .with(csrf())
                )
                .andExpect(status().isUnauthorized());

        mockMvc.perform(delete("/api/inventory").with(csrf())
                )
                .andExpect(status().isUnauthorized());

        mockMvc.perform(patch("/api/inventory").with(csrf())
                )
                .andExpect(status().isUnauthorized());
    }
}