package com.strategygame.frontlines1950;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.strategygame.frontlines1950.utils.Bresenham;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.strategygame.frontlines1950.Frontlines1950.WORLD_WIDTH;

public class MapLabel {
    private final BitmapFont font;
    private final Country entity;
    private final GlyphLayout glyphLayout;
    private final Matrix4 transformMatrix;
    private final String text;
    private int maxDistance = 0;
    private Province.Pixel pixelA;
    private Province.Pixel pixelB;

    public MapLabel(String fontFile, String text, Country entity) {
        this.font = new BitmapFont(Gdx.files.internal(fontFile));
        this.font.setUseIntegerPositions(false);
        this.glyphLayout = new GlyphLayout();
        this.transformMatrix = new Matrix4().idt();
        this.text = text;
        this.entity = entity;
        this.pixelA = null;
        this.pixelB = null;
    }

    private void calculateDistance(Set<Province.Pixel> borderPixels) {
        List<Province.Pixel> borderPixelsList = new ArrayList<>(borderPixels);
        int step = 10;

        for (int i = 0; i < borderPixelsList.size(); i += step) {
            Province.Pixel tempPixelA = borderPixelsList.get(i);
            for (int j = i + step; j < borderPixelsList.size(); j += step) {
                Province.Pixel tempPixelB = borderPixelsList.get(j);
                int distance = (int) Math.sqrt(Math.pow(tempPixelA.getX() - tempPixelB.getX(), 2) + Math.pow(tempPixelA.getY() - tempPixelB.getY(), 2));
                if (distance > this.maxDistance && Bresenham.isLineInCountry(tempPixelA, tempPixelB, entity) && tempPixelA.getX() < tempPixelB.getX()) {
                    this.maxDistance = distance;
                    this.pixelA = tempPixelA;
                    this.pixelB = tempPixelB;
                }
            }
        }
    }

    private float calculateAngle() {
        return (float) Math.toDegrees(Math.atan2(this.pixelB.getY() - this.pixelA.getY(), this.pixelB.getX() - this.pixelA.getX()));
    }

    private Matrix4 createTransformMatrix(int midX, int midY, float angle) {
        this.transformMatrix.idt();
        this.transformMatrix.translate(midX, midY, 0);
        this.transformMatrix.rotate(0, 0, 1, angle);
        this.transformMatrix.translate(-midX, -midY, 0);
        return this.transformMatrix;
    }

    public void draw(SpriteBatch batch, Set<Province.Pixel> borderPixels) {
        if(this.pixelA == null || this.pixelB == null) {
            if(entity.getNumberStates() > 0) {
                this.calculateDistance(borderPixels);
            } else {
                return;
            }
        }

        float scaleFactor = this.maxDistance / 5000f;
        this.font.getData().setScale(scaleFactor);
        this.glyphLayout.setText(this.font, this.text);

        int midX = (this.pixelA.getX() + this.pixelB.getX()) / 2;
        int midY = (this.pixelA.getY() + this.pixelB.getY()) / 2;
        float angle = this.calculateAngle();

        Matrix4 oldTransformMatrix = batch.getTransformMatrix().cpy();
        Matrix4 newTransformMatrix = this.createTransformMatrix(midX, midY, angle);

        batch.setTransformMatrix(newTransformMatrix);
        this.font.draw(batch, glyphLayout, midX - glyphLayout.width / 2, midY);

        newTransformMatrix = this.createTransformMatrix(midX - WORLD_WIDTH, midY, angle);
        batch.setTransformMatrix(newTransformMatrix);
        this.font.draw(batch, glyphLayout, midX - WORLD_WIDTH - glyphLayout.width / 2, midY);

        newTransformMatrix = this.createTransformMatrix(midX + WORLD_WIDTH, midY, angle);
        batch.setTransformMatrix(newTransformMatrix);
        this.font.draw(batch, glyphLayout, midX + WORLD_WIDTH - glyphLayout.width / 2, midY);

        batch.setTransformMatrix(oldTransformMatrix);
    }
}