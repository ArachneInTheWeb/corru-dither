package web.arachne.corrudither.imageprocessors;

import javafx.scene.image.Image;
import web.arachne.corrudither.filters.Filter;

import java.io.File;
import java.io.IOException;
import java.util.List;

public abstract class ImageProcessor {

    public abstract void load(File file) throws IOException;

    public abstract Image process(List<Filter> filters);

    public abstract void save(File output) throws IOException;
}
