package com.br.contasapagar.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class ContaRequest {

    @JsonProperty("dataVencimento")
    private LocalDate dataVencimento;

    @JsonProperty("dataPagamento")
    private LocalDate dataPagamento;

    @JsonProperty("valor")
    private BigDecimal valor;

    @JsonProperty("descricao")
    private String descricao;

    @JsonProperty("situacao")
    private String situacao;
}
