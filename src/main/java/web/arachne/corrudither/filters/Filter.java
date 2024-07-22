package web.arachne.corrudither.filters;

import java.awt.image.BufferedImage;

public abstract class Filter {

    public abstract BufferedImage process(BufferedImage image);
}
