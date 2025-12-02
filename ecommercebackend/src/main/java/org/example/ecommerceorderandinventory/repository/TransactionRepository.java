package org.example.ecommerceorderandinventory.repository;

import org.example.ecommerceorderandinventory.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t WHERE t.user.id = :userId")
    List<Transaction> getUserTransactions(long userId);

}
