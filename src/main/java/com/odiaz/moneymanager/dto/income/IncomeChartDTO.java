package com.odiaz.moneymanager.dto.income;


import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IncomeChartDTO {
    private LocalDate date;
    private BigDecimal value;
}
