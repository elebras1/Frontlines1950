package com.strategygame.frontlines1950.player;

import com.strategygame.frontlines1950.map.Country;

public class Player {
    private final Country country;

    public Player(Country country) {
        this.country = country;
    }

    public Country getCountry() {
        return this.country;
    }
}
