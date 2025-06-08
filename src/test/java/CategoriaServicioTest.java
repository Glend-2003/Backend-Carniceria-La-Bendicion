import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.bendicion.la.carniceria.carniceria.domain.Categoria;
import com.bendicion.la.carniceria.carniceria.service.CategoriaService;
import com.bendicion.la.carniceria.carniceria.jpa.CategoriaRepository;

@ExtendWith(MockitoExtension.class)
public class CategoriaServicioTest {
    
    @Mock
    private CategoriaRepository categoriaRepository;
    
    @InjectMocks
    private CategoriaService categoriaService;

    @Test
    public void crearCategoriaTest() {
        Categoria categoriaMock = new Categoria();
        categoriaMock.setNombreCategoria("Prueba unitaria");
        categoriaMock.setDescripcionCategoria("Prueba unitaria");
        categoriaMock.setEstadoCategoria(true);

        doNothing().when(categoriaRepository)
            .saveProcedureCategoria(anyString(), anyString(), anyBoolean());

        Categoria resultado = categoriaService.addCategoria(categoriaMock);

        verify(categoriaRepository, times(1))
            .saveProcedureCategoria("Prueba unitaria", "Prueba unitaria", true);
        
        assertNotNull(resultado, "La categoría retornada no debería ser null");
        assertEquals("Prueba unitaria", resultado.getNombreCategoria());
        assertEquals("Prueba unitaria", resultado.getDescripcionCategoria());
        assertTrue(resultado.isEstadoCategoria());
    }

    @Test
    public void crearCategoriaConNullPointerTest() {
        Categoria categoriaNula = null;
        
        assertThrows(NullPointerException.class, () -> {
            categoriaService.addCategoria(categoriaNula);
        });
        
        verify(categoriaRepository, never()).saveProcedureCategoria(anyString(), anyString(), anyBoolean());
    }

    /*@Test
    public void crearCategoriaEstadoSiempreEsTrueTest() {
        Categoria categoria = new Categoria();
        categoria.setNombreCategoria("Test");
        categoria.setDescripcionCategoria("Test Description");
        categoria.setEstadoCategoria(false);
        
        doNothing().when(categoriaRepository)
            .saveProcedureCategoria(anyString(), anyString(), anyBoolean());
        
        Categoria resultado = categoriaService.addCategoria(categoria);
        
        assertTrue(resultado.isEstadoCategoria());
        verify(categoriaRepository, times(1))
            .saveProcedureCategoria("Test", "Test Description", true);
    }*/

    @Test
    public void verificarLlamadaAlRepositorioTest() {
        Categoria categoria = new Categoria();
        categoria.setNombreCategoria("Carnes");
        categoria.setDescripcionCategoria("Productos cárnicos");
        categoria.setEstadoCategoria(false);
        
        doNothing().when(categoriaRepository)
            .saveProcedureCategoria(anyString(), anyString(), anyBoolean());
        
        categoriaService.addCategoria(categoria);
        
        verify(categoriaRepository, times(1))
            .saveProcedureCategoria(
                eq("Carnes"), 
                eq("Productos cárnicos"), 
                eq(true)
            );
    }
}