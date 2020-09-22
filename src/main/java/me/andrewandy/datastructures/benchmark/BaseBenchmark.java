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

import java.util.Random;
import java.util.concurrent.TimeUnit;

@CompilerControl(CompilerControl.Mode.EXCLUDE) @OutputTimeUnit(TimeUnit.MILLISECONDS)
public class BaseBenchmark {

    @Benchmark public void testAdd(final AddState state) {
        state.collection.addAll(state.sample);
    }

    @Benchmark public void testRemove(final RemovalState state) {
        state.collection.removeAll(state.sample);
    }

    @Benchmark public void testContains(final ContainsState state) {
        for (final Integer i : state.containsSample) {
            state.collection.contains(i);
        }
    }


    @State(Scope.Benchmark) public static class AddState {
        public Integer[] sample;
        public Collection<Integer> collection;

        public AddState() {
        }

        @Setup(Level.Trial) public void init(final Main.GlobalValues values) {
            this.collection = values.newCollection();
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
            this.collection = values.newCollection();
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
        public Integer[] containsSample;
        public Collection<Integer> collection;

        public ContainsState() {

        }

        @Setup(Level.Trial) public void init(final Main.GlobalValues values) {
            this.collection = values.newCollection();
            this.containsSample = new Integer[100000];
            final Random random = new Random();
            for (int index = 0; index < this.containsSample.length; index++) {
                containsSample[index] = random.nextInt();
            }
        }

        public final void resetEmpty() {
            this.collection.clear();
        }

        public final void resetFilled() {
            this.collection.clear();
            this.collection.addAll(containsSample);
        }
    }

}
