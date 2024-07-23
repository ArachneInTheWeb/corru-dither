package web.arachne.corrudither;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import web.arachne.corrudither.filters.BrightnessContrast;
import web.arachne.corrudither.filters.Filter;
import web.arachne.corrudither.filters.FloydSteinberg;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CorruController {

    @FXML
    public Slider contrastSlider;

    @FXML
    public TextField contrastSliderValue;

    @FXML
    public TextField brightnessSliderValue;

    @FXML Slider brightnessSlider;

    @FXML
    private ImageView imageView;

    private final ImageHandler imageHandler = new ImageHandler();

    private File loadedFile;


    @FXML
    private TextField imageUri;

    @FXML
    private Button fileChooseButton;

    @FXML
    private final FileChooser fileChooser = new FileChooser();


    private final ArrayList<javafx.scene.paint.Color> palette = new ArrayList<>(Arrays.asList(
//            javafx.scene.paint.Color.rgb( 85, 255, 255),
//            javafx.scene.paint.Color.rgb( 255, 85, 255),
            javafx.scene.paint.Color.rgb( 255, 0, 101),
            javafx.scene.paint.Color.rgb( 255, 255, 0),
            javafx.scene.paint.Color.rgb( 255, 255, 255),
            javafx.scene.paint.Color.rgb( 0, 0, 0)
    )){

    };

    @FXML
    protected void openFile(){
        File file = fileChooser.showOpenDialog(fileChooseButton.getScene().getWindow());

        if (file != null){

            imageUri.setText(file.getAbsolutePath());
            try {
                imageHandler.load(file);
                loadedFile = file;
            } catch (IOException e) {
                Alert alert  = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.CLOSE);
                alert.showAndWait();
            }

            updateDisplay();
        }

    }

    @FXML
    protected void saveFile(){
        File fileToSave = fileChooser.showSaveDialog(fileChooseButton.getScene().getWindow());
        if (fileToSave != null){
            try {
                imageHandler.save(fileToSave);

            } catch (IOException e) {
                Alert alert  = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.CLOSE);
                alert.showAndWait();
            }
        }
    }

    @FXML
    protected void updateDisplay(){

        if (loadedFile == null){
            return;
        }

        List<Filter> filters = new ArrayList<>();

        filters.add(new BrightnessContrast(contrastSlider.getValue(), brightnessSlider.getValue()));
        filters.add(new FloydSteinberg(palette));

        imageView.setImage(imageHandler.process(filters));
    }

    @FXML
    protected void quit(){
        Platform.exit();
    }

    @FXML
    public void initialize(){
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.gif", "*.bmp");
        fileChooser.getExtensionFilters().add(extFilter);


        brightnessSliderValue.textProperty().bind(brightnessSlider.valueProperty().asString("%.2f"));
        contrastSliderValue.textProperty().bind(contrastSlider.valueProperty().asString("%.2f"));

        brightnessSlider.setOnMouseReleased((event) -> {
            updateDisplay();
        });

        contrastSlider.setOnMouseReleased((event) -> {
            updateDisplay();
        });
    }


}