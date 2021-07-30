import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MoveTest extends BaseTest{

    private static final Logger logger = LogManager.getLogger(MoveTest.class);

    @Test
    public void move() {

        driver.get("https://www.ozon.ru/");

        WebElement myOzon = driver.findElement(By.xpath("//div[@data-widget='profileMenuAnonymous']"));

        action.moveToElement(myOzon).perform();

        String content = driver.findElement(By.xpath("//div[contains(@id,\"state-profileMenuAnonymous\")]")).getAttribute("data-state");

        JSONObject jsonObj = new JSONObject(content.toString());
        JSONObject rootObj = jsonObj.getJSONObject("tooltip");// получить поле "message" как строку
        String text = rootObj.getString("text");
        String notice = rootObj.getString("notice");

        StringBuilder sb = new StringBuilder();
        sb.append("\n\t" + text + "\n\t");
        sb.append(notice + "\n\t");

        JSONArray arrayJson = rootObj.getJSONArray("buttons");
        for (int i = 0; i < arrayJson.length(); i++) {
            sb.append(arrayJson.getJSONObject(i).getString("text") + "\n\t");

        }
        logger.info(sb.toString());

    }

    @Test
    public void draw() {
        driver.get("http://www.htmlcanvasstudio.com/");

        WebElement canvas = driver.findElement(By.cssSelector("#imageTemp"));

        Actions beforeBuild = action
                .clickAndHold(canvas)
                .moveByOffset(50, -50)
                .moveByOffset(50, 0)
                .moveByOffset(50, 50)
                .moveByOffset(-150, 150)
                .moveByOffset(-150, -150)
                .moveByOffset(50, -50)
                .moveByOffset(50, 0)
                .moveByOffset(50, 50)
                .release();

        Action afterBuild = beforeBuild.build();
        afterBuild.perform();
    }

    @Test
    public void contextMenu() {
        driver.get("http://swimlane.github.io/ngx-datatable/#contextmenu");

        WebElement row = driver.findElement(By.cssSelector("datatable-row-wrapper"));
        String firstCellText = row.findElement(By.cssSelector("datatable-body-cell")).getText();

        action
                .moveToElement(row.findElement(By.cssSelector("datatable-body-cell")))
                .contextClick()
                .build()
                .perform();

        wait.until(drv -> drv.findElements(By.cssSelector("contextmenu-demo .info p")).size() > 1);
        String infoText = driver.findElements(By.cssSelector("contextmenu-demo .info p")).get(2).getText();
        Assert.assertTrue(infoText.contains(firstCellText));
    }

    @Test
    public void dragAndDropTest() {
        driver.get("https://professorweb.ru/my/javascript/jquery/level4/files/test29.html");

        WebElement dragMe = driver.findElement(By.cssSelector("div.draggable.ui-state-error.ui-draggable"));
        WebElement snapper = driver.findElement(By.cssSelector("#snapper"));

        action
                .dragAndDropBy(dragMe, 0, 250)
                .pause(2000)
                .dragAndDropBy(dragMe, 0, -150)
                .pause(2000)
                .dragAndDrop(dragMe, snapper)
                .build()
                .perform();
    }
}
