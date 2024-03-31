package com.strategygame.frontlines1950.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Debug {
    Map<String, Label> labels = new HashMap<>();

    public Map<String, Label> firstConfiguration() {
        int x = Gdx.graphics.getWidth() - 200;
        int y = Gdx.graphics.getHeight() - 20;
        this.labels.put("fps", createLabel(x, y));
        y -= 20;
        this.labels.put("mousePosition", createLabel(x, y));
        return this.labels;
    }

    public void actualize() {
        for(String key : this.labels.keySet()) {
            if(key.equals("fps")) {
                this.labels.get(key).setText("FPS: " + Gdx.graphics.getFramesPerSecond());
            }
            if(key.equals("mousePosition")) {
                this.labels.get(key).setText("Mouse Position: " + Gdx.input.getX() + ", " + Gdx.input.getY());
            }
        }
    }
    private Label createLabel(int x, int y) {
        Label label = new Label("", new Label.LabelStyle(new BitmapFont(), Color.GREEN));
        label.setPosition(x, y);
        return label;
    }
}