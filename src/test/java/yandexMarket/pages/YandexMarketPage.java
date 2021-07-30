package yandexMarket.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;

import java.util.Locale;

public class YandexMarketPage {

    private WebDriver driver;
    private static final Logger logger = LogManager.getLogger(YandexMarketPage.class);
    public static String YANDEX_LINK = "https://market.yandex.ru/catalog--smartfony/54726/list";

    public YandexMarketPage(WebDriver driver) {
        this.driver = driver;
        driver.get(YANDEX_LINK);

       /* if (!"Мобильные телефоны — купить на Яндекс.Маркете".equals(driver.getTitle())) {
            logger.error("This is not right page");
            throw new IllegalStateException("This is not right page");
        }*/
    }

    private By SHOW_ALL_MANUFACTURERS_LOCATOR = By.xpath("//legend[contains(text(), 'Производитель')]/ancestor::fieldset//button[text()='Показать всё']");
    private By INPUT_MANUFACTURERS_LOCATOR = By.xpath("//legend[contains(text(), 'Производитель')]/ancestor::fieldset//input[@type='text']");
    private By XIAOMI_CHECKBOX_MANUFACTURER_LOCATOR = By.xpath("//legend[contains(text(), 'Производитель')]/ancestor::fieldset//li//input[@type='checkbox' and contains(@name,'Xiaomi')]/ancestor::label");
    private By HONOR_CHECKBOX_MANUFACTURER_LOCATOR = By.xpath("//legend[contains(text(), 'Производитель')]/ancestor::fieldset//li//input[@type='checkbox' and contains(@name,'HONOR')]/ancestor::label");
    private By SORT_BY_COST_LOCATOR = By.xpath("//button[text()='по цене']");

    //private By ADD_TO_COMPARATION_LOCATOR = By.xpath("//div[@aria-label=\"Добавить к сравнению\"]");
    private String ADD_TO_COMPARATION = "//div[@aria-label=\"Добавить к сравнению\"]";
    private String XIAOMI_FIRST_DEVICE_NAME = "//article//h3//a[contains(.,'Xiaomi')]";
    private String HONOR_FIRST_DEVICE_NAME = "//article//h3//a[contains(.,'HONOR')]";

    private By FRST_XIAOMI_ADD_TO_COMPARATION_LOCATOR = By.xpath(XIAOMI_FIRST_DEVICE_NAME + "/ancestor::article" + ADD_TO_COMPARATION);
    private By NAME_OF_FRST_XIAOMI_ADD_TO_COMPARATION_LOCATOR = By.xpath(XIAOMI_FIRST_DEVICE_NAME + "/ancestor::article" + ADD_TO_COMPARATION + "/ancestor::article//h3//a");

    private By FRST_HONOR_ADD_TO_COMPARATION_LOCATOR = By.xpath(HONOR_FIRST_DEVICE_NAME + "/ancestor::article" + ADD_TO_COMPARATION);
    private By NAME_OF_FRST_HONOR_ADD_TO_COMPARATION_LOCATOR = By.xpath(HONOR_FIRST_DEVICE_NAME + "/ancestor::article" + ADD_TO_COMPARATION + "/ancestor::article//h3//a");

    private By ADDED_TO_COMPARATION_TITLE_LOCATOR = By.xpath("//div[contains(text(),\"добавлен к сравнению\")]");
    private By GO_TO_COMPARATION_LOCATOR = By.xpath("//div/a[text()=\"Сравнить\"]");
    private By COMPARING_LIST_LOCATOR = By.xpath("//div[@data-apiary-widget-id=\"/content/compareContent\"]//div/img");

    private By ALL_FEATURES_BUTTON = By.xpath("//button[text()=\"Все характеристики\"]");
    private By DIFFER_FEATURES_BUTTON = By.xpath("//button[text()=\"Различающиеся характеристики\"]");
    private By OS_FEATURE_LOCATOR = By.xpath("//div[text()=\"Операционная система\"]");

    public void selectNeededManufacturers(Wait wait) {
        // click on 'Show all' manufacturers
        driver.findElement(SHOW_ALL_MANUFACTURERS_LOCATOR).click();

        //select Xiaomi
        selectManufacturer(wait, "xiaomi", XIAOMI_CHECKBOX_MANUFACTURER_LOCATOR);
        //select honor
        selectManufacturer(wait, "honor", HONOR_CHECKBOX_MANUFACTURER_LOCATOR);

    }

    public void selectManufacturer(Wait wait, String manufacturer, By manufacturerLocator) {
        // clear input
        driver.findElement(INPUT_MANUFACTURERS_LOCATOR).clear();
        // input Xiaomi
        driver.findElement(INPUT_MANUFACTURERS_LOCATOR).sendKeys(manufacturer + Keys.ENTER);
        // wait for result and select checkbox
        wait.until(ExpectedConditions.presenceOfElementLocated(manufacturerLocator));
        driver.findElement(manufacturerLocator).click();
        logger.info("'{}' option was checked", manufacturer);
    }

    public void sortResultsASC(Wait wait) {
        // sort results by cost asc
        driver.findElement(SORT_BY_COST_LOCATOR).click();
        logger.info("Results are sorted");
        wait.until(ExpectedConditions.stalenessOf(driver.findElement(FRST_XIAOMI_ADD_TO_COMPARATION_LOCATOR)));
    }

    public By getFirstDeviceLocator(String device, boolean isName) {
        switch (device.toLowerCase(Locale.ROOT)) {
            case "xiaomi":
                if (isName) {
                    return NAME_OF_FRST_XIAOMI_ADD_TO_COMPARATION_LOCATOR;
                }
                return FRST_XIAOMI_ADD_TO_COMPARATION_LOCATOR;
            case "honor":
                if (isName) {
                    return NAME_OF_FRST_HONOR_ADD_TO_COMPARATION_LOCATOR;
                }
                return FRST_HONOR_ADD_TO_COMPARATION_LOCATOR;
            default:
                logger.error("Device is not supported in this test");
                Assert.assertTrue(device.toLowerCase(Locale.ROOT).equals("xiaomi")
                        || device.toLowerCase(Locale.ROOT).equals("honor"));
                return null;
        }
    }

    public String findFirstXDevice(Wait wait, String device) {

        By neededDevice = getFirstDeviceLocator(device, false);
        By deviceName = getFirstDeviceLocator(device, true);

        // wait until be able to click 'add to comparing' and click
        wait.until(ExpectedConditions.presenceOfElementLocated(neededDevice));
        String deviceAddedToComparation = driver.findElement(deviceName).getText();
        driver.findElement(neededDevice).click();
        logger.info("Device '{}' was added to comparation",deviceAddedToComparation);

        return deviceAddedToComparation;
    }

    public Boolean isItemAddedMsgDisplayed() {
        return driver.findElement(ADDED_TO_COMPARATION_TITLE_LOCATOR).isDisplayed();
    }

    public Boolean isRightDeviceAdded(String deviceAddedToComparation) {

        // get actual message's text
        String actualComparationDevice = driver.findElement(ADDED_TO_COMPARATION_TITLE_LOCATOR).getText();
        actualComparationDevice = actualComparationDevice.split("Товар ")[1].split(" добавлен к сравнению")[0];
        deviceAddedToComparation = deviceAddedToComparation.split(",")[0];

        // check if added device is the same that were selected
        if (!deviceAddedToComparation.equals(actualComparationDevice)) {
            logger.error("Added device's name and device's name in messages are not the same:\n\t" + deviceAddedToComparation + " != " + actualComparationDevice);
            return false;
        }

        return true;
    }

    public int checkComparationTab(Wait wait) {
        // go to comparation
        wait.until(ExpectedConditions.presenceOfElementLocated(GO_TO_COMPARATION_LOCATOR));
        driver.findElement(GO_TO_COMPARATION_LOCATOR).click();
        logger.info("Now at comparation tab");

        // check if only 2 items listed
        wait.until(ExpectedConditions.presenceOfElementLocated(COMPARING_LIST_LOCATOR));
        return driver.findElements(COMPARING_LIST_LOCATOR).size();
        //logger.info("There are exactly 2 item in comparation list");

    }

    public Boolean checkAllFeature() {
        // click on 'all features'
        driver.findElement(ALL_FEATURES_BUTTON).click();
        logger.info("'All features' option selected");

        boolean isOS = false;

        try {
            final WebElement elem = driver.findElement(OS_FEATURE_LOCATOR);
            isOS =  driver.findElement(OS_FEATURE_LOCATOR).isDisplayed();
        } catch (NoSuchElementException nse) {
            isOS =  false;
            logger.error("'OS' feature is not displayed at 'all features tab'");
        }

        return isOS;
    }

    public Boolean checkDifferFeature() {
        // click on 'differed features'
        driver.findElement(DIFFER_FEATURES_BUTTON).click();

        Boolean isOS = false;

        try {
            final WebElement elem = driver.findElement(OS_FEATURE_LOCATOR);
            isOS =  true;
            logger.error("'OS' feature is displayed at 'differ features tab'");

        } catch (NoSuchElementException nse) {
            isOS =  false;
        }

        return isOS;
    }



}
