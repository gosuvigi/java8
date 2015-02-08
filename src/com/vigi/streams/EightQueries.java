package com.vigi.streams;

import com.vigi.Trader;
import com.vigi.Transaction;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

/**
 * Created by vigi on 2/8/2015.
 */
public class EightQueries {

    private static final Trader raoul = new Trader("Raoul", "Cambridge");
    private static final Trader mario = new Trader("Mario", "Milan");
    private static final Trader alan = new Trader("Alan", "Cambridge");
    private static final Trader brian = new Trader("Brian", "Cambridge");

    private static final List<Trader> traders = Arrays.asList(raoul, mario, alan, brian);

    private static final List<Transaction> transactions = Arrays.asList(
            new Transaction(brian, 2011, 300),
            new Transaction(raoul, 2012, 1000),
            new Transaction(raoul, 2011, 400),
            new Transaction(mario, 2012, 710),
            new Transaction(mario, 2012, 700),
            new Transaction(alan, 2012, 950)
    );

    /**
     * Find all transactions in the year 2011 and sort them by value (small to high).
     */
    private static void transactionsIn2011() {
        List<Transaction> list = transactions.stream()
                .filter(t -> t.getYear() == 2011)
                .sorted(comparing(Transaction::getValue))
                .collect(toList());
        System.out.println("--- 2011 sorted transactions: " + list);
    }

    /**
     *  What are all the unique cities where the traders work?
     */
    private static void uniqueCities() {
        List<String> cities = traders.stream()
                .map(Trader::getCity)
                .distinct()
                .collect(toList());
        System.out.println("--- unique cities: " + cities);
    }

    /**
     * Find all traders from Cambridge and sort them by name.
     */
    private static void tradersFromCambridge() {
        List<Trader> traderList = traders.stream()
                .filter(t -> "Cambridge".equals(t.getCity()))
                .distinct()
                .sorted(comparing(Trader::getName))
                .collect(toList());
        System.out.println("--- traders from Cambridge: " + traderList);
    }

    /**
     * Return a string of all traders’ names sorted alphabetically.
     */
    private static void traderNames() {
        List<String> names = traders.stream()
                .map(Trader::getName)
                .distinct()
                .sorted(comparing(String::toString))
                .collect(toList());
        System.out.println("--- sorted names: " + names);
    }

    /**
     * Are any traders based in Milan?
     */
    private static void tradersInMilan() {
        boolean exists = traders.stream()
                .anyMatch(t -> "Milan".equals(t.getCity()));
        System.out.println("--- traders in Milan: " + exists);
    }

    /**
     * Print all transactions’ values from the traders living in Cambridge.
     */
    private static void cambridgeTransactionValues() {
        List<Integer> transactionList = transactions.stream()
                .filter(tran -> "Cambridge".equals(tran.getTrader().getCity()))
                .map(Transaction::getValue)
                .collect(toList());
        System.out.println("--- cambridge transactions: " + transactionList);
    }

    /**
     * What’s the highest value of all the transactions?
     */
    private static void highestTransactionValue() {
        Optional<Integer> max = transactions.stream()
                .max(comparing(Transaction::getValue))
                .map(Transaction::getValue);
        System.out.println("--- max transaction: " + max.get());
    }

    private static void smallestTransaction() {
        Optional<Transaction> min = transactions.stream()
                .min(comparing(Transaction::getValue));
        System.out.println("--- min transaction: " + min.get());
    }

    public static void main(String[] args) {
        transactionsIn2011();
        uniqueCities();
        tradersFromCambridge();
        traderNames();
        tradersInMilan();
        cambridgeTransactionValues();
        highestTransactionValue();
        smallestTransaction();
    }

}
