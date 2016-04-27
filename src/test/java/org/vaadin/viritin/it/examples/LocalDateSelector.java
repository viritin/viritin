package org.vaadin.viritin.it.examples;

import com.vaadin.annotations.Theme;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.viritin.fields.TypedSelect;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 *
 * @author Matti Tahvonen
 */
@Theme("valo")
public class LocalDateSelector extends AbstractTest {
    
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    List<LocalDate> dates = Arrays.asList(
            LocalDate.now(), 
            LocalDate.now().minusDays(1),
            LocalDate.now().plusDays(1)
    );

    @Override
    public Component getTestComponent() {

        Label value = new Label("Value:");

        TypedSelect<LocalDate> field = new TypedSelect<>();
        field.setOptions(dates);
        field.setCaptionGenerator(localDate -> localDate.format(formatter));

        field.addMValueChangeListener(e -> value.setValue(
                e.getValue().toString()));
        return new MVerticalLayout(field, value);
    }

}
