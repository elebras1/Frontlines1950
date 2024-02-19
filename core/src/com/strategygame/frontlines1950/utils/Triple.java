package com.strategygame.frontlines1950.utils;

import java.util.Objects;

public class Triple<A, B, C> {
    /**
     * the first value
     */
    private A value0;
    /**
     * the second value
     */
    private B value1;
    /**
     * the third value
     */
    private C value2;

    /**
     * Create a triple
     * @param value0 the first value
     * @param value1 the second value
     * @param value2 the third value
     */
    public Triple(A value0, B value1, C value2) {
        this.value0 = value0;
        this.value1 = value1;
        this.value2 = value2;
    }

    /**
     * Get the first value
     * @return the first value
     */
    public A getValue0() {
        return value0;
    }

    /**
     * Get the second value
     * @return the second value
     */
    public B getValue1() {
        return value1;
    }

    /**
     * Get the third value
     * @return the third value
     */
    public C getValue2() {
        return value2;
    }

    public void setValue0(A value0) {
        this.value0 = value0;
    }

    public void setValue1(B value1) {
        this.value1 = value1;
    }

    public void setValue2(C value2) {
        this.value2 = value2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Triple<?, ?, ?> triple = (Triple<?, ?, ?>) o;
        return Objects.equals(value0, triple.value0) && Objects.equals(value1, triple.value1) && Objects.equals(value2, triple.value2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value0, value1, value2);
    }
}
