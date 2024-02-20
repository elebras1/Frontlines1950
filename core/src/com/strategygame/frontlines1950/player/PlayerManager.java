package com.strategygame.frontlines1950.player;

import java.util.ArrayList;
import java.util.List;

public class PlayerManager {
    private final List<Player> players;
    private int currentPlayerIndex;

    public PlayerManager() {
        this.players = new ArrayList<>();
        this.currentPlayerIndex = 0;
    }

    public Player getHumanPlayer() {
        return this.players.get(0);
    }

    public void addPlayer(Player player) {
        this.players.add(player);
    }

    public Player getCurrentPlayer() {
        return this.players.get(this.currentPlayerIndex);
    }

    public void nextPlayer() {
        this.currentPlayerIndex = (this.currentPlayerIndex + 1) % this.players.size();
    }
}
