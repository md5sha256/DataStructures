package me.andrewandy.datastructures;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;


public class DataStructureTest2 {

    private final Collection<Integer> collection;

    private final int testSize;
    private final Integer[] toAdd;
    private final Integer[] toCheckLookups;

    public DataStructureTest2(final Collection<Integer> collection, final int testSize) {
        this.collection = collection;
        this.testSize = testSize;
        this.toAdd = new Integer[testSize];
        this.toCheckLookups = new Integer[10000];
        loadValues();
        collection.addAll(Arrays.asList(toAdd));
    }

    private void loadValues() {
        final ThreadLocalRandom random = ThreadLocalRandom.current();
        final int shift = testSize / 2;
        for (int index = 0; index < testSize; index++) {
            toAdd[index] = index - shift;
        }
        for (int index = 0; index < toCheckLookups.length; index++) {
            toCheckLookups[index] = random.nextInt(0, testSize);
        }
    }

    private void test() {
        for (final Integer i : toCheckLookups) {
            collection.contains(i);
        }
    }

    private long runTrials() {
        return Utils.getExecutionTimeNanos(this::test, 50);
    }

    public void runTest(final int runs) {
        System.out.println("---- " + collection.getClass().getName() + " Test ----");
        System.out.println(" ");
        System.out.println("Number Range: " + testSize);
        System.out.println("Trials: " + runs);
        final long[] nanos = new long[runs];
        final long start = System.nanoTime();
        for (int index = 0; index < runs; index++) {
            nanos[index] = runTrials();
            System.out.println("Running... Index: " + (index + 1));
        }
        final long end = System.nanoTime();
        int index = 0;
        long avg = 0;
        for (final long totalNanos : nanos) {
            index++;
            if (avg != 0) {
                avg += totalNanos;
                avg /= 2;
            } else {
                avg += totalNanos;
            }
            final double seconds = totalNanos / 1000d / 1000d / 1000d;
            final double singleNanos = (double) totalNanos / toCheckLookups.length;
            System.out.println("Trial " + index + " | Execution Time: " + seconds
                                   + "s | Average Time Per Operation: " + singleNanos + "ns");
        }
        final double seconds = avg / 1000d / 1000d / 1000d;
        final double singleNanos = (double) avg / toCheckLookups.length;
        System.out.println(
            "Average Execution Time: " + seconds + "s | Average Time Per Operation: " + singleNanos
                + "ns");
        System.out.println(
            "Total Execution Time: " + ((end - start) / 1000d / 1000d / 1000d) + "s");
        System.out.println(" ");

    }


}
