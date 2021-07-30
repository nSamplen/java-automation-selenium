package booksProject;

import booksProject.pages.BookPage;
import com.opencsv.CSVWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.Test;
import pageobject.pages.yandex.YaRuPage;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOfElementLocated;
import static org.openqa.selenium.support.ui.ExpectedConditions.numberOfElementsToBeMoreThan;

public class BooksTest extends BaseTest {
    private static final Logger logger = LogManager.getLogger(BaseTest.class);

    private static String BOOKS_FILE_PATH = "logs/test.csv";
    private static String[] HEADER = {"Link", "Title", "Author", "Price", "Link to demo fragment"};

    @Test
    public void getAllBooks() {

        BookPage bookPage = new BookPage(driver);

        List<WebElement> cards = new ArrayList<>();
        int currentSize = -1;

        CSVWriter writer = null;
        try {
            writer = new CSVWriter(new FileWriter(BOOKS_FILE_PATH));
            logger.info("CSV file successfully created: " + BOOKS_FILE_PATH);
        } catch (IOException e) {
            logger.error("Error with CSVWriter: " + e);
        }
        writer.writeNext(HEADER);
        logger.info("HEADER added to CSV file: " + HEADER);

        int i = 0;

        while (currentSize < cards.size()) {

            currentSize = cards.size();

            bookPage.scrollDown(action, wait, currentSize);

            cards = bookPage.getBooksItems();

            for (int j = i; j < 10 && j < cards.size(); j++, i++) {

                String bookLink = bookPage.getBookLinkAndClick(cards.get(i));
                bookPage.switchToBookTab();
                String[] nextBook = bookPage.getBookInfo(cards.get(i), wait, bookLink);

                // System.out.println(Arrays.toString(nextBook));

                writer.writeNext(nextBook);

                bookPage.switchToBookListTab();

            }
        }
        try {
            writer.close();
        } catch (IOException e) {
            logger.error("Error close CSVWriter: " + e);
        }

    }
}
