/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bendicion.la.carniceria.carniceria.test;
//PRUEBAS TEST
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.bendicion.la.carniceria.carniceria.controller.TipoPagoController;
import com.bendicion.la.carniceria.carniceria.domain.TipoPago;
import com.bendicion.la.carniceria.carniceria.service.ITipoPagoService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(TipoPagoController.class)
public class TipoPagoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ITipoPagoService tipoPagoService;

    @Autowired
    private ObjectMapper objectMapper;

    private TipoPago tipoPagoTest;

    @BeforeEach
    void setUp() {
        tipoPagoTest = new TipoPago();
        tipoPagoTest.setIdTipoPago(1);
        tipoPagoTest.setDescripcionTipoPago("Efectivo");
        tipoPagoTest.setEstadoTipoPago(true);
    }

    @Test
    void testAgregarTipoPago_Exitoso() throws Exception {
        // Arrange
        when(tipoPagoService.addTipoPago(any(TipoPago.class))).thenReturn(tipoPagoTest);

        // Act & Assert
        mockMvc.perform(post("http://localhost:8080/tipopago/agregar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tipoPagoTest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Tipo de pago guardado con éxito con ID: 1"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testAgregarTipoPago_DatosIncompletos() throws Exception {
        // Arrange
        TipoPago tipoPagoIncompleto = new TipoPago();
        tipoPagoIncompleto.setDescripcionTipoPago(""); // Descripción vacía
        
        when(tipoPagoService.addTipoPago(any(TipoPago.class))).thenReturn(tipoPagoIncompleto);

        // Act & Assert
        mockMvc.perform(post("http://localhost:8080/tipopago/agregar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tipoPagoIncompleto)))
                .andExpect(status().isOk());
    }

    @Test
    void testAgregarTipoPago_ErrorInterno() throws Exception {
        // Arrange
        when(tipoPagoService.addTipoPago(any(TipoPago.class)))
                .thenThrow(new RuntimeException("Error de base de datos"));

        // Act & Assert
        mockMvc.perform(post("http://localhost:8080/tipopago/agregar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tipoPagoTest)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Error al agregar el tipo de pago: Error de base de datos"));
    }
}