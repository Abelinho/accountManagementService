package com.management.account.request;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.math.BigDecimal;

public class DepositAccountRequest {

    private String accountNumber;

    @JsonAlias("amountToDeposit")
    private BigDecimal amountDeposited;

    public DepositAccountRequest(){}

    public DepositAccountRequest(String accountNumber, BigDecimal amountDeposited){
        this.accountNumber = accountNumber;
        this.amountDeposited = amountDeposited;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getAmountDeposited() {
        return amountDeposited;
    }

    public void setAmountDeposited(BigDecimal amountDeposited) {
        this.amountDeposited = amountDeposited;
    }
}
