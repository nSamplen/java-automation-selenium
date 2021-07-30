package yandexMarket;

import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.proxy.CaptureType;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;
import yandexMarket.pages.YandexMarketPage;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class TestYandexMarker extends BaseTest {
    private static final Logger logger = LogManager.getLogger(TestYandexMarker.class);

    @Test
    public void testComparingTwoItems(){
        // open catalog of mobile phones on yandex market
        YandexMarketPage marketPage = new YandexMarketPage(driver);

        //show only xiaomi and honor and sort results
        marketPage.selectNeededManufacturers(wait);
        marketPage.sortResultsASC(wait);

        // add first 'xiaomi' device to comparation
        String addedDevice = marketPage.findFirstXDevice(wait, "xiaomi");
        // check if message 'added to comparation' is displayed
        Assert.assertTrue("'Device was added to comparation' message is not displayed",
                marketPage.isItemAddedMsgDisplayed());
        Assert.assertTrue("Actual added to comparation device's name doesn't equal expected", marketPage.isRightDeviceAdded(addedDevice));


        // add first 'honor' device to comparation
        addedDevice = marketPage.findFirstXDevice(wait, "honor");
        // check if message 'added to comparation' is displayed
        Assert.assertTrue("'Device was added to comparation' message is not displayed",
                marketPage.isItemAddedMsgDisplayed());
        Assert.assertTrue("Actual added to comparation device's name doesn't equal expected", marketPage.isRightDeviceAdded(addedDevice));


        int numOdDevicesAtComparationList = marketPage.checkComparationTab(wait);
        Assert.assertTrue("There are not extactly 2 items in comparation list",numOdDevicesAtComparationList == 2);

        // check that feature 'OS' is displayed
        Boolean isOS = marketPage.checkAllFeature();
        Assert.assertTrue("'OS' feature is not displayed in 'All features' option", isOS);

        // check that feature 'OS' is not displayed
        isOS = marketPage.checkDifferFeature();
        Assert.assertTrue("'OS' feature is displayed in 'Differ features' option", !isOS);

    }


}
