package com.vigi.async;

import com.vigi.Quote;

import java.util.List;

/**
 * Created by vigi on 2/21/2015.
 */
public class Discount {

    public static enum Code {

        NONE(0), SILVER(5), GOLD(10), PLATINUM(15), DIAMOND(20);

        private final int percentage;

        Code(int percentage) {
            this.percentage = percentage;
        }
    }

    public static String applyDiscount(Quote quote) {
        return quote.getShopName() + " price is " + apply(quote.getPrice(), quote.getDiscountCode());
    }

    private static String apply(double price, Code code) {
        delay();
        return format(price - (100 * code.percentage) / 100);
    }

    private static String format(double v) {
        return String.format("Price is: %.2f", v);
    }

    private static void delay() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
