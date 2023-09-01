package com.example.jura;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;


public class SeleniumApiTest {

    @BeforeAll
    public static void setUpAll() {
        Configuration.browser = "firefox";
        Configuration.browserSize = "1280x800";
        SelenideLogger.addListener("allure", new AllureSelenide());
        System.setProperty("webdriver.gecko.driver", "geckodriver.exe");

    }

    @Test
    public void logIn() throws InterruptedException {
        WebDriver driver = new FirefoxDriver();
        driver.get("https://www.saucedemo.com/");

        WebElement userTextBox = driver.findElement(new By.ByCssSelector("#user-name"));
        userTextBox.sendKeys("standard_user");

        WebElement pwdTextBox = driver.findElement(new By.ByCssSelector("#password"));
        pwdTextBox.sendKeys("secret_sauce");

        WebElement loginButton = driver.findElement(new By.ByCssSelector("#login-button"));
        loginButton.click();

        WebElement item1Button = driver.findElement(new By.ByCssSelector("#add-to-cart-sauce-labs-backpack"));
        item1Button.click();
        Thread.sleep(300);

        WebElement item2Button = driver.findElement(new By.ByCssSelector(	"#add-to-cart-sauce-labs-bike-light"));
        item2Button.click();
        Thread.sleep(300);

        WebElement item3Button = driver.findElement(new By.ByCssSelector("#add-to-cart-sauce-labs-bolt-t-shirt"));
        item3Button.click();
        Thread.sleep(300);

        WebElement item4Button = driver.findElement(new By.ByCssSelector(	"#add-to-cart-sauce-labs-fleece-jacket"));
        item4Button.click();
        Thread.sleep(300);

        WebElement item5Button = driver.findElement(new By.ByCssSelector(	"#add-to-cart-sauce-labs-onesie"));
        item5Button.click();
        Thread.sleep(300);

        WebElement item6Button = driver.findElement(new By.ByCssSelector(	"#add-to-cart-test\\.allthethings\\(\\)-t-shirt-\\(red\\)"));
        item6Button.click();
        Thread.sleep(300);

        WebElement shoppingCartButton = driver.findElement(new By.ByCssSelector(".shopping_cart_link"));
        shoppingCartButton.click();
        Thread.sleep(300);

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0,document.body.scrollHeight)", "");

        Thread.sleep(3000);
        driver.close();
    }
}
