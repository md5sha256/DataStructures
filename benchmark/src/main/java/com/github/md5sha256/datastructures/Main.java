package com.github.md5sha256.datastructures;

import com.github.md5sha256.datastructures.benchmark.ArrayBenchmark;
import com.github.md5sha256.datastructures.benchmark.BaseBenchmark;
import com.github.md5sha256.datastructures.benchmark.JavaBenchmark;
import com.github.md5sha256.datastructures.collections.Collection;
import com.github.md5sha256.datastructures.collections.FixedSizeHashSet;
import com.github.md5sha256.datastructures.collections.LinkedList;

import org.openjdk.jmh.annotations.Mode;
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

/**
 * Main class of the program, change values here to alter the test parameters.
 */
public class Main {

    public static void main(final String[] args) {
        final OptionsBuilder builder = new OptionsBuilder();
        final Options options = builder
                                       // Output everything in Milliseconds | Single Shot as we are testing the time for one invocation
                                       .timeUnit(TimeUnit.MILLISECONDS).mode(Mode.SingleShotTime)
                                       // Do the individual warmup for every benchmark
                                       .warmupMode(WarmupMode.INDI)
                                       // We want two forks + 10 iterations to warm up the jvm
                                       .forks(2).warmupIterations(10)
                                       // 5 trials
                                       .measurementIterations(5)
                                       // Disable the JIT compiler; force jvm to run the benchmark code in interpreter mode
                                       .jvmArgs("-Xint")
                                       // Don't invoke GC between measurements
                                       .shouldDoGC(false)
                                       // Include both the base and array benchmarks
                                       .include(ArrayBenchmark.class.getSimpleName())
                                       .include(BaseBenchmark.class.getSimpleName())
                                       .include(JavaBenchmark.class.getSimpleName())
                                       // Output results in CSV format
                                       .resultFormat(ResultFormatType.CSV)
                                       .build();
        // Run the test!
        try {
            new Runner(options).run();
        } catch (final RunnerException ex) {
            ex.printStackTrace();
        }

    }


    /**
     * State which hold benchmark parameters for the benchmark of the Java primitive array.
     * @see ArrayBenchmark
     */
    @State(Scope.Benchmark)
    public static class ArrayValues {
        // Test values from 10 to 100k. These represent the initial size of the collection
        // Before any of the tests are performed.
        @Param({"10", "100", "1000", "10000", "100000"})
        public int collectionSize;

        // Represents how many values should be tested. I.e how many elements to add, remove or search.
        @Param("1000")
        public int sampleSize;

    }


    /**
     * State which hold benchmark parameters + convenience method to instantiate collections.
     * @see BaseBenchmark
     */
    @State(Scope.Benchmark)
    public static class BaseValues {

        // Test values from 10 to 100k. These represent the initial size of the collection
        // Before any of the tests are performed.
        @Param({"10", "100", "1000", "10000", "100000"})
        public int collectionSize;

        // Represents how many values should be tested. I.e how many elements to add, remove or search.
        @Param("1000")
        public int sampleSize;

        // Parameter for the name of the collection. Accepted values are "LinkedList" and "FixedSizeHashSet"
        @Param({"LinkedList", "FixedSizeHashSet"})
        public String collection;

        /**
         * Obtain a new instance of a collection specified by {@link #collection}. The {@link #collectionSize} parameter
         * will be utilized for collections which support it.
         *
         * @param <T> A generic type, can be anything.
         * @return Returns a new instance of an {@link Collection}
         * @throws IllegalArgumentException Thrown if {@link #collection} is invalid.
         */
        public <T> Collection<T> newCollection() throws IllegalArgumentException{
            switch (collection) {
                case "LinkedList":
                    return new LinkedList<>();
                case "FixedSizeHashSet":
                    return new FixedSizeHashSet<>(collectionSize);
                default:
                    throw new IllegalArgumentException("Unknown Collection: " + collection);
            }
        }
    }

    /**
     * State which hold benchmark parameters + convenience method to instantiate collections.
     * @see JavaBenchmark
     */
    @State(Scope.Benchmark)
    public static class JavaValues {

        // Test values from 10 to 100k. These represent the initial size of the collection
        // Before any of the tests are performed.
        @Param({"10", "100", "1000", "10000", "100000"})
        public int collectionSize;

        // Represents how many values should be tested. I.e how many elements to add, remove or search.
        @Param("1000")
        public int sampleSize;

        // Parameter for the name of the collection. Accepted values are "ArrayList", "LinkedList" and "HashSet"
        @Param({"LinkedList", "HashSet"})
        public String collection;

        /**
         * Obtain a new instance of a collection specified by {@link #collection}. The {@link #collectionSize} parameter
         * will be utilized for collections which support it.
         *
         * @param <T> A generic type, can be anything.
         * @return Returns a new instance of an {@link Collection}
         * @throws IllegalArgumentException Thrown if {@link #collection} is invalid.
         */
        public <T> java.util.Collection<T> newCollection() throws IllegalArgumentException {
            switch (collection) {
                case "ArrayList":
                    return new java.util.ArrayList<>();
                case "LinkedList":
                    return new java.util.LinkedList<>();
                case "HashSet":
                    return new java.util.HashSet<>();
                default:
                    throw new IllegalArgumentException("Unknown Collection: " + collection);
            }
        }
    }
}
