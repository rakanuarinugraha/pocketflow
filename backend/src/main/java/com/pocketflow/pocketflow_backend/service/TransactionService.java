package com.pocketflow.pocketflow_backend.service;

import com.pocketflow.pocketflow_backend.dto.TransactionRequest;
import com.pocketflow.pocketflow_backend.dto.TransactionResponse;
import com.pocketflow.pocketflow_backend.entity.Category;
import com.pocketflow.pocketflow_backend.entity.Transaction;
import com.pocketflow.pocketflow_backend.repository.CategoryRepository;
import com.pocketflow.pocketflow_backend.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<TransactionResponse> getAllTransactions(){
        return transactionRepository.findAll()
                .stream()
                .map(TransactionResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TransactionResponse getTransactionById(Long id){
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction with ID " + id + " not found"));
        return TransactionResponse.fromEntity(transaction);
    }

    @Transactional
    public TransactionResponse createTransaction(TransactionRequest request){
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category with ID " + request.getCategoryId() + " not found"));

        Transaction transaction = Transaction.builder()
                .amount(request.getAmount())
                .description(request.getDescription())
                .transactionDate(request.getTransactionDate() != null ? request.getTransactionDate() : LocalDate.now())
                .category(category)
                .build();

        Transaction savedTransaction = transactionRepository.save(transaction);
        return TransactionResponse.fromEntity(savedTransaction);
    }

    @Transactional
    public void deleteTransaction(Long id){
        if(!transactionRepository.existsById(id)){
            throw new RuntimeException("Tranaction with ID " + id + " not found");
        }
        transactionRepository.deleteById(id);
    }
}
