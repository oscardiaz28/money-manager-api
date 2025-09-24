package com.odiaz.moneymanager.service;

import com.odiaz.moneymanager.dto.expense.ExpenseDTO;
import com.odiaz.moneymanager.dto.income.IncomeChartDTO;
import com.odiaz.moneymanager.dto.income.IncomeDTO;
import com.odiaz.moneymanager.dto.income.IncomeMapper;
import com.odiaz.moneymanager.model.CategoryEntity;
import com.odiaz.moneymanager.model.ExpenseEntity;
import com.odiaz.moneymanager.model.IncomeEntity;
import com.odiaz.moneymanager.model.ProfileEntity;
import com.odiaz.moneymanager.repository.CategoryRepository;
import com.odiaz.moneymanager.repository.IncomeRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class IncomeService {

    private final IncomeRepository incomeRepository;
    private final IncomeMapper incomeMapper;
    private final CategoryRepository categoryRepository;
    private final ProfileService profileService;
    private final ExcelService excelService;

    public IncomeService(IncomeRepository incomeRepository, IncomeMapper incomeMapper, CategoryRepository categoryRepository, ProfileService profileService, ExcelService excelService) {
        this.incomeRepository = incomeRepository;
        this.incomeMapper = incomeMapper;
        this.categoryRepository = categoryRepository;
        this.profileService = profileService;
        this.excelService = excelService;
    }

    private CategoryEntity findCategory(Integer categoryId){
        return categoryRepository.findById(categoryId)
                .orElseThrow( () -> new RuntimeException("Category not found") );
    }

    public List<IncomeDTO> getIncomes(){
        ProfileEntity profile = profileService.getUserLogged();
        List<IncomeEntity> incomes = incomeRepository.findByProfileIdOrderByDateDesc(profile.getId());
        return incomes.stream()
                .map( incomeMapper::toDto )
                .toList();
    }

    public List<IncomeDTO> getCurrentMontIncomesForCurrentUser(){
        ProfileEntity profile = profileService.getUserLogged();
        LocalDate now = LocalDate.now();
        LocalDate startDate = now.withDayOfMonth(1);
        LocalDate endDate = now.withDayOfMonth(now.lengthOfMonth());
        System.out.println("StartDate: " + startDate);
        System.out.println("EndDate: " + endDate);
        List<IncomeEntity> incomes = incomeRepository
                .findByProfileIdAndDateBetweenOrderByDateDesc(profile.getId(), startDate, endDate);
        return incomes.stream()
                .map( incomeMapper::toDto )
                .toList();
    }

    // get latest 5 incomes for current user
    public List<IncomeDTO> getLatestFiveExpensesForCurrentUser(){
        ProfileEntity profile = profileService.getUserLogged();
        List<IncomeEntity> expenses = incomeRepository.findTop5ByProfileIdOrderByDateDesc(profile.getId());
        return expenses.stream()
                .map( incomeMapper::toDto )
                .toList();
    }

    // get total incomes for current user
    public BigDecimal getTotalIncomesForCurrentUser(){
        ProfileEntity profile = profileService.getUserLogged();
        BigDecimal total = incomeRepository.findTotalIncomesByProfile(profile.getId());
        return total != null ? total : BigDecimal.ZERO;
    }

    public void deleteIncome(Integer incomeId){
        ProfileEntity profile = profileService.getUserLogged();
        IncomeEntity income = incomeRepository.findById(incomeId)
                .orElseThrow( () -> new RuntimeException("Income not found") );
        if( !income.getProfile().getId().equals(profile.getId()) ){
            throw new RuntimeException("Unauthorized to delete thisincome");
        }
        incomeRepository.delete(income);
    }

    public IncomeDTO addExpense(IncomeDTO body){
        ProfileEntity profile = profileService.getUserLogged();
        CategoryEntity category = this.findCategory(body.getCategoryId());
        IncomeEntity newIncome = incomeMapper.toEntity(body, profile, category);
        IncomeEntity saved = incomeRepository.save(newIncome);
        return incomeMapper.toDto(saved);
    }

    public List<IncomeDTO> filterExpenses(LocalDate startDate, LocalDate endDate, String keyword, Sort sort){
        ProfileEntity profile = profileService.getUserLogged();
        List<IncomeEntity> incomes =  incomeRepository.findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(profile.getId(), startDate, endDate, keyword, sort);
        return incomes.stream().map(incomeMapper::toDto).toList();
    }

    public List<IncomeChartDTO> getLast7Days(){
        ProfileEntity profile = profileService.getUserLogged();
        LocalDate fromDate = LocalDate.now().minusDays(6);
        return incomeRepository.findLast7Days(fromDate, profile.getId());
    }

    public byte[] getExcel(){
        ProfileEntity profile = profileService.getUserLogged();
        LocalDate now = LocalDate.now();
        LocalDate startDate = now.withDayOfMonth(1);
        LocalDate endDate = now.withDayOfMonth(now.lengthOfMonth());

        List<IncomeEntity> data = incomeRepository
                .findByProfileIdAndDateBetweenOrderByDateDesc(profile.getId(), startDate, endDate);

        List<IncomeDTO> incomes =  data.stream()
                .map( incomeMapper::toDto )
                .toList();

        return excelService.writeIncomesToExcel(incomes);
    }

}
