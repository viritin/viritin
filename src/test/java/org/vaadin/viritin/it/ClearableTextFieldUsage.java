package org.vaadin.viritin.it;

import com.vaadin.ui.Component;
import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.viritin.fields.ClearableTextField;
import org.vaadin.viritin.layouts.MPanel;

/**
 *
 * @author Niki
 */
public class ClearableTextFieldUsage extends AbstractTest {

    @Override
    public Component getTestComponent() {

        ClearableTextField ctf = new ClearableTextField();
        ctf.setInputPrompt("Enter text");
        ctf.setValue("Some Text");

        return new MPanel()
                .withCaption("ClearableTextField")
                .withDescription("Click the X to clear…")
                .withFullWidth()
                .withFullHeight()
                .withContent(ctf);
    }
}
