package org.vaadin.viritinv7;

import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.testdomain.Person;
import org.vaadin.viritin.testdomain.Service;
import org.vaadin.viritinv7.fields.MTable;

import com.vaadin.annotations.Theme;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;

/**
 *
 * @author Matti Tahvonen
 */
@Theme("valo")
public class MTableWithTraditionalPaging extends AbstractTest {

    private static final long serialVersionUID = -3645542297869930694L;

    /**
     * A quickly drafted version of pagination bar.
     */
    public static class PaginationBar extends MHorizontalLayout {

        private static final long serialVersionUID = 7799263034212965499L;

        private final ClickListener handler = new Button.ClickListener() {
            private static final long serialVersionUID = 5019806363620874205L;
            
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if (event.getButton() == first) {
                    currentPage = 0;
                } else if (event.getButton() == last) {
                    currentPage = pages - 1;
                } else if (event.getButton() == next) {
                    currentPage++;
                } else if (event.getButton() == previous) {
                    currentPage--;
                }
                updateState();
                listener.pageRequested(currentPage);
            }

        };

        private void updateState() {
            final boolean hasPrev = currentPage > 0;
            first.setEnabled(hasPrev);
            previous.setEnabled(hasPrev);
            final boolean hasNext = currentPage < pages - 1;
            last.setEnabled(hasNext);
            next.setEnabled(hasNext);
            status.setValue((currentPage + 1) + "/" + pages);
        }

        private void initButtons() {
            first = new MButton(VaadinIcons.FAST_BACKWARD, handler)
                    .withStyleName(ValoTheme.BUTTON_BORDERLESS);
            last = new MButton(VaadinIcons.FAST_FORWARD, handler)
                    .withStyleName(ValoTheme.BUTTON_BORDERLESS);
            next = new MButton(VaadinIcons.FORWARD, handler)
                    .withStyleName(ValoTheme.BUTTON_BORDERLESS);
            previous = new MButton(VaadinIcons.BACKWARDS, handler)
                    .withStyleName(ValoTheme.BUTTON_BORDERLESS);
        }

        public interface PagingListener {

            void pageRequested(int page);
        }

        private PagingListener listener;
        final private long size;
        private final int pageSize;
        private int currentPage;
        final private int pages;

        public long getSize() {
            return size;
        }

        public PagingListener getListener() {
            return listener;
        }

        public void setListener(PagingListener listener) {
            this.listener = listener;
        }

        private Button first, last, next, previous;
        private final Label status = new Label();

        public PaginationBar(PagingListener listener, int pageSize, long size) {
            this.listener = listener;
            this.size = size;
            this.pageSize = pageSize;
            pages = (int) (size / pageSize);
            initButtons();
            updateState();
            addComponents(first, previous, status, next, last);
            alignAll(Alignment.MIDDLE_CENTER);
            withFullWidth();
        }

    }

    @Override
    public Component getTestComponent() {

        final int pageSize = 10;
        final long results = Service.count();

        final MTable<Person> table = new MTable<>(Person.class);
        table.setPageLength(10);
        // set the initial content
        table.setBeans(Service.findAll(0, pageSize));

        // create a paginationBar
        PaginationBar paginationBar = new PaginationBar(new PaginationBar.PagingListener() {
            @Override
            public void pageRequested(int page) {
                // reset the proper content to table 
                // when page is changed by user
                int firstResult = page * pageSize;
                table.setBeans(Service.findAll(firstResult, pageSize));
            }
        }, pageSize, results);

        return new MVerticalLayout(table,paginationBar).withWidth("");
    }

}
