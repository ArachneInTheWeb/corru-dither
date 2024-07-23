package web.arachne.corrudither.filters;

import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

public class BrightnessContrast extends Filter {
    private final float scaleFactor;
    private final int brightnessAdd;
    public BrightnessContrast(double contrastScale, double brightnessAdd){
        this.scaleFactor = (float)(contrastScale + 1.0);
        this.brightnessAdd = (int) Math.round(brightnessAdd * 127);
    }

    @Override
    public BufferedImage process(BufferedImage image) {

        RescaleOp rescaleOp = new RescaleOp(scaleFactor, brightnessAdd, null);
        return rescaleOp.filter(image, null);
    }
}
