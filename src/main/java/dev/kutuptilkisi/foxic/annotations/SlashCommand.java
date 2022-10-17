package dev.kutuptilkisi.foxic.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SlashCommand {

    String label();

    String description() default "No description provided";

    boolean guildOnly() default true;

    boolean developerMode() default false;

}
