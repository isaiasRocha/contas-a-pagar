package com.br.contasapagar.application.service;

import com.br.contasapagar.domain.entity.Conta;
import com.br.contasapagar.domain.repository.ContaRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CsvService {

    private final ContaRepository contaRepository;

    public void importCsv(MultipartFile file) throws RuntimeException {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream(), "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {

            List<Conta> contas = new ArrayList<>();
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            for (CSVRecord csvRecord : csvRecords) {
                Conta conta = new Conta();
                conta.setDataVencimento(LocalDate.parse(csvRecord.get("dataVencimento"), formatter));
                conta.setDataPagamento(LocalDate.parse(csvRecord.get("dataPagamento"), formatter));
                conta.setValor(new BigDecimal(csvRecord.get("valor")));
                conta.setDescricao(csvRecord.get("descricao"));
                conta.setSituacao(csvRecord.get("situacao"));

                contas.add(conta);
            }

            contaRepository.saveAll(contas);

        } catch (IOException e) {
            throw new RuntimeException("Erro ao processar o arquivo CSV: " + e.getMessage());
        }
    }
}
