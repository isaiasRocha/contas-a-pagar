package com.br.contasapagar.api;

import com.br.contasapagar.application.controller.ContaController;
import com.br.contasapagar.application.dto.ContaRequest;
import com.br.contasapagar.application.dto.ContaResponse;
import com.br.contasapagar.application.service.ContaService;
import com.br.contasapagar.application.service.CsvService;
import com.br.contasapagar.support.ContaTestSupport;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ContaController.class)
@AutoConfigureMockMvc(addFilters = false)
class ContaControllerTest {

    @MockBean
    private ContaService contaService;

    @MockBean
    private CsvService csvService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateConta() throws Exception {
        ContaRequest contaRequest = ContaTestSupport.getContaRequest();
        ContaResponse contaResponse = ContaTestSupport.getContaResponse();

        when(contaService.save(any(ContaRequest.class))).thenReturn(contaResponse);

        mockMvc.perform(post("/api/contas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contaRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(contaResponse.getId()));
    }

    @Test
    void testUpdateConta() throws Exception {
        Integer id = 1;
        ContaRequest contaRequest = ContaTestSupport.getContaRequest();
        ContaResponse contaResponse = ContaTestSupport.getContaResponse();

        when(contaService.update(any(Integer.class), any(ContaRequest.class))).thenReturn(contaResponse);

        mockMvc.perform(put("/api/contas" + "/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contaRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
    }

    @Test
    void testUpdateSituacao() throws Exception {
        Integer contaId = 1;
        String situacao = "PAGA";
        ContaResponse contaResponse = ContaTestSupport.getContaResponse();

        when(contaService.updateSituacao(any(Integer.class), any(String.class))).thenReturn(contaResponse);

        mockMvc.perform(put("/api/contas/" + contaId + "/situacao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(situacao)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.situacao").value(situacao));
    }

    @Test
    void testGetContaById() throws Exception {
        Integer contaId = 1;
        ContaResponse contaResponse = ContaTestSupport.getContaResponse();

        when(contaService.findById(contaId)).thenReturn(contaResponse);

        mockMvc.perform(get("/api/contas/" + contaId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(contaId));
    }

    @Test
    void testDeleteConta() throws Exception {
        Integer contaId = 1;

        mockMvc.perform(delete("/api/contas/" + contaId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void testImportCsv() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "contas.csv", MediaType.TEXT_PLAIN_VALUE, "data".getBytes());

        mockMvc.perform(multipart("/api/contas/import")
                        .file("file", file.getBytes())
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(content().string("Arquivo CSV importado com sucesso!"));
    }
}
