package com.br.contasapagar.application.controller;

import com.br.contasapagar.application.dto.ContaRequest;
import com.br.contasapagar.application.dto.ContaResponse;
import com.br.contasapagar.application.service.ContaService;
import com.br.contasapagar.application.service.CsvService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/contas")
@RequiredArgsConstructor
public class ContaController {

    private final ContaService contaService;

    private final CsvService csvService;

    @PostMapping
    public ResponseEntity<ContaResponse> createConta(@RequestBody ContaRequest contaRequest) {
        return ResponseEntity.ok(contaService.save(contaRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContaResponse> updateConta(@PathVariable Integer id, @RequestBody ContaRequest contaRequest) {
        ContaResponse contaResponse = contaService.update(id, contaRequest);
        if (contaResponse != null) {
            return ResponseEntity.ok(contaResponse);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/situacao")
    public ResponseEntity<ContaResponse> updateSituacao(@PathVariable Integer id, @RequestBody String situacao) {
        ContaResponse contaResponse = contaService.updateSituacao(id, situacao);
        if (contaResponse != null) {
            return ResponseEntity.ok(contaResponse);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<Page<ContaResponse>> getAllContas(Pageable pageable) {
        return ResponseEntity.ok(contaService.findAll(pageable));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ContaResponse>> searchContas(
            @RequestParam(required = false) String descricao,
            @RequestParam(required = false) LocalDate dataVencimento,
            Pageable pageable
    ) {
        return ResponseEntity.ok(contaService.searchContas(descricao, dataVencimento, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContaResponse> getContaById(@PathVariable Integer id) {
        ContaResponse contaResponse = contaService.findById(id);
        if (contaResponse != null) {
            return ResponseEntity.ok(contaResponse);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/valor-total")
    public ResponseEntity<BigDecimal> getValorTotalPagoPorPeriodo(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate
    ) {
        return ResponseEntity.ok(contaService.getTotalPagoPorPeriodo(startDate, endDate));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConta(@PathVariable Integer id) {
        contaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/import")
    public ResponseEntity<String> importCsv(@RequestParam("file") MultipartFile file) {
        csvService.importCsv(file);
        return ResponseEntity.ok("Arquivo CSV importado com sucesso!");
    }
}
