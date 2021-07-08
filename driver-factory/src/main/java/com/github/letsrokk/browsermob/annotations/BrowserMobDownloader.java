package com.github.letsrokk.browsermob.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.CONSTRUCTOR})
public @interface BrowserMobDownloader {

    String fileCharset() default "UTF-8";
    String[] contentTypes() default {};

}
