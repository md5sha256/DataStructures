package me.andrewandy.datastructures.benchmark;

import me.andrewandy.datastructures.Collection;
import me.andrewandy.datastructures.Main;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.CompilerControl;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import java.util.SplittableRandom;
import java.util.concurrent.TimeUnit;


/**
 * Benchmark for {@link Collection}s
 * Methods annotated with {@link Benchmark} test
 * a specific operation; These methods are equivalent
 * to those in {@link ArrayBenchmark}.
 */
@CompilerControl(CompilerControl.Mode.EXCLUDE)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class BaseBenchmark {

    @Benchmark
    public void testAdd(final ContainsState state) {
        for (Integer i : state.randomValues) {
            state.collection.add(i);
        }
    }

    @Benchmark
    public void removeFirstOccurrence(final ContainsState state) {
        for (Integer i : state.randomValues) {
            state.collection.removeFirst(i);
        }
    }

    @Benchmark
    public void testContains(final ContainsState state) {
        for (final Integer i : state.randomValues) {
            state.collection.contains(i);
        }
    }


    @State(Scope.Benchmark)
    public static class ContainsState {
        public Integer[] initialState;
        public Integer[] randomValues;

        public Collection<Integer> collection;
        private Main.GlobalValues values;

        @Setup(Level.Trial)
        public void init(final Main.GlobalValues values) {
            this.values = values;
            this.collection = values.newCollection();
            final SplittableRandom random = new SplittableRandom();
            this.initialState = random.ints(values.collectionSize, Integer.MIN_VALUE, 0).parallel().boxed()
                                      .toArray(Integer[]::new);
            this.randomValues =
                random.ints(values.sampleSize, 1, Integer.MAX_VALUE).parallel().boxed().toArray(Integer[]::new);

        }

        @Setup(Level.Iteration)
        public void reset() {
            collection.clear();
            for (int index = 0; index < values.collectionSize; index++) {
                this.collection.add(initialState[index]);
            }
        }
    }

}
