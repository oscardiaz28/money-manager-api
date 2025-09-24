package com.odiaz.moneymanager.controller;

import com.odiaz.moneymanager.dto.FilterDTO;
import com.odiaz.moneymanager.dto.expense.ExpenseDTO;
import com.odiaz.moneymanager.dto.income.IncomeDTO;
import com.odiaz.moneymanager.service.ExpenseService;
import com.odiaz.moneymanager.service.IncomeService;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/filters")
public class FilterController {

    private final ExpenseService expenseService;
    private final IncomeService incomeService;

    public FilterController(ExpenseService expenseService, IncomeService incomeService) {
        this.expenseService = expenseService;
        this.incomeService = incomeService;
    }

    @PostMapping
    public ResponseEntity<?> filterTransactions(@RequestBody FilterDTO filter){
        LocalDate start = LocalDate.now().withDayOfMonth(1);
        LocalDate startDate = filter.getStartDate() != null ? filter.getStartDate() : start;
        LocalDate endDate = filter.getEndDate() != null ? filter.getEndDate() : LocalDate.now();
        String keyword = filter.getKeyword() != null ? filter.getKeyword() : "";
        String sortField = filter.getSortField() != null ? filter.getSortField() : "date";
        Sort.Direction direction = "desc".equalsIgnoreCase(filter.getSortOrder()) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, sortField);

        if("income".equalsIgnoreCase(filter.getType())){
            List<IncomeDTO> incomes = incomeService.filterExpenses(startDate, endDate, keyword, sort);
            return ResponseEntity.ok().body(incomes);
        }else if( "expense".equalsIgnoreCase(filter.getType()) ){
            List<ExpenseDTO> expenses = expenseService.filterExpenses(startDate, endDate, keyword, sort);
            return ResponseEntity.ok().body(expenses);
        }else{
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid type. Must be 'income' or 'expense' "));
        }
    }
}
