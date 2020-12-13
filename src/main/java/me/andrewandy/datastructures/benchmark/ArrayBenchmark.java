package me.andrewandy.datastructures.benchmark;

import me.andrewandy.datastructures.Main;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.CompilerControl;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import java.util.Objects;
import java.util.SplittableRandom;
import java.util.concurrent.TimeUnit;

/**
 * Benchmark for the primitive array in Java
 * Methods annotated with {@link Benchmark} test
 * a specific operation; These methods are equivalent
 * to those in {@link BaseBenchmark}, albeit with inlined logic.
 */
@CompilerControl(CompilerControl.Mode.EXCLUDE)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class ArrayBenchmark {

    @Benchmark
    public void testAdd(final ContainsState state) {
        // Loop over the test sample
        for (Integer i : state.randomValues) {
            Integer[] copy = new Integer[state.collection.length + 1];
            // Copy over existing elements
            for (int index = 0; index < state.collection.length; index++) {
                copy[index] = state.collection[index];
            }
            // Set the index at last value to the target value to be inserted.
            copy[state.collection.length] = i;
            // Re-assign the object reference to the copied array
            state.collection = copy;
        }
    }

    @Benchmark
    public void removeFirstOccurrence(final ContainsState state) {
        for (Integer toTest : state.randomValues) {
            int toRemove = -1;
            for (int index = 0; index < state.collection.length; index++) {
                if (Objects.equals(state.collection[index], toTest)) {
                    toRemove = index;
                    break;
                }
            }
            if (toRemove == -1) {
                continue;
            }
            final Integer[] newArr = new Integer[state.collection.length - 1];
            int newIndex = 0;
            for (int index = 0; index < newArr.length; index++) {
                if (index == toRemove) {
                    continue;
                }
                newArr[newIndex++] = state.collection[index];
            }
            state.collection = newArr;
        }
    }

    @Benchmark
    public void testContains(final ContainsState state) {
        for (Integer toTest : state.randomValues) {
            for (int index = 0; index < state.collection.length; index++) {
                if (Objects.equals(state.collection[index], toTest)) {
                    break;
                }
            }
        }
    }


    @State(Scope.Benchmark)
    public static class ContainsState {
        public Integer[] initialState;
        public Integer[] randomValues;
        public Integer[] collection;
        private Main.ArrayValues values;

        @Setup(Level.Trial)
        public void init(final Main.ArrayValues values) {
            this.values = values;
            this.collection = new Integer[values.collectionSize];
            final SplittableRandom random = new SplittableRandom();
            this.initialState = random.ints(values.collectionSize, Integer.MIN_VALUE, 0).parallel().boxed()
                                      .toArray(Integer[]::new);
            this.randomValues =
                random.ints(values.sampleSize, 1, Integer.MAX_VALUE).parallel().boxed().toArray(Integer[]::new);
        }

        @Setup(Level.Iteration)
        public void reset() {
            this.collection = new Integer[values.collectionSize];
            System.arraycopy(initialState, 0, this.collection, 0, collection.length);
        }
    }

}
