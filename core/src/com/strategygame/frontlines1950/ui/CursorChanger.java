package com.strategygame.frontlines1950.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;

public class CursorChanger {
    private AnimatedCursor animatedCursor;

    public void defaultCursor() {
        if(this.animatedCursor != null) {
            this.animatedCursor.dispose();
            this.animatedCursor = null;
        }
        Pixmap pixmap = new Pixmap(Gdx.files.internal("ui/cursor/normal.png"));
        int xHotspot = 0, yHotspot = 0;
        Cursor cursor = Gdx.graphics.newCursor(pixmap, xHotspot, yHotspot);
        pixmap.dispose();
        Gdx.graphics.setCursor(cursor);
    }

    public void animatedCursor(String name) {
        if(this.animatedCursor == null) {
            this.animatedCursor = new AnimatedCursor(name);
            this.animatedCursor.update(Gdx.graphics.getDeltaTime());
        }
    }

    public AnimatedCursor getAnimatedCursor() {
        return this.animatedCursor;
    }

    public void dispose() {
        this.animatedCursor.dispose();
    }
}
