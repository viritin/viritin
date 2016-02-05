package org.vaadin.viritin.it;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.viritin.BeanBinder;
import org.vaadin.viritin.MBeanFieldGroup;
import org.vaadin.viritin.fields.IntegerField;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 *
 * @author Matti Tahvonen
 */
public class IntegerFieldUsage extends AbstractTest {

    public static class Domain {

        private Integer integer;
        private int intti = 2;
        @NotNull
        @Min(10)
        @Max(100)
        private Integer validatedInteger;

        public Integer getValidatedInteger() {
            return validatedInteger;
        }

        public void setValidatedInteger(Integer validatedInteger) {
            this.validatedInteger = validatedInteger;
        }

        public Integer getInteger() {
            return integer;
        }

        public void setInteger(Integer integer) {
            this.integer = integer;
        }

        public int getIntti() {
            return intti;
        }

        public void setIntti(int intti) {
            this.intti = intti;
        }

        @Override
        public String toString() {
            return "Domain{" + "integer=" + integer + ", intti=" + intti + ", validatedInteger=" + validatedInteger + '}';
        }

    }

    private IntegerField integer = new IntegerField().withCaption("Integer");
    private IntegerField intti = new IntegerField().withCaption("int");
    private IntegerField validatedInteger = new IntegerField().withCaption(
            "validated");

    @Override
    public Component getTestComponent() {

        final Domain domain = new Domain();

        BeanBinder.bind(domain, this).withEagerValidation(
                new MBeanFieldGroup.FieldGroupListener<Domain>() {
            @Override
            public void onFieldGroupChange(
                    MBeanFieldGroup<Domain> beanFieldGroup) {
                if (beanFieldGroup.isValid()) {
                    Notification.show("Bean is now valid!");
                } else {
                    Notification.show("Bean is invalid!");
                }
            }
        });

        Button show = new Button("Show value", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                Notification.show(domain.toString());
            }
        });

        Button toggleVisible = new Button("Toggle visibility",
                new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                integer.setVisible(!integer.isVisible());
            }
        });

        // Put invalid value to the backing bean, otherwise demo might appear
        // broken
        validatedInteger.setInvalidCommitted(true);

        return new MVerticalLayout(
                integer, 
                intti, 
                validatedInteger, 
                show,
                toggleVisible
        );

    }

}
