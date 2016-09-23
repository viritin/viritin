package org.vaadin.viritin;

import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Table;
import java.util.Collection;
import java.util.Collections;
import org.vaadin.viritin.fields.MTable;
import org.vaadin.viritin.fields.MValueChangeEvent;
import org.vaadin.viritin.fields.MValueChangeListener;

// JUnit tests here
public class TableUsageExample {

    public void tableAsBeanSelector() {
        Table table = new Table();
        BeanItemContainer<Entity> beanItemContainer = new BeanItemContainer<>(Entity.class);
        beanItemContainer.addAll(findBeans());
        table.setContainerDataSource(beanItemContainer);
        table.setVisibleColumns("property", "another");
        table.setColumnHeaders("Property 1", "Second");
        table.setSelectable(true);
        table.setImmediate(true);
        table.addValueChangeListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                Entity entity = (Entity) event.getProperty().getValue();
                editEntity(entity);
            }
        });

        MTable<Entity> t = new MTable(findBeans())
                .withProperties("property", "another")
                .withColumnHeaders("Property 1", "Second");
        t.addMValueChangeListener(new MValueChangeListener<Entity>() {

            private static final long serialVersionUID = 382491506910830168L;

            @Override
            public void valueChange(MValueChangeEvent<Entity> event) {
                editEntity(event.getValue());
            }
        });

    }

    public static class Entity {

        private String another;

        private String property;

        public String getAnother() {
            return another;
        }

        public void setAnother(String another) {
            this.another = another;
        }

        public String getProperty() {
            return another;
        }

        public void setProperty(String property) {
            this.another = property;
        }
    }

    public Collection<Entity> findBeans() {
        return Collections.singletonList(new Entity());
    }

    private void editEntity(Entity emtity) {
        // NOP
    }
}
