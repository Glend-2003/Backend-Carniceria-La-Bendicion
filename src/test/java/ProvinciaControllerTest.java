import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.mockito.Mockito.*;

import com.bendicion.la.carniceria.carniceria.domain.Provincia;
import com.bendicion.la.carniceria.carniceria.service.IProvinciaService;

import java.util.Arrays;
import java.util.List;

@SpringBootTest(classes = com.bendicion.la.carniceria.carniceria.CarniceriaApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ProvinciaControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private IProvinciaService provinciaService;

    @Test
    public void cuandoSeObtienenTodasLasProvincias() throws Exception {
        Provincia provincia1 = new Provincia();
        provincia1.setNombreProvincia("Provincia 1");
        
        Provincia provincia2 = new Provincia();
        provincia2.setNombreProvincia("Provincia 2");
        
        List<Provincia> provinciasMock = Arrays.asList(provincia1, provincia2);
        
        when(provinciaService.getProvincia()).thenReturn(provinciasMock);

        mockMvc.perform(MockMvcRequestBuilders.get("/provincia/leer")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombreProvincia").value("Provincia 1"))
                .andExpect(jsonPath("$[1].nombreProvincia").value("Provincia 2"))
                .andDo(print());
    }

    @Test
    public void cuandoNoHayProvincias() throws Exception {
        when(provinciaService.getProvincia()).thenReturn(List.of());

        mockMvc.perform(MockMvcRequestBuilders.get("/provincia/leer")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty())
                .andDo(print());
    }
}