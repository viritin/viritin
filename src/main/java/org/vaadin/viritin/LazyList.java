package org.vaadin.viritin;

import java.io.Serializable;
import java.util.*;

/**
 * A general purpose helper class to us MTable/ListContainer for service layers
 * (EJBs, Spring Data etc) that provide large amount of data. Makes paged
 * requests to PagingProvider, caches recently used pages in memory and this way
 * hides away Vaadin Container complexity from you. The class generic helper and
 * is probably useful also other but Vaadin applications as well.
 *
 * @author Matti Tahvonen
 * @param <T> The type of the objects in the list
 */
public class LazyList<T> extends AbstractList<T> implements Serializable {

    // Split into subinterfaces for better Java 8 lambda support
    /**
     * Interface via the LazyList communicates with the "backend"
     *
     * @param <T> The type of the objects in the list
     */
    public interface PagingProvider<T> extends Serializable {

        /**
         * Fetches one "page" of entities form the backend. The amount
         * "maxResults" should match with the value configured for the LazyList
         *
         * @param firstRow the index of first row that should be fetched
         * @return a sub list from given first index
         */
        public List<T> findEntities(int firstRow);
    }

    /**
     * LazyList detects the size of the "simulated" list with via this
     * interface. Backend call is cached as COUNT queries in databases are
     * commonly heavy.
     */
    public interface CountProvider extends Serializable {

        /**
         * @return the count of entities listed in the LazyList
         */
        public int size();
    }

    /**
     * Interface via the LazyList communicates with the "backend"
     *
     * @param <T> The type of the objects in the list
     */
    public interface EntityProvider<T> extends PagingProvider,
            CountProvider {
    }

    private PagingProvider pageProvider;
    private final CountProvider countProvider;

    // Vaadin table by default has 15 rows, 2x that to cache up an down
    // With this setting it is maximum of 2 requests that happens. With
    // normal scrolling just 0-1 per user interaction
    public static final int DEFAULT_PAGE_SIZE = 15 + 15*2;

    private List<T> currentPage;
    private List<T> prevPage;
    private List<T> nextPage;

    private int pageIndex = -10;
    private final int pageSize;
    
    protected LazyList(CountProvider countProvider, int pageSize) {
        this.countProvider = countProvider;
        this.pageSize = pageSize;
    }

    /**
     * Constructs a new LazyList with given provider and default page size of
     * DEFAULT_PAGE_SIZE (30).
     *
     * @param dataProvider the data provider that is used to fetch pages of
     * entities and to detect the total count of entities
     */
    public LazyList(EntityProvider dataProvider) {
        this(dataProvider, DEFAULT_PAGE_SIZE);
    }

    /**
     * Constructs a new LazyList with given provider and default page size of
     * DEFAULT_PAGE_SIZE (30).
     *
     * @param dataProvider the data provider that is used to fetch pages of
     * entities and to detect the total count of entities
     * @param pageSize the page size to be used
     */
    public LazyList(EntityProvider dataProvider, int pageSize) {
        this.pageProvider = dataProvider;
        this.countProvider = dataProvider;
        this.pageSize = pageSize;
    }

    /**
     * Constructs a new LazyList with given providers and default page size of
     * DEFAULT_PAGE_SIZE (30).
     *
     * @param pageProvider the interface via "pages" of entities are requested
     * @param countProvider the interface via the total count of entities is
     * detected.
     */
    public LazyList(PagingProvider pageProvider,
            CountProvider countProvider) {
        this(pageProvider, countProvider, DEFAULT_PAGE_SIZE);
    }

    /**
     * Constructs a new LazyList with given providers and page size.
     *
     * @param pageProvider the interface via "pages" of entities are requested
     * @param countProvider the interface via the total count of entities is
     * detected.
     * @param pageSize the page size that should be used
     */
    public LazyList(PagingProvider pageProvider,
            CountProvider countProvider, int pageSize) {
        this.pageProvider = pageProvider;
        this.countProvider = countProvider;
        this.pageSize = pageSize;
    }

    @Override
    public T get(final int index) {
        final int pageIndexForReqest = index / pageSize;
        final int indexOnPage = index % pageSize;

        // Find page from cache
        List<T> page = null;
        if (pageIndex == pageIndexForReqest) {
            page = currentPage;
        } else if (pageIndex - 1 == pageIndexForReqest && prevPage != null) {
            page = prevPage;
        } else if (pageIndex + 1 == pageIndexForReqest && nextPage != null) {
            page = nextPage;
        }

        if (page == null) {
            // Page not in cache, change page, move next/prev is feasible
            if (pageIndexForReqest - 1 == pageIndex) {
                // going to next page
                prevPage = currentPage;
                currentPage = nextPage;
                nextPage = null;
            } else if (pageIndexForReqest + 1 == pageIndex) {
                // going to previous page
                nextPage = currentPage;
                currentPage = prevPage;
                prevPage = null;
            } else {
                currentPage = null;
                prevPage = null;
                nextPage = null;
            }
            pageIndex = pageIndexForReqest;
            if (currentPage == null) {
                currentPage = findEntities(pageIndex * pageSize);
            }
            if (currentPage == null) {
                return null;
            } else {
                page = currentPage;
            }
        }
        final T get = page.get(indexOnPage);
        return get;
    }

    protected List findEntities(int i) {
        return pageProvider.findEntities(i);
    }

    private Integer cachedSize;

    @Override
    public int size() {
        if (cachedSize == null) {
            cachedSize = countProvider.size();
        }
        return cachedSize;
    }

    private transient WeakHashMap<T,Integer> indexCache ;

    private Map<T, Integer> getIndexCache() {
        if(indexCache == null) {
            indexCache = new WeakHashMap<T, Integer>();
        }
        return indexCache;
    }

    @Override
    public int indexOf(Object o) {
        // optimize: check the buffers first
        Integer indexViaCache = getIndexCache().get(o);
        if(indexViaCache != null) {
            return indexViaCache;
        } else if (currentPage != null) {
            int idx = currentPage.indexOf(o);
            if (idx != -1) {
                indexViaCache = pageIndex * pageSize + idx;
            }
        }
        if (indexViaCache == null && prevPage != null) {
            int idx = prevPage.indexOf(o);
            if (idx != -1) {
                indexViaCache =  (pageIndex - 1) * pageSize + idx;
            }
        }
        if (indexViaCache == null && nextPage != null) {
            int idx = nextPage.indexOf(o);
            if (idx != -1) {
                indexViaCache =  (pageIndex + 1) * pageSize + idx;
            }
        }
        if(indexViaCache != null) {
            /*
             * In some cases (selected value) components like Vaadin combobox calls this, then stuff from elsewhere with indexes and
             * finally again this method with the same object (possibly on other page). Thus, to avoid heavy iterating,
             * cache the location.
             */
            getIndexCache().put((T) o, indexViaCache);
            return indexViaCache;
        }
        // fall back to iterating, this will most likely be sloooooow....
        // If your app gets here, consider overwriting this method, and to
        // some optimization at service/db level
        return super.indexOf(o);
    }

    @Override
    public boolean contains(Object o) {
        // Although there would be the indexed version, vaadin sometimes calls this
        // First check caches, then fall back to sluggish iterator :-(
        if(getIndexCache().containsKey(o)) {
            return true;
        } else if (currentPage != null && currentPage.contains(o)) {
            return true;
        } else if (prevPage != null && prevPage.contains(o)) {
            return true;
        }  else if (nextPage != null && nextPage.contains(o)) {
            return true;
        }
        return super.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {

            private int index = -1;
            private final int size = size();

            @Override
            public boolean hasNext() {
                return index + 1 < size;
            }

            @Override
            public T next() {
                index++;
                return get(index);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Not supported.");
            }
        };
    }

    /**
     * Resets buffers used by the LazyList.
     */
    public void reset() {
        currentPage = null;
        prevPage = null;
        nextPage = null;
        pageIndex = -10;
        cachedSize = null;
        if(indexCache != null) {
            indexCache.clear();
        }
    }

}
