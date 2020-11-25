package me.andrewandy.datastructures;

import me.andrewandy.datastructures.benchmark.BaseBenchmark;
import me.andrewandy.datastructures.benchmark.ArrayBenchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.results.format.ResultFormatType;
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
                                       .warmupIterations(2)
                                       .measurementIterations(5)
                                       .jvmArgs("-Xint")
                                       .include(BaseBenchmark.class.getSimpleName())
                                       .include(ArrayBenchmark.class.getSimpleName())
                                       .resultFormat(ResultFormatType.CSV)
                                       .build();
        try {
            new Runner(options).run();
        } catch (final RunnerException ex) {
            ex.printStackTrace();
        }

    }

    @State(Scope.Benchmark) public static class ArrayValues {
        @Param({"10", "100", "1000","10000"})
        //@Param({"100000"})
        public int collectionSize;
        public final int sampleSize = 1000;

    }

    @State(Scope.Benchmark) public static class GlobalValues {

        @Param({"10", "100", "1000","10000"})
        //@Param({"100000"})
        public int collectionSize;
        public final int sampleSize = 1000;

        @Param({"LinkedList", "DynamicHashSet", "FixedSizeHashSet"})
        public String collection;

        public <T> Collection<T> newCollection() {
            switch (collection) {
                case "LinkedList":
                    return new LinkedList<>();
                case "DynamicHashSet":
                    return new DynamicHashSet<>(collectionSize, 0.75f);
                case "FixedSizeHashSet":
                    return new FixedSizeHashSet<>(collectionSize);
                default:
                    throw new IllegalArgumentException("Unknown Collection: " + collection);
            }
        }
    }

}
