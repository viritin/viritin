/*
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.vaadin.viritin.it.aspect;

import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.vaadin.addonhelpers.automated.AbstractWebDriverCase;
import org.vaadin.addonhelpers.automated.VaadinConditions;

public class AspectAttributeTest extends AbstractWebDriverCase {


    public AspectAttributeTest() {
        WebDriver webDriver = new PhantomJSDriver();
        startBrowser(webDriver);
    }

    @Test
    @Ignore(value = "Argh... travis, selenium, crossplatform, perkele")
    public void testLanguageByBrowser() throws InterruptedException {
        driver.navigate().to(
                "http://localhost:5678/"
                        + MTableLazyLoadingWithEntityAspect.class.getCanonicalName());
        new WebDriverWait(driver, 30).until(VaadinConditions::
                ajaxCallsCompleted);

        //Thread.sleep(10000);
    }
}
