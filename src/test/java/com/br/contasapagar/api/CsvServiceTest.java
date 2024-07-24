package com.br.contasapagar.api;

import com.br.contasapagar.application.service.CsvService;
import com.br.contasapagar.domain.repository.ContaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CsvServiceTest {

    @Mock
    private ContaRepository contaRepository;

    @InjectMocks
    private CsvService csvService;

    @Test
    void testImportCsv() throws Exception {
        String csvContent = "dataVencimento,dataPagamento,valor,descricao,situacao\n" +
                            "2024-08-01,2024-08-05,1500.00,Pagamento de aluguel,PAGA\n" +
                            "2024-09-01,2024-09-05,200.00,Pagamento de internet,PENDENTE";
        MultipartFile file = new MockMultipartFile("file", "contas.csv", "text/csv", csvContent.getBytes());

        csvService.importCsv(file);

        verify(contaRepository, times(1)).saveAll(anyList());
    }
}
