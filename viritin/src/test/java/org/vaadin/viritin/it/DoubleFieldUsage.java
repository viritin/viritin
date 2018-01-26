package org.vaadin.viritin.it;

import com.vaadin.data.Binder;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import java.util.Objects;

import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.viritin.fields.DoubleField;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.util.HtmlElementPropertySetter;

/**
 *
 * @author Matti Tahvonen
 */
public class DoubleFieldUsage extends AbstractTest {

    private static final long serialVersionUID = -1841759044439831663L;

    public static class Domain {

        private Double doubleUsingTextField;
        private Double basicDouble;

        public Double getDoubleUsingTextField() {
            return doubleUsingTextField;
        }

        public void setDoubleUsingTextField(Double doubleUsingTextField) {
            this.doubleUsingTextField = doubleUsingTextField;
        }

        public Double getBasicDouble() {
            return basicDouble;
        }

        public void setBasicDouble(Double basicDouble) {
            this.basicDouble = basicDouble;
        }

        @Override
        public String toString() {
            return "Domain{" + "doubleUsingTextField=" + doubleUsingTextField + ", basicDouble=" + basicDouble + '}';
        }
        
        



    }

    private final TextField doubleUsingTextField = new TextField("Integer with basic TextField");
    private final DoubleField basicDouble = new DoubleField().withCaption("Double field");

    @Override
    public Component getTestComponent() {
        
        final Domain domain = new Domain();
        
        Binder<Domain> b = new Binder<>(Domain.class);
        b.forMemberField(doubleUsingTextField).withConverter(toModel->Double.parseDouble(toModel), toPresentation -> Objects.toString(toPresentation));
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

        Button toggleVisible = new Button("Toggle visibility",
                new Button.ClickListener() {
            private static final long serialVersionUID = 5019806363620874205L;
            @Override
            public void buttonClick(Button.ClickEvent event) {
                basicDouble.setVisible(!basicDouble.isVisible());
            }
        });

        return new MVerticalLayout(
                basicDouble,
                doubleUsingTextField,
                show,
                toggleVisible
        );

    }

}
