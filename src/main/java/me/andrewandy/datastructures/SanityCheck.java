package me.andrewandy.datastructures;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class SanityCheck {

    public Integer[] dynamicSample;
    public Integer[] collection;

    public SanityCheck() {

    }

    public static void main(String[] args) {
        final SanityCheck sanityCheck = new SanityCheck();
        sanityCheck.init(10000, 1000);
        sanityCheck.testSearch();
        sanityCheck.testAdd();
        sanityCheck.testRemove();
    }

    public void init(int size, int sampleSize) {
        this.collection = new Integer[size];
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
            Integer[] copy = new Integer[collection.length + 1];
            for (int index = 0; index < collection.length; index++) {
                copy[index] = collection[index];
            }
            copy[collection.length] = i;
            this.collection = copy;
        }
        System.out.println("took: " + (System.currentTimeMillis() - start) + "ms.");
    }

    public void testRemove() {
        long start = System.currentTimeMillis();
        for (Integer toTest : dynamicSample) {
            int toRemove = -1;
            for (int index = 0; index < collection.length; index++) {
                if (Objects.equals(collection[index], toTest)) {
                    toRemove = index;
                    break;
                }
            }
            if (toRemove == -1) {
                break;
            }
            final Integer[] newArr = new Integer[collection.length - 1];
            int newIndex = 0;
            for (int index = 0; index < newArr.length; index++) {
                if (index == toRemove) {
                    continue;
                }
                newArr[newIndex++] = collection[index];
            }
            collection = newArr;
        }
        System.out.println("took: " + (System.currentTimeMillis() - start) + "ms.");
    }

}
