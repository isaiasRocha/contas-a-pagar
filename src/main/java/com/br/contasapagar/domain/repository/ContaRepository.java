package com.br.contasapagar.domain.repository;

import com.br.contasapagar.domain.entity.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;

@Repository
public interface ContaRepository extends JpaRepository<Conta, Integer>, ContaRepositoryCustom {

    @Query("""
        SELECT SUM(c.valor) FROM Conta c WHERE c.dataPagamento BETWEEN :startDate AND :endDate
    """)
    BigDecimal getTotalPagoPorPeriodo(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
