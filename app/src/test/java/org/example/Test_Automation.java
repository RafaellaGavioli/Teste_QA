package org.example;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.logging.Logger;

public class Test_Automation {

    WebDriver driver;
    WebDriverWait wait;

    private static final Logger logger = Logger.getLogger(Test_Automation.class.getName());

    @Before
    public void setUp() {
        logger.info("Iniciando configuração do WebDriver e abrindo o navegador");
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Rafaella\\Desktop\\Teste para Analista de QA\\Teste Automatizado QA\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();

        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        logger.info("Navegando para https://www.demoblaze.com/index.html");
        driver.get("https://www.demoblaze.com/index.html");
    }

    public void addToCart(String productName, int productId) throws InterruptedException {
        logger.info("Clicando no produto: " + productName);
        clickProductByName(productName);

        logger.info("Clicando no botão Add to cart para o produto com id: " + productId);
        WebElement addButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@onclick='addToCart(" + productId + ")']")));
        addButton.click();

        logger.info("Aguardando alerta e aceitando-o");
        wait.until(ExpectedConditions.alertIsPresent());
        Alert alert = driver.switchTo().alert();
        alert.accept();

        logger.info("Voltando para a página inicial clicando no logo");
        WebElement logo = wait.until(ExpectedConditions.elementToBeClickable(By.id("nava")));
        logo.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nava")));
    }

    public void clickProductByName(String productName) {
        logger.info("Localizando produto pelo nome: " + productName);
        WebElement productLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(),'" + productName + "')]")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", productLink);
        productLink.click();
    }

    @Test
    public void test_add_car() throws InterruptedException {
        logger.info("Iniciando teste de adição ao carrinho");
        addToCart("Sony vaio i7", 9);
        addToCart("Iphone 6 32gb", 5);

        logger.info("Abrindo o carrinho");
        WebElement cartLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Cart")));
        cartLink.click();

        logger.info("Verificando o total do carrinho");
        WebElement totalElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("totalp")));
        String totalText = totalElement.getText();
        int total = Integer.parseInt(totalText);
        int expectedTotal = 1580;

        Assert.assertEquals("Total no carrinho não confere", expectedTotal, total);
        logger.info("Teste de adição ao carrinho finalizado com sucesso");
    }

    @Test
    public void testPlaceOrder() throws InterruptedException {
        logger.info("Iniciando teste de pedido");
        addToCart("Samsung galaxy s6", 1);

        logger.info("Abrindo o carrinho");
        WebElement cartLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Cart")));
        cartLink.click();

        logger.info("Clicando em Place Order");
        WebElement placeOrderButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@data-target='#orderModal']")));
        placeOrderButton.click();

        logger.info("Preenchendo formulário de compra");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("orderModal")));
        driver.findElement(By.id("name")).sendKeys("Hanzo Hasashi");
        driver.findElement(By.id("country")).sendKeys("Japan");
        driver.findElement(By.id("city")).sendKeys("Tokyo");
        driver.findElement(By.id("card")).sendKeys("4485572801000971");
        driver.findElement(By.id("month")).sendKeys("01");
        driver.findElement(By.id("year")).sendKeys("2039");

        logger.info("Clicando em Purchase");
        WebElement purchaseButton = driver.findElement(By.xpath("//button[@onclick='purchaseOrder()']"));
        purchaseButton.click();

        logger.info("Aguardando e validando mensagem de confirmação");
        WebElement confirmationTextElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[contains(@class,'lead')]")));
        String confirmationText = confirmationTextElement.getText();
        Assert.assertTrue("Mensagem de confirmação incorreta", confirmationText.contains("Id:") && confirmationText.contains("Amount:"));

        Thread.sleep(2000);
        logger.info("Clicando em OK no popup final");
        WebElement okButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(@class,'confirm')]")));
        okButton.click();

        logger.info("Teste de pedido finalizado com sucesso");
    }

    @After
    public void tearDown() {
        logger.info("Finalizando execução - fechando navegador");
        if (driver != null) {
            driver.quit(); 
        }
    }
}
