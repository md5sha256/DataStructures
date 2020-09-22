package me.andrewandy.datastructures;

import me.andrewandy.datastructures.benchmark.BaseBenchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.WarmupMode;

import java.util.concurrent.TimeUnit;

@OutputTimeUnit(TimeUnit.MILLISECONDS) @Fork(value = 2) public class Main {

    public static void main(final String[] args) {
        final OptionsBuilder builder = new OptionsBuilder();
        final Options options = builder.timeUnit(TimeUnit.MILLISECONDS)
                                       .mode(Mode.SingleShotTime)
                                       .warmupMode(WarmupMode.INDI)
                                       .forks(2)
                                       .warmupIterations(3)
                                       .measurementIterations(5)
                                       .jvmArgs("-Xint")
                                       .include(BaseBenchmark.class.getSimpleName())
                                       //.include(JavaLinkedListBenchmark.class.getSimpleName())
                                      //.include(JavaHashSetBenchmark.class.getSimpleName())
                                       .build();
        try {
            new Runner(options).run();
        } catch (final RunnerException ex) {
            ex.printStackTrace();
        }

    }


    @State(Scope.Benchmark) public static class GlobalValues {

        @Param({"100", "1000", "10000", "100000", "1000000"}) public int size;

        public int size() {
            return size;
        }

        @Param({"LinkedList", "DynamicHashSet", "FixedSizeHashSet"})
        public String collection;

        public <T> Collection<T> newCollection() {
            switch (collection) {
                case "LinkedList":
                    return new LinkedList<>();
                case "DynamicHashSet":
                    return new DynamicHashSet<>(size, 0.75f);
                case "FixedSizeHashSet":
                    return new FixedSizeHashSet<>(size);
                default:
                    throw new IllegalArgumentException("Unknown Collection: " + collection);
            }
        }
    }

}
