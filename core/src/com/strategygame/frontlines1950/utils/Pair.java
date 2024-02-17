package com.strategygame.frontlines1950.utils;

import java.util.Objects;

public class Pair<A, B> {
    private final A value0;
    private final B value1;

    public Pair(A value0, B value1) {
        this.value0 = value0;
        this.value1 = value1;
    }

    public A getValue0() {
        return this.value0;
    }

    public B getValue1() {
        return this.value1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(value0, pair.value0) && Objects.equals(value1, pair.value1);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value0, value1);
    }
}
