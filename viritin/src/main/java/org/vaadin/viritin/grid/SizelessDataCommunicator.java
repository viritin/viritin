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

import java.util.List;

import com.vaadin.data.provider.DataCommunicator;
import com.vaadin.shared.Range;
import com.vaadin.shared.data.DataCommunicatorClientRpc;

import elemental.json.Json;
import elemental.json.JsonArray;

/**
 *
 * @author Teemu Suo-Anttila
 */
public class SizelessDataCommunicator<T> extends DataCommunicator<T> {

    DataCommunicatorClientRpc rpc = getRpcProxy(
            DataCommunicatorClientRpc.class);
    int knownSize = 0;

    @Override
    protected void sendDataToClient(boolean initial) {
        if (getDataProvider() == null) {
            return;
        }

        if (initial || reset) {
            rpc.reset(0);
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
}
