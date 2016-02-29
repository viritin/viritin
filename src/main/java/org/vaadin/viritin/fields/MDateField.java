package org.vaadin.viritin.fields;

import com.vaadin.data.Property;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.DateField;
import java.util.Date;
import java.util.Map;

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

    public enum InitialTimeMode {
        START_OF_DAY, END_OF_DAY, NOW
    }

    private InitialTimeMode initialTimeMode;

    /**
     * Flag to tell if setting the initial (from null to something) value by the
     * end user
     */
    private boolean settingInitialValue;

    /**
     * @return the method how value of the time part is to be initialized when 
     * resolution setting don't show time.
     */
    public InitialTimeMode getInitialTimeMode() {
        return initialTimeMode;
    }

    /**
     * 
     * @param initialTimeMode the method how value of the time part is to be initialized when 
     * resolution setting don't show time.
     */
    public void setInitialTimeMode(InitialTimeMode initialTimeMode) {
        this.initialTimeMode = initialTimeMode;
    }

    @Override
    public void changeVariables(Object source, Map<String, Object> variables) {
        if (getValue() == null) {
            settingInitialValue = true;
        }
        super.changeVariables(source, variables);
        settingInitialValue = false;
    }

    @Override
    protected void setValue(Date newValue, boolean repaintIsNotNeeded) throws ReadOnlyException {
        if (settingInitialValue && getResolution().ordinal() > Resolution.HOUR.ordinal()) {
            if (getInitialTimeMode() == InitialTimeMode.START_OF_DAY) {
                newValue.setHours(0);
                newValue.setMinutes(0);
                newValue.setSeconds(0);
                newValue.setTime(
                        newValue.getTime() - newValue.getTime() % 1000l);
            } else if (getInitialTimeMode() == InitialTimeMode.END_OF_DAY) {
                newValue.setHours(23);
                newValue.setMinutes(59);
                newValue.setSeconds(59);
                newValue.setTime(
                        newValue.getTime() - newValue.getTime() % 1000l + 999l);
            }
        }
        super.setValue(newValue, repaintIsNotNeeded);
    }

    public MDateField withIcon(Resource icon) {
        setIcon(icon);
        return this;
    }

    public MDateField withResolution(Resolution resolution) {
        setResolution(resolution);
        return this;
    }

    public MDateField withStyleName(String... styleNames) {
        for (String styleName : styleNames) {
            addStyleName(styleName);
        }
        return this;
    }

    public MDateField withRequired(boolean required) {
        setRequired(required);
        return this;
    }

    public MDateField withRequiredError(String requiredError) {
        setRequiredError(requiredError);
        return this;
    }

}
