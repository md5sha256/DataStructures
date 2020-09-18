package me.andrewandy.datastructures;

public class Utils {

    @SuppressWarnings("unchecked")
    public static <T> T unsafeCast(Object o) {
        return (T) o;
    }

    public static long getExecutionTimeNanos(Runnable runnable, int trials) {
        long start = System.nanoTime();
        for (int index = 0; index < trials; index++) {
            runnable.run();
        }
        long end = System.nanoTime();
        return (end - start) / trials;
    }

    public static long getExecutionTimeNanos(Runnable runnable, int trials, Runnable resetter) {
        long total = 0;
        resetter.run();
        for (int index = 0; index < trials; index++) {
            long start = System.nanoTime();
            runnable.run();
            long end = System.nanoTime();
            resetter.run();
            total += end - start;
        }
        return total / trials;
    }

}
