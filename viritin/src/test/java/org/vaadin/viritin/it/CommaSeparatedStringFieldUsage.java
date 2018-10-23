package org.vaadin.viritin.it;

import com.vaadin.data.Binder;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.viritin.fields.CommaSeparatedStringField;
import org.vaadin.viritin.fields.DoubleField;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 *
 * @author Matti Tahvonen
 */
public class CommaSeparatedStringFieldUsage extends AbstractTest {

    public static class Domain {

        private Collection<String> tags = new ArrayList<>();

        public Collection<String> getTags() {
            return tags;
        }

        public void setTags(Collection<String> tags) {
            this.tags = tags;
        }

        @Override
        public String toString() {
            return "Domain{" + "tags=" + tags + '}';
        }

    }

    private CommaSeparatedStringField<Collection<String>> tags = new CommaSeparatedStringField<>();

    @Override
    public Component getTestComponent() {
        
        final Domain domain = new Domain();
        
        Binder<Domain> b = new Binder<>(Domain.class);
        b.bindInstanceFields(this);
        b.setBean(domain);
        
        b.addValueChangeListener(e-> {
            Notification.show("Value change " + e.getValue() , Notification.Type.TRAY_NOTIFICATION);
        });

        Button show = new Button("Show value", new Button.ClickListener() {
            private static final long serialVersionUID = 5019806363620874205L;
            
            @Override
            public void buttonClick(Button.ClickEvent event) {
                Notification.show(domain.toString());
            }
        });


        return new MVerticalLayout(
                tags,
                show
        );

    }

}
