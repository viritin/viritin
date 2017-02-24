package org.vaadin.viritin.it;

import com.vaadin.annotations.Theme;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.Label;
import java.time.LocalDate;
import java.util.function.Consumer;

import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.viritin.fields.TypedSelect;
import org.vaadin.viritin.layouts.MGridLayout;

/**
 *
 * @author Niki Hansche
 */
@Theme("valo")
@SuppressWarnings("serial")
public class TypedSelectWithBean extends AbstractTest {

    @Override
    public Component getTestComponent() {
        final MGridLayout layout = new MGridLayout(2, 4);

        final ComboBox comboBox = new ComboBox("Combobox");

        final TypedSelect<Quarter> typedComboBox = new TypedSelect<>(Quarter.class)
                .withCaption("Typed combobox")
                .withSelectType(ComboBox.class);

        Quarter.generateQuarters((q) -> {
            comboBox.addItem(q);
            typedComboBox.addOption(q);
        });

        final Quarter thisQuarter = new Quarter(LocalDate.now().getYear(), LocalDate.now().getMonthValue() / 4 + 1);

        comboBox.setValue(thisQuarter);
        typedComboBox.setValue(thisQuarter);

        layout.add(
                comboBox,
                typedComboBox,
                new Label(comboBox.getValue().toString()),
                new Label(typedComboBox.getValue().toString()),
                new Label(((Boolean) comboBox.getValue().equals(thisQuarter)).toString()),
                new Label(((Boolean) typedComboBox.getValue().equals(thisQuarter)).toString())
        );
        layout.addComponent(new Label("I’d expect both Comboboxes to show the same value after load."), 0, 3, 1, 3);

        return layout;
    }

    private static class Quarter {

        private final int year;
        private final int quarter;

        Quarter(final int year, final int quarter) {
            this.year = year;
            this.quarter = quarter;
        }

        public static void generateQuarters(Consumer<Quarter> consumer) {
            for (int i = 2020; i >= 2009; i--) {
                for (int j = 4; j >= 1; j--) {
                    consumer.accept(new Quarter(i, j));
                }
            }
        }

        public int getYear() {
            return year;
        }

        public int getQuarter() {
            return quarter;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 37 * hash + this.year;
            hash = 37 * hash + this.quarter;
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }

            final Quarter other = (Quarter) obj;
            if (this.year != other.year) {
                return false;
            }
            return this.quarter == other.quarter;
        }

        @Override
        public String toString() {
            return String.format("%d / %d", getQuarter(), getYear());
        }

    }
}
