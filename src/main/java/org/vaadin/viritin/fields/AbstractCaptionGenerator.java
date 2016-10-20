package org.vaadin.viritin.fields;

/**
 * Base implementation of the {@link CaptionGenerator} interface. Ensures an
 * empty string as caption if parameter is null.
 * 
 * @author Daniel Nordhoff-Vergien
 *
 * @param <T> the type of the object for which caption is to be generated
 */
public abstract class AbstractCaptionGenerator<T> implements
        CaptionGenerator<T> {

    private static final long serialVersionUID = 7397954730260358087L;
    
    @Override
    public String getCaption(T option) {
        if (option == null) {
            return "";
        } else {
            return generateCaption(option);
        }
    }

    public abstract String generateCaption(T option);
}