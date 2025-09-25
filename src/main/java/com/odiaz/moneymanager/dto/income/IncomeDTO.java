package com.odiaz.moneymanager.dto.income;

import com.odiaz.moneymanager.dto.TransactionDTO;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IncomeDTO implements TransactionDTO {
    private Integer id;
    private String name;
    private String icon;
    private String categoryName;
    private Integer categoryId;
    private BigDecimal amount;
    private LocalDate date;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
