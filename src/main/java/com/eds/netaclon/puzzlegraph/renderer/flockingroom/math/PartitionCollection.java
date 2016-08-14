package com.eds.netaclon.puzzlegraph.renderer.flockingroom.math;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by emanuelesan on 07/08/16.
 */
public class PartitionCollection {
    private static final Logger logger = Logger.getLogger("logger");

    private final List<Partition> partitions;

    public PartitionCollection(List<Partition> partitions) {
        this.partitions = partitions;
    }


    /**
     * returns a new PArtitionCollection having each partition as
     * the union of the intersections of itself with all other pc's partitions.
     */
    public PartitionCollection intersect(PartitionCollection pc) {
        List<Partition> intersected = partitions.parallelStream().map(
                partition ->
                        pc.partitions
                                .parallelStream()
                                .map(partition::intersect)
                                .reduce(Partition::nonDegenerate)

        ).filter(Optional::isPresent).map(Optional::get)

                .collect(Collectors.toList());
        return new PartitionCollection(intersected);

    }

    @Override
    public String toString() {
        return "PartitionCollection{" +
                "partitions=" + partitions +
                '}';
    }

    public Partition get(int i) {
        return partitions.get(i);
    }

    public List<Partition> partitions() {
        return partitions;
    }
}
