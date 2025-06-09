import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import com.bendicion.la.carniceria.carniceria.service.RolService;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@SpringBootTest(classes = com.bendicion.la.carniceria.carniceria.CarniceriaApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class RolControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RolService rolService;

    @Test
    public void cuandoSeLlamaRoles() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/rol/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void cuandoSeLlamaRolesConParametro() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/rol/")
                .param("estadoRol", "true")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }
}