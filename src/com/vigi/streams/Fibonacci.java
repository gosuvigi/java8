package com.vigi.streams;

import java.util.stream.Stream;

/**
 * Created by vigi on 2/8/2015.
 */
public class Fibonacci {

    public static void main(String[] args) {
        Stream.iterate(new int[] {0, 1}, n -> new int[] { n[1], n[0] + n[1]} )
                .limit(25)
                .forEach(t -> System.out.println("(" + t[0] + "," + t[1] +")"));
    }
}
