package com.management.account.request;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.math.BigDecimal;

public class WithdrawalAccountRequest {

    private String accountNumber;

    @JsonAlias("amountToWithdraw")
    private BigDecimal amountWithdrawn;

    public WithdrawalAccountRequest(){}

    public WithdrawalAccountRequest(String accountNumber, BigDecimal amountWithdrawn){
        this.accountNumber = accountNumber;
        this.amountWithdrawn = amountWithdrawn;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getAmountWithdrawn() {
        return amountWithdrawn;
    }

    public void setAmountWithdrawn(BigDecimal amountWithdrawn) {
        this.amountWithdrawn = amountWithdrawn;
    }
}
