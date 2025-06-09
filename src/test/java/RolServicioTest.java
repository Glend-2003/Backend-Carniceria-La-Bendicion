import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.bendicion.la.carniceria.carniceria.domain.Rol;
import com.bendicion.la.carniceria.carniceria.service.RolService;
import com.bendicion.la.carniceria.carniceria.jpa.RolRepository;

@ExtendWith(MockitoExtension.class)
public class RolServicioTest {
    
    @Mock
    private RolRepository rolRepository;
    
    @InjectMocks
    private RolService rolService;

    @Test
    public void crearRolTest() {
        Rol rolMock = new Rol();
        rolMock.setNombreRol("Administrador");
        rolMock.setDescripcionRol("Acceso total al sistema");
        rolMock.setEstadoRol(true);

        doNothing().when(rolRepository)
            .saveProcedureRol(anyString(), anyString(), anyBoolean());

        Rol resultado = rolService.addRol(rolMock);

        verify(rolRepository, times(1))
            .saveProcedureRol("Administrador", "Acceso total al sistema", true);
        
        assertNotNull(resultado, "El rol retornado no debería ser null");
        assertEquals("Administrador", resultado.getNombreRol());
        assertEquals("Acceso total al sistema", resultado.getDescripcionRol());
        assertTrue(resultado.isEstadoRol());
    }

    @Test
    public void crearRolConNullPointerTest() {
        Rol rolNulo = null;
        
        assertThrows(NullPointerException.class, () -> {
            rolService.addRol(rolNulo);
        });
        
        verify(rolRepository, never()).save(any(Rol.class));
    }

}