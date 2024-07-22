package web.arachne.corrudither;

import javafx.scene.image.Image;
import org.apache.commons.io.FilenameUtils;
import web.arachne.corrudither.filters.Filter;
import web.arachne.corrudither.imageprocessors.AnimatedGifProcessor;
import web.arachne.corrudither.imageprocessors.ImageProcessor;
import web.arachne.corrudither.imageprocessors.StaticImageProcessor;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ImageHandler {
    ImageProcessor processor;


    public void load(File file) throws IOException {
        if (FilenameUtils.getExtension(file.getAbsolutePath()).equals("gif")){
            processor = new AnimatedGifProcessor();
        } else {
            processor = new StaticImageProcessor();
        }

        processor.load(file);

    }

    public Image process(List<Filter> filters){

        return processor.process(filters);
    }

    public void save(File fileToSave) throws IOException {

        processor.save(fileToSave);
    }

}
