package com.github.manevolent.jbot.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventHandler {

    /**
     * The events priority.
     * @return Priority. HIGHEST executes first, while LOWEST executes last.
     */
    public EventPriority priority() default EventPriority.NORMAL;

}
