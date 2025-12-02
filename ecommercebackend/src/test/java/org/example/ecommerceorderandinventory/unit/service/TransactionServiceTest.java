package org.example.ecommerceorderandinventory.unit.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.ecommerceorderandinventory.dto.in.TransactionCreateDTO;
import org.example.ecommerceorderandinventory.dto.out.FinishedTransactionDetailsDTO;
import org.example.ecommerceorderandinventory.entity.Item;
import org.example.ecommerceorderandinventory.entity.Transaction;
import org.example.ecommerceorderandinventory.entity.user.Address;
import org.example.ecommerceorderandinventory.entity.user.User;
import org.example.ecommerceorderandinventory.mappers.TransactionMapper;
import org.example.ecommerceorderandinventory.rabbitmq.producer.TransactionProducer;
import org.example.ecommerceorderandinventory.repository.ItemRepository;
import org.example.ecommerceorderandinventory.repository.TransactionRepository;
import org.example.ecommerceorderandinventory.repository.UserRepository;
import org.example.ecommerceorderandinventory.security.UserDetailsImpl;
import org.example.ecommerceorderandinventory.service.TransactionService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TransactionServiceTest {
    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TransactionMapper transactionMapper;

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private TransactionProducer transactionProducer;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        TransactionSynchronizationManager.initSynchronization();
    }


    @AfterEach
    void clearSync() {
        TransactionSynchronizationManager.clearSynchronization();
    }

    @Test
    void getTransactionDetails_returnsDTO_whenFound() {
        Transaction tx = new Transaction();
        FinishedTransactionDetailsDTO dto = new FinishedTransactionDetailsDTO();

        when(transactionRepository.findById(1L)).thenReturn(Optional.of(tx));
        when(transactionMapper.finishedTransactionDetailsDTOFromTransaction(tx)).thenReturn(dto);

        FinishedTransactionDetailsDTO result = transactionService.getTransactionDetails(1L);

        assertNotNull(result);
        assertSame(dto, result);
        verify(transactionRepository).findById(1L);
        verify(transactionMapper).finishedTransactionDetailsDTOFromTransaction(tx);
    }

    @Test
    void getTransactionDetails_throws_whenNotFound() {
        when(transactionRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> transactionService.getTransactionDetails(1L));

        assertEquals("Transaction not found!", ex.getMessage());
        verify(transactionRepository).findById(1L);
        verifyNoInteractions(transactionMapper);
    }

    @Test
    void getUserTransactionList_returnsMappedList() {
        Transaction tx1 = new Transaction();
        Transaction tx2 = new Transaction();

        List<Transaction> transactions = List.of(tx1, tx2);
        List<FinishedTransactionDetailsDTO> mappedList = List.of(new FinishedTransactionDetailsDTO(), new FinishedTransactionDetailsDTO());

        when(transactionRepository.getUserTransactions(5L)).thenReturn(transactions);
        when(transactionMapper.finishedTransactionDetailsDTOFromTransaction(transactions)).thenReturn(mappedList);

        List<FinishedTransactionDetailsDTO> result = transactionService.getUserTransactionList(5L);

        assertEquals(2, result.size());
        assertSame(mappedList, result);
        verify(transactionRepository).getUserTransactions(5L);
        verify(transactionMapper).finishedTransactionDetailsDTOFromTransaction(transactions);
    }

    private User getUserWithAddress(double balance) {
        User user = new User();
        user.setId(1L);
        user.setBalance(balance);
        Address addr = new Address();
        addr.setStreet("Street");
        user.setAddress(addr);
        return user;
    }

    private Item getItem(int qty, double price) {
        Item item = new Item();
        item.setId(10L);
        item.setQuantity(qty);
        item.setPrice(price);
        return item;
    }

    private UserDetailsImpl getPrincipalUser() {
        return new UserDetailsImpl("test@mail.com", "pass", Collections.emptyList(), 1L);
    }

    @Test
    void makeTransaction_success() {

        TransactionCreateDTO dto = new TransactionCreateDTO();
        dto.setItemId(10L);
        dto.setQuantity(2);

        User user = getUserWithAddress(100.0);
        Item item = getItem(10, 20.0);

        Transaction transaction = new Transaction();
        transaction.setTransactionId(999L);

        when(transactionMapper.transactionFromCreateTransactionDTO(dto)).thenReturn(transaction);
        when(userRepository.getReferenceById(1L)).thenReturn(user);
        when(itemRepository.getReferenceById(10L)).thenReturn(item);

        transactionService.makeTransaction(dto, getPrincipalUser());

        verify(transactionRepository).save(transaction);

        assertEquals(60.0, user.getBalance()); // 100 - (20*2)
        assertEquals(8, item.getQuantity()); // 10 - 2

    }

    @Test
    void makeTransaction_noAddress_throws() {
        TransactionCreateDTO dto = new TransactionCreateDTO();
        dto.setItemId(10L);
        dto.setQuantity(1);

        User user = new User();
        user.setId(1L);
        user.setBalance(100.0);

        when(transactionMapper.transactionFromCreateTransactionDTO(dto)).thenReturn(new Transaction());
        when(userRepository.getReferenceById(1L)).thenReturn(user);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> transactionService.makeTransaction(dto, getPrincipalUser()));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    }
}
