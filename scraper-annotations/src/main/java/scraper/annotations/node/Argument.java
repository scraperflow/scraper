package scraper.annotations.node;

import scraper.annotations.NotNull;

import java.lang.annotation.*;

/**
 * Fields annotated with this annotation are evaluated as templates once during initialization.
 * A custom JSON converter can be provided.
 *
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface Argument {
    /**
     * Target class which provides a static Object convert(String, Class<?>) method.
     * Default implementation can handle primitive types and Addresses only.
     */
    @NotNull Class<?> converter() default void.class;
}

