package com.management.account.response;

import com.management.account.request.CreateAccountRequest;

public class CreateAccountResponse extends CreateAccountRequest {

    private String accountNumber;

    public CreateAccountResponse(String accountName, String phoneNumber, String accountNumber){
        super(accountName, phoneNumber);
        this.accountNumber = accountNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    @Override
    public String toString() {
        return "CreateAccountResponse(accountName=" + getAccountName() + ", phoneNumber=" + getPhoneNumber() + ", accountNumber=" + getAccountNumber() + ")";
    }
}
