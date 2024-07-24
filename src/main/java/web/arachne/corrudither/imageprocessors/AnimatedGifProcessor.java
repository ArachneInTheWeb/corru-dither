package web.arachne.corrudither.imageprocessors;

import javafx.scene.image.Image;
import org.apache.commons.imaging.formats.gif.GifImageMetadata;
import org.apache.commons.imaging.formats.gif.GifImageParser;
import web.arachne.corrudither.filters.Filter;
import web.arachne.gifencoder.AnimatedGifEncoder;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;


public class AnimatedGifProcessor extends ImageProcessor {

    List<BufferedImage> images;
    List<BufferedImage> processedImages = new ArrayList<>();
    GifImageMetadata metadata;

    public void load(File file) throws IOException {
        GifImageParser gifParser = new GifImageParser();

        metadata = (GifImageMetadata) gifParser.getMetadata(file);

        images = gifParser.getAllBufferedImages(file);
    }

    public Image process(List<Filter> filters) {
        processedImages.clear();

        for (int i = 0; i < images.size(); i++){
            ColorModel cm = images.get(i).getColorModel();
            boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
            WritableRaster raster = images.get(i).copyData(images.get(i).getRaster().createCompatibleWritableRaster());
            processedImages.add(new BufferedImage(cm, raster, isAlphaPremultiplied, null));
        }

        for (Filter filter : filters){
            processedImages.replaceAll(filter::process);
        }

        PipedOutputStream pipedOutputStream = new PipedOutputStream();

        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                save(pipedOutputStream);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        try (PipedInputStream pipedInputStream = new PipedInputStream(pipedOutputStream)){
            Image image = new Image(pipedInputStream);
            while (image.getProgress() != 1.0){
                Thread.sleep(50);
            }
            return image;
        } catch (IOException e) {
            return null;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void save (File file) throws IOException {
        FileOutputStream fileOutput = new FileOutputStream(file);
        save(fileOutput);
    }

    private void save(OutputStream output){
        AnimatedGifEncoder gifEncoder = new AnimatedGifEncoder();
        gifEncoder.start(output);
        gifEncoder.setRepeat(0);
        gifEncoder.setSize(metadata.getWidth(), metadata.getHeight());

        for (int i = 0; i < processedImages.size(); i++){
            gifEncoder.setTopLeft(metadata.getItems().get(i).getLeftPosition(), metadata.getItems().get(i).getTopPosition());
            gifEncoder.setDelay(metadata.getItems().get(i).getDelay() * 10);
            gifEncoder.setSize(processedImages.get(i).getWidth(), processedImages.get(i).getHeight());
            gifEncoder.setDispose(metadata.getItems().get(i).getDisposalMethod().ordinal());
            gifEncoder.addFrame(processedImages.get(i));
        }

        gifEncoder.finish();
    }
}
