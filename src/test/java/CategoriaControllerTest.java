import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@SpringBootTest(classes = com.bendicion.la.carniceria.carniceria.CarniceriaApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CategoriaControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void cuandoSeLlamaCategorias() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/categoria/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void cuandoSeLlamaCategoriasConParametro() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/categoria/")
                .param("estadoCategoria", "true")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    @Test
    public void cuandoSeCreaNuevaCategoria() throws Exception {
        String categoriaJson = """
            {
                "nombreCategoria": "Test Categoria",
                "descripcionCategoria": "Descripción de prueba",
                "estadoCategoria": true
            }
            """;

        mockMvc.perform(MockMvcRequestBuilders.post("/categoria/agregar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(categoriaJson))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void cuandoSeBuscaCategoriaPorId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/categoria/obtenerPorId/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }
}