package me.andrewandy.datastructures;

public class Main {

    public static void main(String[] args) {

        final int testSize = 100_000;

        try {
            new DataStructureTest(new LinkedList<>(), testSize).runTest(5);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.gc();
        try {
            new DataStructureTest(new FastReadHashSet<>(10, 0.75f), testSize).runTest(5);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.gc();
        try {
            new DataStructureTest(new FastWriteHashSet<>(testSize), testSize).runTest(5);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //new DataStructureTest2(new java.util.LinkedList<>(), 10000).runTest(3)
        //new DataStructureTest2(new java.util.HashSet<>(10, 0.75f), 10_000_000).runTest(3);
    }

}
