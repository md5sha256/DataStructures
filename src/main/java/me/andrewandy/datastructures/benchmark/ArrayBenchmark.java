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
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@CompilerControl(CompilerControl.Mode.EXCLUDE)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class ArrayBenchmark {

    @Benchmark
    public void testAdd(final ContainsState state) {
        for (Integer i : state.dynamicSample) {
            Integer[] copy = new Integer[state.collection.length + 1];
            int index = 0;
            for (Integer integer : state.collection) {
                copy[index++] = integer;
            }
            copy[index] = i;
            state.collection = copy;
        }
    }

    @Benchmark
    public void removeAllOccurrences(final ContainsState state) {
        for (Integer i : state.dynamicSample) {
            int[] toRemove = new int[0];
            int index = 0;
            for (Integer value : state.collection) {
                if (Objects.equals(value, i)) {
                    int[] temp = new int[toRemove.length + 1];
                    int j = 0;
                    for (int k : toRemove) {
                        temp[j++] = k;
                    }
                    temp[j] = index;
                    toRemove = temp;
                }
            }
            Integer[] newValues = new Integer[state.collection.length - toRemove.length];
            for (int counter = 0; counter < state.collection.length; counter++) {
                boolean skip = false;
                for (int num : toRemove) {
                    if (counter == num) {
                        skip = true;
                        break;
                    }
                }
                if (skip) {
                    continue;
                }
                newValues[counter] = state.collection[counter];
            }
            state.collection = newValues;
        }
    }

    @Benchmark
    public void testContains(final ContainsState state) {
        for (int j = 0; j < state.dynamicSample.length; j++) {
            final Integer val = state.dynamicSample[j];
            if (val == null) {
                for (int k = 0; k < state.collection.length; k++) {
                    if (state.collection[k] == null) {
                        break;
                    }
                }
            } else {
                for (int k = 0; k < state.collection.length; k++) {
                    Integer val2 = state.collection[k];
                    if (val2 != null && val.intValue() == val2.intValue()) {
                        break;
                    }
                }
            }
        }
    }


    @State(Scope.Benchmark)
    public static class ContainsState {
        public Integer[] staticSample;
        public Integer[] dynamicSample;
        public Integer[] collection;
        private Main.ArrayValues values;

        @Setup(Level.Trial)
        public void init(final Main.ArrayValues values) {
            this.values = values;
            this.collection = new Integer[values.collectionSize];

            this.staticSample = new Integer[values.sampleSize];
            final ThreadLocalRandom random = ThreadLocalRandom.current();
            for (int index = 0; index < this.staticSample.length; index++) {
                staticSample[index] = random.nextInt(Integer.MIN_VALUE, 0);
            }
            this.dynamicSample = new Integer[values.collectionSize];
            for (int index = 0; index < this.dynamicSample.length; index++) {
                dynamicSample[index] = random.nextInt(1, Integer.MAX_VALUE);
            }
        }

        @Setup(Level.Iteration)
        public void reset() {
            collection = new Integer[values.collectionSize];
            System.arraycopy(staticSample, 0, this.collection, 0,
                             Math.min(values.collectionSize, values.sampleSize));
        }

    }

}
