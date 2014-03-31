package febner.xing;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * 
 */

/**
 * @author Franz
 * 
 */
public class Resorter {

    private Resorter() {

    }

    private static final long TIMEOUT = 60;
    private static final String OFFER_FILE = "/offers.txt";

    public static void main(String[] args) {

        WebDriver driver = new FirefoxDriver();
        driver.get("http://www.xing.com");
        driver.manage().timeouts().implicitlyWait(TIMEOUT, TimeUnit.SECONDS);

        System.out.println("Please logon [in the next " + TIMEOUT + "s].");

        driver.navigate().refresh();
        driver.findElement(By.xpath("//ul[@id='myxing-bar']/li/a/img")).click();

        driver.findElement(By.cssSelector("#haves > a.edit-icon")).click();

        URL offerUrl = Resorter.class.getResource(OFFER_FILE);
        Objects.requireNonNull(offerUrl);

        Path offerPath = null;
        try {
            offerPath = Paths.get(offerUrl.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        Objects.requireNonNull(offerPath);

        List<String> offers = new ArrayList<String>();

        try {
            offers = Files.readAllLines(offerPath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int i = 1;
        for (String offer : offers) {
            driver.findElement(By.xpath("//section[@id='cv-tab']/div/div[2]/div/form/div/div[" + i + "]/div/input"))
                    .clear();
            driver.findElement(By.xpath("//section[@id='cv-tab']/div/div[2]/div/form/div/div[" + i++ + "]/div/input"))
                    .sendKeys(offer.trim());
        }

    }

}
