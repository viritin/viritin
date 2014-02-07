/*
 * Copyright 2014 mattitahvonenitmill.
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
 *//*
 * Copyright 2014 mattitahvonenitmill.
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
package org.vaadin.maddon.fields;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Table;
import java.util.Arrays;
import java.util.Collection;

public class MTable<T> extends Table {

    private BeanItemContainer<T> bic;
    private String[] pendingProperties;
    private String[] pendingHeaders;

    public MTable() {
    }

    public MTable(T... beans) {
        this();
        addBeans(beans);
    }

    public MTable(Collection<T> beans) {
        this();
        ensureBeanItemContainer(beans.iterator().next());
        bic.addAll(beans);
    }

    public MTable<T> withProperties(String... visibleProperties) {
        if (containerInitialized()) {
            setVisibleColumns((Object[]) visibleProperties);
        } else {
            pendingProperties = visibleProperties;
        }
        return this;
    }

    private boolean containerInitialized() {
        return bic != null;
    }

    public MTable<T> withColumnHeaders(String... columnNamesForVisibleProperties) {
        if (containerInitialized()) {
            setColumnHeaders(columnNamesForVisibleProperties);
        } else {
            pendingHeaders = columnNamesForVisibleProperties;
        }
        return this;
    }

    public void addValueChangeListener(MValueChangeListener<T> listener) {
        addListener(MValueChangeEvent.class, listener, MValueChangeEventImpl.VALUE_CHANGE_METHOD);
        // implicitly consider the table should be selectable
        setSelectable(true);
        // TODO get rid of this when 7.2 is out
        setImmediate(true);
    }

    public void removeValueChangeListener(MValueChangeListener<T> listener) {
        removeListener(MValueChangeEvent.class, listener, MValueChangeEventImpl.VALUE_CHANGE_METHOD);
        setSelectable(hasListeners(MValueChangeEvent.class));
    }

    @Override
    protected void fireValueChange(boolean repaintIsNotNeeded) {
        fireEvent(new MValueChangeEventImpl(this));
        if (!repaintIsNotNeeded) {
            markAsDirty();
        }
    }

    private void ensureBeanItemContainer(T bean) {
        if (!containerInitialized()) {
            bic = new BeanItemContainer<T>((Class<? super T>) bean.getClass());
            setContainerDataSource(bic);
            if (pendingProperties != null) {
                setVisibleColumns((Object[]) pendingProperties);
                pendingProperties = null;
            }
            if (pendingHeaders != null) {
                setColumnHeaders(pendingHeaders);
                pendingHeaders = null;
            }
        }
    }

    @Override
    public T getValue() {
        return (T) super.getValue();
    }

    @Override
    @Deprecated
    public void setMultiSelect(boolean multiSelect) {
        super.setMultiSelect(multiSelect);
    }

    public void addBeans(T... beans) {
        addBeans(Arrays.asList(beans));
    }

    public void addBeans(Collection<T> beans) {
        if (!beans.isEmpty()) {
            ensureBeanItemContainer(beans.iterator().next());
            bic.addAll(beans);
        }
    }

    public void setBeans(T... beans) {
        removeAllItems();
        addBeans(beans);
    }

    public void setBeans(Collection<T> beans) {
        removeAllItems();
        addBeans(beans);
    }

}
