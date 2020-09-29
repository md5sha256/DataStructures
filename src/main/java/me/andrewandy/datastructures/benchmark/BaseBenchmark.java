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

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@CompilerControl(CompilerControl.Mode.EXCLUDE)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class BaseBenchmark {

    @Benchmark
    public void testAdd(final AddRemoveState state) {
        for (Integer i : state.dynamicSample) {
            state.collection.add(i);
        }
    }

    @Benchmark
    public void removeAllOccurrences(final AddRemoveState state) {
        for (Integer i : state.dynamicSample) {
            state.collection.remove(i);
        }
    }

    @Benchmark
    public void testContains(final ContainsState state) {
        for (final Integer i : state.dynamicSample) {
            state.collection.contains(i);
        }
    }


    @State(Scope.Benchmark)
    public static class AddRemoveState extends ContainsState {

        @Setup(Level.Iteration)
        public void reset() {
            collection.clear();
            collection.addAll(staticSample);
        }

    }


    @State(Scope.Benchmark)
    public static class ContainsState {
        public Integer[] staticSample;
        public Integer[] dynamicSample;
        public Collection<Integer> collection;

        public ContainsState() {

        }

        @Setup(Level.Trial)
        public void init(final Main.GlobalValues values) {
            this.collection = values.newCollection();

            this.staticSample = new Integer[values.sampleSize];
            final ThreadLocalRandom random = ThreadLocalRandom.current();
            for (int index = 0; index < this.staticSample.length; index++) {
                staticSample[index] = random.nextInt(Integer.MIN_VALUE, 0);
            }
            this.dynamicSample = new Integer[values.collectionSize()];
            for (int index = 0; index < this.dynamicSample.length; index++) {
                dynamicSample[index] = random.nextInt(1, Integer.MAX_VALUE);
            }
        }
    }

}
