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
            copy[state.collection.length] = i;
            state.collection = copy;
        }
    }

    @Benchmark
    public void removeFirst(final ContainsState state) {
        for (Integer toTest : state.removeDynamicSample) {
            int toRemove = -1;
            //System.out.println("*** " + Arrays.toString(state.collection));
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
        for (Integer toTest : state.dynamicSample) {
            for (int index = 0; index < state.collection.length; index++) {
                if (Objects.equals(state.collection[index], toTest)) {
                    break;
                }
            }
        }
    }


    @State(Scope.Benchmark)
    public static class ContainsState {
        public Integer[] staticSample;
        public Integer[] dynamicSample;
        public Integer[] removeDynamicSample;
        public Integer[] collection;
        private Main.ArrayValues values;

        @Setup(Level.Trial)
        public void init(final Main.ArrayValues values) {
            this.values = values;
            this.collection = new Integer[values.collectionSize];

            this.staticSample = new Integer[values.collectionSize];
            final ThreadLocalRandom random = ThreadLocalRandom.current();
            for (int index = 0; index < this.staticSample.length; index++) {
                staticSample[index] = random.nextInt(Integer.MIN_VALUE, 0);
            }
            this.dynamicSample = new Integer[values.sampleSize];
            this.removeDynamicSample = new Integer[values.sampleSize];
            for (int index = 0; index < this.dynamicSample.length; index++) {
                dynamicSample[index] = random.nextInt(1, Integer.MAX_VALUE);
                removeDynamicSample[index] = dynamicSample[index];
            }
            removeDynamicSample[dynamicSample.length - 1] = staticSample[staticSample.length - 1];
        }

        @Setup(Level.Iteration)
        public void reset() {
            collection = new Integer[values.collectionSize];
            System.arraycopy(staticSample, 0, this.collection, 0, collection.length);
        }
    }

}
