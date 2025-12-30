package seleniumwebdriverconcepts.seleniumlearning;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.Scanner;

public class WebDriverCardFlipping {
    public static void main(String[] args) {
        WebDriver driver = new ChromeDriver();
        final String baseURL = "https://www.zohoschools.com";
        driver.get(baseURL);

        driver.findElement(By.cssSelector(".zpbutton-icon:nth-child(1)")).click();
        Scanner userInputs = new Scanner(System.in);
        System.out.print("Are you wants to close the tab(y/n)? ");
        String choice = userInputs.nextLine();
        if (choice.equalsIgnoreCase("y") || choice.equalsIgnoreCase("yes")) {
            System.out.println("Web driver is closed!");
            driver.close();
        }
        else
            return;
        System.out.println("hi");
    }
}
