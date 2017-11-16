package org.vaadin.viritinv7.grid;

import com.vaadin.v7.data.Item;
import com.vaadin.v7.data.util.PropertyValueGenerator;
import org.vaadin.viritinv7.ListContainer;

import java.io.Serializable;

/**
 *
 * @author datenhahn (http://datenhahn.de)
 * @since 23.04.2016
 * @param <M> the entity type listed in the consumer of the generator's container, Vaadin Grid
 * @param <P> the presentation type, displays the generated value
 */
public class TypedPropertyValueGenerator<M, P> extends PropertyValueGenerator<P> {

    private static final long serialVersionUID = 1250403117667296988L;

    protected Class<M> modelType;
    protected Class<P> presentationType;
    protected ValueGenerator<M, P> valueGenerator;

    public TypedPropertyValueGenerator(Class<M> modelType,
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
