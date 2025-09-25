package com.odiaz.moneymanager.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface TransactionDTO {
    String getName();
    BigDecimal getAmount();
    LocalDate getDate();
    Integer getCategoryId();
}
