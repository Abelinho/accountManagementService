package com.management.account;

import com.management.account.request.CreateAccountRequest;
import com.management.account.response.CreateAccountResponse;
import com.management.account.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

@Configuration
public class AppRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(AppRunner.class);

    private final AccountService accountService;

    public AppRunner(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void run(String... args) throws Exception {
        // Start the clock
        long start = System.currentTimeMillis();

        // Kick of multiple, asynchronous lookups
        CompletableFuture<CreateAccountResponse> task1 = accountService.createAccount(new CreateAccountRequest("wisdom-1", "090"));
        CompletableFuture<CreateAccountResponse> task2 = accountService.createAccount(new CreateAccountRequest("wisdom-2", "090"));
        CompletableFuture<CreateAccountResponse> task3 = accountService.createAccount(new CreateAccountRequest("wisdom-3", "090"));
        CompletableFuture<CreateAccountResponse> task4 = accountService.createAccount(new CreateAccountRequest("wisdom-4", "090"));
        CompletableFuture<CreateAccountResponse> task5 = accountService.createAccount(new CreateAccountRequest("wisdom-5", "090"));
        CompletableFuture<CreateAccountResponse> task6 = accountService.createAccount(new CreateAccountRequest("wisdom-6", "090"));
        CompletableFuture<CreateAccountResponse> task7 = accountService.createAccount(new CreateAccountRequest("wisdom-7", "090"));
        CompletableFuture<CreateAccountResponse> task8 = accountService.createAccount(new CreateAccountRequest("wisdom-8", "090"));
        CompletableFuture<CreateAccountResponse> task9 = accountService.createAccount(new CreateAccountRequest("wisdom-9", "090"));
        CompletableFuture<CreateAccountResponse> task10 = accountService.createAccount(new CreateAccountRequest("wisdom-10", "090"));
        CompletableFuture<CreateAccountResponse> task11 = accountService.createAccount(new CreateAccountRequest("wisdom-11", "090"));

        // Wait until they are all done
        CompletableFuture.allOf(task1, task2, task3, task4, task5, task6, task7, task8, task9, task10, task11).join();

        // Print results, including elapsed time
        logger.info("Elapsed time: " + (System.currentTimeMillis() - start));

//        logger.info("Instant.now=" + Instant.now().getEpochSecond());
//        logger.info("System.now=" + System.currentTimeMillis()/1000L);

        logger.info("--> " + task1.get());
        logger.info("--> " + task2.get());
        logger.info("--> " + task3.get());
        logger.info("--> " + task4.get());
        logger.info("--> " + task5.get());
        logger.info("--> " + task6.get());
        logger.info("--> " + task7.get());
        logger.info("--> " + task8.get());
        logger.info("--> " + task9.get());
        logger.info("--> " + task10.get());
        logger.info("--> " + task11.get());

    }
}
