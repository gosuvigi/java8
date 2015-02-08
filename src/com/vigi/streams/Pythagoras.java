package com.vigi.streams;

import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by vigi on 2/8/2015.
 */
public class Pythagoras {

    public static void main(String[] args) {
        Stream<int[]> triples1 = pythagoras1();
        triples1.limit(25)
                .forEach(t -> System.out.println(t[0] + ", " + t[1] + ", " + t[2]));

        Stream<double[]> triples2 = pythagoras2();
        triples2.limit(25)
                .forEach(t -> System.out.println(t[0] + ", " + t[1] + ", " + t[2]));
    }

    private static Stream<int[]> pythagoras1() {
        return IntStream.rangeClosed(1, 100)
                .boxed()
                .flatMap(a -> IntStream.rangeClosed(1, 100)
                        .filter(b -> Math.sqrt(a * a + b * b) % 1 == 0)
                        .mapToObj(b -> new int[]{a, b, (int) Math.sqrt(a * a + b * b)}));
    }

    private static Stream<double[]> pythagoras2() {
        return IntStream.rangeClosed(1, 100)
                .boxed()
                .flatMap(a -> IntStream.rangeClosed(1, 100)
                        .mapToObj(b -> new double[]{a, b, Math.sqrt(a * a + b * b)})
                .filter(t -> t[2] % 1 == 0));
    }
}
