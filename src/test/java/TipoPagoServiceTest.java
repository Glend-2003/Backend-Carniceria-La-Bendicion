/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Dilan
 */
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.bendicion.la.carniceria.carniceria.domain.TipoPago;
import com.bendicion.la.carniceria.carniceria.service.TipoPagoService;
import com.bendicion.la.carniceria.carniceria.jpa.TipoPagoRepository;

@ExtendWith(MockitoExtension.class)
public class TipoPagoServiceTest {
    
    @Mock
    private TipoPagoRepository tipoPagoRepository;
    
    @InjectMocks
    private TipoPagoService tipoPagoService;

    @Test
    public void crearTipoPagoTest() {
        TipoPago tipoPagoMock = new TipoPago();
        tipoPagoMock.setDescripcionTipoPago("Tarjeta de Crédito");
        tipoPagoMock.setEstadoTipoPago(true);

        doNothing().when(tipoPagoRepository)
            .saveProcedureTipoPago(anyString(), anyBoolean());

        TipoPago resultado = tipoPagoService.addTipoPago(tipoPagoMock);

        verify(tipoPagoRepository, times(1))
            .saveProcedureTipoPago("Tarjeta de Crédito", true);
        
        assertNotNull(resultado, "El tipo de pago retornado no debería ser null");
        assertEquals("Tarjeta de Crédito", resultado.getDescripcionTipoPago());
        assertTrue(resultado.isEstadoTipoPago());
    }

    @Test
    public void crearTipoPagoConNullPointerTest() {
        TipoPago tipoPagoNulo = null;
        
        assertThrows(NullPointerException.class, () -> {
            tipoPagoService.addTipoPago(tipoPagoNulo);
        });
        
        verify(tipoPagoRepository, never()).saveProcedureTipoPago(anyString(), anyBoolean());
    }

    @Test
    public void actualizarTipoPagoTest() {
        TipoPago tipoPago = new TipoPago();
        tipoPago.setIdTipoPago(1);
        tipoPago.setDescripcionTipoPago("Efectivo");
        tipoPago.setEstadoTipoPago(true);
        
        doNothing().when(tipoPagoRepository)
            .updateProcedureTipoPago(anyInt(), anyString(), anyBoolean());
        
        TipoPago resultado = tipoPagoService.updateTipoPago(tipoPago);
        
        verify(tipoPagoRepository, times(1))
            .updateProcedureTipoPago(1, "Efectivo", true);
        
        assertNotNull(resultado, "El tipo de pago actualizado no debería ser null");
        assertEquals("Efectivo", resultado.getDescripcionTipoPago());
        assertTrue(resultado.isEstadoTipoPago());
    }

    @Test
    public void obtenerTipoPagoPorIdTest() {
        int idTipoPago = 1;
        TipoPago tipoPagoMock = new TipoPago();
        tipoPagoMock.setIdTipoPago(idTipoPago);
        tipoPagoMock.setDescripcionTipoPago("PayPal");
        tipoPagoMock.setEstadoTipoPago(true);
        
        when(tipoPagoRepository.listProcedureTipoPagoById(idTipoPago)).thenReturn(tipoPagoMock);
        
        TipoPago resultado = tipoPagoService.getTipoPagoById(idTipoPago);
        
        verify(tipoPagoRepository, times(1)).listProcedureTipoPagoById(idTipoPago);
        assertNotNull(resultado, "El tipo de pago no debería ser null");
        assertEquals(idTipoPago, resultado.getIdTipoPago());
        assertEquals("PayPal", resultado.getDescripcionTipoPago());
        assertTrue(resultado.isEstadoTipoPago());
    }

    @Test
    public void obtenerTipoPagoPorIdNoExistenteTest() {
        int idTipoPago = 999;
        
        when(tipoPagoRepository.listProcedureTipoPagoById(idTipoPago)).thenReturn(null);
        
        TipoPago resultado = tipoPagoService.getTipoPagoById(idTipoPago);
        
        verify(tipoPagoRepository, times(1)).listProcedureTipoPagoById(idTipoPago);
        assertNull(resultado, "El tipo de pago no existente debería ser null");
    }

    @Test
    public void verificarLlamadaAlRepositorioTest() {
        TipoPago tipoPago = new TipoPago();
        tipoPago.setDescripcionTipoPago("Transferencia Bancaria");
        tipoPago.setEstadoTipoPago(false);
        
        doNothing().when(tipoPagoRepository)
            .saveProcedureTipoPago(anyString(), anyBoolean());
        
        tipoPagoService.addTipoPago(tipoPago);
        
        verify(tipoPagoRepository, times(1))
            .saveProcedureTipoPago(
                eq("Transferencia Bancaria"), 
                eq(true)
            );
    }
}