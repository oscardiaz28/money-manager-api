package com.odiaz.moneymanager.repository;

import com.odiaz.moneymanager.model.IncomeEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface IncomeRepository extends JpaRepository<IncomeEntity, Integer> {
    // select * from tbl_incomes where profile_id = :1 order by date desc
    List<IncomeEntity> findByProfileIdOrderByDateDesc(Integer profileId);

    // select * from tbl_incomes where profile_id = :1 order by date desc limit 5
    List<IncomeEntity> findTop5ByProfileIdOrderByDateDesc(Integer profileId);

    @Query("SELECT SUM(i.amount) FROM IncomeEntity i WHERE i.profile.id = :profileId ")
    BigDecimal findTotalIncomesByProfile(@Param("profileId") Integer profileId);

    List<IncomeEntity> findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(Integer profileId, LocalDate startDate, LocalDate endDate, String keyword, Sort sort);

    List<IncomeEntity> findByProfileIdAndDateBetween(Integer profileId, LocalDate startDate, LocalDate endDate);
}
