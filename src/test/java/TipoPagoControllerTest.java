/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Dilan
 */
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
public class TipoPagoControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void cuandoSeLlamaTiposPago() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/tipopago/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    @Test
    public void cuandoSeBuscaTipoPagoPorId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/tipopago/obtenerPorId/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    @Test
    public void cuandoSeCreaNuevoTipoPago() throws Exception {
        String tipoPagoJson = """
            {
                "descripcionTipoPago": "Tarjeta de DÃ©bito",
                "estadoTipoPago": true
            }
            """;

        mockMvc.perform(MockMvcRequestBuilders.post("/tipopago/agregar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(tipoPagoJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.id").exists())
                .andDo(print());
    }
}