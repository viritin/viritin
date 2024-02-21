package org.vaadin.viritinv7;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.vaadin.viritin.it.AbstractWebDriverCase;
import org.vaadin.viritin.it.VaadinConditions;

import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Ignore;
import org.openqa.selenium.firefox.FirefoxOptions;

/**
 * Created by marco on 07/05/16.
 */
@RunWith(Parameterized.class)
public class MBeanFieldGroupRequiredErrorMessageTest extends AbstractWebDriverCase {

    @Parameterized.Parameters
    public static Collection languages() {
        return Arrays.asList(new Object[][] {
            { "en", "may not be null" },
            { "it", "Non deve essere nullo" },
        });

    }

    private final String expectedMessage;


    public MBeanFieldGroupRequiredErrorMessageTest(String language, String expectedMessage) {
        this.expectedMessage = expectedMessage;
        FirefoxProfile profile = new FirefoxProfile();
        profile.setPreference("intl.accept_languages", language);
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.setProfile(profile);
        WebDriver webDriver = new FirefoxDriver(firefoxOptions);
        startBrowser(webDriver);
    }

    @Test
    @Ignore("See if can be run with phantomjs and/or update to geckodriver")
    public void testErrorMessageForNonNullAnnotatedComponent() throws InterruptedException {
        driver.navigate().to(
            "http://localhost:5678/"
                + MBeanFieldGroupRequiredErrorMessage.class.getCanonicalName());
        new WebDriverWait(driver, 30).until(VaadinConditions::ajaxCallsCompleted);

        WebElement txtField = driver.findElement(By.id("txtStreet"));
        Actions toolAct = new Actions(driver);
        toolAct.moveToElement(txtField).build().perform();
        Thread.sleep(1000);
        WebElement toolTipElement = driver.findElement(By.cssSelector(".v-app.v-overlay-container > .v-tooltip > .popupContent .v-errormessage > div > div"));

        assertThat(toolTipElement.getText(), is(expectedMessage));
    }

}
