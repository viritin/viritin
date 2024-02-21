package org.vaadin.viritin.it;

import java.io.File;
import java.io.FileOutputStream;

import org.junit.After;
import org.junit.Rule;
import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.github.webdriverextensions.WebDriverExtensionsContext;
import com.github.webdriverextensions.internal.junitrunner.DriverPathLoader;

/**
 * Base class for webDriver test. If you need a local test server use
 * {@link AbstractWebDriverCase}.
 * 
 */
public class AbstractBaseWebDriverCase {

    protected WebDriver driver;
    @Rule
    public ScreenshotTestRule screenshotTestRule = new ScreenshotTestRule();

    public AbstractBaseWebDriverCase() {
        super();
        DriverPathLoader.loadDriverPaths(null);
    }

    @After
    public void tearDown() {

    }

    /**
     * Starts a new firefox browser.
     */
    protected void startBrowser() {
        startBrowser(new FirefoxDriver());
    }

    /**
     * Starts a new browser.
     * 
     * @param driver
     *            the WebDriver instance to use
     */
    protected void startBrowser(WebDriver driver) {
        if (this.driver != null) {
            this.driver.quit();
        }
        this.driver = driver;
        WebDriverExtensionsContext.setDriver(driver);
    }

    protected void clickNotification() {
        try {
            WebElement notification = driver.findElement(By
                    .className("v-Notification"));
            notification.click();
            // wait for animations...
            sleep(1000);
        } catch (NoSuchElementException e) {
            throw e;
        }

    }

    protected void waitForLoading() {
        sleep(1000);
        // driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        (new WebDriverWait(driver, 20)).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver d) {
                boolean stopWait = false;
                try {
                    WebElement findElement = d.findElement(By
                            .cssSelector(".v-loading-indicator"));
                    if (!findElement.isDisplayed()) {
                        sleep(200);
                        stopWait = true;
                    }
                } catch (NoSuchElementException e) {
                    stopWait = true;
                }
                return stopWait;
            }
        });
    }

    protected void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    class ScreenshotTestRule implements MethodRule {
        @Override
        public Statement apply(final Statement statement,
                final FrameworkMethod frameworkMethod, final Object o) {
            return new Statement() {
                @Override
                public void evaluate() throws Throwable {
                    try {
                        statement.evaluate();
                    } catch (Throwable t) {
                        captureScreenshot(frameworkMethod.getName());
                        // rethrow to allow the failure to be reported to JUnit
                        throw t;
                    } finally {
                        // Closing the webdriver
                        after();
                    }
                }

                public void after() {
                    // We can't close the webdriver in @After annotated method,
                    // because the method is called before this rule
                    if (driver != null) {
                        driver.quit();
                    }
                }

                public void captureScreenshot(String fileName) {
                    try {
                        if (driver instanceof TakesScreenshot) {
                            // Insure directory is there
                            new File("target/surefire-reports/screenshots/")
                                    .mkdirs();
                            FileOutputStream out = new FileOutputStream(
                                    "target/surefire-reports/screenshots/"
                                            + fileName + ".png");
                            out.write(((TakesScreenshot) driver)
                                    .getScreenshotAs(OutputType.BYTES));
                            out.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                }
            };
        }
    }
}