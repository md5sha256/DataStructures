package com.github.md5sha256.datastructures.benchmark;

import com.github.md5sha256.datastructures.Main;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.CompilerControl;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import java.util.SplittableRandom;
import java.util.concurrent.TimeUnit;
import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;

/**
 * Benchmark for the primitive array in Java
 * Methods annotated with {@link Benchmark} test
 * a specific operation; These methods are equivalent
 * to those in {@link BaseBenchmark}, albeit with inlined logic.
 */
@CompilerControl(CompilerControl.Mode.EXCLUDE)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class ArrayBenchmark {

    /**
     * Test appending values to an array (tail insertion)
     */
    @Benchmark
    public void testAdd(final ContainsState state) {
        // Loop over the test sample
        for (Integer i : state.randomValues) {
            // Worst-Case Time Complexity = O(2n + 1) = O(n)

            // Create a temp array
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

    /**
     * Test removing the first occurrence of an element from an array.
     */
    @Benchmark
    public void testRemoveFirstOccurrence(final ContainsState state) {
        for (Integer toTest : state.initialStateReversed) {
            // Worst-Case Time Complexity = O(2n - 1) = O(n)

            int toRemove = -1;
            if (toTest == null) {
                for (int index = 0; index < state.collection.length; index++) {
                    if (state.collection[index] == null) {
                        // Mark index to remove
                        toRemove = index;
                        break;
                    }
                }
            } else {
                for (int index = 0; index < state.collection.length; index++) {
                    if (state.collection[index].equals(toTest)) {
                        // Mark index to remove
                        toRemove = index;
                        break;
                    }
                }
            }
            if (toRemove == -1) {
                continue;
            }
            // Create temp array
            final Integer[] newArr = new Integer[state.collection.length - 1];
            int newIndex = 0;
            for (int index = 0; index < newArr.length; index++) {
                if (index == toRemove) {
                    // Skip index to remove
                    continue;
                }
                newArr[newIndex++] = state.collection[index];
            }
            state.collection = newArr;
        }
    }

    /**
     * Test performing a search (lookup) for a given element in an array
     */
    @Benchmark
    public void testSearch(final ContainsState state) {
        for (Integer toTest : state.randomValues) {
            // Worst-Case Time Complexity = O(n)

            if (toTest == null) {
                // Loop over all elements
                for (int index = 0; index < state.collection.length; index++) {
                    // Check if object equals target | Worst-Case Time Complexity = O(1)
                    if (state.collection[index] == null) {
                        break;
                    }
                }
            } else {
                // Loop over all elements
                for (int index = 0; index < state.collection.length; index++) {
                    // Check if object equals target | Worst-Case Time Complexity = O(1)
                    if (toTest.equals(state.collection[index])) {
                        break;
                    }
                }
            }
        }
    }


    /**
     * Data values generated for each test
     */
    @State(Scope.Benchmark)
    public static class ContainsState {

        public Integer[] initialState;
        public Integer[] randomValues;
        public Integer[] initialStateReversed;
        public Integer[] collection;
        private Main.ArrayValues values;

        @Setup(Level.Trial)
        public void init(final Main.ArrayValues values) {
            this.values = values;
            this.collection = new Integer[values.collectionSize];
            // Use a splittable random so we can generate values in a parallel manner.
            final RandomGenerator random = new SplittableRandom();

            this.initialState = random.ints(values.collectionSize, Integer.MIN_VALUE, 0).parallel().boxed()
                                      .toArray(Integer[]::new);
            this.randomValues = random.ints(values.sampleSize, 1, Integer.MAX_VALUE).parallel().boxed()
                      .toArray(Integer[]::new);

            // Populate reversed initial state for use in array removals.
            this.initialStateReversed = new Integer[values.sampleSize];
            int j = values.collectionSize - 1;
            for (int i = 0; i < initialStateReversed.length; i++) {
                this.initialStateReversed[i] = j == -1 ? 1 : this.initialState[j--];
            }

        }

        /**
         * Reset the {@link #collection} after every test trial/run
         */
        @Setup(Level.Iteration)
        public void reset() {
            // Create a new array
            this.collection = new Integer[this.values.collectionSize];
            // Copy the initial values over
            System.arraycopy(this.initialState, 0, this.collection, 0, this.collection.length);
        }
    }

}
