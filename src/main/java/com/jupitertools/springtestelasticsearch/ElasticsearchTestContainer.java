package com.jupitertools.springtestelasticsearch;


import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Start a docker container with the Elasticsearch cluster
 * and set a host/port to default spring boot properties.
 *
 * @author Anatoliy Korovin
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(ElasticsearchTestContainers.class)
public @interface ElasticsearchTestContainer {

	/**
	 * Docker image value of ElasticSearch
	 *
	 * @return necessary value of the docker image
	 */
	String value() default "6.4.1";

	String clusterNodesPropertyHolder() default "spring.data.elasticsearch.cluster-nodes";

	String clusterNamePropertyHolder() default "spring.data.elasticsearch.cluster-name";
}
