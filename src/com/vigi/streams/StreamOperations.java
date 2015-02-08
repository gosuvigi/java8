package com.vigi.streams;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

/**
 * Created by vigi on 2/8/2015.
 */
public class StreamOperations {

    public static void main(String[] args) {
        filterDistinctNumbers();
        filterUniqueWords();
        filterUniqueWordsFlattened();
        numbersSquared();
        numberPairs();
        findAny();
        reduce();
    }


    private static void filterDistinctNumbers() {
        List<Integer> ls = Arrays.asList(2, 6, 3, 4, 5, 2, 3, 1, 4, 1, 6, 7, 3);
        List<Integer> distinct = ls.stream()
                .filter(i -> i % 2 == 0)
                .distinct()
                .skip(1)
                .collect(toList());
        System.out.println("filtered distinct: " + distinct);
    }

    private static void filterUniqueWords() {
        List<String> words = Arrays.asList("Hello", "World");
        List<String[]> strings = words.stream()
                .map(word -> word.split(""))
                .distinct()
                .collect(toList());
        strings.stream()
                .forEach(arr -> System.out.println(Arrays.toString(arr)));
    }

    private static void filterUniqueWordsFlattened() {
        List<String> words = Arrays.asList("Hello", "World");
        List<String> strings = words.stream()
                .map(word -> word.split(""))
                .flatMap(Arrays::stream)
                .distinct()
                .collect(toList());
        System.out.println("uniques: " + strings);
    }

    private static void numbersSquared() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> squares = numbers.stream()
                .map(n -> n * n)
                .collect(toList());
        System.out.println("Squares: " + squares);
    }

    private static void numberPairs() {
        List<Integer> ls1 = Arrays.asList(1, 2, 3);
        List<Integer> ls2 = Arrays.asList(4, 5);
        List<int[]> pairs = ls1.stream()
                .flatMap(i -> ls2.stream()
                        .map(j -> new int[]{i, j}))
                .collect(toList());
        pairs.stream()
                .forEach(p -> System.out.println("Pair: " + Arrays.toString(p)));

        pairs.stream()
                .filter(p -> (p[0] + p[1]) % 6 == 0)
                .forEach(p -> System.out.println("Sum divisible by 6: " + Arrays.toString(p)));
    }

    private static void findAny() {
        Arrays.asList(1, 2, 3, 4, 5, 6).stream()
                .filter(i -> i > 4)
                .findFirst()
                .ifPresent(d -> System.out.println("found first: " + d));
    }

    private static void reduce() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6);
        Integer sum = numbers.stream()
                .reduce(0, (a, b) -> a + b);
        Integer product = numbers.stream()
                .reduce(1, (a, b) -> a * b);
        System.out.println("Sum: " + sum + " product: " + product);
        Optional<Integer> max = numbers.stream()
                .reduce(Integer::max);
    }

}
