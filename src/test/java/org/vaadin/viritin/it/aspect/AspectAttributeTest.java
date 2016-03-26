package org.vaadin.viritin.it.aspect;

import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.vaadin.addonhelpers.automated.AbstractWebDriverCase;
import org.vaadin.addonhelpers.automated.VaadinConditions;

public class AspectAttributeTest extends AbstractWebDriverCase {


    public AspectAttributeTest() {
        FirefoxProfile profile = new FirefoxProfile();
        WebDriver webDriver = new FirefoxDriver(profile);
        startBrowser(webDriver);
    }

    @Test
    public void testLanguageByBrowser() throws InterruptedException {
        driver.navigate().to(
                "http://localhost:5678/"
                        + MTableLazyLoadingWithEntityAspect.class.getCanonicalName());
        new WebDriverWait(driver, 30).until(VaadinConditions
                .ajaxCallsCompleted());

        //Thread.sleep(10000);
    }
}
