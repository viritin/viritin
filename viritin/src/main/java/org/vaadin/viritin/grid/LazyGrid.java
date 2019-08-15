/*
 * Copyright 2018 Matti Tahvonen.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.vaadin.viritin.grid;

import com.vaadin.data.provider.GridSortOrder;
import com.vaadin.event.SortEvent;
import com.vaadin.event.SortEvent.SortListener;
import com.vaadin.server.SerializableSupplier;

/**
 * A Grid which doesn't require you to report the size when doing lazy loading
 * data binding. This makes building lazy databinding to many backends a lot
 * easier and in some cases with magnitudes less server/database resources.
 * <p>
 * The component also caches some calls to the backend and ensures that backend
 * is accessed as pages (although requested with start index + limit), which
 * makes for example integration with Spring Data based services a lot easier.
 * <p>
 * An example to create a 100 row paged lazy binding with sorting and filtering
 * to Spring Data JPA based data source, just call setItems method with a
 * {@link FetchItemsCallback} as follows:
 * 
 * <pre>
 * <code>
 * personTable.setPageSize(100);
 * personTable.setItems( (sortOrder, offset, limit) -&gt; {
 *   Sort.Direction sortDirection = sortOrder.isEmpty() || sortOrder.get(0).getDirection() == SortDirection.ASCENDING ? Sort.Direction.ASC : Sort.Direction.DESC;
 *   String sortProperty = sortOrder.isEmpty() ? "id" : sortOrder.get(0).getSorted();
 *    return repo.findByNameLikeIgnoreCase(likeFilter, 
 *    		PageRequest.of(offset / limit, limit, sortDirection, sortProperty))
 *    		.stream();
 *});
 *</code>
 * </pre>
 *
 * @author mstahv
 * @param <T> The type of data shown in the Grid
 */
public class LazyGrid<T> extends MGrid<T> {

	private FetchItemsCallback<T> fetchItemsCallback;
	private SortListener<GridSortOrder<T>> sortListener = new SortListener<GridSortOrder<T>>() {

		@Override
		public void sort(SortEvent<GridSortOrder<T>> event) {
			resetContent();
		}

	};

	private void resetContent() {
		getDataCommunicator().reset();
		setItems(fetchItemsCallback);
	}

	public LazyGrid() {
		super(new SizelessPagingDataCommunicator<>());
	}

	public LazyGrid(Class<T> clazz) {
		super(clazz, new SizelessPagingDataCommunicator<>());
		addSortListener(sortListener);
	}

	protected SizelessPagingDataCommunicator<T> getSizelessPagingDataCommunicator() {
		return (SizelessPagingDataCommunicator<T>) getDataCommunicator();
	}

	/**
	 * Creates a lazy binding to backend with only {@link FetchItemsCallback}. The
	 * calls to the backend are done in as "pages", defined by
	 * {@link #setPageSize(int)}
	 * 
	 * @param fetchItems the callback to return chuncks of data.
	 */
	public void setItems(FetchItemsCallback<T> fetchItems) {
		SerializableSupplier<Integer> sizeCallback = () -> {
			// This should never be called by the framework
			// System.err.println("Size requested although should not be needed");
			return 0;
		};
		this.fetchItemsCallback = fetchItems;
		super.setDataProvider(fetchItems, sizeCallback);
	}

	/**
	 * @return the number of rows the component fetches from the backend at once.
	 */
	public int getPageSize() {
		return getSizelessPagingDataCommunicator().getPageSize();
	}

	/**
	 * Adjusts the page size that the component uses to access the backend.
	 * 
	 * @param pageSize the number of rows requested from the backend
	 */
	public void setPageSize(int pageSize) {
		getSizelessPagingDataCommunicator().setPageSize(pageSize);
	}

}
