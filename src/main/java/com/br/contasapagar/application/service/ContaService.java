package com.br.contasapagar.application.service;

import com.br.contasapagar.application.dto.ContaRequest;
import com.br.contasapagar.application.dto.ContaResponse;
import com.br.contasapagar.application.mapper.ContaMapper;
import com.br.contasapagar.domain.entity.Conta;
import com.br.contasapagar.domain.repository.ContaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ContaService {

    private final ContaRepository contaRepository;

    private final ContaMapper contaMapper;

    public ContaResponse save(ContaRequest contaRequest) {
        Conta conta = contaMapper.toEntity(contaRequest);
        conta = contaRepository.save(conta);
        return contaMapper.toResponse(conta);
    }

    public ContaResponse update(Integer id, ContaRequest contaRequest) {
        Optional<Conta> optionalConta = contaRepository.findById(id);
        if (optionalConta.isPresent()) {
            Conta conta = optionalConta.get();
            contaMapper.updateEntity(conta, contaRequest);
            return contaMapper.toResponse(contaRepository.save(conta));
        }
        return null;
    }

    public ContaResponse updateSituacao(Integer id, String situacao) {
        Optional<Conta> optionalConta = contaRepository.findById(id);
        if (optionalConta.isPresent()) {
            Conta conta = optionalConta.get();
            contaMapper.updateSituacao(conta, situacao);
            return contaMapper.toResponse(conta);
        }
        return null;
    }

    public void delete(Integer id) {
        contaRepository.deleteById(id);
    }

    public ContaResponse findById(Integer id) {
        Optional<Conta> contaOpt = contaRepository.findById(id);
        return contaOpt.map(contaMapper::toResponse).orElse(null);
    }

    public Page<ContaResponse> searchContas(String descricao, LocalDate dataVencimento, Pageable pageable) {
        return contaRepository.searchContas(descricao, dataVencimento, pageable).map(contaMapper::toResponse);
    }

    public BigDecimal getTotalPagoPorPeriodo(LocalDate startDate, LocalDate endDate) {
        return contaRepository.getTotalPagoPorPeriodo(startDate, endDate);
    }
}
