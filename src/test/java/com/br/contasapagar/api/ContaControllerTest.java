package com.br.contasapagar.api;

import com.br.contasapagar.application.controller.ContaController;
import com.br.contasapagar.application.dto.ContaRequest;
import com.br.contasapagar.application.dto.ContaResponse;
import com.br.contasapagar.application.service.ContaService;
import com.br.contasapagar.application.service.CsvService;
import com.br.contasapagar.support.ContaTestSupport;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;

@ExtendWith(MockitoExtension.class)
public class ContaControllerTest {

    @Mock
    private ContaService contaService;

    @Mock
    private CsvService csvService;

    @InjectMocks
    private ContaController contaController;

    @Test
    public void testCreateConta() {
        ContaRequest contaRequest = ContaTestSupport.getContaRequest();
        ContaResponse contaResponse = ContaTestSupport.getContaResponse();

        when(contaService.save(contaRequest)).thenReturn(contaResponse);

        ResponseEntity<ContaResponse> response = contaController.createConta(contaRequest);

        assertNotNull(response);
        assertEquals(ResponseEntity.ok(contaResponse), response);
        verify(contaService, times(1)).save(contaRequest);
    }

    @Test
    public void testUpdateConta() {
        ContaRequest contaRequest = ContaTestSupport.getContaRequest();
        ContaResponse contaResponse = ContaTestSupport.getContaResponse();

        when(contaService.update(contaResponse.getId(), contaRequest)).thenReturn(contaResponse);

        ResponseEntity<ContaResponse> response = contaController.updateConta(contaResponse.getId(), contaRequest);

        assertNotNull(response);
        assertEquals(ResponseEntity.ok(contaResponse), response);
        verify(contaService, times(1)).update(contaResponse.getId(), contaRequest);
    }

    @Test
    public void testUpdateSituacao() {
        String situacao = "PAGA";
        ContaResponse contaResponse = ContaTestSupport.getContaResponse();

        when(contaService.updateSituacao(contaResponse.getId(), situacao)).thenReturn(contaResponse);

        ResponseEntity<ContaResponse> response = contaController.updateSituacao(contaResponse.getId(), situacao);

        assertNotNull(response);
        assertEquals(ResponseEntity.ok(contaResponse), response);
        verify(contaService, times(1)).updateSituacao(contaResponse.getId(), situacao);
    }

    @Test
    public void testGetAllContas() {
        List<ContaResponse> contaResponseList = ContaTestSupport.getContaResponseList();
        Pageable pageable = PageRequest.of(0, 10);
        Page<ContaResponse> contaPage = new PageImpl<>(contaResponseList, pageable, contaResponseList.size());

        when(contaService.findAll(pageable)).thenReturn(contaPage);

        ResponseEntity<Page<ContaResponse>> response = contaController.getAllContas(pageable);

        assertNotNull(response);
        assertEquals(ResponseEntity.ok(contaPage), response);
        verify(contaService, times(1)).findAll(pageable);
    }

    @Test
    public void testGetContaById() {
        ContaResponse contaResponse = ContaTestSupport.getContaResponse();

        when(contaService.findById(contaResponse.getId())).thenReturn(contaResponse);

        ResponseEntity<ContaResponse> response = contaController.getContaById(contaResponse.getId());

        assertNotNull(response);
        assertEquals(ResponseEntity.ok(contaResponse), response);
        verify(contaService, times(1)).findById(contaResponse.getId());
    }

    @Test
    public void testGetValorTotalPagoPorPeriodo() {
        BigDecimal valorTotal = new BigDecimal("1500.00");
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);

        when(contaService.getTotalPagoPorPeriodo(startDate, endDate)).thenReturn(valorTotal);

        ResponseEntity<BigDecimal> response = contaController.getValorTotalPagoPorPeriodo(startDate, endDate);

        assertNotNull(response);
        assertEquals(ResponseEntity.ok(valorTotal), response);
        verify(contaService, times(1)).getTotalPagoPorPeriodo(startDate, endDate);
    }

    @Test
    public void testDeleteConta() {
        ContaResponse contaResponse = ContaTestSupport.getContaResponse();

        doNothing().when(contaService).delete(contaResponse.getId());

        ResponseEntity<Void> response = contaController.deleteConta(contaResponse.getId());

        assertNotNull(response);
        assertEquals(ResponseEntity.noContent().build(), response);
        verify(contaService, times(1)).delete(contaResponse.getId());
    }

    @Test
    public void testImportCsv() {
        MultipartFile file = new MockMultipartFile("file", "contas.csv", MediaType.TEXT_PLAIN_VALUE, "data".getBytes());

        doNothing().when(csvService).importCsv(file);

        ResponseEntity<String> response = contaController.importCsv(file);

        assertNotNull(response);
        assertEquals(ResponseEntity.ok("Arquivo CSV importado com sucesso!"), response);
        verify(csvService, times(1)).importCsv(file);
    }
}
