/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bendicion.la.carniceria.carniceria.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bendicion.la.carniceria.carniceria.domain.TipoPago;
import com.bendicion.la.carniceria.carniceria.jpa.TipoPagoRepository;
import com.bendicion.la.carniceria.carniceria.service.TipoPagoService;

@ExtendWith(MockitoExtension.class)
public class TipoPagoServiceTest {

    @Mock
    private TipoPagoRepository tipoPagoRepository;

    @InjectMocks
    private TipoPagoService tipoPagoService;

    private TipoPago tipoPagoTest;

    @BeforeEach
    void setUp() {
        tipoPagoTest = new TipoPago();
        tipoPagoTest.setDescripcionTipoPago("Tarjeta de Crédito");
        tipoPagoTest.setEstadoTipoPago(false); // Se establecerá como true en el servicio
    }

    @Test
    void testAgregarTipoPago_Exitoso() {
        // Arrange
        doNothing().when(tipoPagoRepository).saveProcedureTipoPago(anyString(), anyBoolean());

        // Act
        TipoPago resultado = tipoPagoService.addTipoPago(tipoPagoTest);

        // Assert
        assertNotNull(resultado);
        assertEquals("Tarjeta de Crédito", resultado.getDescripcionTipoPago());
        assertTrue(resultado.isEstadoTipoPago()); // Debe ser true después del servicio
        
        // Verificar que se llamó al método del repositorio
        verify(tipoPagoRepository, times(1)).saveProcedureTipoPago("Tarjeta de Crédito", true);
    }

    @Test
    void testAgregarTipoPago_DescripcionVacia() {
        // Arrange
        tipoPagoTest.setDescripcionTipoPago("");

        // Act & Assert
        assertDoesNotThrow(() -> {
            TipoPago resultado = tipoPagoService.addTipoPago(tipoPagoTest);
            assertEquals("", resultado.getDescripcionTipoPago());
            assertTrue(resultado.isEstadoTipoPago());
        });
        
        verify(tipoPagoRepository, times(1)).saveProcedureTipoPago("", true);
    }

    @Test
    void testAgregarTipoPago_DescripcionNula() {
        // Arrange
        tipoPagoTest.setDescripcionTipoPago(null);

        // Act & Assert
        assertDoesNotThrow(() -> {
            TipoPago resultado = tipoPagoService.addTipoPago(tipoPagoTest);
            assertNull(resultado.getDescripcionTipoPago());
            assertTrue(resultado.isEstadoTipoPago());
        });
        
        verify(tipoPagoRepository, times(1)).saveProcedureTipoPago(null, true);
    }
}