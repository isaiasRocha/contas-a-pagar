package com.br.contasapagar.application.controller;

import com.br.contasapagar.application.dto.ContaRequest;
import com.br.contasapagar.application.dto.ContaResponse;
import com.br.contasapagar.application.service.ContaService;
import com.br.contasapagar.application.service.CsvService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/contas")
@RequiredArgsConstructor
@Tag(name = "Contas API", description = "API para gerenciamento de contas a pagar")
public class ContaController {

    private final ContaService contaService;

    private final CsvService csvService;

    @PostMapping
    @Operation(summary = "Cadastrar uma nova conta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conta cadastrada com sucesso",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ContaResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content)
    })
    public ResponseEntity<ContaResponse> createConta(@RequestBody ContaRequest contaRequest) {
        return ResponseEntity.ok(contaService.save(contaRequest));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar uma conta existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conta atualizada com sucesso",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ContaResponse.class)) }),
            @ApiResponse(responseCode = "404", description = "Conta não encontrada", content = @Content)
    })
    public ResponseEntity<ContaResponse> updateConta(@PathVariable @Parameter(description = "ID da conta") Integer id,
                                                     @RequestBody ContaRequest contaRequest) {
        ContaResponse contaResponse = contaService.update(id, contaRequest);
        if (contaResponse != null) {
            return ResponseEntity.ok(contaResponse);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/situacao")
    @Operation(summary = "Alterar a situação de uma conta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Situação da conta atualizada com sucesso",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ContaResponse.class)) }),
            @ApiResponse(responseCode = "404", description = "Conta não encontrada", content = @Content)
    })
    public ResponseEntity<ContaResponse> updateSituacao(@PathVariable @Parameter(description = "ID da conta") Integer id,
                                                        @RequestBody @Parameter(description = "Nova situação da conta") String situacao) {
        ContaResponse contaResponse = contaService.updateSituacao(id, situacao);
        if (contaResponse != null) {
            return ResponseEntity.ok(contaResponse);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    @Operation(summary = "Buscar contas com filtros de data de vencimento e descrição")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de contas filtradas",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Page.class)) })
    })
    public ResponseEntity<Page<ContaResponse>> searchContas(
            @RequestParam(required = false) @Parameter(description = "Descrição da conta") String descricao,
            @RequestParam(required = false) @Parameter(description = "Data de vencimento da conta") LocalDate dataVencimento,
            Pageable pageable
    ) {
        return ResponseEntity.ok(contaService.searchContas(descricao, dataVencimento, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter uma conta pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conta encontrada",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ContaResponse.class)) }),
            @ApiResponse(responseCode = "404", description = "Conta não encontrada", content = @Content)
    })
    public ResponseEntity<ContaResponse> getContaById(@PathVariable @Parameter(description = "ID da conta") Integer id) {
        ContaResponse contaResponse = contaService.findById(id);
        if (contaResponse != null) {
            return ResponseEntity.ok(contaResponse);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/valor-total")
    @Operation(summary = "Obter o valor total pago por período")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Valor total pago no período",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BigDecimal.class)) })
    })
    public ResponseEntity<BigDecimal> getValorTotalPagoPorPeriodo(
            @RequestParam @Parameter(description = "Data de início do período") LocalDate startDate,
            @RequestParam @Parameter(description = "Data de término do período") LocalDate endDate
    ) {
        return ResponseEntity.ok(contaService.getTotalPagoPorPeriodo(startDate, endDate));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar uma conta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Conta deletada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Conta não encontrada", content = @Content)
    })
    public ResponseEntity<Void> deleteConta(@PathVariable @Parameter(description = "ID da conta") Integer id) {
        contaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/import")
    @Operation(summary = "Importar contas via arquivo CSV")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Arquivo CSV importado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao processar o arquivo CSV", content = @Content)
    })
    public ResponseEntity<String> importCsv(@RequestParam("file") @Parameter(description = "Arquivo CSV a ser importado") MultipartFile file) {
        csvService.importCsv(file);
        return ResponseEntity.ok("Arquivo CSV importado com sucesso!");
    }
}
