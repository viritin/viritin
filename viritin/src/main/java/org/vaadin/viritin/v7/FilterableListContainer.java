package org.vaadin.viritin.v7;

import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Container.Filterable;
import com.vaadin.v7.data.Item;
import com.vaadin.v7.data.util.filter.UnsupportedFilterException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.DynaClass;

/**
 * A filterable ({@link Container.Filterable}) version of {@link ListContainer}.
 *
 * @author John Ryan
 *
 * @param <T> the type of beans in the backed list
 */
public class FilterableListContainer<T> extends ListContainer<T> implements
        Filterable {

    private static final long serialVersionUID = 6410519255465731727L;

    private final Set<Filter> filters = new HashSet<>();

    private List<T> filteredItems = new ArrayList<>();

    public FilterableListContainer(Class<? extends T> type) {
        super(type);
    }
    
    public FilterableListContainer(DynaClass type, String... properties) {
		super(type, properties);
	}

	public FilterableListContainer(DynaClass type) {
		super(type);
	}

	public FilterableListContainer(Collection<? extends T> backingList) {
        super(backingList);
    }

    private void addFilter(Filter filter) {
        filters.add(filter);
        filterContainer();
    }

    private Set<Filter> getFilters() {
        return filters;
    }

    private void removeAllFilters() {
        if (filters.isEmpty()) {
            return;
        }
        filters.clear();
        filterContainer();

    }

    private void removeFilter(Filter filter) {
        filters.remove(filter);
        filterContainer();
    }

    private void filterContainer() {
        applyFilters();
        super.fireItemSetChange();
    }

    private void applyFilters() {
        filteredItems = new ArrayList<>();
        if (isFiltered()) {
            for (T itemId : super.getBackingList()) {
                if (passesFilters(itemId)) {
                    filteredItems.add(itemId);
                }
            }
        }
    }

    private boolean passesFilters(T itemId) {
        if (isFiltered()) {
            Item item = super.getItem(itemId);
            for (Filter f : getFilters()) {
                if (!f.passesFilter(itemId, item)) {
                    return false;
                }
            }
        }
        return true;

    }

    private boolean isFiltered() {
        return filters == null ? false : filters.size() > 0;
    }

    private boolean contains(T itemId) {
        return getBackingList().contains(itemId);
    }

    /**
     * If the parent {@link ListContainer} wants to fire an ItemSetChange, we
     * need to refilter.
     *
     * @see com.vaadin.v7.data.util.AbstractContainer#fireItemSetChange()
     *
     */
    @Override
    public void fireItemSetChange() {
        applyFilters();
        super.fireItemSetChange();
    }

    @Override
    protected List<T> getBackingList() {
        return isFiltered() ? filteredItems : super.getBackingList();
    }

    public List<T> getUnfilteredItemIds() {
      return super.getBackingList();
    }

    @Override
    public T getIdByIndex(int index) {
        return getBackingList().get(index);
    }

    @Override
    public Item getItem(Object itemId) {
        if (itemId == null) {
            return null;
        }
        if (isFiltered() && !filteredItems.contains(itemId)) {
            return null;
        }
        return super.getItem(itemId);
    }

    @Override
    public Collection<T> getItemIds() {
        return getBackingList();
    }

    @Override
    public List<T> getItemIds(int startIndex, int numberOfItems) {
        return getBackingList().subList(startIndex, startIndex + numberOfItems);
    }

    @Override
    public int indexOfId(Object itemId) {
        return getBackingList().indexOf(itemId);
    }

    @Override
    public int size() {
        return getBackingList().size();
    }

    @Override
    public boolean containsId(Object itemId) {
        return contains((T) itemId);
    }

    @Override
    public void addContainerFilter(Filter filter) throws UnsupportedFilterException {
        addFilter(filter);
    }

    @Override
    public void removeContainerFilter(Filter filter) {
        removeFilter(filter);
    }

    @Override
    public void removeAllContainerFilters() {
        removeAllFilters();
    }

    @Override
    public Collection<Filter> getContainerFilters() {
        return getFilters();
    }

    @Override
    public void sort(Object[] propertyId, boolean[] ascending) {
      super.sort(propertyId, ascending);

      if (isFiltered()) {
        filterContainer();
      }
    }

}
