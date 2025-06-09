import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.bendicion.la.carniceria.carniceria.domain.Provincia;
import com.bendicion.la.carniceria.carniceria.service.ProvinciaService;
import com.bendicion.la.carniceria.carniceria.jpa.ProvinciaRepository;

import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ProvinciaServicioTest {
    
    @Mock
    private ProvinciaRepository provinciaRepository;
    
    @InjectMocks
    private ProvinciaService provinciaService;

    @Test
    public void obtenerTodasLasProvinciasTest() {
        Provincia provincia1 = new Provincia();
        provincia1.setNombreProvincia("Provincia 1");
        
        Provincia provincia2 = new Provincia();
        provincia2.setNombreProvincia("Provincia 2");
        
        List<Provincia> provinciasMock = Arrays.asList(provincia1, provincia2);
        
        when(provinciaRepository.listProcedureProvincia()).thenReturn(provinciasMock);

        List<Provincia> resultado = provinciaService.getProvincia();
        verify(provinciaRepository, times(1)).listProcedureProvincia();
        
        assertNotNull(resultado, "La lista de provincias no debería ser null");
        assertEquals(2, resultado.size(), "Deberían retornarse 2 provincias");
        assertEquals("Provincia 1", resultado.get(0).getNombreProvincia());
        assertEquals("Provincia 2", resultado.get(1).getNombreProvincia());
    }

    @Test
    public void verificarLlamadaAlRepositorioTest() {
        when(provinciaRepository.listProcedureProvincia()).thenReturn(List.of());
        provinciaService.getProvincia();
        verify(provinciaRepository, times(1)).listProcedureProvincia();
    }
}