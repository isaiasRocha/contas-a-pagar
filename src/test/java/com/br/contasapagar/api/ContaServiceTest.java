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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
    void testFindAll() {
        List<Conta> contaList = ContaTestSupport.getContaEntityList();
        List<ContaResponse> contaResponseList = ContaTestSupport.getContaResponseList();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Conta> contaPage = new PageImpl<>(contaList, pageable, contaList.size());

        when(contaRepository.findAll(pageable)).thenReturn(contaPage);
        when(contaMapper.toResponse(any(Conta.class))).thenReturn(contaResponseList.get(0));

        Page<ContaResponse> result = contaService.findAll(pageable);

        assertNotNull(result);
        assertEquals(contaList.size(), result.getTotalElements());
        verify(contaRepository, times(1)).findAll(pageable);
    }
}
