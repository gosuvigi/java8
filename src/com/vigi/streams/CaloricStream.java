package com.vigi.streams;

import com.vigi.Dish;

import java.util.List;
import java.util.Map;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

/**
 * Created by vigi on 2/8/2015.
 */
public class CaloricStream {

    /**
     * Return the names of dishes that are low in calories, sorted by number of calories.
     *
     * @param dishes
     * @return
     */
    private static List<String> lowCaloricDishes(List<Dish> dishes) {
        return dishes.stream()
                .filter(d -> {
                    System.out.println("Filtering: " + d.getName() + " with calories: " + d.getCalories());
                    return d.getCalories() > 300;
                })
                .sorted(comparing(Dish::getCalories))
                .map(d -> {
                    System.out.println("Mapping: " + d.getName());
                    return d.getName();
                })
                .collect(toList());
    }

    private static Map<Dish.Type, List<Dish>> groupByType(List<Dish> dishes) {
        return dishes.stream()
                .collect(groupingBy(Dish::getType));
    }

    public static void main(String[] args) {
        System.out.println(lowCaloricDishes(Dish.MENU));
    }

}
