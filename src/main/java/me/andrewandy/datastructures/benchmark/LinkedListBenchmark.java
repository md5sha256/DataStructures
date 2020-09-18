package me.andrewandy.datastructures.benchmark;

import me.andrewandy.datastructures.Collection;
import me.andrewandy.datastructures.LinkedList;
import me.andrewandy.datastructures.Main;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.CompilerControl;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@CompilerControl(CompilerControl.Mode.EXCLUDE) @OutputTimeUnit(TimeUnit.MILLISECONDS)
public class LinkedListBenchmark {

    @Benchmark public void testSingularAdd(final AddState state) {
        for (final Integer i : state.sample) {
            state.collection.add(i);
        }
    }

    @Benchmark public void testBulkAdd(final AddState state) {
        state.collection.addAll(state.sample);
    }

    @Benchmark public void testSingularRemove(final RemovalState state) {
        for (final Integer i : state.sample) {
            state.collection.add(i);
        }
    }

    @Benchmark public void testBulkRemove(final RemovalState state) {
        state.collection.removeAll(state.sample);
    }

    @Benchmark public void testContains(final ContainsState state) {
        for (final Integer i : state.sample) {
            state.collection.contains(i);
        }
    }

    @State(Scope.Benchmark) public static class AddState {
        public Integer[] sample;
        public Collection<Integer> collection;

        public AddState() {
        }

        @Setup(Level.Trial) public void init(final Main.GlobalValues values) {
            this.collection = new LinkedList<>();
            this.sample = new Integer[values.size()];
            final Random random = new Random();
            for (int index = 0; index < values.size(); index++) {
                sample[index] = random.nextInt();
            }
        }

        public final void resetEmpty() {
            this.collection.clear();
        }

        public final void resetFilled() {
            this.collection.clear();
            this.collection.addAll(sample);
        }
    }


    @State(Scope.Benchmark) public static class RemovalState {
        public Integer[] sample;
        public Collection<Integer> collection;

        public RemovalState() {

        }

        @Setup(Level.Trial) public void init(final Main.GlobalValues values) {
            this.collection = new LinkedList<>();
            this.sample = new Integer[values.size()];
            final Random random = new Random();
            for (int index = 0; index < values.size(); index++) {
                sample[index] = random.nextInt();
            }
        }

        public final void resetEmpty() {
            this.collection.clear();
        }

        public final void resetFilled() {
            this.collection.clear();
            this.collection.addAll(sample);
        }
    }


    @State(Scope.Benchmark) public static class ContainsState {
        public Integer[] sample;
        public Collection<Integer> collection;
        public int initialAdded, lastAdded;

        public ContainsState() {

        }

        @Setup(Level.Trial) public void init(final Main.GlobalValues values) {
            this.collection = new LinkedList<>();
            this.sample = new Integer[values.size()];
            final Random random = new Random();
            final int i = random.nextInt();
            initialAdded = i;
            sample[0] = i;
            for (int index = 1; index < values.size(); index++) {
                sample[index] = random.nextInt();
            }
            lastAdded = sample[sample.length - 1];
        }

        public final void resetEmpty() {
            this.collection.clear();
        }

        public final void resetFilled() {
            this.collection.clear();
            this.collection.addAll(sample);
        }
    }

}
