package com.odiaz.moneymanager.service;

import com.odiaz.moneymanager.dto.expense.ExpenseChartDTO;
import com.odiaz.moneymanager.dto.expense.ExpenseDTO;
import com.odiaz.moneymanager.dto.expense.ExpenseMapper;
import com.odiaz.moneymanager.dto.income.IncomeChartDTO;
import com.odiaz.moneymanager.dto.income.IncomeDTO;
import com.odiaz.moneymanager.model.CategoryEntity;
import com.odiaz.moneymanager.model.ExpenseEntity;
import com.odiaz.moneymanager.model.IncomeEntity;
import com.odiaz.moneymanager.model.ProfileEntity;
import com.odiaz.moneymanager.repository.CategoryRepository;
import com.odiaz.moneymanager.repository.ExpenseRepository;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final ExpenseMapper expenseMapper;
    private final ProfileService profileService;
    private final CategoryRepository categoryRepository;
    private final ExcelService excelService;

    public ExpenseService(ExpenseRepository expenseRepository, ExpenseMapper expenseMapper, ProfileService profileService, CategoryRepository categoryRepository, ExcelService excelService) {
        this.expenseRepository = expenseRepository;
        this.expenseMapper = expenseMapper;
        this.profileService = profileService;
        this.categoryRepository = categoryRepository;
        this.excelService = excelService;
    }

    private CategoryEntity findCategory(Integer categoryId){
        return categoryRepository.findById(categoryId)
                .orElseThrow( () -> new RuntimeException("Category not found") );
    }

    public List<ExpenseDTO> getExpenses(){
        ProfileEntity profile = profileService.getUserLogged();
        List<ExpenseEntity> expenses = expenseRepository.findByProfileIdOrderByDateDesc(profile.getId());
        return expenses.stream()
                .map( expenseMapper::toDto )
                .toList();
    }

    public List<ExpenseDTO> getCurrentMontExpensesForCurrentUser(){
        ProfileEntity profile = profileService.getUserLogged();
        LocalDate now = LocalDate.now();
        LocalDate startDate = now.withDayOfMonth(1);
        LocalDate endDate = now.withDayOfMonth(now.lengthOfMonth());
        System.out.println("StartDate: " + startDate);
        System.out.println("EndDate: " + endDate);
        List<ExpenseEntity> expenses = expenseRepository
                .findByProfileIdAndDateBetween(profile.getId(), startDate, endDate);
        return expenses.stream()
                .map( expenseMapper::toDto )
                .toList();
    }

    public void deleteExpense(Integer expenseId){
        ProfileEntity profile = profileService.getUserLogged();
        ExpenseEntity expense = expenseRepository.findById(expenseId)
                .orElseThrow( () -> new RuntimeException("Expense not found") );
        if( !expense.getProfile().getId().equals(profile.getId()) ){
            throw new RuntimeException("Unauthorized to delete this expense");
        }
        expenseRepository.delete(expense);
    }

    public ExpenseDTO addExpense(ExpenseDTO body){
        ProfileEntity profile = profileService.getUserLogged();
        CategoryEntity category = this.findCategory(body.getCategoryId());

        ExpenseEntity newExpense = expenseMapper.toEntity(body, profile, category);
        ExpenseEntity saved = expenseRepository.save(newExpense);

        return expenseMapper.toDto(saved);
    }

    // get latest 5 expenses for current user
    public List<ExpenseDTO> getLatestFiveExpensesForCurrentUser(){
        ProfileEntity profile = profileService.getUserLogged();
        List<ExpenseEntity> expenses = expenseRepository.findTop5ByProfileIdOrderByDateDesc(profile.getId());
        return expenses.stream()
                .map( expenseMapper::toDto )
                .toList();
    }

    // get total expenses for current user
    public BigDecimal getTotalExpenseForCurrentUser(){
        ProfileEntity profile = profileService.getUserLogged();
        BigDecimal total = expenseRepository.findTotalIncomesByProfile(profile.getId());
        return total != null ? total : BigDecimal.ZERO;
    }

    public List<ExpenseDTO> filterExpenses(LocalDate startDate, LocalDate endDate, String keyword, Sort sort){
        ProfileEntity profile = profileService.getUserLogged();
        List<ExpenseEntity> expenses =  expenseRepository.findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(profile.getId(), startDate, endDate, keyword, sort);
        return expenses.stream().map(expenseMapper::toDto).toList();
    }

    //notifications
    public List<ExpenseDTO> getExpensesForUserOnDate(Integer profileId, LocalDate date){
        List<ExpenseEntity> expenses = expenseRepository.findByProfileIdAndDate(profileId, date);
        return expenses.stream()
                .map(expenseMapper::toDto).toList();
    }

    public List<ExpenseChartDTO> getLast7Days(){
        ProfileEntity profile = profileService.getUserLogged();
        LocalDate fromDate = LocalDate.now().minusDays(6);
        return expenseRepository.findLast7Days(fromDate, profile.getId());
    }

    public byte[] getExcel(){
        ProfileEntity profile = profileService.getUserLogged();
        LocalDate now = LocalDate.now();
        LocalDate startDate = now.withDayOfMonth(1);
        LocalDate endDate = now.withDayOfMonth(now.lengthOfMonth());

        List<ExpenseEntity> data = expenseRepository
                .findByProfileIdAndDateBetweenOrderByDateDesc(profile.getId(), startDate, endDate);

        List<ExpenseDTO> expenses =  data.stream()
                .map( expenseMapper::toDto )
                .toList();

        return excelService.writeTransactionsToExcel(expenses);
    }

}
