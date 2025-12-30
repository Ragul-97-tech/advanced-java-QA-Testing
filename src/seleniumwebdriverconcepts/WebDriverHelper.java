package seleniumwebdriverconcepts;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class WebDriverHelper {
    static WebDriver webDriver;
    private WebDriverHelper() {
    }
    private static class SingletonWebDriver {
        private static final WebDriver WEB_DRIVER = new ChromeDriver();
    }
    public static WebDriver getWebDriver() {
        return SingletonWebDriver.WEB_DRIVER;
    }
}
