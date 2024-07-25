package web.arachne.corrudither.filters;

import java.awt.image.BufferedImage;

public class BrightnessContrast extends Filter {
    private final double brightness;
    private final double slant;
    public BrightnessContrast(double contrast, double brightness){
        this.brightness = brightness / 2.0;
        this.slant = Math.tan((contrast + 1)  * 0.785398163);
    }

    /*
     * Brightness/Contrast sliders implementation based on the one used by GIMP
     * https://github.com/GNOME/gimp/blob/45f390ffc4ec765578f4a47a002e722b1d2e4a8f/app/operations/gimpoperationbrightnesscontrast.c
     */

    @Override
    public BufferedImage process(BufferedImage source) {


        ColorVector[][] imageArray = ColorVector.bufferedImageToArray(source);

        for (int row = 0; row < source.getWidth(); row++){
            for (int column = 0; column < source.getHeight(); column++){
                ColorVector workingColor = imageArray[row][column];

                if (brightness <= 0){
                    workingColor = workingColor.mult(1.0 + brightness);
                } else {
                    workingColor = workingColor.add((new ColorVector(255, 255, 255).sub(workingColor)).mult(brightness));
                }

                workingColor = (workingColor.sub(0.5).mult(slant)).add(0.5);

                imageArray[row][column] = workingColor;
            }
        }




        return ColorVector.arrayToBufferedImage(imageArray);
    }
}
