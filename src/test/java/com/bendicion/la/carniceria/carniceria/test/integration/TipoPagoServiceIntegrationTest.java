/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bendicion.la.carniceria.carniceria.test.integration;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.bendicion.la.carniceria.carniceria.domain.TipoPago;
import com.bendicion.la.carniceria.carniceria.service.TipoPagoService;
import com.bendicion.la.carniceria.carniceria.TestMailConfig;
import org.springframework.context.annotation.Import;


@SpringBootTest
@Transactional
@Import(TestMailConfig.class)
public class TipoPagoServiceIntegrationTest {

    @Autowired
    private TipoPagoService tipoPagoService;

    private TipoPago tipoPagoTest;

    @BeforeEach
    void setUp() {
        tipoPagoTest = new TipoPago();
        tipoPagoTest.setDescripcionTipoPago("Tarjeta de Débito");
        tipoPagoTest.setEstadoTipoPago(false);
    }

    @Test
    void testAddTipoPago_Exitoso() {
        TipoPago resultado = tipoPagoService.addTipoPago(tipoPagoTest);
        
        assertNotNull(resultado);
        assertEquals("Tarjeta de Débito", resultado.getDescripcionTipoPago());
        assertTrue(resultado.isEstadoTipoPago()); // Debe ser true después del servicio
    }

    @Test
    void testGetTipoPagoById_Exitoso() {
        // Agregamos un tipo de pago primero
        TipoPago agregado = tipoPagoService.addTipoPago(tipoPagoTest);
        
        TipoPago encontrado = tipoPagoService.getTipoPagoById(agregado.getIdTipoPago());
        
        assertNotNull(encontrado);
        assertEquals(agregado.getIdTipoPago(), encontrado.getIdTipoPago());
    }
}