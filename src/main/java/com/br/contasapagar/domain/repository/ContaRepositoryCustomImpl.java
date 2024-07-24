package com.br.contasapagar.domain.repository;

import com.br.contasapagar.domain.entity.Conta;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ContaRepositoryCustomImpl implements ContaRepositoryCustom {

    private final EntityManager entityManager;

    @Override
    public Page<Conta> searchContas(String descricao, LocalDate dataVencimento, Pageable pageable) {
        StringBuilder jpql = new StringBuilder("SELECT c FROM Conta c WHERE 1=1");
        if (descricao != null && !descricao.isEmpty()) {
            jpql.append(" AND c.descricao LIKE :descricao");
        }
        if (dataVencimento != null) {
            jpql.append(" AND c.dataVencimento = :dataVencimento");
        }

        TypedQuery<Conta> query = entityManager.createQuery(jpql.toString(), Conta.class);
        if (descricao != null && !descricao.isEmpty()) {
            query.setParameter("descricao", "%" + descricao + "%");
        }
        if (dataVencimento != null) {
            query.setParameter("dataVencimento", dataVencimento);
        }

        int totalRows = query.getResultList().size();
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<Conta> contas = query.getResultList();
        return new PageImpl<>(contas, pageable, totalRows);
    }
}
