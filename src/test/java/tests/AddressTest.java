package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;

public class AddressTest {

    private WebDriver driver;
    private final String expectedAddress = "305000, г.Курск, ул. Радищева, 17";
    private final String url = "https://adm.rkursk.ru/";
    private final By locatorGovernance = By.xpath("//nav//a[contains(., 'Власть')]");
    private final By locatorStructs = By.xpath("//a[contains(., 'Структурные подразделения')]");
    private final By locatorServices = By.xpath("//a[contains(., 'Комитет жилищно-коммунального')]");
    private final By locatorH1 = By.xpath("//h1");
    private final By locatorAddress = By.xpath("//div[text() = 'Адрес:']/following-sibling::div[1]");
    private WebDriverWait wait;
    private Actions act;

    @BeforeClass
    public void beforeClass() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();

        wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        act = new Actions(driver);

        driver.get(url);
    }

    @Test
    public void step1() {
        moveTo(locatorGovernance); // Наводим курсор на вкладку "Власть"
        click(locatorStructs); // Кликаем по ссылке "Структурные подразделения"

        Assert.assertEquals(getText(locatorH1), "Структурные подразделения Администрации Курской области"); // Получаем текст заголовка страницы и сравниваем
    }

    @Test(dependsOnMethods = "step1")
    public void step2() {
        click(locatorServices); // Кликаем по ссылке "Комитет жилищно-коммунального хозяйства"

        Assert.assertEquals(getText(locatorH1), "Комитет жилищно-коммунального хозяйства и ТЭК Курской области"); // Получаем текст заголовка страницы и сравниваем
    }

    @Test(dependsOnMethods = "step2")
    public void step3() {
        Assert.assertEquals(getText(locatorAddress), expectedAddress); // Сравниваем фактический адрес с ожидаемым
    }

    @AfterClass
    public void afterClass() {
        // Добавил для наглядности, чтобы выделить то, что мы искали

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].style = 'border: solid 2px red; background: yellow; font-size: 30px'", driver.findElement(locatorAddress));
        js.executeScript("scroll(0, 700);");
    }

    private void click(By locator) {
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(locator))).click();
    }

    private void moveTo(By locator) {
        act.moveToElement(driver.findElement(locator)).build().perform();
    }

    private String getText(By locator) {
        return driver.findElement(locator).getText();
    }
}
