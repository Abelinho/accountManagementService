package com.management.account.repository;

import com.management.account.model.Account;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public class AccountRepository {

    private final Database<String, Account> database;

    public AccountRepository(Database<String, Account> database){
        this.database = database;
    }

    public Account save(Account account){
        if(Objects.nonNull(account)){
            return database.save(account.getAccountNumber(), account);
        }
        return null;
    }

    public Account getAccount(String accountNumber){
        return database.read(accountNumber);
    }

    public List<Account> getAccounts(List<String> accountNumbers){
        return database.read(accountNumbers);
    }

    public List<Account> getAllAccount(){
        return database.readAll();
    }

    public void removeAccount(String accountNumber){
        database.delete(accountNumber);
    }

}
