package com.jupitertools.springtestelasticsearch;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created on 2019-05-24
 * <p>
 * This annotation provide an ability to use the @{@link ElasticsearchTestContainer}
 * annotation as a repeatable annotation.
 *
 * @author Korovin Anatoliy
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ElasticsearchTestContainers {

    ElasticsearchTestContainer[] value();
}
