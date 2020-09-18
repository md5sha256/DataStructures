package me.andrewandy.datastructures;

import me.andrewandy.datastructures.benchmark.FastReadHSBenchmark;
import me.andrewandy.datastructures.benchmark.FastWriteHSBenchmark;
import me.andrewandy.datastructures.benchmark.LinkedListBenchmark;
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

import java.util.concurrent.TimeUnit;

@OutputTimeUnit(TimeUnit.MILLISECONDS) @Fork(value = 2) public class Main {

    public static void main(final String[] args) {
        final OptionsBuilder builder = new OptionsBuilder();
        final Options options = builder.timeUnit(TimeUnit.MILLISECONDS)
                                       .mode(Mode.SingleShotTime)
                                       .forks(2)
                                       .warmupIterations(1)
                                       .measurementIterations(5)
                                       .jvmArgs("-Xint")
                                       .include(LinkedListBenchmark.class.getSimpleName())
                                       .include(FastReadHSBenchmark.class.getSimpleName())
                                       .include(FastWriteHSBenchmark.class.getSimpleName())
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
    }

}
