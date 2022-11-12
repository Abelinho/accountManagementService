package com.management.account.response;

import com.management.account.request.DepositAccountRequest;

import java.math.BigDecimal;

public class DepositAccountResponse extends DepositAccountRequest {

    private BigDecimal totalBalance;

    public DepositAccountResponse(String accountNumber, BigDecimal amountDeposited, BigDecimal totalBalance){
        super(accountNumber, amountDeposited);
        this.totalBalance = totalBalance;
    }

    public BigDecimal getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(BigDecimal totalBalance) {
        this.totalBalance = totalBalance;
    }
}
