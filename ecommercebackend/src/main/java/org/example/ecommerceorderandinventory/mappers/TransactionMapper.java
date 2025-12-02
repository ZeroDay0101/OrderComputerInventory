package org.example.ecommerceorderandinventory.mappers;

import org.example.ecommerceorderandinventory.dto.in.TransactionCreateDTO;
import org.example.ecommerceorderandinventory.dto.out.FinishedTransactionDetailsDTO;
import org.example.ecommerceorderandinventory.entity.Transaction;
import org.example.ecommerceorderandinventory.entity.user.Address;
import org.example.ecommerceorderandinventory.rabbitmq.event.TransactionEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    //    void transactionFromCreateTransactionDTO(@MappingTarget Transaction transaction, CreateTransactionDTO transactionDetailsDTO);
    Transaction transactionFromCreateTransactionDTO(TransactionCreateDTO transactionDetailsDTO);


    @Mapping(target = "userId", expression = "java(transaction.getUser().getId())")
    FinishedTransactionDetailsDTO finishedTransactionDetailsDTOFromTransaction(Transaction transaction);

    List<FinishedTransactionDetailsDTO> finishedTransactionDetailsDTOFromTransaction(List<Transaction> transaction);

    void setAddressOnTransactionEvent(@MappingTarget TransactionEvent transactionEvent, Address address);

}
