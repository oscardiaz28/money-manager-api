package com.odiaz.moneymanager.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
public class FilterDTO {
    private String type;
    private LocalDate startDate;
    private LocalDate endDate;
    private String keyword;
    private String sortField; // date, amount, name
    private String sortOrder; // desc, asc
}
