package com.br.contasapagar.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class ContaRequest {

    @JsonProperty("dataVencimento")
    @Schema(description = "Data de vencimento da conta", example = "2024-12-31")
    private LocalDate dataVencimento;

    @JsonProperty("dataPagamento")
    @Schema(description = "Data de pagamento da conta", example = "2024-12-25")
    private LocalDate dataPagamento;

    @JsonProperty("valor")
    @Schema(description = "Valor da conta", example = "1500.00")
    private BigDecimal valor;

    @JsonProperty("descricao")
    @Schema(description = "Descrição da conta", example = "Conta de Luz")
    private String descricao;

    @JsonProperty("situacao")
    @Schema(description = "Situação da conta", example = "PAGA")
    private String situacao;
}
