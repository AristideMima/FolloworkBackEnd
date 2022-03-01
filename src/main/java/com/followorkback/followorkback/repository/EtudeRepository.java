package com.followorkback.followorkback.repository;

import com.followorkback.followorkback.entity.Credit;
import com.followorkback.followorkback.entity.Etude;
import com.followorkback.followorkback.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

public interface EtudeRepository extends JpaRepository<Etude, String> {

    @Transactional
    long deleteAllByGenericCode(String generic_code);

    Etude findByGenericCode(String generic_code);

    @Query(value = "Select e from Etude e  left join EtudeAnalysis b on e.genericCode = b.etudeGeneric")
    List<?> findAllJoinAnalysis();

    @Query(value = "Select e, b from Etude e left join EtudeAnalysis b on e.genericCode = b.etudeGeneric where b.username = :username order by b.updatedAt desc ")
    List<?> findAnalystJoinAnalysis(@Param("username") String username);

    @Query(value = "Select m, e from Monitor m left join Etude e on m.dossierId = e.genericCode where m.dossierId = :genericCode order by m.updatedAt desc ")
    List<?> findGenericJoinMonitor(@Param("genericCode") String genericCode);

    @Query(value = "Select e, b from Etude e left join EtudeAnalysis b on e.genericCode = b.etudeGeneric where e.userManager.username = :username order by b.updatedAt desc ")
    List<?> findAnalystJoinManager(@Param("username") String username);


}
