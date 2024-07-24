package com.br.contasapagar.support;

import com.br.contasapagar.application.dto.ContaRequest;
import com.br.contasapagar.application.dto.ContaResponse;
import com.br.contasapagar.application.mapper.ContaMapper;
import com.br.contasapagar.domain.entity.Conta;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class ContaTestSupport {

    private static final ContaMapper mapper = new ContaMapper();

    public static ContaResponse getContaResponse() {
        return mapper.toResponse(getContaEntity());
    }

    public static Conta getContaEntity() {
        Conta conta = new Conta();
        conta.setId(1);
        conta.setDataVencimento(LocalDate.of(2024, 8, 1));
        conta.setDataPagamento(LocalDate.of(2024, 8, 5));
        conta.setValor(new BigDecimal("1500.00"));
        conta.setDescricao("Pagamento de aluguel");
        conta.setSituacao("PAGA");
        return conta;
    }

    public static ContaRequest getContaRequest() {
        return createContaRequest();
    }

    private static ContaRequest createContaRequest() {
        ContaRequest request = new ContaRequest();
        request.setDataVencimento(LocalDate.of(2024, 8, 1));
        request.setDataPagamento(LocalDate.of(2024, 8, 5));
        request.setValor(new BigDecimal("1500.00"));
        request.setDescricao("Pagamento de aluguel");
        request.setSituacao("PAGA");
        return request;
    }

    public static List<Conta> getContaEntityList() {
        return Arrays.asList(getContaEntity());
    }

    public static List<ContaResponse> getContaResponseList() {
        return Arrays.asList(getContaResponse());
    }
}
