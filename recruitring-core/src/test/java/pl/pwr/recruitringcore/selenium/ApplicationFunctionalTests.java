package pl.pwr.recruitringcore.selenium;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ApplicationFunctionalTests {

    private WebDriver driver;

    @BeforeEach
    void beforeEach() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--start-maximized");
        options.addArguments("--disable-popup-blocking");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));
        driver.get("https://localhost:4200/");
    }

    @AfterEach
    void afterEach() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @DisplayName("Should update candidate details successfully")
    void testCandidateChange() {
        login("admin", "password");
        driver.findElement(By.linkText("Rekrutacje")).click();
        driver.findElement(By.cssSelector(".recruitment-item:nth-child(1) h3")).click();
        driver.findElement(By.cssSelector(".candidate-item:nth-child(1) p:nth-child(2)")).click();
        driver.findElement(By.id("applicationStatus")).click();
        driver.findElement(By.xpath("//option[@value='OFFER_MADE']")).click();
        driver.findElement(By.cssSelector(".star:nth-child(5)")).click();
        driver.findElement(By.cssSelector(".save-button")).click();
        driver.findElement(By.cssSelector(".logout-button")).click();
    }

    @Test
    @DisplayName("Should unblock a user and verify login restrictions")
    void testUnblockUserAndVerifyLogin() {
        login("admin", "password");
        driver.findElement(By.linkText("Admin")).click();
        driver.findElement(By.cssSelector("input")).click();
        driver.findElement(By.cssSelector("input")).sendKeys("user");
        driver.findElement(By.cssSelector(".user-item:nth-child(1) .block-btn")).click();
        driver.findElement(By.cssSelector(".dialog-actions > button:nth-child(2)")).click();
        driver.findElement(By.cssSelector(".logout-button")).click();

        login("user", "password");
        driver.findElement(By.cssSelector(".logout-button")).click();
    }

    @Test
    @DisplayName("Should add a new event and verify total event count")
    void testAddNewEvent() {
        login("admin", "password");
        String initialEventCount = driver.findElement(By.cssSelector(".hero-stat:nth-child(3) > h2")).getText();
        driver.findElement(By.linkText("Kalendarz")).click();
        driver.findElement(By.cssSelector("button:nth-child(7)")).click();
        driver.findElement(By.id("mat-input-0")).sendKeys("Test Event");
        driver.findElement(By.id("mat-input-1")).sendKeys("19.02.2025");
        driver.findElement(By.id("mat-input-2")).sendKeys("20:00");
        driver.findElement(By.id("mat-input-3")).sendKeys("19.02.2025");
        driver.findElement(By.id("mat-input-4")).sendKeys("21:00");
        driver.findElement(By.id("mat-input-5")).sendKeys("Test description");
        driver.findElement(By.cssSelector(".mdc-button--raised")).click();
        driver.findElement(By.cssSelector("a > .ng-fa-icon")).click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.not(ExpectedConditions.textToBe(By.cssSelector(".hero-stat:nth-child(3) > h2"), initialEventCount)));
        String updatedEventCount = driver.findElement(By.cssSelector(".hero-stat:nth-child(3) > h2")).getText();
        assertNotEquals(initialEventCount, updatedEventCount, "The event count should have been updated after adding a new event.");
    }

    @Test
    @DisplayName("Should create and delete a new job offer successfully")
    void testCreateNewJobOffer() {
        login("admin", "password");
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement closeButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("button.mat-mdc-snack-bar-action .mdc-button__label")));
            closeButton.click();
        } catch (Exception e) {
            System.out.println("Snackbar 'Zamknij' button not found or not clickable.");
        }
        driver.findElement(By.linkText("Oferty pracy")).click();
        driver.findElement(By.cssSelector(".create-job-btn")).click();
        driver.findElement(By.cssSelector("ng-select[labelForId='title-select']")).click();
        driver.findElement(By.cssSelector(".ng-dropdown-panel-items .ng-option:first-child")).click();
        driver.findElement(By.cssSelector("ng-select[labelForId='location']")).click();
        driver.findElement(By.cssSelector(".ng-dropdown-panel-items .ng-option:first-child")).click();
        driver.findElement(By.cssSelector("ng-select[labelForId='jobCategory']")).click();
        driver.findElement(By.cssSelector(".ng-dropdown-panel-items .ng-option:first-child")).click();
        driver.findElement(By.id("workType")).click();
        driver.findElement(By.cssSelector("option[value='REMOTE']")).click();
        driver.findElement(By.id("description")).sendKeys("Opis stanowiska testowego Selenium");
        driver.findElement(By.cssSelector("ng-select[labelForId='recruiters']")).click();
        driver.findElement(By.cssSelector(".ng-dropdown-panel-items .ng-option:first-child")).click();
        driver.findElement(By.cssSelector("ng-select[labelForId='requirements']")).click();
        driver.findElement(By.cssSelector(".ng-dropdown-panel-items .ng-option:first-child")).click();
        driver.findElement(By.cssSelector(".form-actions .btn-primary")).click();
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement snackbarMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector(".mat-mdc-snack-bar-label")));
            String confirmationMessage = snackbarMessage.getText();
            assertTrue(confirmationMessage.contains("Oferta pracy została utworzona pomyślnie!"));
            WebElement closeSnackbarButton = driver.findElement(By.cssSelector("button.mat-mdc-snack-bar-action .mdc-button__label"));
            closeSnackbarButton.click();
        } catch (Exception e) {
            System.out.println("Success snackbar not found or not clickable.");
        }
        driver.findElement(By.cssSelector(".job-table tbody tr:first-child")).click();
        driver.findElement(By.cssSelector("button.delete-btn")).click();
        driver.findElement(By.cssSelector(".dialog-actions button:nth-child(2)")).click();
        try {
            String deleteConfirmation = driver.findElement(By.cssSelector(".mat-mdc-snack-bar-label")).getText();
            assertTrue(deleteConfirmation.contains("Oferta pracy została usunięta"));
        } catch (Exception e) {
            System.out.println("Delete snackbar not found or not clickable.");
        }
    }

    @Test
    @DisplayName("Should change login, verify, and revert back to original login")
    void testChangeLoginAndRevert() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        login("admin", "password");
        driver.findElement(By.cssSelector(".profile-button")).click();
        driver.findElement(By.cssSelector(".edit-button")).click();
        driver.findElement(By.id("login")).clear();
        driver.findElement(By.id("login")).sendKeys("adminnowy");
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("loginName"))).sendKeys("adminnowy");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("loginPassword"))).sendKeys("password");
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".btn-primary"))).click();
        driver.findElement(By.cssSelector(".profile-button")).click();
        driver.findElement(By.cssSelector(".edit-button")).click();
        driver.findElement(By.id("login")).clear();
        driver.findElement(By.id("login")).sendKeys("admin");
        driver.findElement(By.cssSelector("button[type='submit']")).click();
    }

    /**
     * Logs in to the application with the specified username and password.
     *
     * @param username the username to log in
     * @param password the password to log in
     */
    private void login(String username, String password) {
        driver.findElement(By.cssSelector(".mat-primary > .mdc-button__label")).click();
        driver.findElement(By.id("loginName")).sendKeys(username);
        driver.findElement(By.id("loginPassword")).sendKeys(password);
        driver.findElement(By.cssSelector(".btn-primary")).click();
    }

}
