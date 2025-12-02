package org.example.ecommerceorderandinventory.controller;

import jakarta.validation.Valid;
import org.example.ecommerceorderandinventory.dto.in.TransactionCreateDTO;
import org.example.ecommerceorderandinventory.dto.out.FinishedTransactionDetailsDTO;
import org.example.ecommerceorderandinventory.security.UserDetailsImpl;
import org.example.ecommerceorderandinventory.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transaction")
@Validated
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }


    /*
    Generate only id's of transactions that the currently authenticated user made himself.
    Otherwise, if admin permissions are present, look up ANY transaction
     */
    @GetMapping
    @PostAuthorize("hasRole('ADMIN') or returnObject.body.userId == principal.userId")
    public ResponseEntity<FinishedTransactionDetailsDTO> getTransactionDetails(@RequestParam long transactionId) {
        FinishedTransactionDetailsDTO finishedTransactionDetailsDTO = transactionService.getTransactionDetails(transactionId);
        return ResponseEntity.ok(finishedTransactionDetailsDTO);
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('ADMIN') or #userId == principal.userId")
    public ResponseEntity<List<FinishedTransactionDetailsDTO>> getUserTransactions(@RequestParam long userId) {
        List<FinishedTransactionDetailsDTO> finishedTransactionDetailsDTOS = transactionService.getUserTransactionList(userId);

        return ResponseEntity.ok(finishedTransactionDetailsDTOS);
    }

    @PostMapping
    public ResponseEntity<Void> makeTransaction(@RequestBody @Valid TransactionCreateDTO transactionDetailsDTO, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        transactionService.makeTransaction(transactionDetailsDTO, userDetails);

        return ResponseEntity.ok().build();
    }
}
