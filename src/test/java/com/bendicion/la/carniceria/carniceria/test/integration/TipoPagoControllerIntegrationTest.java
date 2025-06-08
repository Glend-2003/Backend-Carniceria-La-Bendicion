/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bendicion.la.carniceria.carniceria.test.integration;

/**
 *
 * @author Dilan
 */

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.bendicion.la.carniceria.carniceria.domain.TipoPago;
import com.bendicion.la.carniceria.carniceria.jpa.TipoPagoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.bendicion.la.carniceria.carniceria.TestMailConfig;
import org.springframework.context.annotation.Import;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Import(TestMailConfig.class)
public class TipoPagoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TipoPagoRepository tipoPagoRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private TipoPago tipoPagoTest;

    @BeforeEach
    void setUp() {
        tipoPagoTest = new TipoPago();
        tipoPagoTest.setDescripcionTipoPago("Tarjeta de Crédito");
        tipoPagoTest.setEstadoTipoPago(true);
        
        // Limpiar la base de datos antes de cada prueba
        tipoPagoRepository.deleteAll();
    }

    @Test
    void testAgregarTipoPago_Exitoso() throws Exception {
        mockMvc.perform(post("/tipopago/agregar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tipoPagoTest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void testListarTiposPago_Exitoso() throws Exception {
        // Primero agregamos un tipo de pago
        tipoPagoRepository.saveProcedureTipoPago("Efectivo", true);
        
        mockMvc.perform(get("/tipopago/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].descripcionTipoPago").value("Efectivo"));
    }

    @Test
    void testActivarTipoPago_Exitoso() throws Exception {
        // Agregamos un tipo de pago desactivado
        tipoPagoRepository.saveProcedureTipoPago("Transferencia", false);
        
        mockMvc.perform(put("/tipopago/activar/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}