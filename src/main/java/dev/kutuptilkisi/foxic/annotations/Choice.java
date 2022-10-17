package dev.kutuptilkisi.foxic.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Choice {
    String name();
    String value() default "";
}
