package org.vaadin.viritin.it;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.net.MalformedURLException;
import java.util.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.*;
import org.junit.runners.Parameterized.Parameters;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.vaadin.addonhelpers.automated.*;
import org.vaadin.addonhelpers.components.VaadinComboBox;

@RunWith(Parameterized.class)
public class VaadinLocaleTest extends AbstractWebDriverCase {
    private String expectedLanguage;

    @Parameters
    public static Collection languages() {
        return Arrays.asList(new Object[][] { { "en-us", "English" },
                { "de", "Deutsch" }, { "fi", "English" } });

    }

    public VaadinLocaleTest(String language, String expectedLanuage) {
        this.expectedLanguage = expectedLanuage;
        FirefoxProfile profile = new FirefoxProfile();
        profile.setPreference("intl.accept_languages", language);
        WebDriver webDriver = new FirefoxDriver(profile);
        startBrowser(webDriver);
    }

    @Test
    public void testLanguageByBrowser() {
        driver.navigate().to(
                "http://localhost:5678/"
                        + VaadinLocaleDemo.class.getCanonicalName());
        new WebDriverWait(driver, 30).until(VaadinConditions
                .ajaxCallsCompleted());

        VaadinComboBox languageSelectionBox = new VaadinComboBox(
                driver.findElement(By.id("language-selection")));

        assertThat(languageSelectionBox.getValue(), is(expectedLanguage));
    }
}
