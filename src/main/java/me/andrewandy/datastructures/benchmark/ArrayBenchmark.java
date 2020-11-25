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
            for (int index = 0; index < state.collection.length; index++) {
                copy[index] = state.collection[index];
            }
            copy[state.collection.length - 1] = i;
            state.collection = copy;
        }
    }

    @Benchmark
    public void removeAllOccurrences(final ContainsState state) {

        for (int i = 0; i < state.dynamicSample.length; i++) {
            Integer[] newValues = new Integer[state.collection.length];
            int skipped = 0;
            for (int j = 0; j < state.collection.length; j++) {
                if (!Objects.equals(state.collection[j], state.dynamicSample[i])) {
                    newValues[j] = state.collection[j];
                    skipped++;
                }
            }
            if (skipped == 0) {
                continue;
            }
            Integer[] finalValues = new Integer[newValues.length - skipped];
            for (int index = 0; index < finalValues.length; index++) {
                finalValues[index] = newValues[index];
            }
            state.collection = finalValues;
        }
    }

    @Benchmark
    public void testContains(final ContainsState state) {
        for (Integer val : state.dynamicSample) {
            for (int index = 0; index < state.collection.length; index++) {
                if (Objects.equals(state.collection[index], val)) {
                    break;
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
