package com.odiaz.moneymanager.service;

import com.odiaz.moneymanager.dto.RecentTransactionDTO;
import com.odiaz.moneymanager.dto.expense.ExpenseDTO;
import com.odiaz.moneymanager.dto.income.IncomeDTO;
import com.odiaz.moneymanager.model.ProfileEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class DashboardService {

    private final ExpenseService expenseService;
    private final IncomeService incomeService;
    private final ProfileService profileService;

    public DashboardService(ExpenseService expenseService, IncomeService incomeService, ProfileService profileService) {
        this.expenseService = expenseService;
        this.incomeService = incomeService;
        this.profileService = profileService;
    }

    public Map<String, Object> getDashboardData(){
        ProfileEntity profile = profileService.getUserLogged();
        Map<String, Object> data = new LinkedHashMap<>();
        List<IncomeDTO> latestIncomes = incomeService.getLatestFiveExpensesForCurrentUser();
        List<ExpenseDTO> latestExpenses = expenseService.getLatestFiveExpensesForCurrentUser();
        List<RecentTransactionDTO> recentTransactions = Stream.concat(
                latestIncomes.stream().map( i -> RecentTransactionDTO.builder()
                        .id(i.getId())
                        .profileId( profile.getId() )
                        .icon( i.getIcon() )
                        .name( i.getName() )
                        .amount( i.getAmount() )
                        .date( i.getDate() )
                        .createdAt( i.getCreatedAt() )
                        .updatedAt( i.getUpdatedAt() )
                        .type("income")
                        .build() ),
                latestExpenses.stream().map( e -> RecentTransactionDTO.builder()
                        .id(e.getId())
                        .profileId( profile.getId() )
                        .icon( e.getIcon() )
                        .name( e.getName() )
                        .amount( e.getAmount() )
                        .date( e.getDate() )
                        .createdAt( e.getCreatedAt() )
                        .updatedAt( e.getUpdatedAt() )
                        .type("expense")
                        .build() ))
                .sorted( (a, b) -> {
                    int cmp = b.getDate().compareTo(a.getDate());
                    if( cmp == 0 && a.getCreatedAt() != null && b.getCreatedAt() != null ){
                        return b.getCreatedAt().compareTo(a.getCreatedAt());
                    }
                    return cmp;
                }).toList();
        data.put("totalBalance", incomeService.getTotalIncomesForCurrentUser().subtract(expenseService.getTotalExpenseForCurrentUser()) );
        data.put("totalIncomes", incomeService.getTotalIncomesForCurrentUser() );
        data.put("totalExpenses", expenseService.getTotalExpenseForCurrentUser());
        data.put("recent5Expenses", latestExpenses);
        data.put("recent5Incomes", latestIncomes);
        data.put("recentTransactions", recentTransactions);
        return data;
    }

}







