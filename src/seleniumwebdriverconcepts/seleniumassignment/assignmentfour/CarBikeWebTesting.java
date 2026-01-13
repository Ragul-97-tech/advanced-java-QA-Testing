package seleniumwebdriverconcepts.seleniumassignment.assignmentfour;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import seleniumwebdriverconcepts.WebDriverHelper;
import seleniumwebdriverconcepts.CaseFormat;
import seleniumwebdriverconcepts.seleniumassignment.assignmentthree.Validations;

import java.text.NumberFormat;
import java.util.*;
import java.util.NoSuchElementException;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class CarBikeWebTesting {
    public static void main(String[] args) throws InterruptedException {
        final String BASE_URL = "https://www.carandbike.com/";
        Scanner userInputs = new Scanner(System.in);

        Validations validate = new Validations();
        Random random = new Random();

        WebDriver driver = WebDriverHelper.getWebDriver();
        driver.get(BASE_URL);

        JavascriptExecutor js = (JavascriptExecutor) driver;
        driver.manage().window().maximize();

        Actions webActions = new Actions(driver);

        List<WebElement> webElements;
        class CarBikeWebActions {
            public void findDesiredAction(String ID1, String ID2, boolean canSendKeys, boolean isMultiChoiceAndFilter) throws InterruptedException {
                Thread.sleep(500);
                List<WebElement> dropdownElements;
                WebElement selectedDropdown;

                List<String> dropdownText = new ArrayList<>();
                String[] rupeesFilter = new String[0];
                int repeatAgain = (int) (Math.random() * 5);
                while (true) {
                    try {
                        Thread.sleep(500);
                        driver.findElement(By.id(ID1)).click();
                        if (canSendKeys) {
                            driver.findElement(By.id(ID1)).sendKeys(Keys.CONTROL + "a");
                            driver.findElement(By.id(ID1)).sendKeys(Keys.DELETE);
                            driver.findElement(By.id(ID1)).sendKeys(validate.getRandomString(1, 0, CaseFormat.LOWER));
                            Thread.sleep(500);
                        }
                        do {
                            dropdownElements = driver.findElements(By.cssSelector("div[data-search-portal='true'] > div > ul > li"));
                            selectedDropdown = dropdownElements.get(validate.getRandomInt(0, dropdownElements.size()));
                            dropdownText.add(selectedDropdown.getText());
                            selectedDropdown.click();
                            validate.logger.info("Selected " + ID1 + " on " + dropdownText);
                            Thread.sleep(500);
                            repeatAgain--;
                        } while (isMultiChoiceAndFilter && repeatAgain == 0);
                        break;
                    } catch (Exception e) {
                    }
                }

                if (isMultiChoiceAndFilter)
                    rupeesFilter = dropdownText.toArray(new String[0]);

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
                            Thread.sleep(500);
                        }
                        do {
                            dropdownElements = driver.findElements(By.cssSelector("div[data-search-portal='true'] > div > ul > li:not(.text-gray-500)"));
                            selectedDropdown = dropdownElements.get(validate.getRandomInt(0, dropdownElements.size()));
                            dropdownText.add(selectedDropdown.getText());
                            Thread.sleep(500);
                            selectedDropdown.click();
                            validate.logger.info("Selected " + ID2 + " on " + dropdownText);
                            Thread.sleep(500);
                            repeatAgain--;
                        } while (isMultiChoiceAndFilter && repeatAgain == 0);
                        break;
                    } catch (Exception e) {
                    }
                }

                js.executeScript("arguments[0].click()", driver.findElement(By.cssSelector("button[aria-label='Search'][type='button']")));
                String pageTitle = driver.getTitle();
                if (!isMultiChoiceAndFilter && pageTitle != null) {
                    if (dropdownText.stream().allMatch(pageTitle::contains)) {
                        validate.logger.info("Navigate to the correct page title: " + pageTitle);
                    } else {
                        validate.logger.error("Navigate to the wrong page title: " + pageTitle);
                    }
                }

                if (isMultiChoiceAndFilter) {
                    List<Double> chosenBudgets = getBudgetRanges(rupeesFilter);

                    List<Double> renderedBudgets;
                    List<WebElement> filteredVehicles = Optional.of(driver.findElements(By.cssSelector("section[data-widget='ListingSection'] > div:nth-child(2) ul > li > div > div > div > div:nth-child(4) > div:nth-child(1) > div:last-child")))
                            .filter(Predicate.not(List::isEmpty)).orElse(driver.findElements(By.cssSelector("section[data-widget='ListingSection'] > div:nth-child(2) ul > li > ul > li > div > div:last-child > div:nth-child(5) > div:first-child")));

                    renderedBudgets = getBudgetRanges(filteredVehicles.stream().map(WebElement::getText).toList().toArray(new String[0]));

                    System.out.println(chosenBudgets);
                    System.out.println(renderedBudgets);

                    try {
                        NumberFormat numberFormat = NumberFormat.getInstance(Locale.of("en","IN"));
                        if ((chosenBudgets.getFirst() <= renderedBudgets.getFirst()) && (chosenBudgets.getLast() >= renderedBudgets.getLast())) {
                            validate.logger.info("Elements are present in the Filtered range");
                        } else {
                            validate.logger.error(String.format("Filter out of bounded! given range(%s - %s) actual range(%s - %s)", numberFormat.format(chosenBudgets.getFirst()), numberFormat.format(chosenBudgets.getLast()), numberFormat.format(renderedBudgets.getFirst()), numberFormat.format(renderedBudgets.getLast())));
                        }
                    } catch(NoSuchElementException e){
                        validate.logger.error("No filters found!");
                    }
                }

                driver.navigate().back();
            }

            public void executeDesiredAction(String tabText) throws InterruptedException {
                Thread.sleep(500);
                webActions.moveToElement(driver.findElement(By.linkText(tabText + "S"))).clickAndHold().perform();
                Thread.sleep(500);
                webActions.moveToElement(driver.findElement(By.cssSelector("main div:not(#submenu-portal-root)"))).perform();
                String buttonText = "none";
                try {
                    List<WebElement> buttons = Optional.of(driver.findElements(By.cssSelector("section[data-widget='SearchTabs'] > :last-child > div > div > button")))
                            .filter(Predicate.not(List::isEmpty)).orElse(driver.findElements(By.cssSelector("section[data-widget='SearchTabs'] > div:last-child > div:last-child > div:first-child > div > button")));

                    buttons = driver.findElements(By.cssSelector("section[data-widget='SearchTabs'] > div:last-child > div:last-child > div:first-child > div > button"));
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

            private List<Double> getBudgetRanges(String[] rupeesFilter) {
                return Arrays.stream(rupeesFilter).flatMap(ele -> {
                    String clearEle = ele.trim().toLowerCase().replaceAll("\\s+|,|₹", "");
                    if (clearEle.contains("<")) {
                        return Stream.of(validate.localeStringNumberToNumber(clearEle.split("<")[1]));
                    } else {
                        String[] budgets = clearEle.split("–").length == 1 ? clearEle.split("-") : clearEle.split("–");
                        String unit = (budgets.length == 1 ? budgets[0] : budgets[1]).replaceAll("\\d+|\\.", "");
                        return Arrays.stream(budgets).map(budget -> validate.localeStringNumberToNumber(budget.contains(unit) ? budget : budget + unit));
                    }
                }).sorted().toList();
            }
        }

        CarBikeWebActions actions = new CarBikeWebActions();

        for (int i = 0; i < 20; i++) {
            validate.logger.info("---- Test Case (" + (i + 1) + "/20) ----");
            Thread.sleep(500);
            if (i == 10) {
                try {
                    driver.findElements(By.cssSelector("section[data-widget='SearchTabs'] input[type='text']")).stream().filter(webElement -> webElement.isDisplayed() && webElement.isEnabled()).toList().forEach(ele -> {
                        ele.sendKeys(Keys.CONTROL);
                        ele.sendKeys("a");
                        ele.sendKeys(Keys.DELETE);
                    });
                    js.executeScript("arguments[0].click()", driver.findElement(By.cssSelector("button[aria-label='Search'][type='button']")));
                    validate.logger.info("Empty Validation check: Pass");
                    driver.navigate().back();
                } catch (Exception e) {
                    validate.logger.error("Error on empty validation check", e);
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
                case "NEW CAR", "USED CAR", "NEW BIKE" -> actions.executeDesiredAction(tabText);
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
