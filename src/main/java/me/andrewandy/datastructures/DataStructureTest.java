package me.andrewandy.datastructures;

import java.util.concurrent.ThreadLocalRandom;

public class DataStructureTest {

    private static final Integer[] toCheckLookups = new Integer[10000];

    static {
        final ThreadLocalRandom random = ThreadLocalRandom.current();
        for (int index = 0; index < toCheckLookups.length; index++) {
            toCheckLookups[index] = random.nextInt();
        }
    }

    private final Collection<Integer> collection;
    private final int testSize;
    private final Integer[] toAdd;

    public DataStructureTest(final Collection<Integer> collection, final int testSize) {
        this.collection = collection;
        this.testSize = testSize;
        this.toAdd = new Integer[testSize];
        loadValues();
    }

    private void loadValues() {
        final int shift = testSize / 2;
        for (int index = 0; index < toAdd.length; index++) {
            toAdd[index] = index - shift;
        }
    }

    private void testContains() {
        for (Integer i : toCheckLookups) {
            collection.contains(i);
        }
    }

    private void testAdd() {
        collection.addAll(toAdd);
    }

    private void testRemove() {
        collection.removeAll(toAdd);
    }

    private void runTestResults(final String name, Runnable toRun, int runs, long sampleSize,
                                Runnable resetter) {
        System.out.println(" ");
        System.out.println("Test Name: " + name);
        System.out.println("Trials: " + runs);
        System.out.println(" ");
        long start = System.nanoTime();
        long avg = 0;
        for (int index = 0; index < runs; index++) {
            long test = Utils.getExecutionTimeNanos(toRun, 10, resetter);
            if (avg != 0) {
                avg += test;
                avg /= 2;
            } else {
                avg += test;
            }
        }
        long end = System.nanoTime();
        double elapsed = (end - start) / 1000d / 1000d / 1000d;
        final double singleNanos = (double) avg / sampleSize;
        System.out.println("Average Time Per Operation: " + singleNanos + "ns");
        System.out.println("Test Execution Time: " + elapsed + "s");
    }

    public void runTest(int runs) {
        System.out.println("---- " + collection.getClass().getName() + " Test ----");
        runTestResults("Bulk Add", this::testAdd, runs, testSize, collection::clear);
        runTestResults("Bulk Remove", this::testRemove, runs, testSize,
                       () -> collection.addAll(toAdd));
        runTestResults("Bulk Contains", this::testContains, runs, toCheckLookups.length, () -> {
        });
        System.out.println(" ");
    }


}
