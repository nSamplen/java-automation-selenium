package screenshots;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

public class ScreenShotTest extends BaseTest{

    private static Logger logger = LogManager.getLogger(ScreenShotTest.class);

    @Test
    public void takeScreenShotTest() {

        driver.get("https://ya.ru");
        driver.findElement(By.cssSelector("#text")).sendKeys("base64");
        String base64 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
        saveBase64(base64);

        driver.findElement(By.cssSelector("#text")).clear();
        driver.findElement(By.cssSelector("#text")).sendKeys("bytes");
        byte[] bytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        saveBytes(bytes);

        driver.findElement(By.cssSelector("#text")).clear();
        driver.findElement(By.cssSelector("#text")).sendKeys("file");
        File file = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        saveFile(file);
    }

    private void saveBase64(String data) {
        File file = OutputType.FILE.convertFromBase64Png(data);
        saveFile(file);
    }

    private void saveBytes(byte[] data) {
        File file = OutputType.FILE.convertFromPngBytes(data);
        saveFile(file);
    }

    private void saveFile(File file) {
        String path = "target/" + System.currentTimeMillis() + ".png";
        try {
            FileUtils.copyFile(file, new File(path));
        } catch (IOException e) {
            logger.error(e);
        }
    }

    @Test
    public void elementScreenShotTest() {
        driver.get("https://ya.ru");

        driver.findElement(By.cssSelector("#text")).sendKeys("element");
        File file = driver.findElement(By.cssSelector("#text")).getScreenshotAs(OutputType.FILE);
        saveFile(file);
    }
}
