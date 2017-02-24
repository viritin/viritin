
package org.vaadin.viritin;

import com.vaadin.v7.data.util.ObjectProperty;
import com.vaadin.v7.ui.TextField;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.vaadin.viritin.fields.ElementCollectionField;
import org.vaadin.viritin.fields.MTextField;

/**
 *
 * @author Matti Tahvonen
 */
public class ElementCollectionFieldTest {
    
    public static class Bean {
        private String property1;

        public String getProperty1() {
            return property1;
        }

        public void setProperty1(String property1) {
            this.property1 = property1;
        }
    }
    
    public static class BeanEditor {
        public TextField property1 = new MTextField();
    }
    
    @Test
    public void testDisabledRemovingButton() {
        ElementCollectionField<Bean> elementCollectionField = 
                new ElementCollectionField<>(Bean.class, BeanEditor.class)
                .setVisibleProperties(Arrays.asList("property1"))
                ;
        
        List<Bean> list = new ArrayList<>();
        
        ObjectProperty objectProperty = new ObjectProperty(list);
        elementCollectionField.setPropertyDataSource(objectProperty);
        
        elementCollectionField.setAllowRemovingItems(false);
        elementCollectionField.addElement(new Bean());
                
    }

}
