package com.management.account.service;

import com.management.account.constant.TransactionType;
import com.management.account.model.Account;
import com.management.account.model.Transaction;
import com.management.account.repository.AccountRepository;
import com.management.account.repository.TransactionRepository;
import com.management.account.request.CreateAccountRequest;
import com.management.account.request.DepositAccountRequest;
import com.management.account.request.WithdrawalAccountRequest;
import com.management.account.response.CreateAccountResponse;
import com.management.account.response.DepositAccountResponse;
import com.management.account.response.WithdrawalAccountResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class accountServiceTest {

    @Autowired
    AccountService accountService;

    @MockBean
    AccountRepository accountRepository;

    @MockBean
    TransactionRepository transactionRepository;

    Account account = new Account();
    Transaction transaction = new Transaction();
    DepositAccountRequest depositAccountRequest = new DepositAccountRequest();
    WithdrawalAccountRequest withdrawalAccountRequest = new WithdrawalAccountRequest();
    CreateAccountRequest createAccountRequest = new CreateAccountRequest();
    List<Transaction> transList = new ArrayList<>();
    CreateAccountResponse createAccountResponse;
    DepositAccountResponse depositAccountResponse;
    WithdrawalAccountResponse withdrawalAccountResponse;
    CompletableFuture completableFuture;

    @BeforeEach
    void setUp() throws Exception {
        //create AccReq,Transaction and Account and CompletableFuture, return Transaction obj when TransRepo is called
        // return Account obj when account repository is called. run assertEquals with the CompletableFuture

        createAccountRequest.setAccountName("Onyenna");
        createAccountRequest.setPhoneNumber("1234567");

        depositAccountRequest.setAccountNumber("1234567890");
        depositAccountRequest.setAmountDeposited(new BigDecimal(2000));

        withdrawalAccountRequest.setAmountWithdrawn(new BigDecimal(500));

        account.setAccountName("Onyenna");

        account.setPhoneNumber("1234567");
        account.setAccountNumber("1234567890");
        account.setBalance(new BigDecimal(1000));

        createAccountResponse = new CreateAccountResponse(createAccountRequest.getAccountName(), createAccountRequest.getPhoneNumber(),"");

        depositAccountResponse = new DepositAccountResponse("1234567890",depositAccountRequest.getAmountDeposited(),new BigDecimal(3000));

        withdrawalAccountResponse = new WithdrawalAccountResponse("",withdrawalAccountRequest.getAmountWithdrawn(),new BigDecimal(0));

        transaction.setAccountNumber("1234567890");
        transaction.setTransactionAmount(new BigDecimal(2000));
        transaction.setType(TransactionType.CREATE);

        transList.add(transaction);

        completableFuture = CompletableFuture.completedFuture(createAccountResponse);

        Mockito.when(accountRepository.save(account))
        .thenReturn(account);

        Mockito.when(transactionRepository.save(transaction))
                .thenReturn(transaction);

        Mockito.when(accountRepository.getAccount("1234567890"))
                .thenReturn(account);

       // Mockito.when(accountService.saveTransaction(account,createAccountRequest,TransactionType.CREATE))
                //.thenReturn(transaction);

        Mockito.when(transactionRepository.getAllTransactions())
                .thenReturn(transList);
    }

    @Test
    void testCreateAccount() throws ExecutionException, InterruptedException {

         CompletableFuture returnedCompletableFuture = accountService.createAccount(createAccountRequest);

        Account returnedAccount = accountRepository.save(account);
      // Transaction returnedTransaction = accountService.saveTransaction(account,createAccountRequest,TransactionType.CREATE);

       // CompletableFuture returnedCompletableFuture = CompletableFuture.completedFuture(new CreateAccountResponse(returnedAccount.getAccountName(),returnedAccount.getPhoneNumber(),returnedAccount.getAccountNumber()));
        Object returnedCreateAccountResponse = returnedCompletableFuture.get();
         CreateAccountResponse castCreateAccountResponse = (CreateAccountResponse) returnedCreateAccountResponse;
        assertEquals(createAccountResponse.getAccountNumber(),castCreateAccountResponse.getAccountNumber());
    }

    @Test
    void testDeposit() throws ExecutionException, InterruptedException {
        CompletableFuture returnedCompletableFuture = accountService.deposit(depositAccountRequest);

        Object returnedCreateAccountResponse = returnedCompletableFuture.get();
        DepositAccountResponse castReturnedDepositAccountResponse = (DepositAccountResponse) returnedCreateAccountResponse;

        assertEquals(depositAccountResponse.getAmountDeposited(),castReturnedDepositAccountResponse.getAmountDeposited());

    }

    @Test
    void testWithdrawal() throws ExecutionException, InterruptedException {
        //Account returnedAccount = accountRepository.getAccount("1234567890");
        CompletableFuture returnedCompletableFuture =  accountService.withdrawal(withdrawalAccountRequest);
        Object returnedWithdrawalResponse = returnedCompletableFuture.get();
        WithdrawalAccountResponse castWithdrawalResponse = (WithdrawalAccountResponse) returnedWithdrawalResponse;

        assertEquals(withdrawalAccountResponse.getCurrentBalance(),castWithdrawalResponse.getCurrentBalance());
    }

    @Test
    void testTransaction() throws ExecutionException, InterruptedException {
        CompletableFuture returnedCompletableFuture = accountService.transaction("1234567890");
        Object returnedtransactionResponse = returnedCompletableFuture.get();
        List<Transaction> castTransactionResponse = (List<Transaction>) returnedtransactionResponse;
        //List<Transaction> returnedTransactionList = transactionRepository.getAllTransactions();
        assertEquals(new ArrayList<>(),castTransactionResponse);//no transactions done yet!
    }
}
