package web.arachne.corrudither.imageprocessors;

import javafx.scene.image.Image;
import org.apache.commons.imaging.formats.gif.GifImageMetadata;
import org.apache.commons.imaging.formats.gif.GifImageParser;
import web.arachne.corrudither.filters.Filter;
import web.arachne.gifencoder.AnimatedGifEncoder;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;
import java.util.concurrent.Executors;


public class AnimatedGifProcessor extends ImageProcessor {

    List<BufferedImage> images;
    GifImageMetadata metadata;

    public void load(File file) throws IOException {
        GifImageParser gifParser = new GifImageParser();

        metadata = (GifImageMetadata) gifParser.getMetadata(file);

        images = gifParser.getAllBufferedImages(file);
    }

    public Image process(List<Filter> filters) {

        for (Filter filter : filters){
            images.replaceAll(filter::process);
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
                Thread.sleep(100);
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

        for (int i = 0; i < images.size(); i++){
            gifEncoder.setTopLeft(metadata.getItems().get(i).getLeftPosition(), metadata.getItems().get(i).getTopPosition());
            gifEncoder.setDelay(metadata.getItems().get(i).getDelay() * 10);
            gifEncoder.setSize(images.get(i).getWidth(), images.get(i).getHeight());
            gifEncoder.setDispose(metadata.getItems().get(i).getDisposalMethod().ordinal());
            gifEncoder.addFrame(images.get(i));
        }

        gifEncoder.finish();
    }
}
