package org.example.ecommerceorderandinventory.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.example.ecommerceorderandinventory.dto.in.TransactionCreateDTO;
import org.example.ecommerceorderandinventory.dto.out.FinishedTransactionDetailsDTO;
import org.example.ecommerceorderandinventory.entity.Item;
import org.example.ecommerceorderandinventory.entity.Transaction;
import org.example.ecommerceorderandinventory.entity.user.User;
import org.example.ecommerceorderandinventory.mappers.TransactionMapper;
import org.example.ecommerceorderandinventory.rabbitmq.event.TransactionEvent;
import org.example.ecommerceorderandinventory.rabbitmq.producer.TransactionProducer;
import org.example.ecommerceorderandinventory.repository.ItemRepository;
import org.example.ecommerceorderandinventory.repository.TransactionRepository;
import org.example.ecommerceorderandinventory.repository.UserRepository;
import org.example.ecommerceorderandinventory.security.UserDetailsImpl;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Service
@Transactional
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final TransactionMapper transactionMapper;
    private final TransactionProducer transactionProducer;

    public TransactionService(TransactionRepository transactionRepository, UserRepository userRepository, ItemRepository itemRepository, TransactionMapper transactionMapper, TransactionProducer transactionProducer) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.transactionMapper = transactionMapper;
        this.transactionProducer = transactionProducer;
    }


    public void makeTransaction(TransactionCreateDTO transactionCreateDTO, UserDetailsImpl userDetails) {
        Transaction transaction = transactionMapper.transactionFromCreateTransactionDTO(transactionCreateDTO);

        User user = userRepository.getReferenceById(userDetails.getUserId());
        if (user.getAddress() == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "You must set address before placing an order");
        Item item = itemRepository.getReferenceById(transactionCreateDTO.getItemId());

        int quantity = transactionCreateDTO.getQuantity();
        if (item.getQuantity() <= 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Out of stock!");

        double transactionAmount = item.getPrice() * quantity; // assuming DTO has getAmount()

        if (transactionAmount > user.getBalance()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient balance!");
        }

        // Deduct balance
        user.setBalance(user.getBalance() - transactionAmount);

        item.setQuantity(item.getQuantity() - quantity);
        transaction.setUser(user);

        transactionRepository.save(transaction);

        TransactionEvent transactionEvent = new TransactionEvent(
                item.getId(),
                transactionCreateDTO.getQuantity(),
                user.getId(),
                transaction.getTransactionId()
        );


        //Published after transaction finishes
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                log.info("Transaction published");
                transactionProducer.publish(transactionEvent);
            }
        });
    }

    public FinishedTransactionDetailsDTO getTransactionDetails(long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId).orElseThrow(
                () -> new EntityNotFoundException("Transaction not found!"));

        return transactionMapper.finishedTransactionDetailsDTOFromTransaction(transaction);
    }

    public List<FinishedTransactionDetailsDTO> getUserTransactionList(long userId) {
        List<Transaction> transactionList = transactionRepository.getUserTransactions(userId);

        return transactionMapper.finishedTransactionDetailsDTOFromTransaction(transactionList);
    }
}
