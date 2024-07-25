package com.br.contasapagar.api;

import com.br.contasapagar.application.dto.ContaRequest;
import com.br.contasapagar.application.dto.ContaResponse;
import com.br.contasapagar.application.mapper.ContaMapper;
import com.br.contasapagar.application.service.ContaService;
import com.br.contasapagar.domain.entity.Conta;
import com.br.contasapagar.domain.repository.ContaRepository;
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

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContaServiceTest {

    @Mock
    private ContaRepository contaRepository;

    @Mock
    private ContaMapper contaMapper;

    @InjectMocks
    private ContaService contaService;

    @Test
    void testSave() {
        ContaRequest contaRequest = ContaTestSupport.getContaRequest();
        Conta conta = ContaTestSupport.getContaEntity();
        ContaResponse contaResponse = ContaTestSupport.getContaResponse();

        when(contaMapper.toEntity(contaRequest)).thenReturn(conta);
        when(contaRepository.save(conta)).thenReturn(conta);
        when(contaMapper.toResponse(conta)).thenReturn(contaResponse);

        ContaResponse result = contaService.save(contaRequest);

        assertNotNull(result);
        assertEquals(contaResponse, result);
        verify(contaRepository, times(1)).save(conta);
    }

    @Test
    void testUpdate() {
        ContaRequest contaRequest = ContaTestSupport.getContaRequest();
        Conta conta = ContaTestSupport.getContaEntity();
        ContaResponse contaResponse = ContaTestSupport.getContaResponse();

        when(contaRepository.findById(conta.getId())).thenReturn(Optional.of(conta));
        doNothing().when(contaMapper).updateEntity(conta, contaRequest);
        when(contaRepository.save(conta)).thenReturn(conta);
        when(contaMapper.toResponse(conta)).thenReturn(contaResponse);

        ContaResponse result = contaService.update(conta.getId(), contaRequest);

        assertNotNull(result);
        assertEquals(contaResponse, result);
        verify(contaRepository, times(1)).findById(conta.getId());
        verify(contaRepository, times(1)).save(conta);
    }

    @Test
    void testFindById() {
        Conta conta = ContaTestSupport.getContaEntity();
        ContaResponse contaResponse = ContaTestSupport.getContaResponse();

        when(contaRepository.findById(conta.getId())).thenReturn(Optional.of(conta));
        when(contaMapper.toResponse(conta)).thenReturn(contaResponse);

        ContaResponse result = contaService.findById(conta.getId());

        assertNotNull(result);
        assertEquals(contaResponse, result);
        verify(contaRepository, times(1)).findById(conta.getId());
    }

    @Test
    void testDelete() {
        Conta conta = ContaTestSupport.getContaEntity();

        doNothing().when(contaRepository).deleteById(conta.getId());

        contaService.delete(conta.getId());

        verify(contaRepository, times(1)).deleteById(conta.getId());
    }

    @Test
    void testSearchContas() {
        String descricao = "Conta de Luz";
        LocalDate dataVencimento = LocalDate.of(2024, 7, 24);
        Pageable pageable = PageRequest.of(0, 1);

        Conta conta = new Conta();
        conta.setId(1);
        conta.setDescricao(descricao);
        conta.setDataVencimento(dataVencimento);

        ContaResponse contaResponse = new ContaResponse();
        contaResponse.setId(1);
        contaResponse.setDescricao(descricao);
        contaResponse.setDataVencimento(dataVencimento);

        Page<Conta> contaPage = new PageImpl<>(Collections.singletonList(conta));
        when(contaRepository.searchContas(any(), any(), any())).thenReturn(contaPage);
        when(contaMapper.toResponse(any(Conta.class))).thenReturn(contaResponse);

        Page<ContaResponse> result = contaService.searchContas(descricao, dataVencimento, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(descricao, result.getContent().get(0).getDescricao());
        assertEquals(dataVencimento, result.getContent().get(0).getDataVencimento());
    }
}
