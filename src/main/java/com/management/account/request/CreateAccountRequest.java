package com.management.account.request;

public class CreateAccountRequest {
    private String accountName;
    private String phoneNumber;

    public CreateAccountRequest(){}

    public CreateAccountRequest(String accountName, String phoneNumber){
        this.accountName = accountName;
        this.phoneNumber = phoneNumber;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
