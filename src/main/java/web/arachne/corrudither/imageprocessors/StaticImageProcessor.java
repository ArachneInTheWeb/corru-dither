package web.arachne.corrudither.imageprocessors;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.apache.commons.io.FilenameUtils;
import web.arachne.corrudither.filters.Filter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;

public class StaticImageProcessor extends ImageProcessor {

    Image storedImage;
    BufferedImage image;

    @Override
    public void load(File file) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        storedImage = new Image(fileInputStream);
    }

    @Override
    public Image process(List<Filter> filters) {
        image = SwingFXUtils.fromFXImage(storedImage, null);

        for (Filter filter : filters){
            image = filter.process(image);

        }
        return SwingFXUtils.toFXImage(image, null);
    }

    @Override
    public void save(File output) throws IOException {
        ImageIO.write(image, FilenameUtils.getExtension(output.getAbsolutePath()), output);
    }
}
