package com.management.account.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class AccountUtil {

    private AccountUtil(){}

    private static final AtomicLong counter = new AtomicLong(1);
    private static final AtomicLong accountNumber = new AtomicLong(1000000001);

    public static long generateId(){
        return counter.incrementAndGet();
    }

    public static String generateAccountNumber(){
        return accountNumber.getAndIncrement() + "";
    }

    private static long generateRandom(int length) {
        Random random = new Random();
        String digits = "";
        for (int i = 0; i < length; i++) {
            digits += random.nextInt(10) + 0;
        }
        return Long.parseLong(digits);
    }
}
