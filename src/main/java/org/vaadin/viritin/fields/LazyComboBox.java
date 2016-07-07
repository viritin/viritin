package org.vaadin.viritin.fields;

import com.vaadin.data.Container;
import com.vaadin.data.Container.Filterable;
import com.vaadin.data.util.filter.UnsupportedFilterException;
import com.vaadin.server.Resource;
import com.vaadin.shared.Version;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.ComboBox;
import org.apache.commons.lang3.ObjectUtils;
import org.vaadin.viritin.LazyList;
import org.vaadin.viritin.LazyList.CountProvider;
import org.vaadin.viritin.ListContainer;

import java.util.Collection;
import java.util.List;
import org.vaadin.viritin.util.HtmlElementPropertySetter;

/**
 * This class tries to provide a simple lazy loading connection form ComboBox to
 * typical service layers. See Viritin projects tests for example usage.
 *
 * @author Matti Tahvonen
 * @param <T> the type of entities listed in the combobox
 */
public class LazyComboBox<T> extends TypedSelect<T> {

    private String currentFilter;

    /**
     * Interface via the LazyComboBox communicates with the "backend"
     *
     * @param <T> The type of the objects in the list
     */
    public interface FilterablePagingProvider<T> {

        /**
         * Fetches one "page" of entities form the backend. The amount
         * "maxResults" should match with the value configured for the LazyList
         *
         * @param firstRow the index of first row that should be fetched
         * @param filter the filter typed in by the user
         * @return a sub list from given first index
         */
        public List<T> findEntities(int firstRow, String filter);
    }

    public interface FilterableCountProvider {

        public int size(String filter);
    }

    private LazyList<T> piggybackLazyList;

    /**
     * Instantiates a memory and CPU efficient ComboBox, typically wired to EJB
     * or Spring Data repository. By default page size of
     * LazyList.DEFAULT_PAGE_SIZE (30) is used.
     *
     * @param elementType the type of options in the select
     * @param filterablePageProvider the interface via entities are fetched
     * @param countProvider the interface via the count of items is detected
     */
    public LazyComboBox(Class<T> elementType,
            final FilterablePagingProvider filterablePageProvider,
            final FilterableCountProvider countProvider) {
        this(elementType, filterablePageProvider, countProvider,
                LazyList.DEFAULT_PAGE_SIZE);
    }

    /**
     * Instantiates a memory and CPU efficient ComboBox, typically wired to EJB
     * or Spring Data repository.
     *
     * @param elementType the type of options in the select
     * @param filterablePageProvider the interface via entities are fetched
     * @param countProvider the interface via the count of items is detected
     * @param pageLength the maximum page size to be used with service calls
     */
    public LazyComboBox(Class<T> elementType,
            final FilterablePagingProvider filterablePageProvider,
            final FilterableCountProvider countProvider, int pageLength) {
        this();
        initList(elementType, filterablePageProvider, countProvider, pageLength);
    }

    protected final ComboBox initList(
            Class<T> elementType,
            final FilterablePagingProvider filterablePageProvider,
            final FilterableCountProvider countProvider1, int pageLength) {
        // piggyback to simple paging provider
        piggybackLazyList = new LazyList<T>(new LazyList.PagingProvider() {

            @Override
            public List findEntities(int firstRow) {
                return filterablePageProvider.findEntities(firstRow,
                        getCurrentFilter());
            }
        },
                new LazyList.CountProvider() {
                    @Override
                    public int size() {
                        return countProvider1.size(getCurrentFilter());
                    }
                }, pageLength);
        final ComboBox comboBox = new ComboBox() {
            @SuppressWarnings("unchecked")
            @Override
            public String getItemCaption(Object itemId) {
                return LazyComboBox.this.getCaption((T) itemId);
            }

            @Override
            public Resource getItemIcon(Object itemId) {
                if(getIconGenerator() != null) {
                    return LazyComboBox.this.getIcon((T) itemId);
                }
                return super.getItemIcon(itemId);
            }

            @Override
            protected Container.Filter buildFilter(String filterString,
                    FilteringMode filteringMode) {

                if (getValue() != null && getItemCaption(getValue()).equals(
                        filterString)) {
                    // Yes, the combobox calls filtering with the item caption when opening popup with selection,
                    // ignore that, fall back to ""
                    filterString = "";
                }

                /*
                 * Save for the thinner interface.
                 */
                if (ObjectUtils.notEqual(currentFilter, filterString)) {
                    currentFilter = filterString;
                    piggybackLazyList.reset();
                }
                return super.buildFilter(filterString, filteringMode);
            }

            {
                // This is needed for the lazy loading to work for some reason
                setItemCaptionMode(ItemCaptionMode.PROPERTY);
                setItemCaptionPropertyId("FAKE");
            }

        };

        setBic(new DummyFilterableListContainer<T>(elementType,
                piggybackLazyList));
        comboBox.setContainerDataSource(getBic());
        if(Version.getMajorVersion() >= 7  && Version.getMinorVersion() >= 5 ) {
            // broken in earler Vaadin versions, so skip otherwise
            // Set to false for much better performance if selection is in
            // large index
            comboBox.setScrollToSelectedItem(false);
        }
        
        fixComboBoxVaadinIssue16647(comboBox);
        setSelectInstance(comboBox);

        return comboBox;
    }

    public static void fixComboBoxVaadinIssue16647(final ComboBox comboBox) {
        HtmlElementPropertySetter heps = new HtmlElementPropertySetter(comboBox);
        heps.setProperty("./input", "autocorrect", "off");
        heps.setProperty("./input", "autocomplete", "off");
        heps.setProperty("./input", "autocapitalize", "off");
    }

    /**
     * Instantiates a new LazyCombobox. Be sure to call
     * preparePiggybackLazyList
     *
     */
    protected LazyComboBox() {
        setCaptionGenerator(new CaptionGenerator<T>() {
            @Override
            public String getCaption(T option) {
                return option.toString();
            }
        });
    }
    
    /**
     * Refreshes entities cached in the lazy backing list.
     */
    public void refresh() {
        piggybackLazyList.reset();
        markAsDirty();
    }

    public String getCurrentFilter() {
        return currentFilter;
    }

    /**
     * Just fake for ComboBox that we implement Filterable, handle filtering
     * with simpler interfaces.
     *
     * @param <T> the type of the elements in the list
     */
    private static class DummyFilterableListContainer<T> extends ListContainer<T>
            implements Filterable {

        public DummyFilterableListContainer(Class<T> type,
                Collection<T> backingList) {
            super(type, backingList);
        }

        @Override
        public void addContainerFilter(Filter filter) throws UnsupportedFilterException {
            // Who cares
        }

        @Override
        public void removeContainerFilter(Filter filter) {
            /// So not
        }

        @Override
        public void removeAllContainerFilters() {
            // Why would I bother
        }

        @Override
        public Collection<Filter> getContainerFilters() {
            // Why?
            return null;
        }

        @Override
        public boolean containsId(Object itemId) {
            // Vaadin is pretty eager to do "sanity checks", which is not good
            // for performance. This may cause a full DB seek, so just fake
            // that all values are there. 
            return true;
        }

    }

    @Override
    public LazyComboBox<T> withWidth(String width) {
        return (LazyComboBox<T>) super.withWidth(width);
    }

    @Override
    public LazyComboBox<T> withWidth(float width, Unit unit) {
        return (LazyComboBox<T>) super.withWidth(width, unit);
    }

    @Override
    public LazyComboBox<T> withReadOnly(boolean readOnly) {
        return (LazyComboBox<T>) super.withReadOnly(readOnly);
    }

    @Override
    public LazyComboBox<T> withFullWidth() {
        return (LazyComboBox<T>) super.withFullWidth();
    }

    @Override
    public LazyComboBox<T> setNullSelectionAllowed(boolean nullAllowed) {
        return (LazyComboBox<T>) super.setNullSelectionAllowed(nullAllowed);
    }

    @Override
    public LazyComboBox<T> setCaptionGenerator(
            CaptionGenerator<T> captionGenerator) {
        return (LazyComboBox<T>) super.setCaptionGenerator(captionGenerator);
    }
    
    @Override
    public LazyComboBox<T> setIconGenerator(IconGenerator<T> generator) {
        return (LazyComboBox<T>) super.setIconGenerator(generator);
    }

    @Override
    public LazyComboBox<T> addMValueChangeListener(
            MValueChangeListener<T> listener) {
        return (LazyComboBox<T>) super.addMValueChangeListener(listener);
    }

    @Override
    public LazyComboBox setFieldType(Class<T> type) {
        return (LazyComboBox) super.setFieldType(type);
    }

    @Override
    public LazyComboBox<T> withCaption(String caption) {
        return (LazyComboBox<T>) super.withCaption(caption);
    }

}
