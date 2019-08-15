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

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.vaadin.data.provider.DataCommunicator;
import com.vaadin.shared.Range;
import com.vaadin.shared.data.DataCommunicatorClientRpc;

import elemental.json.Json;
import elemental.json.JsonArray;

/**
 *
 * @author Teemu Suo-Anttila
 * @author Matti Tahvonen
 */
public class SizelessPagingDataCommunicator<T> extends DataCommunicator<T> {

    protected static final int CACHE_SIZE = 5;
    
	DataCommunicatorClientRpc rpc = getRpcProxy(
            DataCommunicatorClientRpc.class);
    int knownSize = 0;
    
    private boolean useCache = true;
    
    private int pageSize = getMinPushSize()*2;
    
    private Map<Integer,List<T>> pageCache = new LinkedHashMap(16, 0.75f, true ) {
    	
    	protected boolean removeEldestEntry(java.util.Map.Entry eldest) {
    		return size() > CACHE_SIZE;
    	};
    };

    @Override
    protected void sendDataToClient(boolean initial) {
        if (getDataProvider() == null) {
            return;
        }
        
        if (initial) {
            rpc.reset(0);
            if (reset) {
            	reset = false;
            }
        }

        if (reset) {
        	pageCache.clear();
        	rpc.reset(getDataProviderSize());
        	reset = false;
        }

        Range requestedRows = getPushRows();
        if (!requestedRows.isEmpty()) {
            int offset = requestedRows.getStart();
            // Always fetch some extra rows.
            int limit = requestedRows.length() + getMinPushSize();
            List<T> rowsToPush = fetchItemsWithRange(offset, limit);
            int lastIndex = offset + rowsToPush.size();
            if (lastIndex > knownSize) {
                int rowsToAdd = lastIndex - knownSize;
                rpc.insertRows(knownSize,
                        rowsToAdd + (rowsToPush.size() == limit ? 1 : 0));
                knownSize = lastIndex;
            } else if (rowsToPush.size() < requestedRows.length()) {
                // Size decreased
                int rowsToRemove = Math.max(
                        requestedRows.length() - rowsToPush.size(),
                        knownSize - lastIndex);
                knownSize = lastIndex;
                rpc.removeRows(knownSize, rowsToRemove);
            }
            pushData(offset, rowsToPush);
        }

        if (!getUpdatedData().isEmpty()) {
            JsonArray dataArray = Json.createArray();
            int i = 0;
            for (T data : getUpdatedData()) {
                dataArray.set(i++, getDataObject(data));
            }
            rpc.updateData(dataArray);
        }

        setPushRows(Range.withLength(0, 0));
        getUpdatedData().clear();
    }

    @Override
    public int getDataProviderSize() {
        return knownSize;
    }
    
	@Override
	public List<T> fetchItemsWithRange(final int offset, final int limit) {
		// adapt to paged requests and merge
		// TODO this could maybe be done more efficiently on an other layer
		
        final int pageSize = getPageSize();
        final int startPage = (int) Math.floor((double) offset / pageSize);
        final int endPage = (int) Math.floor((double) (offset + limit - 1) / pageSize);
        if (startPage != endPage) {
        	List<T> page0 = accessDataProvider(startPage*pageSize, pageSize);
            List<T> page1;
            if(page0.size() < pageSize) {
            	page1 = Collections.EMPTY_LIST;
            } else {
                page1 = accessDataProvider(endPage*pageSize, pageSize);
            }
            page0 = page0.subList(offset % pageSize, page0.size());
            page1 = page1.subList(0, Math.min(limit - page0.size(), page1.size()));
            List<T> result = new ArrayList<>(page0);
            result.addAll(page1);
            return result;
        } else {
        	List<T> page0 = accessDataProvider(startPage*pageSize, pageSize);
        	final int start = offset % pageSize;
        	final int end = Math.min(start + limit, page0.size());
            return page0.subList(start, end);
        }
	}
	
	protected List<T> accessDataProvider(final int offset, final int limit) {
		if(useCache) {
			final int page = offset/limit;
			return pageCache.computeIfAbsent(page, p -> super.fetchItemsWithRange(offset, limit));
		} else {
			return super.fetchItemsWithRange(offset, limit);
		}
	}
	
	public int getPageSize() {
		return pageSize;
	}
	
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	public void setUseCache(boolean useCache) {
		this.useCache = useCache;
	}
	
	public boolean isUseCache() {
		return useCache;
	}

}
