package seleniumwebdriverconcepts.seleniumassignment;

import org.openqa.selenium.*;
import seleniumwebdriverconcepts.WebDriverHelper;

import java.io.File;
import java.util.Scanner;

public class FullFormValidation {
    public static void main(String[] args) throws InterruptedException {
        final String BASE_URL = "https://demoqa.com/automation-practice-form";
        Scanner userInputs = new Scanner(System.in);
        WebDriver driver = WebDriverHelper.getWebDriver();
        driver.get(BASE_URL);
        driver.manage().window().maximize();
        JavascriptExecutor js = (JavascriptExecutor) driver;

        String[] hobbies = {"Sports","Reading", "Music"};

        Validations validate = new Validations();

        validate.emptyValidation();
        js.executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.id("submit")));
        Thread.sleep(500);
        driver.findElement(By.id("submit")).click();
        validate.formInputAcknowledge(true);
        validate.formInputAcknowledge(false);

        int nOfElements;
        WebElement element;

        for (int i = 0; i < 20; i++) {

            Thread.sleep(500);
            validate.emptyValidation();

            Thread.sleep(500);
            driver.findElement(By.id("firstName")).sendKeys(validate.getRandomString(5, 0, CaseFormat.LOWER));

            Thread.sleep(500);
            driver.findElement(By.id("lastName")).sendKeys(validate.getRandomString(5, 0, CaseFormat.LOWER));

            Thread.sleep(500);
            driver.findElement(By.id("userEmail")).sendKeys(validate.getRandomString(16,0,CaseFormat.LOWER) + "gmail.com");

            Thread.sleep(500);
            driver.findElement(By.cssSelector(String.format("label[for='gender-radio-%d']", (int) (Math.random() * 3 + 1)))).click();

            driver.findElement(By.id("userNumber")).sendKeys(validate.getRandomMobileNo());

            driver.findElement(By.id("dateOfBirthInput")).click();
            // Year
            nOfElements = driver.findElements(By.cssSelector(".react-datepicker__year-select option")).size();
            driver.findElement(By.cssSelector(".react-datepicker__year-select option:nth-child("+(int)(Math.random() * nOfElements + 1)+")")).click();

            // Month
            nOfElements = driver.findElements(By.cssSelector(".react-datepicker__month-select option")).size();
            driver.findElement(By.cssSelector(".react-datepicker__month-select option:nth-child("+(int)(Math.random() * nOfElements + 1)+")")).click();

            // Day
            nOfElements = driver.findElements(By.cssSelector(".react-datepicker__week .react-datepicker__day")).size();
            driver.findElements(By.cssSelector(".react-datepicker__week .react-datepicker__day")).get((int)(Math.random() * nOfElements)).click();

            while (true) {
                Thread.sleep(500);
                driver.findElement(By.cssSelector("#subjectsContainer input")).clear();
                driver.findElement(By.cssSelector("#subjectsContainer input")).sendKeys(validate.getRandomString(1, 0, CaseFormat.LOWER));
                nOfElements = driver.findElements(By.cssSelector(".subjects-auto-complete__option")).size();
                try {
                    element = driver.findElement(By.cssSelector(".subjects-auto-complete__option:nth-child(" + ((int) (Math.random() * nOfElements + 1)) + ")"));
                    Thread.sleep(500);
                    element.click();
                    break;
                } catch (NoSuchElementException e) {

                }
            }

            driver.findElement(By.cssSelector(String.format("label[for='hobbies-checkbox-%d']", (int) (Math.random() * hobbies.length + 1)))).click();

            driver.findElement(By.id("currentAddress")).sendKeys(validate.getRandomString(30, 6, CaseFormat.RANDOM));

            Thread.sleep(1000);
            driver.findElement(By.id("uploadPicture")).sendKeys(new File("src/Main.java").getAbsolutePath());

            // state
            Thread.sleep(500);
            js.executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.id("state")));
            driver.findElement(By.id("state")).click();
            nOfElements = driver.findElements(By.cssSelector("#state .css-11unzgr > *")).size();
            Thread.sleep(1000);
            driver.findElement(By.cssSelector("#state .css-11unzgr #react-select-3-option-" + (int) (Math.random() * nOfElements))).click();
            Thread.sleep(500);

            // city
            driver.findElement(By.id("city")).click();
            nOfElements = driver.findElements(By.cssSelector("#city .css-11unzgr > *")).size();
            Thread.sleep(500);
            driver.findElement(By.cssSelector("#city .css-11unzgr #react-select-4-option-" + (int) (Math.random() * nOfElements))).click();

            js.executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.id("submit")));
            Thread.sleep(500);
            driver.findElement(By.id("submit")).click();

            if (validate.isValidInputs()) {
                Thread.sleep(2000);
                driver.findElement(By.id("closeLargeModal")).click();
                validate.logger.info("All inputs are valid!");
                validate.formInputAcknowledge(true);
            } else {
                validate.logger.fatal("Some of the inputs are invalid!");
                validate.formInputAcknowledge(false);
            }
        }

        System.out.print("Are you wants to close the tab(y/n)? ");
        String choice = userInputs.nextLine();
        while (choice.equalsIgnoreCase("y") || choice.equalsIgnoreCase("yes")) {
            System.out.println("Web driver is closed!");
            driver.close();
        }
    }
}
