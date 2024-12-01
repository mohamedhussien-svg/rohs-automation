package utilis;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ChromeUtility {
    private String downloadDir;

    public ChromeUtility(String downloadDir) {
        this.downloadDir = downloadDir;
    }

    public WebDriver getDriver() {
        String workingDir = System.getProperty("user.dir");
        System.setProperty("webdriver.chrome.driver", workingDir + "\\chromedriver-win64\\chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("plugins.always_open_pdf_externally", true);
        prefs.put("download.prompt_for_download", false);
        prefs.put("download.default_directory", downloadDir);
        options.setExperimentalOption("prefs", prefs);

        ChromeDriver driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        return driver;
    }

    public WebDriver getFireFoxDriver() {
        String workingDir = System.getProperty("user.dir");
        System.setProperty("webdriver.gecko.driver", workingDir + "\\geckodriver-v0.34.0-win64\\geckodriver.exe");
        FirefoxOptions options = new FirefoxOptions();
        options.setBinary("C:\\Users\\A79386\\AppData\\Local\\Mozilla Firefox\\firefox.exe");
        options.addPreference("browser.download.folderList", 2);
        options.addPreference("browser.download.dir", downloadDir);
        options.addPreference("browser.helperApps.neverAsk.saveToDisk", "application/pdf");
        options.addPreference("pdfjs.disabled", true);

        FirefoxDriver driver = new FirefoxDriver(options);
        driver.manage().window().maximize();
        return driver;
    }


    public void wait(WebDriver driver, int time) {
        driver.manage().timeouts().implicitlyWait(time, TimeUnit.SECONDS);
    }

    public WebElement getElementByXpath(WebDriver driver, String xpathExpression) {
        try {

            return driver.findElement(By.xpath(xpathExpression));
        } catch (Exception e) {
            return null;
        }
    }

    public WebElement getElementByXpathJs(WebDriver driver, String xpathExpression) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(10000L));
            return wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpathExpression)));
        } catch (Exception e) {
            return null;
        }
    }

    public WebElement getElementByClassName(WebDriver driver, String className) {
        try {

            return driver.findElement(By.className(className));
        } catch (Exception e) {
            return null;
        }
    }
}
