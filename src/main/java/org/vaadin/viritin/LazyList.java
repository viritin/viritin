package org.vaadin.viritin;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

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

    private static final long serialVersionUID = 2423832460602269469L;

    private Runnable refreshCallback;

    private List<T> findPageFromCache(int pageIndexForReqest) {
        int p = pageIndexForReqest - pageIndex;
        if (p < 0) {
            return null;
        }
        if (pages.size() <= p) {
            return null;
        }
        return pages.get(p);
    }

    private void loadPreviousPage() {
        pageIndex--;
        List<T> page = findEntities(pageIndex * pageSize);
        pages.add(0, page);
        if (pages.size() > maxPages) {
            pages.remove(pages.size() - 1);
        }
    }

    private void loadNextPage() {
        List<T> page = findEntities((pageIndex + pages.size()) * pageSize);
        pages.add(page);
        if (pages.size() > maxPages) {
            pages.remove(0);
            pageIndex++;
        }
    }

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
    public interface EntityProvider<T> extends PagingProvider<T>, CountProvider {
    }

    private PagingProvider<T> pageProvider;
    private final CountProvider countProvider;

    // Vaadin table by default has 15 rows, 2x that to cache up an down
    // With this setting it is maximum of 2 requests that happens. With
    // normal scrolling just 0-1 per user interaction
    public static final int DEFAULT_PAGE_SIZE = 15 + 15 * 2;

    public int getMaxPages() {
        return maxPages;
    }

    /**
     * Sets the maximum of pages that are held in memory. By default 3, but it
     * is adjusted automatically based on requests that are made to the list,
     * like subList method calls. Most often this shouldn't be called by end
     * user.
     *
     * @param maxPages the number of pages to be held in memory
     */
    public void setMaxPages(int maxPages) {
        this.maxPages = maxPages;
    }

    private int maxPages = 3;

    List<List<T>> pages = new ArrayList<>();

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
    public LazyList(EntityProvider<T> dataProvider) {
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
    public LazyList(EntityProvider<T> dataProvider, int pageSize) {
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
    public LazyList(PagingProvider<T> pageProvider, CountProvider countProvider) {
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
    public LazyList(PagingProvider<T> pageProvider, CountProvider countProvider, int pageSize) {
        this.pageProvider = pageProvider;
        this.countProvider = countProvider;
        this.pageSize = pageSize;
    }

    @Override
    public T get(final int index) {
        final int pageIndexForReqest = index / pageSize;
        final int indexOnPage = index % pageSize;

        // Find page from cache
        List<T> page = findPageFromCache(pageIndexForReqest);

        if (page == null) {
            if (pageIndex >= 0) {
                if (pageIndexForReqest > pageIndex && pageIndexForReqest < pageIndex + pages.size() + maxPages) {
                    // load next n pages forward
                    while (pageIndexForReqest >= pageIndex + pages.size()) {
                        loadNextPage();
                    }
                } else if (pageIndexForReqest < pageIndex && pageIndexForReqest > pageIndex - maxPages) {
                    //load prev page to cache
                    while (pageIndexForReqest < pageIndex) {
                        loadPreviousPage();
                    }
                } else {
                    initCacheFormPage(pageIndexForReqest);
                }
            } else {
                // first page to load
                initCacheFormPage(pageIndexForReqest);
            }
            page = findPageFromCache(pageIndexForReqest);
        }
        final T get = page.get(indexOnPage);
        return get;
    }

    protected void initCacheFormPage(final int pageIndexForReqest) {
        // clear cache
        pageIndex = pageIndexForReqest;
        pages.clear();
        pages.add(findEntities(pageIndex * pageSize));
    }

    protected List<T> findEntities(int i) {
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

    private transient WeakHashMap<T, Integer> indexCache;

    private Map<T, Integer> getIndexCache() {
        if (indexCache == null) {
            indexCache = new WeakHashMap<>();
        }
        return indexCache;
    }

    @Override
    public int indexOf(Object o) {
        // optimize: check the buffers first
        Integer indexViaCache = getIndexCache().get(o);
        if (indexViaCache != null) {
            return indexViaCache;
        }
        for (int i = 0; i < pages.size(); i++) {
            List<T> page = pages.get(i);
            int indexOf = page.indexOf(o);
            if (indexOf != -1) {
                indexViaCache = (pageIndex + i) * pageSize + indexOf;
            }
        }
        if (indexViaCache != null) {
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
        if (getIndexCache().containsKey(o)) {
            return true;
        }
        for (List<T> t : pages) {
            if (t.contains(o)) {
                return true;
            }
        }
        return super.contains(o);
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        if (refreshCallback != null && !isLoadedIntoCache(fromIndex, toIndex)) {
            int nonCachedSize = countProvider.size();
            if (size() != nonCachedSize) {
                reset();
                refreshCallback.run();
                if (toIndex > nonCachedSize) {
                    if (nonCachedSize > fromIndex){
                        return new ArrayList<>(super.subList(fromIndex, nonCachedSize));
                    } else {
                        return Collections.EMPTY_LIST;
                    }
                }
            }
        }

        final int sizeOfSublist = toIndex - fromIndex;
        if (sizeOfSublist > maxPages * (pageSize -1)) {
            // Increase the amount of cached pages if necessary
            maxPages = sizeOfSublist/pageSize + 1;
        }

        return new ArrayList<>(super.subList(fromIndex, toIndex));
    }

    private boolean isLoadedIntoCache(int fromIndex, int toIndex) {
        return fromIndex >= cachedFromIndex()
                && toIndex <= cachedToIndex();
    }

    private int cachedFromIndex() {
        if (pageIndex < 0) {
            return 0;
        }
        return pageIndex * pageSize;
    }

    private int cachedToIndex() {
        if (pageIndex < 0) {
            return 0;
        }

        int numberOfCachedItems = 0;

        for (List<T> page : pages) {
            numberOfCachedItems += page.size();
        }

        return numberOfCachedItems + pageIndex * pageSize;
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

    public void setRefreshCallback(Runnable callback){
        this.refreshCallback = callback;
    }

    /**
     * Resets buffers used by the LazyList.
     */
    public void reset() {
        pages.clear();
        pageIndex = -10;
        cachedSize = null;
        if (indexCache != null) {
            indexCache.clear();
        }
    }

}
