package seleniumwebdriverconcepts.seleniumassignment.assignmentfour;

import org.openqa.selenium.*;
import seleniumwebdriverconcepts.WebDriverHelper;
import seleniumwebdriverconcepts.seleniumassignment.assignmentthree.CaseFormat;
import seleniumwebdriverconcepts.seleniumassignment.assignmentthree.Validations;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;

public class CarBikeWebTesting {
    public static void main(String[] args) throws InterruptedException {
        final String BASE_URL = "https://www.carandbike.com/";
        Scanner userInputs = new Scanner(System.in);

        Validations validate = new Validations();
        Random random = new Random();

        WebDriver driver = WebDriverHelper.getWebDriver();
        driver.get(BASE_URL);

        JavascriptExecutor js = (JavascriptExecutor) driver;
//        driver.manage().window().maximize();

         List<WebElement> webElements;

        class CarBikeWebActions {
            public void findDesiredAction(String ID1, String ID2, boolean canSendKeys, boolean isMultiChoice) throws InterruptedException {
                Thread.sleep(500);
                List<WebElement> dropdownElements;
                WebElement selectedDropdown;

                String dropdownText;
                int repeatAgain = (int) (Math.random() * 3);
                while (true) {
                    try {
                        Thread.sleep(500);
                        driver.findElement(By.id(ID1)).click();
                        if (canSendKeys) {
                            driver.findElement(By.id(ID2)).sendKeys(Keys.CONTROL + "a");
                            driver.findElement(By.id(ID2)).sendKeys(Keys.DELETE);
                            driver.findElement(By.id(ID1)).sendKeys(validate.getRandomString(1, 0, CaseFormat.LOWER));
                        }
                        do {
                            dropdownElements = driver.findElements(By.cssSelector("div[data-search-portal='true'] > div > ul > li"));
                            selectedDropdown = dropdownElements.get(validate.getRandomInt(0, dropdownElements.size()));
                            dropdownText = selectedDropdown.getText();
                            selectedDropdown.click();
                            validate.logger.info("Selected " + ID1 + " on " + dropdownText);
                            Thread.sleep(500);
                            repeatAgain--;
                        } while (isMultiChoice && repeatAgain == 0);
                        break;
                    } catch (Exception e) {
                    }
                }

                String mindAbout;
                repeatAgain = (int) (Math.random() * 3);
                while (true) {
                    try {
                        Thread.sleep(500);
                        driver.findElement(By.id(ID2)).sendKeys("");
                        driver.findElement(By.id(ID2)).click();
                        if (canSendKeys) {
                            driver.findElement(By.id(ID2)).sendKeys(Keys.CONTROL + "a");
                            driver.findElement(By.id(ID2)).sendKeys(Keys.DELETE);
                            driver.findElement(By.id(ID2)).sendKeys(validate.getRandomString(1, 0, CaseFormat.LOWER));
                        }
                        do {
                            dropdownElements = driver.findElements(By.cssSelector("div[data-search-portal='true'] > div > ul > li:not(.text-gray-500)"));
                            selectedDropdown = dropdownElements.get(validate.getRandomInt(0, dropdownElements.size()));
                            mindAbout = selectedDropdown.getText();
                            selectedDropdown.click();
                            validate.logger.info("Selected " + ID2 + " on " + mindAbout);
                            Thread.sleep(500);
                            repeatAgain--;
                        } while (isMultiChoice && repeatAgain==0);
                        break;
                    } catch (Exception e) {
                    }
                }

                js.executeScript("arguments[0].click()", driver.findElement(By.cssSelector("button[aria-label='Search'][type='button']")));
                System.out.println(driver.getTitle());
                String pageTitle = driver.getTitle();
                if (pageTitle != null && pageTitle.toLowerCase().contains(mindAbout.toLowerCase()) || pageTitle.toLowerCase().contains(dropdownText.toLowerCase())) {
                    validate.logger.info("Navigate to the correct page title: " + driver.getTitle());
                } else {
                    validate.logger.error("Navigate to the wrong page title: " + driver.getTitle());
                }
                driver.navigate().back();
            }

            public void executeDesiredAction() throws InterruptedException {
                Thread.sleep(500);
                String buttonText = "none";
                try {
                    List<WebElement> buttons = driver.findElements(By.cssSelector("section[data-widget='SearchTabs'] > :last-child > div > div > button"));
                    WebElement selectedButton = buttons.get(random.nextInt(buttons.size()));
                    buttonText = selectedButton.getText();
                    selectedButton.click();
                    validate.logger.info("Click on " + buttonText);
                } catch (Exception e) {
                    validate.logger.error("Error on clicking " + buttonText, e);
                }

                switch (buttonText) {
                    case "BY BRAND" -> findDesiredAction("brand-input", "model-input", true, false);
                    case "BY BUDGET" -> findDesiredAction("price-input", "carType-input", false, true);
                }
            }
        }

        CarBikeWebActions actions = new CarBikeWebActions();

        for (int i = 0; i < 20; i++) {
            Thread.sleep(500);
            if (i == 10) {
                try {
                    driver.findElements(By.cssSelector("section[data-widget='SearchTabs'] input[type='text']")).stream().filter(webElement -> webElement.isDisplayed() && webElement.isEnabled()).toList().forEach(WebElement::click);
                    js.executeScript("arguments[0].click()", driver.findElement(By.cssSelector("button[aria-label='Search'][type='button']")));
                    validate.logger.info("Empty Validation check: Pass");
                    driver.navigate().back();
                } catch (Exception e) {
                    validate.logger.error("Error on empty validation check",e);
                }
            }
            Thread.sleep(1000);

            String tabText = "none";
            try {
                webElements = driver.findElements(By.cssSelector("section[data-widget='SearchTabs'] > div > ul > li"));
                WebElement selectedTab = webElements.get(random.nextInt(webElements.size()));
                tabText = selectedTab.getText();
                selectedTab.click();
                validate.logger.info("Navigate to " + tabText);
            } catch (Exception e) {
                validate.logger.error("Error on clicking " + tabText, e);
            }
            System.out.println(tabText);
            switch (tabText) {
                case "NEW CAR", "USED CAR", "NEW BIKE" -> actions.executeDesiredAction();
            }
        }
        System.out.print("Are you wants to close the tab(y/n)? ");
        String choice = userInputs.nextLine();
        if (choice.equalsIgnoreCase("y") || choice.equalsIgnoreCase("yes")) {
            System.out.println("Web driver is closed!");
            driver.quit();
        }
    }
}
