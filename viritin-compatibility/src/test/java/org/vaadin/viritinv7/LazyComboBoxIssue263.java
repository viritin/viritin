package org.vaadin.viritinv7;

import com.vaadin.annotations.Theme;
import com.vaadin.v7.data.Validator;
import com.vaadin.v7.data.Validator.InvalidValueException;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.ui.Button;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.v7.ui.VerticalLayout;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.vaadin.viritin.it.AbstractTest;
import org.vaadin.viritinv7.fields.LazyComboBox;

@Theme("valo")
public class LazyComboBoxIssue263 extends AbstractTest {

    
    public static final int PAGE_LENGTH = 50;

    private class ListProvider implements LazyComboBox.FilterablePagingProvider<String>, LazyComboBox.FilterableCountProvider {

        private final List<String> backingList;
        private final int pageLength;

        public ListProvider(List<String> backingList, int pageLength) {
            this.backingList = backingList;
            this.pageLength = pageLength;
        }

        private List<String> getFilteredList(String filter) {
            return backingList.stream().filter(item ->
                    item.toLowerCase().indexOf(filter.toLowerCase()) != -1).collect(Collectors.toList());
        }

        @Override
        public int size(String filter) {
            return getFilteredList(filter).size();
        }

        @Override
        public List<String> findEntities(int firstRow, String filter) {
            List<String> filteredList = getFilteredList(filter);
            return filteredList.subList(firstRow, Math.min(filteredList.size(), firstRow + pageLength));
        }
    }

    private class SuperheroValidator implements Validator {

        @Override
        public void validate(Object o) throws InvalidValueException {
            if ("lorenzo".equals(o)) {
                throw new InvalidValueException("Not a superhero!");
            }
        }
    }

    @Override
    public Component getTestComponent() {
        final VerticalLayout layout = new VerticalLayout();

        List<String> superheroes = Arrays.asList("Superman", "Hulk", "Spiderman", "Batman", "lorenzo");

        ListProvider listProvider = new ListProvider(superheroes, PAGE_LENGTH);

        LazyComboBox<String> lazyComboBox = new LazyComboBox<>(
            String.class,
                listProvider,
                listProvider,
                PAGE_LENGTH
        );
        lazyComboBox.setCaption("LazyComboBox");
        SuperheroValidator validator = new SuperheroValidator();
        lazyComboBox.addValidator(validator);

        ComboBox comboBox = new ComboBox();
        comboBox.setCaption("ComboBox");
        superheroes.forEach(superhero -> comboBox.addItem(superhero));
        comboBox.addValidator(validator);

        Button button = new Button("Validate");
        button.addClickListener(event -> {
            validateRequiredField(lazyComboBox);
            validateRequiredField(comboBox);
        });

        layout.addComponents(lazyComboBox, comboBox, button);

        layout.setMargin(true);
        layout.setSpacing(true);

        return layout;
    }

    private void validateRequiredField(AbstractField<?> field) {
        try {
            field.setValidationVisible(false);
            try {
                field.validate();
            } catch (Validator.InvalidValueException e) {
                Notification.show(e.getMessage());
                field.setValidationVisible(true);
            }
        } catch (Validator.InvalidValueException e) {}
    }
    

}
