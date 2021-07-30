package booksProject.pages;

import com.opencsv.CSVWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import pageobject.pages.yandex.YaResultsPage;

import java.util.ArrayList;
import java.util.List;

import static org.openqa.selenium.support.ui.ExpectedConditions.numberOfElementsToBeMoreThan;

public class BookPage {

    private WebDriver driver;
    private static final Logger logger = LogManager.getLogger(BookPage.class);

    public BookPage(WebDriver driver) {
        this.driver = driver;
        driver.get("https://www.mann-ivanov-ferber.ru/tag/samorazvitie-audiobooks/");

        if (!"Подборка книг — аудиокниги по саморазвитию, 2021 года".equals(driver.getTitle())) {
            throw new IllegalStateException("This is not right page");
        }
    }

    private By lastBookItem = By.xpath("//div[@class='c-continuous-list']//div[contains(@class, 'lego-book ')][last()]");
    private By searchField = By.cssSelector("#text");
    private By booksItem = By.xpath("//div[@class='c-continuous-list']//div[contains(@class, 'lego-book ')]");
    private By bookDescription = By.cssSelector("div.l-book-description");
    private By bookTitle = By.cssSelector("div.b-headers .header span");
    private By bookAuthors = By.cssSelector("div.authors");
    private By audiobookPrice = By.xpath("//div[contains(@class, 'p-tab__img') and contains(@class, 'audiobook')]/ancestor::div[1]");
    private By demoLink = By.xpath("//div[contains(@class, 'p-book-download-link') and contains(@class, 'm-audio-mp3')]/a");

    public void scrollDown(Actions action, Wait wait, int currentSize) {
        action.moveToElement(driver.findElement(lastBookItem)).perform();
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 500)");

        try {
            wait.until(numberOfElementsToBeMoreThan(booksItem,currentSize));
            logger.info("Scrolled to end of books list");
        } catch (TimeoutException e) {
            logger.info("all books loaded");
        }
    }

    public List<WebElement> getBooksItems() {
        List<WebElement> cards = new ArrayList<>();
        cards = driver.findElements(booksItem);

        return cards;
    }

    public String getBookLinkAndClick(WebElement elem) {
        List<WebElement> list = new ArrayList<>();
        list = elem.findElements(By.cssSelector("a"));
        if (list.size() == 0) {
            logger.info("There is no links");
        }
        String booLink = elem.findElements(By.cssSelector("a")).get(0).getAttribute("href").toString();
        elem.findElement(By.cssSelector("a")).click();
        return booLink;
    }

    public void switchToBookTab() {
        ArrayList<String> tabs2 = new ArrayList<String>(driver.getWindowHandles());
        if (tabs2.size() != 2) {
            logger.error("There are not 2 tabs");
        }
        driver.switchTo().window(tabs2.get(1));
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 100)");
    }

    public void switchToBookListTab() {

        ArrayList<String> tabs2 = new ArrayList<String>(driver.getWindowHandles());
        if (tabs2.size() != 2) {
            logger.error("There are not 2 tabs");
        }
        driver.close();
        driver.switchTo().window(tabs2.get(0));
    }

    public String[] getBookInfo(WebElement elem, Wait wait, String bookLink) {

        wait.until(ExpectedConditions.presenceOfElementLocated(bookDescription));
        WebElement bookElem = driver.findElement(bookDescription);

        String title = bookElem.findElement(bookTitle).getText().toString();
        String authors = bookElem.findElement(bookAuthors).getText().toString();

        //wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(@class, 'p-tab__img') and contains(@class, 'audiobook')]/ancestor::div[1]")));
        WebElement priceAudio = driver.findElement(audiobookPrice);

        String price = priceAudio.getAttribute("data-price").toString();

        String linkDemo = driver.findElement(demoLink).getAttribute("href").toString();


        String[] nextBook = new String[]{bookLink, title, authors, price, linkDemo};

        return nextBook;
    }

}
