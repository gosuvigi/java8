package com.vigi.streams;

import java.util.function.Function;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * Created by vigi on 2/14/2015.
 */
public class ParallelStreams {

    private static final int ITERATIONS = 10;
    public static final int MAX_VALUE = 1_000_000;

    private static void measure(Function<Long, Long> f, long n) {
        long fastest = Long.MAX_VALUE;
        long median = 0;
        StringBuilder buff = new StringBuilder();
        long bogus = 0;
        for (int i = 0; i < ITERATIONS; i++) {
            long start = System.nanoTime();
            long sum = f.apply(n);
            bogus = sum - 1;
            long duration = (System.nanoTime() - start) / MAX_VALUE;
            median += duration;
            if (duration < fastest) {
                fastest = duration;
            }
        }
        median /= ITERATIONS;
        System.out.println("--- median: " + median + " fastest: " + fastest + " sum: " + (bogus + 1));
    }

    public static long sequentialSum(long n) {
        return Stream.iterate(1L, i -> i + 1)
                .limit(n)
                .reduce(0L, Long :: sum);
    }

    public static long iterativeSum(long n) {
        long sum = 0;
        for (long i = 1L; i <= n; i++) {
            sum += i;
        }
        return sum;
    }

    public static long parallelSum(long n) {
        return Stream.iterate(1L, i -> i + 1)
                .limit(n)
                .parallel()
                .reduce(0L, Long::sum);
    }

    public static long rangedSum(long n) {
        return LongStream.rangeClosed(1, n)
                .reduce(0L, Long :: sum);
    }

    public static long parallelRangedSum(long n) {
        return LongStream.rangeClosed(1, n)
                .parallel()
                .reduce(0L, Long::sum);
    }

    public static void main(String[] args) {
        System.out.println("--- sequential sum");
        measure(ParallelStreams ::sequentialSum, MAX_VALUE);

        System.out.println("--- iterative sum");
        measure(ParallelStreams ::iterativeSum, MAX_VALUE);

        System.out.println("--- parallel sum");
        measure(ParallelStreams ::parallelSum, MAX_VALUE);

        System.out.println("--- ranged sum");
        measure(ParallelStreams ::rangedSum, MAX_VALUE);

        System.out.println("--- parallel ranged sum");
        measure(ParallelStreams ::parallelRangedSum, MAX_VALUE);
    }
}
