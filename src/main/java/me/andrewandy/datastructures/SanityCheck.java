package me.andrewandy.datastructures;

import me.andrewandy.datastructures.benchmark.BaseBenchmark;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class SanityCheck {

    public Integer[] dynamicSample;
    public Collection<Integer> collection;

    public SanityCheck() {

    }

    public static void main(String[] args) {
        /*
        final Main.GlobalValues values = new Main.GlobalValues();
        values.collection = "FixedSizeHashSet";
        values.collectionSize = 10;
        final BaseBenchmark.ContainsState containsState = new BaseBenchmark.ContainsState();
        final BaseBenchmark benchmark = new BaseBenchmark();
        containsState.init(values);
        containsState.reset();
        long now = System.currentTimeMillis();
        benchmark.testAdd(containsState);
        System.out.println((System.currentTimeMillis()) - now);
         */
        final Collection<Integer> collection = new FixedSizeHashSet<>(100);
        for (int i = -101; i < -1; i++) {
            collection.add(-i);
        }
        long now = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            collection.add(i);
        }
        System.out.println((System.currentTimeMillis() - now));
    }

    public void init(int size, int sampleSize) {
        this.collection = new LinkedList<>();
        ThreadLocalRandom random = ThreadLocalRandom.current();
        this.dynamicSample = new Integer[sampleSize];
        for (int index = 0; index < this.dynamicSample.length; index++) {
            dynamicSample[index] = random.nextInt(1, Integer.MAX_VALUE);
        }
    }

    public void testSearch() {
        long start = System.currentTimeMillis();
        for (Integer toCheck : dynamicSample) {
            for (Integer inCollection : collection) {
                if (Objects.equals(inCollection, toCheck)) {
                    break;
                }
            }
        }
        System.out.println("took: " + (System.currentTimeMillis() - start) + "ms.");
    }

    public void testAdd() {
        long start = System.currentTimeMillis();
        for (Integer i : dynamicSample) {
           collection.add(i);
        }
        System.out.println("took: " + (System.currentTimeMillis() - start) + "ms.");
    }

    public void testRemove() {
        long start = System.currentTimeMillis();
        for (Integer toTest : dynamicSample) {
            collection.removeFirst(toTest);
        }
        System.out.println("took: " + (System.currentTimeMillis() - start) + "ms.");
    }

}
