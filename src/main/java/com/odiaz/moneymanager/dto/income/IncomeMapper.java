package com.odiaz.moneymanager.dto.income;

import com.odiaz.moneymanager.dto.expense.ExpenseDTO;
import com.odiaz.moneymanager.model.CategoryEntity;
import com.odiaz.moneymanager.model.ExpenseEntity;
import com.odiaz.moneymanager.model.IncomeEntity;
import com.odiaz.moneymanager.model.ProfileEntity;
import org.springframework.stereotype.Component;

@Component
public class IncomeMapper {

    public IncomeEntity toEntity(IncomeDTO dto, ProfileEntity profile, CategoryEntity category){
        return IncomeEntity.builder()
                .name(dto.getName())
                .icon(dto.getIcon())
                .amount(dto.getAmount())
                .date(dto.getDate())
                .profile(profile)
                .category(category)
                .build();
    }

    public IncomeDTO toDto(IncomeEntity income){
        return IncomeDTO.builder()
                .id(income.getId())
                .name(income.getName())
                .icon(income.getIcon())
                .categoryName(income.getCategory() != null ? income.getCategory().getName() : null )
                .categoryId(income.getCategory() != null ? income.getCategory().getId() : null)
                .amount(income.getAmount())
                .date(income.getDate())
                .createdAt(income.getCreatedAt())
                .updatedAt(income.getUpdatedAt())
                .build();
    }

}
