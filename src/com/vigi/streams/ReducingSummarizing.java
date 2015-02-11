package com.vigi.streams;

import com.vigi.Dish;

import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static com.vigi.Dish.MENU;
import static java.util.stream.Collectors.*;

/**
 * Created by vigi on 2/11/2015.
 */
public class ReducingSummarizing {

    private static void maximum() {
        Comparator<Dish> byCaloriesComparator = Comparator.comparingInt(Dish::getCalories);
        Optional<Dish> maxCaloriesDish = MENU.stream()
                .collect(maxBy(byCaloriesComparator));
        System.out.println("--- max by calories: " + maxCaloriesDish);
    }

    private static void summarization() {
        int sum = MENU.stream()
                .collect(summingInt(Dish::getCalories));
        Double average = MENU.stream()
                .collect(averagingDouble(Dish::getCalories));
        System.out.println("--- sum of calories: " + sum + " average: " + average);

        IntSummaryStatistics statistics = MENU.stream()
                .collect(summarizingInt(Dish::getCalories));
        System.out.println("--- statistics: " + statistics);
    }

    private static void joiningStrings() {
        String names = MENU.stream()
                .map(Dish::getName)
                .collect(joining(", "));
        System.out.println("--- names: " + names);
    }

    public static void main(String[] args) {
        maximum();
        summarization();
        joiningStrings();
    }
}
