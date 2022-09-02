package org.vaadin.viritin.it;

import com.vaadin.ui.Component;
import org.jsoup.safety.Safelist;
import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.viritin.fields.LabelField;
import org.vaadin.viritin.label.RichText;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 *
 * @author Matti Tahvonen
 */
public class RichTextExample extends AbstractTest {

    @Override
    public Component getTestComponent() {

        // note that styles is stripped of by default
        RichText example1 = new RichText(
                "<h1 style='color:red' class='foobar'>Jou!</h1>");

        // This one will tolerate quite a lot more
        RichText example2 = new RichText("<h1 style='color:red'>Jou!</h1>") {
            @Override
            public Safelist getWhitelist() {
                return Safelist.relaxed().addAttributes("h1",
                        "style");
            }
        };

        // This one will tolerate quite a lot more
        RichText example3 = new RichText().withMarkDown(
                "This is **Markdown** formatted *text*");

        // RichText can also be used through LabelField 
        // (and it is with default settings)
        LabelField<Integer> lf = new LabelField<>();
        lf.setLabel(new RichText() {
            @Override
            public Safelist getWhitelist() {
                return Safelist.relaxed().addAttributes("h1",
                        "style");
            }
        });
        lf.setCaptionGenerator(i-> "<h1 style='color:blue'>"+i+"</h1>");
        lf.setValue(69);
 
        return new MVerticalLayout(example1, example2, example3, lf);
    }

}
