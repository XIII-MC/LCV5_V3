package com.xiii.libertycity.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public final class MathUtils {

    public static double decimalRound(final double val, int scale) {
        return BigDecimal.valueOf(val).setScale(scale, RoundingMode.HALF_EVEN).doubleValue();
    }

    public static long elapsed(final long millis) {
        return System.currentTimeMillis() - millis;
    }

    public static <E> E randomElement(final Collection<? extends E> collection) {
        if (collection.size() == 0) return null;

        int index = new Random().nextInt(collection.size());

        if (collection instanceof List) {

            return ((List<? extends E>) collection).get(index);

        } else {

            Iterator<? extends E> iter = collection.iterator();

            for (int i = 0; i < index; i++) iter.next();

            return iter.next();
        }
    }

}
