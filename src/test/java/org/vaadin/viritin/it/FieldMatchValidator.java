package org.vaadin.viritin.it;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.beanutils.BeanUtils;

/**
 *  * JSR303 crossfield validator example by Patrick:
 * http://stackoverflow.com/questions/1972933/cross-field-validation-with-hibernate-validator-jsr-303
 *
 *
 * @author Matti Tahvonen
 */
public class FieldMatchValidator implements
        ConstraintValidator<FieldMatch, Object> {

    private String firstFieldName;
    private String secondFieldName;

    @Override
    public void initialize(final FieldMatch constraintAnnotation) {
        firstFieldName = constraintAnnotation.first();
        secondFieldName = constraintAnnotation.second();
    }

    @Override
    public boolean isValid(final Object value,
            final ConstraintValidatorContext context) {
        try {
            final Object firstObj = BeanUtils.getProperty(value,
                    firstFieldName);
            final Object secondObj = BeanUtils.getProperty(value,
                    secondFieldName);

            return firstObj == null && secondObj == null || firstObj != null && firstObj.
                    equals(secondObj);
        } catch (final Exception ignore) {
            // ignore
        }
        return true;
    }
}
