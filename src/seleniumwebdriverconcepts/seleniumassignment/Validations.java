package seleniumwebdriverconcepts.seleniumassignment;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import seleniumwebdriverconcepts.WebDriverHelper;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;

public class Validations {

    int[] specialChars = {
            '!', '"', '#', '$', '%', '&', '\'', '(', ')', '*', '+', ',', '-', '.', '/',
            ':', ';', '<', '=', '>', '?', '@',
            '[', '\\', ']', '^', '_', '`',
            '{', '|', '}', '~'
    };
    int[] allAsciiCode = {33, 126};
    int[] upperCaseLetters = {65, 90};
    int[] lowerCaseLetters = {97, 122};
    String[] GENDER = {"Male", "Female", "Others"};

    Logger logger = LogManager.getLogger("Validations");
    WebDriver driver = WebDriverHelper.getWebDriver();

    private char getRandomChar(int min, int max) {
        return (char)(int)((Math.random() * (max - min + 1)) + min);
    }

    private char getSpecialChar(){
        return (char)specialChars[(int)(Math.random() * specialChars.length)];
    }

    public String getRandomString(int countOfLetters, int countOfSpecialChar, CaseFormat format) {
        if (countOfLetters == 0) return "";
        StringBuilder word = new StringBuilder();
        int[] choiceOfArray = switch (format) {
            case LOWER -> lowerCaseLetters;
            case UPPER -> upperCaseLetters;
            case RANDOM -> Math.random() > 0.5 ? upperCaseLetters : lowerCaseLetters;
        };
        for (int i = 0; i < countOfLetters; i++) {
            word.append(getRandomChar(choiceOfArray[0], choiceOfArray[1]));
            if (countOfSpecialChar > 0 && Math.random() > 0.75) {
                countOfSpecialChar--;
                word.append(getSpecialChar());
            }
        }
        return word.toString();
    }

    public String getRandomPassword(int countOfLetters, int countOfSpecialChar) {
        if (countOfLetters == 0) return "";
        StringBuilder word = new StringBuilder();
        for (int i = 0; i < countOfLetters; i++) {
            word.append(getRandomChar(allAsciiCode[0], allAsciiCode[1]));
        }
        return word.toString();
    }

    public void emptyValidation() {
        driver.findElements(By.cssSelector("input, select, textarea")).forEach(webElement -> webElement.sendKeys(""));
    }

    public void onlySpaceValidation() {
        driver.findElements(By.tagName("input, select, textarea")).forEach(ele -> ele.sendKeys(" "));
    }

    public String getRandomGender() {
        return GENDER[(int)(Math.random() * GENDER.length)];
    }

    public String getRandomMobileNo() {
        return (long)(Math.random() * (9999999999L - 1000000000) + 1000000000)+"";
    }

    public String getRandomDate() {
        return String.format("%02d %s %d",(int)(Math.random() * 31 + 1), Month.values()[(int)(Math.random() * 12)].getDisplayName(TextStyle.SHORT, Locale.US),(int)(Math.random() * (2027 - 2000) + 2000));
    }

    public void formInputAcknowledge(boolean validOrInvalid) {
        if (validOrInvalid)
            driver.findElements(By.cssSelector("input:valid, select:valid, textarea:valid")).forEach(webElement -> logger.info(webElement.getAttribute("id") + " is valid!"));
        else
            driver.findElements(By.cssSelector("input:invalid, select:invalid, textarea:invalid")).forEach(webElement -> logger.fatal("Invalid Inputs! "+ webElement.getAttribute("id") + " is invalid!"));
    }

    public boolean isValidInputs() {
        return driver.findElements(By.cssSelector("input:valid, select:valid, textarea:valid")).isEmpty();
    }
}
