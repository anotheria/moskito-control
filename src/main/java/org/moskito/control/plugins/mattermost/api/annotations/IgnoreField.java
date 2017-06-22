package org.moskito.control.plugins.mattermost.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to ignore field
 * while parsing request object to map
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.FIELD)
public @interface IgnoreField {

}
