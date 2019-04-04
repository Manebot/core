package io.manebot.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventHandler {

    /**
     * The event handler's priority.
     * @return Priority. HIGHEST executes first, while LOWEST executes last.
     */
    EventPriority priority() default EventPriority.NORMAL;

}
