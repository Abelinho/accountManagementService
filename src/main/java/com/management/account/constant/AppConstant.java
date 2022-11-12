package com.management.account.constant;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class AppConstant {
    private AppConstant(){}
    public static final int INITIAL_DATA_SIZE = 100;
    public static final BigDecimal INITIAL_BALANCE = BigDecimal.valueOf(1000.00).setScale(2, RoundingMode.DOWN);

}
