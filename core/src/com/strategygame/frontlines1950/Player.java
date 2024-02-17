package com.strategygame.frontlines1950;

public class Player {
    private final Country country;

    public Player(Country country) {
        this.country = country;
    }

    public Country getCountry() {
        return this.country;
    }
}
