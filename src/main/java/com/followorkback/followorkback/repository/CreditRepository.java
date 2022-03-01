package com.followorkback.followorkback.repository;

import com.followorkback.followorkback.entity.Credit;
import com.followorkback.followorkback.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;


public interface CreditRepository extends JpaRepository<Credit, String> {
    @Transactional
    long deleteAllByGenericCode(String generic_code);

    Credit findByGenericCode(String generic_code);

    @Query(value = "Select c from Credit c left join CreditAnalysis d on c.genericCode = d.creditGeneric")
    List<?> findAllJoinAnalysis();

    @Query(value = "Select c, d from Credit c left join CreditAnalysis d on c.genericCode = d.creditGeneric where d.username = :username order by d.updatedAt desc ")
    List<?> findAnalystJoinAnalysis(@Param("username") String username);

    @Query(value = "Select m, e from Monitor m left join Credit e on m.dossierId = e.genericCode where m.dossierId = :genericCode order by m.updatedAt desc ")
    List<?> findGenericJoinMonitor(@Param("genericCode") String genericCode);

    @Query(value = "Select c, d from Credit c left join CreditAnalysis d on c.genericCode = d.creditGeneric where c.userManager.username = :username order by d.updatedAt desc ")
    List<?> findAnalystJoinManager(@Param("username") String username);
}
