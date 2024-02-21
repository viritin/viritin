package org.vaadin.viritin.it;

import java.lang.annotation.Documented;
import static java.lang.annotation.ElementType.*;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * JSR303 crossfield validator example by Patrick:
 * http://stackoverflow.com/questions/1972933/cross-field-validation-with-hibernate-validator-jsr-303
 * 
 * 
 * Validation annotation to validate that 2 fields have the same value. An array
 * of fields and their matching confirmation fields can be supplied.
 *
 * Example, compare 1 pair of fields:
 *
 * @FieldMatch(first = "password", second = "confirmPassword", message = "The
 * password fields must match")
 *
 * Example, compare more than 1 pair of fields:
 * @FieldMatch.List({
 * @FieldMatch(first = "password", second = "confirmPassword", message = "The
 * password fields must match"),
 * @FieldMatch(first = "email", second = "confirmEmail", message = "The email
 * fields must match")})
 */
@Target({TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = FieldMatchValidator.class)
@Documented
public @interface FieldMatch {

    String message() default "{constraints.fieldmatch}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * @return The first field
     */
    String first();

    /**
     * @return The second field
     */
    String second();

    /**
     * Defines several <code>@FieldMatch</code> annotations on the same element
     *
     * @see FieldMatch
     */
    @Target({TYPE, ANNOTATION_TYPE})
    @Retention(RUNTIME)
    @Documented
    @interface List {

        FieldMatch[] value();
    }
}
