
package org.vaadin.viritin.fields;

import com.vaadin.data.Property;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.DateField;
import java.util.Date;

/**
 *
 * @author Matti Tahvonen
 */
public class MDateField extends DateField {

    public MDateField() {
    }

    public MDateField(String caption) {
        super(caption);
    }

    public MDateField(String caption, Property dataSource) {
        super(caption, dataSource);
    }

    public MDateField(Property dataSource) throws IllegalArgumentException {
        super(dataSource);
    }

    public MDateField(String caption, Date value) {
        super(caption, value);
    }
    
    public MDateField withIcon(Resource icon) {
        setIcon(icon);
        return this;
    }
    
    public MDateField withResolution(Resolution resolution) {
        setResolution(resolution);
        return this;
    }
    
    

}
