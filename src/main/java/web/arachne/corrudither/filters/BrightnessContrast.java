package web.arachne.corrudither.filters;

import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

public class BrightnessContrast extends Filter {
    private final double brightness;
    private final double slant;
    public BrightnessContrast(double contrast, double brightness){
        this.brightness = brightness / 2.0;
        this.slant = Math.tan((contrast + 1) * 3.14159);
    }

    /*
     * Brightness/Contrast sliders implementation based on the one used by GIMP
     * https://github.com/GNOME/gimp/blob/45f390ffc4ec765578f4a47a002e722b1d2e4a8f/app/operations/gimpoperationbrightnesscontrast.c
     */

    @Override
    public BufferedImage process(BufferedImage image) {

        return image;
    }
}
