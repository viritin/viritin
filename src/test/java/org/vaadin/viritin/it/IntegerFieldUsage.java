package org.vaadin.viritin.it;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.viritin.BeanBinder;
import org.vaadin.viritin.MBeanFieldGroup;
import org.vaadin.viritin.fields.IntegerField;
import org.vaadin.viritin.fields.IntegerSliderField;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 *
 * @author Matti Tahvonen
 */
public class IntegerFieldUsage extends AbstractTest {

    public static class Domain {

        private Integer normalInteger;
        private Integer integer;
        private int intti = 2;
        @Min(10)
        @Max(100)
        private Integer validatedInteger;

        @NotNull
        @Min(5)
        @Max(55)
        private int slider = 50;

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

        public int getSlider() {
            return slider;
        }

        public void setSlider(int slider) {
            this.slider = slider;
        }

        public Integer getNormalInteger() {
            return normalInteger;
        }

        public void setNormalInteger(Integer normalInteger) {
            this.normalInteger = normalInteger;
        }

        @Override
        public String toString() {
            return "Domain{" + "normalInteger=" + normalInteger + ", integer=" + integer + ", intti=" + intti + ", validatedInteger=" + validatedInteger + ", slider=" + slider + '}';
        }
        
        
    }

    private TextField normalInteger = new MTextField().withCaption("Integer with basic TextField");
    private IntegerField integer = new IntegerField().withCaption("Integer");
    private IntegerField intti = new IntegerField().withCaption("int");
    private IntegerField validatedInteger = new IntegerField().withCaption(
            "validated");
    private IntegerSliderField slider = new IntegerSliderField()
            .withCaption("Slider")
            .withStep(5) // .withMax(69) // Set automatically from BeanValidation annotations
            // .withMin(-69) // Set automatically from BeanValidation annotations
            ;

    @Override
    public Component getTestComponent() {

        final Domain domain = new Domain();

        BeanBinder.bind(domain, this).withEagerValidation(
                new MBeanFieldGroup.FieldGroupListener<Domain>() {

            boolean wasvalid = true;

            @Override
            public void onFieldGroupChange(
                    MBeanFieldGroup<Domain> beanFieldGroup) {
                if (wasvalid != beanFieldGroup.isValid()) {

                    if (beanFieldGroup.isValid()) {
                        Notification.show("Bean is now valid!");
                    } else {
                        Notification.show("Bean is invalid!");
                    }
                    wasvalid = beanFieldGroup.isValid();
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
        slider.setInvalidCommitted(true);

        return new MVerticalLayout(
                integer,
                intti,
                validatedInteger,
                slider,
                normalInteger,
                show,
                toggleVisible
        );

    }

}
