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
import com.management.account.util.AccountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.management.account.constant.AppConstant.INITIAL_BALANCE;

@Service
public class AccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public AccountService(AccountRepository accountRepository, TransactionRepository transactionRepository){
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Async
    public CompletableFuture<CreateAccountResponse> createAccount(CreateAccountRequest request){
        Account account = new Account();
        account.setAccountName(request.getAccountName());
        account.setAccountNumber(AccountUtil.generateAccountNumber());
        account.setPhoneNumber(request.getPhoneNumber());
        account.setBalance(INITIAL_BALANCE);
        account.setCreatedDate(LocalDateTime.now());
        account.setUpdatedDate(LocalDateTime.now());

        Transaction transaction = saveTransaction(account, request, TransactionType.CREATE);
        account = accountRepository.save(account);
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(Objects.nonNull(account) && Objects.nonNull(transaction)){
            return CompletableFuture.completedFuture(new CreateAccountResponse(account.getAccountName(), account.getPhoneNumber(), account.getAccountNumber()));
        }
        return CompletableFuture.completedFuture(new CreateAccountResponse(request.getAccountName(), request.getPhoneNumber(), ""));
    }

    @Async
    public CompletableFuture<DepositAccountResponse> deposit(DepositAccountRequest request){
        Account account = accountRepository.getAccount(request.getAccountNumber());
        if(Objects.nonNull(account)){
            account.setBalance(addBalance(account.getBalance(), request.getAmountDeposited()));
            account.setUpdatedDate(LocalDateTime.now());

            Transaction transaction = saveTransaction(account, request, TransactionType.DEPOSIT);
            account = accountRepository.save(account);
            if(Objects.nonNull(account) && Objects.nonNull(transaction)){
                return CompletableFuture.completedFuture(new DepositAccountResponse(account.getAccountNumber(), request.getAmountDeposited(), account.getBalance()));
            }
        }
        return CompletableFuture.completedFuture(new DepositAccountResponse(request.getAccountNumber(), request.getAmountDeposited(), BigDecimal.ZERO));
    }

    @Async
    public CompletableFuture<WithdrawalAccountResponse> withdrawal(WithdrawalAccountRequest request){
        Account account = accountRepository.getAccount(request.getAccountNumber());
        if(Objects.nonNull(account) && request.getAmountWithdrawn().compareTo(account.getBalance()) > 0){
            BigDecimal newBalance = subtractBalance(account.getBalance(), request.getAmountWithdrawn());
            account.setBalance(newBalance);
            account.setUpdatedDate(LocalDateTime.now());

            Transaction transaction = saveTransaction(account, request, TransactionType.WITHDRAWAL);
            account = accountRepository.save(account);
            if(Objects.nonNull(account) && Objects.nonNull(transaction)){
                return CompletableFuture.completedFuture(new WithdrawalAccountResponse(account.getAccountNumber(), request.getAmountWithdrawn(), account.getBalance()));
            }
        }
        return CompletableFuture.completedFuture(new WithdrawalAccountResponse(request.getAccountNumber(), request.getAmountWithdrawn(), BigDecimal.ZERO));
    }

    @Async
    public CompletableFuture<List<Transaction>> transaction(String accountNumber){
        if(!StringUtils.hasText(accountNumber)){
            return CompletableFuture.completedFuture(transactionRepository.getAllTransactions()
                    .stream()
                    //.sorted(Comparator.comparing(Transaction::getCreatedAt).reversed())
                    .sorted(Comparator.comparing(Transaction::getCreatedAt, (datetime1, datetime2) ->{
                        long unixTime1 = datetime1.atZone(ZoneId.systemDefault()).toEpochSecond();
                        long unixTime2 = datetime2.atZone(ZoneId.systemDefault()).toEpochSecond();
                        return  Long.compare(unixTime1, unixTime2);
                    }).reversed())
                    .collect(Collectors.toList()));
        }
        return CompletableFuture.completedFuture(transactionRepository.getTransactionByAccountNumber(accountNumber)
                .stream()
                //.sorted(Comparator.comparing(Transaction::getCreatedAt).reversed())
                .sorted(Comparator.comparing(Transaction::getCreatedAt, (datetime1, datetime2) ->{
                    long unixTime1 = datetime1.atZone(ZoneId.systemDefault()).toEpochSecond();
                    long unixTime2 = datetime2.atZone(ZoneId.systemDefault()).toEpochSecond();
                    return  Long.compare(unixTime1, unixTime2);
                }).reversed())
                .collect(Collectors.toList()));
    }


    private BigDecimal addBalance(BigDecimal balance, BigDecimal newBalanceToAdd){
        return balance.add(newBalanceToAdd.abs()).setScale(2, RoundingMode.DOWN);
    }

    private BigDecimal subtractBalance(BigDecimal balance, BigDecimal newBalanceToSubtract){
        return balance.subtract(newBalanceToSubtract.abs()).setScale(2, RoundingMode.DOWN);
    }

    private Transaction saveTransaction(Account account, CreateAccountRequest request, TransactionType transactionType){
        return persistTransaction(account.getAccountNumber(), BigDecimal.ZERO, INITIAL_BALANCE, INITIAL_BALANCE, transactionType);
    }

    private Transaction saveTransaction(Account account, DepositAccountRequest request, TransactionType transactionType){
        BigDecimal newBalance = addBalance(account.getBalance(), request.getAmountDeposited());
        return persistTransaction(account.getAccountNumber(), account.getBalance(), newBalance, request.getAmountDeposited(), transactionType);
    }

    private Transaction saveTransaction(Account account, WithdrawalAccountRequest request, TransactionType transactionType){
        BigDecimal newBalance = subtractBalance(account.getBalance(), request.getAmountWithdrawn());
        return persistTransaction(account.getAccountNumber(), account.getBalance(), newBalance, request.getAmountWithdrawn(), transactionType);
    }

    private Transaction persistTransaction(String accountNumber, BigDecimal balanceBefore, BigDecimal balanceAfter,
                                           BigDecimal transactionAmount, TransactionType transactionType){
        Transaction transaction = new Transaction();
        transaction.setAccountNumber(accountNumber);
        transaction.setBalanceBefore(balanceBefore);
        transaction.setBalanceAfter(balanceAfter);
        transaction.setTransactionAmount(transactionAmount);
        transaction.setType(transactionType);

        long unixTime = Instant.now().getEpochSecond();
        LocalDateTime createdDate = Instant.ofEpochSecond(unixTime).atZone(ZoneId.systemDefault()).toLocalDateTime();
        transaction.setCreatedAt(createdDate);
        return transactionRepository.save(transaction);
    }

}
