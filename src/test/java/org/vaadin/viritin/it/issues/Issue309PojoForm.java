package org.vaadin.viritin.it.issues;

import com.vaadin.server.VaadinRequest;
import org.vaadin.viritin.fields.IntegerField;
import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.form.AbstractForm.ResetHandler;

import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.UI;

public class Issue309PojoForm extends UI {


    public Issue309PojoForm() {
    }

    @Override
    protected void init(VaadinRequest request) {
        AbstractForm<Pojo> form = new AbstractForm<Pojo>(Pojo.class) {

            private static final long serialVersionUID = 1251886098275380006L;
            IntegerField myInteger = new IntegerField("My Integer");

            @Override
            protected Component createContent() {
                FormLayout layout = new FormLayout(myInteger, getToolbar());
                return layout;
            }
        };

        form.setResetHandler((Pojo entity) -> {
            form.setEntity(null);
        });

        form.setEntity(new Pojo());
        setContent(form);
    }

    public class Pojo {

        private Integer myInteger;

        public Pojo() {
            myInteger = null;
        }

        public Integer getMyInteger() {
            return myInteger;
        }

        public void setMyInteger(Integer myInteger) {
            this.myInteger = myInteger;
        }
    }
}
