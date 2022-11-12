package com.management.account.repository;

import com.management.account.model.Transaction;
import com.management.account.util.AccountUtil;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class TransactionRepository {

    private final Database<Long, Transaction> database;

    public TransactionRepository(Database<Long, Transaction> database){
        this.database = database;
    }

    public Transaction save(Transaction transaction){
        if(Objects.nonNull(transaction)){
            if(Objects.isNull(transaction.getId())){
                transaction.setId(AccountUtil.generateId());
            }
            return database.save(transaction.getId(), transaction);
        }
        return null;
    }

    public Transaction getTransaction(Long transactionId){
        return database.read(transactionId);
    }

    public List<Transaction> getTransactionByAccountNumber(String accountNumber){
        List<Transaction> allTxn = getAllTransactions();
        if(Objects.nonNull(allTxn) && Objects.nonNull(accountNumber)){
            return allTxn.stream().filter(txn -> txn.getAccountNumber().equals(accountNumber)).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public List<Transaction> getTransactions(List<Long> transactionIds){
        return database.read(transactionIds);
    }

    public List<Transaction> getAllTransactions(){
        return database.readAll();
    }

    public void removeTransaction(Long transactionId){
        database.delete(transactionId);
    }

}
