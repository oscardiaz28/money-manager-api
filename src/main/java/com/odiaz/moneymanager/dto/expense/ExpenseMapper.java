package com.odiaz.moneymanager.dto.expense;

import com.odiaz.moneymanager.model.CategoryEntity;
import com.odiaz.moneymanager.model.ExpenseEntity;
import com.odiaz.moneymanager.model.ProfileEntity;
import org.springframework.stereotype.Component;

@Component
public class ExpenseMapper {

    public ExpenseEntity toEntity(ExpenseDTO dto, ProfileEntity profile, CategoryEntity category){
        return ExpenseEntity.builder()
                .name(dto.getName())
                .icon(dto.getIcon())
                .amount(dto.getAmount())
                .date(dto.getDate())
                .profile(profile)
                .category(category)
                .build();
    }

    public ExpenseDTO toDto(ExpenseEntity expense){
        return ExpenseDTO.builder()
                .id(expense.getId())
                .name(expense.getName())
                .icon(expense.getIcon())
                .categoryName(expense.getCategory() != null ? expense.getCategory().getName() : null )
                .categoryId(expense.getCategory() != null ? expense.getCategory().getId() : null)
                .amount(expense.getAmount())
                .date(expense.getDate())
                .createdAt(expense.getCreatedAt())
                .updatedAt(expense.getUpdatedAt())
                .build();
    }

}
