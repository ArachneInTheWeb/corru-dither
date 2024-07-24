package web.arachne.corrudither.filters;

import javafx.scene.paint.Color;

import java.awt.image.BufferedImage;
import java.util.List;

import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

public class FloydSteinberg extends Filter {

    List<Color> palette;

    public FloydSteinberg(List<Color> palette){
        this.palette = palette;
    }

    /*
    * Adapted from here https://scipython.com/blog/floyd-steinberg-dithering/
    *
    */

    public BufferedImage process(BufferedImage source){


        int width = source.getWidth();
        int height = source.getHeight();

        ColorVector[][] imageArray = new ColorVector[width][height];
        for (int row = 0; row < width; row++){
            for (int column = 0; column < height; column++){
                imageArray[row][column] = new ColorVector(source.getRGB(row, column));
            }
        }


        for (int row = 0; row < width; row++){
            for (int column = 0; column < height; column++){
                ColorVector oldColor = imageArray[row][column];
                ColorVector newColor = findNewColor(oldColor);
                ColorVector err = oldColor.sub(newColor);

                imageArray[row][column] = newColor;

                if (column < height - 1){
                    ColorVector outputPixelColor = imageArray[row][column + 1];
                    imageArray[row][column + 1] = outputPixelColor.add(err.mult(7.0/16));
                }

                if (row < width - 1){
                    if (column > 0){
                        ColorVector outputPixelColor = imageArray[row + 1][column - 1];
                        imageArray[row + 1][column - 1] = outputPixelColor.add(err.mult(3.0/16));
                    }

                    ColorVector outputPixelColor = imageArray[row + 1][column];
                    imageArray[row + 1][column] = outputPixelColor.add(err.mult(5.0/16));

                    if (column < height - 1){
                        outputPixelColor = imageArray[row + 1][column + 1];
                        imageArray[row + 1][column + 1] = outputPixelColor.add(err.mult(1.0/16));
                    }

                }
            }
        }
        BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);


        for (int row = 0; row < width; row++){
            for (int column = 0; column < height; column++){
                output.setRGB(row, column, imageArray[row][column].toIntARGB());
            }
        }

        return output;
    }

    ColorVector findNewColor(ColorVector oldColor){
        int shortestDistanceIndex = Integer.MAX_VALUE;
        double shortestDistance = Double.MAX_VALUE;

        for (int i = 0; i < palette.size(); i++){
            double dist = oldColor.getRGBDist(new ColorVector(palette.get(i)));
            if (dist < shortestDistance){
                shortestDistance = dist;
                shortestDistanceIndex = i;
            }
        }
        return new ColorVector(palette.get(shortestDistanceIndex));
    }

        



//    private static double convertToHCL(Color color){
//        List<Double> rgb = Arrays.asList(color.getRed(), color.getGreen(), color.getBlue());
//        double hue = color.getHue();
//        double chroma = Collections.max(rgb) - Collections.min(rgb);
//        double lightness = color.getBrightness();
//        return 0.0;
//    }
}
