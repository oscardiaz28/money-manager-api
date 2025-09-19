package com.odiaz.moneymanager.controller;

import com.odiaz.moneymanager.dto.expense.ExpenseDTO;
import com.odiaz.moneymanager.service.ExpenseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping
    public ResponseEntity<?> getExpenses(){
        List<ExpenseDTO> expenses = expenseService.getCurrentMontExpensesForCurrentUser();
        return ResponseEntity.ok().body(expenses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteExpense(@PathVariable Integer id){
        expenseService.deleteExpense(id);
        return ResponseEntity.ok().body(Map.of("message", "Expense deleted successfully"));
    }

    @PostMapping
    public ResponseEntity<?> addExpense(@RequestBody ExpenseDTO body){
        ExpenseDTO expense = expenseService.addExpense(body);
        return ResponseEntity.ok().body(expense);
    }

}
