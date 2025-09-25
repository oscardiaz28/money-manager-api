package com.odiaz.moneymanager.controller;

import com.odiaz.moneymanager.dto.expense.ExpenseChartDTO;
import com.odiaz.moneymanager.dto.expense.ExpenseDTO;
import com.odiaz.moneymanager.service.ExpenseService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
        List<ExpenseDTO> expenses = expenseService.getExpenses();
        return ResponseEntity.ok().body(expenses);
    }

    @GetMapping("/chart")
    public ResponseEntity<?> getChart(){
        List<ExpenseChartDTO> data = expenseService.getLast7Days();
        return ResponseEntity.ok().body(data);
    }

    @GetMapping("/excel")
    public ResponseEntity<?> getExcelDocument(){
        byte[] excel = expenseService.getExcel();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=expenses.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(excel);
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
