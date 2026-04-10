import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

/**
 * Login Test Suite - E-Commerce Web Application
 * Author: Maryam Akram
 * Tool: Selenium WebDriver + Java + TestNG
 */
public class LoginTest {

    WebDriver driver;
    WebDriverWait wait;
    final String BASE_URL = "https://example-ecommerce.com";

    @BeforeMethod
    public void setUp() {
        // Initialize Chrome driver
        System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get(BASE_URL + "/login");
    }

    // ─── TEST 1: Valid Login ───────────────────────────────────────────────
    @Test(priority = 1, description = "Verify login with valid credentials")
    public void testValidLogin() {
        WebElement emailField = driver.findElement(By.id("email"));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.id("loginBtn"));

        emailField.sendKeys("testuser@example.com");
        passwordField.sendKeys("ValidPass123");
        loginButton.click();

        // Wait for dashboard to load
        wait.until(ExpectedConditions.urlContains("/dashboard"));

        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("/dashboard"),
            "Login failed! Expected dashboard URL but got: " + currentUrl);

        System.out.println("PASS: Valid login redirects to dashboard");
    }

    // ─── TEST 2: Invalid Password ──────────────────────────────────────────
    @Test(priority = 2, description = "Verify error shown for invalid password")
    public void testInvalidPassword() {
        WebElement emailField = driver.findElement(By.id("email"));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.id("loginBtn"));

        emailField.sendKeys("testuser@example.com");
        passwordField.sendKeys("WrongPassword!");
        loginButton.click();

        WebElement errorMsg = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.id("errorMessage"))
        );

        Assert.assertTrue(errorMsg.isDisplayed(),
            "Error message not displayed for invalid password");
        Assert.assertTrue(errorMsg.getText().contains("Invalid credentials"),
            "Unexpected error message: " + errorMsg.getText());

        System.out.println("PASS: Invalid password shows correct error message");
    }

    // ─── TEST 3: Empty Fields Validation ──────────────────────────────────
    @Test(priority = 3, description = "Verify validation error for empty fields")
    public void testEmptyFieldsValidation() {
        WebElement loginButton = driver.findElement(By.id("loginBtn"));
        loginButton.click();

        WebElement emailError = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.id("emailError"))
        );
        WebElement passwordError = driver.findElement(By.id("passwordError"));

        Assert.assertTrue(emailError.isDisplayed(), "Email error not shown");
        Assert.assertTrue(passwordError.isDisplayed(), "Password error not shown");

        System.out.println("PASS: Empty fields show validation errors");
    }

    // ─── TEST 4: Invalid Email Format ─────────────────────────────────────
    @Test(priority = 4, description = "Verify error for invalid email format")
    public void testInvalidEmailFormat() {
        WebElement emailField = driver.findElement(By.id("email"));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.id("loginBtn"));

        emailField.sendKeys("notanemail");
        passwordField.sendKeys("ValidPass123");
        loginButton.click();

        WebElement emailError = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.id("emailError"))
        );

        Assert.assertTrue(emailError.isDisplayed(),
            "No error shown for invalid email format");

        System.out.println("PASS: Invalid email format shows validation error");
    }

    // ─── TEST 5: UI Validation ─────────────────────────────────────────────
    @Test(priority = 5, description = "Verify all UI elements are visible on login page")
    public void testLoginPageUIElements() {
        Assert.assertTrue(driver.findElement(By.id("email")).isDisplayed(),
            "Email field not visible");
        Assert.assertTrue(driver.findElement(By.id("password")).isDisplayed(),
            "Password field not visible");
        Assert.assertTrue(driver.findElement(By.id("loginBtn")).isDisplayed(),
            "Login button not visible");
        Assert.assertTrue(driver.findElement(By.linkText("Forgot Password?")).isDisplayed(),
            "Forgot password link not visible");

        System.out.println("PASS: All login page UI elements are visible");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
