package web.arachne.corrudither.filters;

import javafx.scene.paint.Color;

import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

class ColorVector {
    private final long r;
    private final long g;
    private final long b;

    ColorVector(long r, long g, long b) {

        long fxR;

        if (r > 255) {
            fxR = 255;
        } else fxR = Math.max(r, 0);

        long fxG;

        if (g > 255) {
            fxG = 255;
        } else fxG = Math.max(g, 0);

        long fxB;

        if (b > 255) {
            fxB = 255;
        } else fxB = Math.max(b, 0);

        this.r = fxR;
        this.g = fxG;
        this.b = fxB;
    }

    ColorVector(int color) {
        b = color & 0xff;
        g = (color & 0xff00) >> 8;
        r = (color & 0xff0000) >> 16;
    }

    ColorVector(Color color) {
        this.r = Math.round(color.getRed() * 255);
        this.g = Math.round(color.getGreen() * 255);
        this.b = Math.round(color.getBlue() * 255);
    }
    long r(){
        return r;
    }
    long g(){
        return g;
    }
    long b(){
        return b;
    }

    ColorVector sub(ColorVector cv){
        return new ColorVector(r() - cv.r(), g() - cv.g(), b() - cv.b());
    }

    ColorVector add(ColorVector cv){
        return new ColorVector(r() + cv.r(), g() + cv.g(), b() + cv.b());
    }

    ColorVector mult(ColorVector cv){
        return new ColorVector(r() * cv.r(), g() * cv.g(), b() * cv.b());
    }

    ColorVector mult(double s){
        return new ColorVector(Math.round(r() * s), Math.round(g() * s), Math.round(b() * s));
    }


    public int toIntARGB() {

        return (int)((255 << 24) + (r() << 16) + (g() << 8) + (b()));
    }

    public double getRGBDist(ColorVector candidate){
        return abs(sqrt(
                Math.pow(this.r() - candidate.r(), 2) +
                        Math.pow(this.g() - candidate.g(), 2) +
                        Math.pow(this.b() - candidate.b(), 2)
        ));
    }
}
