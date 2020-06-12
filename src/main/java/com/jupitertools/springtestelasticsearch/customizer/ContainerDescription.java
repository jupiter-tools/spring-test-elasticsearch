package com.jupitertools.springtestelasticsearch.customizer;

import java.util.Objects;

/**
 * Created on 28/11/2019
 * <p>
 * Elastic search context customizer description
 *
 * @author Korovin Anatoliy
 */
public class ContainerDescription {

    private final String clusterNodes;
    private final String clusterName;
    private final String elasticVersion;

    public ContainerDescription(String clusterNodes, String clusterName, String elasticVersion) {
        this.clusterNodes = clusterNodes;
        this.clusterName = clusterName;
        this.elasticVersion = elasticVersion;
    }

    public String getClusterNodes() {
        return clusterNodes;
    }

    public String getClusterName() {
        return clusterName;
    }

    public String getElasticVersion() {
        return elasticVersion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContainerDescription that = (ContainerDescription) o;
        return Objects.equals(clusterNodes, that.clusterNodes) &&
               Objects.equals(clusterName, that.clusterName) &&
               Objects.equals(elasticVersion, that.elasticVersion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clusterNodes, clusterName, elasticVersion);
    }
}
