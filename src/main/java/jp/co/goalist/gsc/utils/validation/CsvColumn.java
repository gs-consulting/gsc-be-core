package jp.co.goalist.gsc.utils.validation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CsvColumn {
    String[] patterns() default {};
    boolean required() default false;
    String name();
}
