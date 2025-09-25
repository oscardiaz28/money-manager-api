package com.odiaz.moneymanager.dto.expense;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExpenseChartDTO {
    private LocalDate date;
    private BigDecimal value;
}
