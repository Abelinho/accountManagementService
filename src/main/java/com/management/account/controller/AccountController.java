package com.management.account.controller;

import com.management.account.model.Transaction;
import com.management.account.request.CreateAccountRequest;
import com.management.account.request.DepositAccountRequest;
import com.management.account.request.WithdrawalAccountRequest;
import com.management.account.response.CreateAccountResponse;
import com.management.account.response.DepositAccountResponse;
import com.management.account.response.WithdrawalAccountResponse;
import com.management.account.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService){
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<CreateAccountResponse> createAccount(@RequestBody CreateAccountRequest request) throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(accountService.createAccount(request).get());
    }

    @PostMapping("/deposit")
    public ResponseEntity<DepositAccountResponse> depositAccount(@RequestBody DepositAccountRequest request) throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(accountService.deposit(request).get());
    }

    @PostMapping("/withdrawal")
    public ResponseEntity<WithdrawalAccountResponse> withdrawal(@RequestBody WithdrawalAccountRequest request) throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(accountService.withdrawal(request).get());
    }

    @PostMapping("/transaction/{accountNumber}")
    public ResponseEntity<List<Transaction>> withdrawal(@PathVariable(required = false) String accountNumber) throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(accountService.transaction(accountNumber).get());
    }

}
