package com.odiaz.moneymanager.controller;

import com.odiaz.moneymanager.dto.income.IncomeChartDTO;
import com.odiaz.moneymanager.dto.income.IncomeDTO;
import com.odiaz.moneymanager.service.IncomeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/incomes")
public class IncomeController {

    private final IncomeService incomeService;

    public IncomeController(IncomeService incomeService) {
        this.incomeService = incomeService;
    }

    @GetMapping
    public ResponseEntity<?> getIncomes(){
        List<IncomeDTO> incomes = incomeService.getCurrentMontExpensesForCurrentUser();
        return ResponseEntity.ok().body(incomes);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteIncome(@PathVariable Integer id){
        incomeService.deleteIncome(id);
        return ResponseEntity.ok().body(Map.of("message", "Income deleted successfully"));
    }

    @PostMapping
    public ResponseEntity<?> addIncome(@RequestBody IncomeDTO body){
        IncomeDTO income = incomeService.addExpense(body);
        return ResponseEntity.ok().body(income);
    }

    @GetMapping("/chart")
    public ResponseEntity<?> getChartData(){
        List<IncomeChartDTO> data = incomeService.getLast7Days();
        return ResponseEntity.ok().body(data);
    }

}
