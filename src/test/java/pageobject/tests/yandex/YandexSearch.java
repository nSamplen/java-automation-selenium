package pageobject.tests.yandex;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageobject.pages.yandex.YaResultsPage;
import pageobject.pages.yandex.YaRuPage;

public class YandexSearch extends BaseTest{

    @Test
    public void search() {
        YaRuPage yaruPage = new YaRuPage(driver);
        YaResultsPage yaresultsPage = yaruPage.search("cats memes");

        Assert.assertEquals(
                "Memes of cats.",
                yaresultsPage.getFirstResultTitle()
        );
    }
}
