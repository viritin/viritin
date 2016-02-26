package org.vaadin.viritin.util;

import com.vaadin.annotations.Theme;
import com.vaadin.server.AbstractJavaScriptExtension;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.JavaScript;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;

/**
 * A simple extension to set custom properties for the html elements used in
 * Vaadin components. By default element are set to the Root element, but an
 * xpath expression can be given for setting the property to any element.
 * <p>
 * Note, be careful when using this add-on as it involves web "real" 
 * development. Consider hiding all usage into your custom components.
 * 
 * @author Matti Tahvonen
 */
@com.vaadin.annotations.JavaScript("viritin.js")
public class HtmlElementPropertySetter extends AbstractJavaScriptExtension {

    public HtmlElementPropertySetter(AbstractComponent c) {
        extend(c);
    }

    public void setProperty(String name, Object value) {
        callFunction("xpathset", false, null, name, value);
    }

    public void setProperty(String xpath, String name, Object value) {
        callFunction("xpathset", false, xpath, name, value);
    }

    public void setJavaScriptEventHandler(String name, String js) {
        callFunction("xpathset", true, null, name, js);
    }

    public void setJavaScriptEventHandler(String xpath, String name, String js) {
        callFunction("xpathset", true, xpath, name, js);
    }

}
