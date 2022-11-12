package com.management.account.response;

import com.management.account.request.WithdrawalAccountRequest;

import java.math.BigDecimal;

public class WithdrawalAccountResponse extends WithdrawalAccountRequest {

    private BigDecimal currentBalance;

    public WithdrawalAccountResponse(String accountNumber, BigDecimal amountWithdrawn, BigDecimal currentBalance){
        super(accountNumber, amountWithdrawn);
        this.currentBalance = currentBalance;
    }

    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(BigDecimal currentBalance) {
        this.currentBalance = currentBalance;
    }
}
