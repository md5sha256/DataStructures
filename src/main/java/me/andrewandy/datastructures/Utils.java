package me.andrewandy.datastructures;

public class Utils {

    @SuppressWarnings("unchecked") public static <T> T unsafeCast(final Object o) {
        return (T) o;
    }

    public static long getExecutionTimeNanos(final Runnable runnable, final int trials) {
        final long start = System.nanoTime();
        for (int index = 0; index < trials; index++) {
            runnable.run();
        }
        final long end = System.nanoTime();
        return (end - start) / trials;
    }

    public static long getExecutionTimeNanos(final Runnable runnable, final int trials, final Runnable resetter) {
        long total = 0;
        resetter.run();
        for (int index = 0; index < trials; index++) {
            final long start = System.nanoTime();
            runnable.run();
            final long end = System.nanoTime();
            resetter.run();
            total += end - start;
        }
        return total / trials;
    }

}
