package com.vigi.streams;

import com.vigi.Dish;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static com.vigi.Dish.MENU;
import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.*;

/**
 * Created by vigi on 2/11/2015.
 */
public class Grouping {

    public enum CaloricLevel {DIET, NORMAL, FAT}

    private static void simpleGrouping() {
        Map<Dish.Type, List<Dish>> byType = MENU.stream()
                .collect(groupingBy(Dish::getType));
        System.out.println("--- grouped by type: " + byType);

        Map<CaloricLevel, List<Dish>> byCalories = MENU.stream()
                .collect(groupingBy(caloricLevel()));
        System.out.println("--- grouped by calories: " + byCalories);
    }

    private static Function<Dish, CaloricLevel> caloricLevel() {
        return dish -> {
            if (dish.getCalories() < 400) return CaloricLevel.DIET;
            else if (dish.getCalories() <= 700) return CaloricLevel.NORMAL;
            else return CaloricLevel.FAT;
        };
    }

    private static void multiLevelGrouping() {
        Map<Dish.Type, Map<CaloricLevel, List<Dish>>> groups = MENU.stream()
                .collect(groupingBy(Dish::getType, groupingBy(caloricLevel())));
        System.out.println("--- multiple grouping: " + groups);
    }

    private static void subgroups() {
        Map<Dish.Type, Long> withCount = MENU.stream()
                .collect(groupingBy(Dish::getType, counting()));
        System.out.println("--- with count: " + withCount);
    }

    private static void highestCaloriesInGroup() {
        Map<Dish.Type, Dish> group = MENU.stream()
                .collect(groupingBy(Dish::getType, collectingAndThen(
                        maxBy(comparingInt(Dish::getCalories)),
                        Optional::get
                )));
        System.out.println("--- highest calories: " + group);
    }

    public static void main(String[] args) {
        simpleGrouping();
        multiLevelGrouping();
        subgroups();
        highestCaloriesInGroup();
    }
}
