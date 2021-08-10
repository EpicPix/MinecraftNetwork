package ga.epicpix.network.common.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * All methods annotated by this may check the caller
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
public @interface CallerSensitive {}
