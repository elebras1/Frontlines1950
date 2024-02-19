package com.strategygame.frontlines1950.utils;

import java.util.Objects;

public class Pair<A, B> {
    /**
     * the first value
     */
    private final A value0;
    /**
     * the second value
     */
    private final B value1;

    /**
     * Create a pair
     * @param value0 the first value
     * @param value1 the second value
     */
    public Pair(A value0, B value1) {
        this.value0 = value0;
        this.value1 = value1;
    }

    /**
     * Get the first value
     * @return the first value
     */
    public A getValue0() {
        return this.value0;
    }

    /**
     * Get the second value
     * @return the second value
     */
    public B getValue1() {
        return this.value1;
    }

    /**
     * Check if the pair is equal to an object
     * @param o
     * @return true if the pair is equal to the object else false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(value0, pair.value0) && Objects.equals(value1, pair.value1);
    }

    /**
     * Get the hash code of the pair
     * @return the hash code of the pair
     */
    @Override
    public int hashCode() {
        return Objects.hash(value0, value1);
    }
}
