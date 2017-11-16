package org.vaadin.viritinv7.locale;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import java.util.Arrays;
import java.util.Collection;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.vaadin.addonhelpers.automated.AbstractWebDriverCase;
import org.vaadin.addonhelpers.automated.VaadinConditions;
import org.vaadin.addonhelpers.components.VaadinComboBox;

@RunWith(Parameterized.class)
public class VaadinLocaleTest extends AbstractWebDriverCase {
    private final String expectedLanguage;

    @Parameters
    public static Collection languages() {
        return Arrays.asList(new Object[][] {
            { "en", "English" },
                { "de", "Deutsch" }, 
            // { "en-us", "English" }, // broken by matti :-(
                // { "es", "English" } // should fall back to English?
        });

    }

    public VaadinLocaleTest(String language, String expectedLanuage) {
        this.expectedLanguage = expectedLanuage;
        FirefoxProfile profile = new FirefoxProfile();
        profile.setPreference("intl.accept_languages", language);
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.setProfile(profile);
        WebDriver webDriver = new FirefoxDriver(firefoxOptions);
        startBrowser(webDriver);
    }

    @Test
    @Ignore("See if can be run with phantomjs and/or update to geckodriver")
    public void testLanguageByBrowser() {
        driver.navigate().to(
                "http://localhost:5678/"
                        + V7VaadinLocaleDemo.class.getCanonicalName());
        new WebDriverWait(driver, 30).until(VaadinConditions::ajaxCallsCompleted);

        VaadinComboBox languageSelectionBox = new VaadinComboBox(
                driver.findElement(By.id("language-selection")));

        assertThat(languageSelectionBox.getValue(), is(expectedLanguage));
    }
}
