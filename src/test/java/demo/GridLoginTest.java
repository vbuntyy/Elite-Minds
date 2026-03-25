package demo;
 
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.Assert;
import org.testng.annotations.*;
 
import java.net.URL;
 
public class GridLoginTest {
 
    // ThreadLocal is important when running parallel (each thread gets its own driver)
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
 
    @Parameters({"browser", "gridUrl"})
    @BeforeMethod
    public void setUp(String browser, String gridUrl) throws Exception {
 
        // 1) Decide browser options based on parameter
        if (browser.equalsIgnoreCase("chrome")) {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--start-maximized");
            // For CI machines without display:
            // options.addArguments("--headless=new");
 
            driver.set(new RemoteWebDriver(new URL(gridUrl), options));
 
        } else if (browser.equalsIgnoreCase("firefox")) {
            FirefoxOptions options = new FirefoxOptions();
            // options.addArguments("-headless");
 
            driver.set(new RemoteWebDriver(new URL(gridUrl), options));
 
        } else {
            throw new RuntimeException("Unsupported browser: " + browser);
        }
    }
 
    @Test
    public void openHomePageAndVerifyTitle() {
        driver.get().get("https://example.com");
 
        String title = driver.get().getTitle();
        System.out.println("Page title = " + title);
 
        // Simple assertion for
        Assert.assertTrue(title != null && !title.trim().isEmpty(), "Title should not be empty");
    }
 
    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver.get() != null) {
            driver.get().quit();
        }
        driver.remove();
    }
}