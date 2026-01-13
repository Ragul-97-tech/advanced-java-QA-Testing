package seleniumwebdriverconcepts.seleniumassignment.assignmentthree;

import org.openqa.selenium.*;
import seleniumwebdriverconcepts.CaseFormat;
import seleniumwebdriverconcepts.WebDriverHelper;
import java.util.Scanner;

public class UserNamePassValidation {
    public static void main(String[] args) throws InterruptedException {
        final String[] BASE_URL = {"https://the-internet.herokuapp.com/login", "https://opensource-demo.orangehrmlive.com/web/index.php/auth/login"};
        Scanner userInputs = new Scanner(System.in);
        Validations validate = new Validations();

        for (int i = 0; i < BASE_URL.length; i++) {
            System.out.println((i+1) + ". " + BASE_URL[i]);
        }
        System.out.print("Choose the Base URL: ");
        int urlChoice = userInputs.nextInt() - 1;
        String chosenURL = BASE_URL[urlChoice];
        System.out.println(chosenURL);

        WebDriver driver = WebDriverHelper.getWebDriver();
        driver.get(chosenURL);
        driver.manage().window().maximize();

        Thread.sleep(2000);

        for (int i = 1; i <= 19; i++) {
            Thread.sleep(1000);

            driver.findElement(By.name("username")).clear();
            driver.findElement(By.name("password")).clear();
            String username;
            String password;
            if (Math.random() > 0.75) {
                username = switch (urlChoice) {
                    case 0 -> "tomsmith";
                    case 1 -> "Admin";
                    default -> throw new IllegalStateException("Unexpected value: " + urlChoice);
                };
                password = switch (urlChoice) {
                    case 0 -> "SuperSecretPassword!";
                    case 1 -> "admin123";
                    default -> throw new IllegalStateException("Unexpected value: " + urlChoice);
                };
            }
            else {
                username = validate.getRandomString(6,2, CaseFormat.RANDOM);
                password = validate.getRandomString(12,0, CaseFormat.RANDOM);
            }
            driver.findElement(By.name("username")).sendKeys(username);
            driver.findElement(By.name("password")).sendKeys(password);

            driver.findElement(By.cssSelector("button[type='submit']")).click();
            Thread.sleep(1500);
            if (driver.findElement(By.id("flash")).getText().contains("logged") || validate.isValidInputs()) {
                validate.logger.info(String.format("%-8s | %-8s | %-8s", "Login Successful!", username, password));
                Thread.sleep(500);
                if (urlChoice == 1) {
                    driver.findElement(By.className("oxd-topbar-header-userarea")).click();
                    driver.findElement(By.linkText("Logout")).click();
                }
            }
            else
                validate.logger.error(String.format("%-8s | %-8s | %-8s", "Invalid Inputs", username, password));

        }

        System.out.print("Are you wants to close the tab(y/n)? ");
        String choice = userInputs.nextLine();
        while (choice.equalsIgnoreCase("y") || choice.equalsIgnoreCase("yes")) {
            System.out.println("Web driver is closed!");
            driver.close();
        }
    }
}
