package com.strategygame.frontlines1950;

public class Bresenham {
    public static boolean isLineInCountry(Province.Pixel pixelA, Province.Pixel pixelB, Country country) {
        int x0 = pixelA.getX();
        int y0 = pixelA.getY();
        int x1 = pixelB.getX();
        int y1 = pixelB.getY();

        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);

        int sx = (x0 < x1) ? 1 : -1;
        int sy = (y0 < y1) ? 1 : -1;

        int err = dx - dy;

        int counter = 0;

        while (true) {
            if (counter % 10 == 0 && !country.isPixelCountry(x0, y0)) {
                return false;
            }

            if (x0 == x1 && y0 == y1) {
                break;
            }

            int e2 = 2 * err;
            if (e2 > -dy) {
                err = err - dy;
                x0 = x0 + sx;
            }

            if (e2 < dx) {
                err = err + dx;
                y0 = y0 + sy;
            }

            counter++;
        }

        return true;
    }
}
