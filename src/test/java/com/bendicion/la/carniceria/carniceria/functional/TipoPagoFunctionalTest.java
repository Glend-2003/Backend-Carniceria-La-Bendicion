package com.bendicion.la.carniceria.carniceria.functional;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TipoPagoFunctionalTest {

    @LocalServerPort
    private int port;

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // Para ejecución sin interfaz gráfica
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test
    public void testAgregarTipoPagoEnUI() {
        driver.get("http://localhost:" + port + "/tipopago");
        
        // Suponiendo que hay un botón para abrir el formulario de agregar
        WebElement agregarBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("btnAgregarTipoPago")));
        agregarBtn.click();
        
        // Llenar el formulario
        WebElement descripcionInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("descripcionTipoPago")));
        descripcionInput.sendKeys("Nuevo Tipo de Pago");
        
        // Enviar el formulario
        WebElement submitBtn = driver.findElement(By.id("btnSubmitTipoPago"));
        submitBtn.click();
        
        // Verificar que se agregó correctamente
        WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("successMessage")));
        assertTrue(successMessage.getText().contains("Tipo de pago guardado con éxito"));
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}