import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

public class ExecuteJs extends BaseTest{
    private static final Logger logger = LogManager.getLogger(ExecuteJs.class);

    private static final String CONSOLE_LOG = "var text = 'I am text'; console.log(text);";
    private static final String RETURN_TEXT = "return 'text'";
    private static final String RETURN_NUMBER = "return 23";
    private static final String RETURN_BOOL = "return true";
    private static final String RETURN_ELEMENT = "return document.querySelector('#text')";
    private static final String RETURN_ASYNC = "var callback = arguments[arguments.length - 1]"
            + "var xhr = new XMLHttpRequest();"
            + "xhr.open('GET', '//yastatic.net/iconostasis/_/8lFaTHLDzmsEZz-5XaQg9iTWZGE.png', true);"
            + "xhr.onreadystatechange = function() {"
            + "    if (xhr.readyState == 4) {"
            + "        callback(xhr.responseText);"
            + "    }"
            + "};"
            + "xhr.send();";


    @Test
    public void execute() {
        driver.get("https://ya.ru/");

        Object wiilBeNull = ((JavascriptExecutor) driver).executeScript(CONSOLE_LOG);
        String str = (String) ((JavascriptExecutor) driver).executeScript(RETURN_TEXT);
        Long number = (Long) ((JavascriptExecutor) driver).executeScript(RETURN_NUMBER);
        Boolean boolVar = (Boolean) ((JavascriptExecutor) driver).executeScript(RETURN_BOOL);
        WebElement element = (WebElement) ((JavascriptExecutor) driver).executeScript(RETURN_ELEMENT);

    }

    @Test
    public void executeAsync() {
        driver.get("https://ya.ru/");

        Object response = ((JavascriptExecutor) driver).executeAsyncScript(RETURN_ASYNC);
        String str = (String) ((JavascriptExecutor) driver).executeScript(RETURN_ASYNC);

    }
}
