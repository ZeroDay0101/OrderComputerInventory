package org.example.ecommerceorderandinventory.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.ecommerceorderandinventory.controller.TransactionController;
import org.example.ecommerceorderandinventory.dto.in.TransactionCreateDTO;
import org.example.ecommerceorderandinventory.dto.out.FinishedTransactionDetailsDTO;
import org.example.ecommerceorderandinventory.security.UserDetailsImpl;
import org.example.ecommerceorderandinventory.service.TransactionService;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = {TransactionController.class})
@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TransactionService transactionService;

    @Autowired
    private ObjectMapper objectMapper;

    private FinishedTransactionDetailsDTO sampleTransaction;

    @BeforeEach
    void setup() {
        sampleTransaction = new FinishedTransactionDetailsDTO(10L, 5L, 3L, 1, LocalDateTime.now());
    }

    // ------------------ GET /api/transaction ------------------
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testGetTransactionDetails_AsOwner() throws Exception {
        Mockito.when(transactionService.getTransactionDetails(10L)).thenReturn(sampleTransaction);

        mockMvc.perform(get("/api/transaction")
                        .with(csrf())
                        .param("transactionId", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionId").value(10L))
                .andExpect(jsonPath("$.userId").value(5L));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testGetTransactionDetails_AsAdmin() throws Exception {
        Mockito.when(transactionService.getTransactionDetails(10L)).thenReturn(sampleTransaction);

        mockMvc.perform(get("/api/transaction")
                        .with(csrf())
                        .param("transactionId", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionId").value(10L));
    }

    // ------------------ GET /api/transaction/user ------------------

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetUserTransactions() throws Exception {
        Mockito.when(transactionService.getUserTransactionList(5L)).thenReturn(List.of(sampleTransaction));


        mockMvc.perform(get("/api/transaction/user")
                        .with(csrf())
                        .param("userId", "5"))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].transactionId").value(10L))
                .andExpect(jsonPath("$[0].userId").value(5L));
    }

    // ------------------ POST /api/transaction ------------------

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testMakeTransaction() throws Exception {
        TransactionCreateDTO createDTO = new TransactionCreateDTO();
        createDTO.setItemId(3L);
        createDTO.setQuantity(1);


        doNothing().when(transactionService).makeTransaction(any(TransactionCreateDTO.class), any(UserDetailsImpl.class));

        mockMvc.perform(post("/api/transaction")
                        .with(csrf())

                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void testMakeTransaction_Unauthenticated() throws Exception {
        TransactionCreateDTO createDTO = new TransactionCreateDTO();
        createDTO.setItemId(3L);
        createDTO.setQuantity(1);

        mockMvc.perform(post("/api/transaction")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isUnauthorized());
    }

}