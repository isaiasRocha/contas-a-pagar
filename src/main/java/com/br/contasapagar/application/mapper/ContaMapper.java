package com.br.contasapagar.application.mapper;

import com.br.contasapagar.application.dto.ContaResponse;
import com.br.contasapagar.application.dto.ContaRequest;
import com.br.contasapagar.domain.entity.Conta;
import org.springframework.stereotype.Component;

@Component
public class ContaMapper {

    public Conta toEntity(ContaRequest contaRequest) {
        Conta conta = new Conta();
        conta.setDataVencimento(contaRequest.getDataVencimento());
        conta.setDataPagamento(contaRequest.getDataPagamento());
        conta.setValor(contaRequest.getValor());
        conta.setDescricao(contaRequest.getDescricao());
        conta.setSituacao(contaRequest.getSituacao());
        return conta;
    }

    public void updateEntity(Conta conta, ContaRequest contaRequest) {
        conta.setDataVencimento(contaRequest.getDataVencimento());
        conta.setDataPagamento(contaRequest.getDataPagamento());
        conta.setValor(contaRequest.getValor());
        conta.setDescricao(contaRequest.getDescricao());
        conta.setSituacao(contaRequest.getSituacao());
    }

    public void updateSituacao(Conta conta, String situacao) {
        conta.setSituacao(situacao);
    }

    public ContaResponse toResponse(Conta conta) {
        ContaResponse contaResponse = new ContaResponse();
        contaResponse.setId(conta.getId());
        contaResponse.setDataVencimento(conta.getDataVencimento());
        contaResponse.setDataPagamento(conta.getDataPagamento());
        contaResponse.setValor(conta.getValor());
        contaResponse.setDescricao(conta.getDescricao());
        contaResponse.setSituacao(conta.getSituacao());
        return contaResponse;
    }
}
