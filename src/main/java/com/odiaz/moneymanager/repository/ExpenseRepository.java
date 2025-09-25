package com.odiaz.moneymanager.repository;

import com.odiaz.moneymanager.dto.expense.ExpenseChartDTO;
import com.odiaz.moneymanager.dto.expense.ExpenseDTO;
import com.odiaz.moneymanager.dto.income.IncomeChartDTO;
import com.odiaz.moneymanager.model.ExpenseEntity;
import com.odiaz.moneymanager.model.IncomeEntity;
import com.odiaz.moneymanager.model.ProfileEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Integer> {
    // select * from tbl_expenses where profile_id = :1 order by date desc
    List<ExpenseEntity> findByProfileIdOrderByDateDesc(Integer profileId);

    // select * from tbl_expenses where profile_id = :1 order by date desc limit 5
    List<ExpenseEntity> findTop5ByProfileIdOrderByDateDesc(Integer profileId);

    @Query("SELECT SUM(e.amount) FROM ExpenseEntity e WHERE e.profile.id = :profileId ")
    BigDecimal findTotalIncomesByProfile(@Param("profileId") Integer profileId);

    List<ExpenseEntity> findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(Integer profileId, LocalDate startDate, LocalDate endDate, String keyword, Sort sort);

    List<ExpenseEntity> findByProfileIdAndDateBetween(Integer profileId, LocalDate startDate, LocalDate endDate);

    List<ExpenseEntity> findByProfileIdAndDateBetweenOrderByDateDesc(Integer profileId, LocalDate startDate, LocalDate endDate);

   List<ExpenseEntity> findByProfileIdAndDate(Integer profileId, LocalDate date);

    @Query("SELECT new com.odiaz.moneymanager.dto.expense.ExpenseChartDTO(ti.date, SUM(ti.amount)) " +
            "FROM ExpenseEntity ti " +
            "WHERE ti.date >= :date AND ti.profile.id = :profileId " +
            "GROUP BY ti.date ORDER BY ti.date ASC")
    List<ExpenseChartDTO> findLast7Days(@Param("date") LocalDate date,
                                        @Param("profileId") Integer profileId);

}
