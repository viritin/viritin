package org.vaadin.viritin.it;

import com.vaadin.annotations.Theme;
import com.vaadin.ui.Component;
import org.jsoup.safety.Whitelist;
import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.viritin.label.RichText;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 *
 * @author Matti Tahvonen
 */
@Theme("valo")
public class RichTextExample extends AbstractTest {

    @Override
    public Component getTestComponent() {
        
        // note that styles is stripped of by default
        RichText example1 = new RichText("<h1 style='color:red' class='foobar'>Jou!</h1>");

        // This one will tolerate quite a lot more
        RichText example2 = new RichText("<h1 style='color:red'>Jou!</h1>") {
            @Override
            public Whitelist getWhitelist() {
                return Whitelist.relaxed().addAttributes("h1",
                        "style");
            }
        };
        
        
        // This one will tolerate quite a lot more
        RichText example3 = new RichText().withMarkDown("This is **Markdown** formatted *text*");
        
        return new MVerticalLayout(example1, example2, example3);
    }

}
