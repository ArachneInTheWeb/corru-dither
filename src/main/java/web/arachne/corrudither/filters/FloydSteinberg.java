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
                ColorVector newColor = oldColor.findNewColor();
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


    private class ColorVector {
        private final long r;
        private final long g;
        private final long b;

        ColorVector(long r, long g, long b){

            long fxR;

            if (r > 255){
                fxR = 255;
            } else fxR = Math.max(r, 0);

            long fxG;

            if (g > 255){
                fxG = 255;
            } else fxG = Math.max(g, 0);

            long fxB;

            if (b > 255){
                fxB = 255;
            } else fxB = Math.max(b, 0);

            this.r = fxR;
            this.g = fxG;
            this.b = fxB;
        }
        
        ColorVector (int color){
            b = color & 0xff;
            g = (color & 0xff00) >> 8;
            r = (color & 0xff0000) >> 16;
        }
        
        ColorVector(Color color){
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

        ColorVector findNewColor(){
            int shortestDistanceIndex = Integer.MAX_VALUE;
            double shortestDistance = Double.MAX_VALUE;

            for (int i = 0; i < palette.size(); i++){
                double dist = getRGBDist(this, new ColorVector(palette.get(i)));
                if (dist < shortestDistance){
                    shortestDistance = dist;
                    shortestDistanceIndex = i;
                }
            }
            return new ColorVector(palette.get(shortestDistanceIndex));
        }

        public int toIntARGB() {

            return (int)((255 << 24) + (r() << 16) + (g() << 8) + (b()));
        }
    }

    private static double getRGBDist(ColorVector current, ColorVector candidate){
        return abs(sqrt(
                Math.pow(current.r() - candidate.r(), 2) +
                        Math.pow(current.g() - candidate.g(), 2) +
                        Math.pow(current.b() - candidate.b(), 2)
        ));
    }

//    private static double convertToHCL(Color color){
//        List<Double> rgb = Arrays.asList(color.getRed(), color.getGreen(), color.getBlue());
//        double hue = color.getHue();
//        double chroma = Collections.max(rgb) - Collections.min(rgb);
//        double lightness = color.getBrightness();
//        return 0.0;
//    }
}
