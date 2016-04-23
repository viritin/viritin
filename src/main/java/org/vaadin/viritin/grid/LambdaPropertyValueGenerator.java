/*
 * Copyright (c) 2016, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package org.vaadin.viritin.grid;

import com.vaadin.data.Item;
import com.vaadin.data.util.PropertyValueGenerator;
import org.vaadin.viritin.ListContainer;

import java.io.Serializable;

/**
 *
 * @author datenhahn (http://datenhahn.de)
 * @since 23.04.2016
 * @param <M> the entity type listed in the consumer of the generator's container, Vaadin Grid
 * @param <P> the presentation type, displays the generated value
 */
public class LambdaPropertyValueGenerator<M, P> extends PropertyValueGenerator<P> {

    Class<M> modelType;
    Class<P> presentationType;
    ValueGenerator<M, P> valueGenerator;

    public LambdaPropertyValueGenerator(Class<M> modelType,
                                        Class<P> presentationType,
                                        ValueGenerator<M, P> valueGenerator) {
        this.modelType = modelType;
        this.presentationType = presentationType;
        this.valueGenerator = valueGenerator;
    }

    @Override
    public P getValue(Item item, Object itemId, Object propertyId) {
        return valueGenerator.getValue((M) ((ListContainer.DynaBeanItem) item).getBean());
    }

    @Override
    public Class<P> getType() {
        return presentationType;
    }

    public interface ValueGenerator<M, P> extends Serializable {
        P getValue(M bean);
    }
}
