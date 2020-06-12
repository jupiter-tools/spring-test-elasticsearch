package com.jupitertools.springtestelasticsearch.customizer;

import java.time.Duration;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.HttpWaitStrategy;
import org.testcontainers.containers.wait.strategy.WaitStrategy;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextCustomizer;
import org.springframework.test.context.MergedContextConfiguration;

import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;

/**
 * Created on 26/11/2019
 * <p>
 * This ContextCustomizer starts the Elasticsearch docker image in tests.
 *
 * @author Korovin Anatoliy
 */
public class ElasticsearchContextCustomizer implements ContextCustomizer {

    private static final Logger log = LoggerFactory.getLogger(ElasticsearchContextCustomizer.class);

    private final Set<ContainerDescription> containerDescriptions;


    public ElasticsearchContextCustomizer(Set<ContainerDescription> descriptions) {
        this.containerDescriptions = descriptions;
    }

    @Override
    public void customizeContext(ConfigurableApplicationContext configurableApplicationContext,
                                 MergedContextConfiguration mergedContextConfiguration) {

        WaitStrategy waitStrategy =
                new HttpWaitStrategy().forPort(9200)
                                      .forStatusCodeMatching(response -> response == HTTP_OK ||
                                                                         response == HTTP_UNAUTHORIZED)
                                      .withStartupTimeout(Duration.ofMinutes(2));

        for (ContainerDescription description : containerDescriptions) {

            String clusterName = "test_cluster_${new Random().nextInt(1000000)}";
            String dockerImageName = getDockerImageName(description);

            GenericContainer container =
                    new FixedHostPortGenericContainer<>(dockerImageName)
                            .withExposedPorts(9200, 9300)
                            .withEnv("cluster.name", clusterName)
                            .withEnv("discovery.type", "single-node")
                            .waitingFor(waitStrategy);

            log.info("Starting Elasticsearch TestContainer");
            container.start();
            log.info("Started Elasticsearch TestContainer at:[{}] and bind to [{}], with cluster name: [{}]",
                     getHostPort(container),
                     description.getClusterNodes(),
                     clusterName);

            TestPropertyValues testPropertyValues =
                    TestPropertyValues.of(
                            description.getClusterNodes() + "=" + getHostPort(container),
                            description.getClusterName() + "=" + clusterName);

            testPropertyValues.applyTo(configurableApplicationContext);
        }
    }

    private String getHostPort(GenericContainer container) {
        String host = container.getContainerIpAddress();
        Integer port = container.getMappedPort(9300);
        return "${host}:${port}";
    }

    private String getDockerImageName(ContainerDescription description){
        String dockerImage = "docker.elastic.co/elasticsearch/elasticsearch";
        return "${dockerImage}:${description.getElasticVersion()}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        ElasticsearchContextCustomizer that = (ElasticsearchContextCustomizer) o;
        return Objects.equals(containerDescriptions, that.containerDescriptions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(containerDescriptions);
    }
}
