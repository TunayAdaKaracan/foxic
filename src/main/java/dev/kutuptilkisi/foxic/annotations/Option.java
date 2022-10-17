package dev.kutuptilkisi.foxic.annotations;

import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Option {

    String name();

    String description() default "Description Not Provided";

    OptionType optionType() default OptionType.STRING;

    boolean required() default false;

    Choice[] choices() default {};


}
