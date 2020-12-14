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

    /**
     * Test adding values to the collection
     */
    @Benchmark
    public void testAdd(final ContainsState state) {
        for (Integer i : state.randomValues) {
            state.collection.add(i);
        }
    }

    /**
     * Test removing the first occurrence of an element from a collection
     */
    @Benchmark
    public void removeFirstOccurrence(final ContainsState state) {
        for (Integer i : state.randomValues) {
            state.collection.removeFirst(i);
        }
    }

    /**
     * Test performing a search (lookup) for a given element on a collection
     */
    @Benchmark
    public void testSearch(final ContainsState state) {
        for (final Integer i : state.randomValues) {
            state.collection.contains(i);
        }
    }


    /**
     * Data values generated for each test
     */
    @State(Scope.Benchmark)
    public static class ContainsState {

        public Integer[] initialState;
        public Integer[] randomValues;

        public Collection<Integer> collection;

        @Setup(Level.Trial)
        public void init(final Main.GlobalValues values) {
            this.collection = values.newCollection();
            // Use a splittable random so we can generate values in a parallel manner.
            final SplittableRandom random = new SplittableRandom();

            this.initialState = random.ints(values.collectionSize, Integer.MIN_VALUE, 0).parallel().boxed()
                                      .toArray(Integer[]::new);

            this.randomValues = random.ints(values.sampleSize, 1, Integer.MAX_VALUE).parallel().boxed()
                      .toArray(Integer[]::new);

        }

        /**
         * Reset the {@link #collection} after every test trial/run
         */
        @Setup(Level.Iteration)
        public void reset() {
            // Clear the collection
            this.collection.clear();
            // Copy all elements from the initial state over
            for (Integer integer : this.initialState) {
                this.collection.add(integer);
            }
        }
    }

}
