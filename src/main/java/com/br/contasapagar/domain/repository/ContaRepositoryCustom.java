package com.br.contasapagar.domain.repository;

import com.br.contasapagar.domain.entity.Conta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface ContaRepositoryCustom {
    Page<Conta> searchContas(String descricao, LocalDate dataVencimento, Pageable pageable);
}