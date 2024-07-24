package web.arachne.corrudither;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
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

    @FXML
    public ListView<List<Color>> paletteList;

    @FXML
    private Slider brightnessSlider;

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

        if (selectedPalette != null && !selectedPalette.isEmpty()){
            filters.add(new FloydSteinberg(selectedPalette));
        }

        imageView.setImage(imageHandler.process(filters));
    }

    @FXML
    protected void quit(){
        Platform.exit();
    }

    private List<Color> selectedPalette;

    @FXML
    public void initialize(){
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.gif", "*.bmp");
        fileChooser.getExtensionFilters().add(extFilter);


        brightnessSliderValue.textProperty().bind(brightnessSlider.valueProperty().asString("%.2f"));
        contrastSliderValue.textProperty().bind(contrastSlider.valueProperty().asString("%.2f"));

        brightnessSlider.setOnMouseReleased((event) -> updateDisplay());

        contrastSlider.setOnMouseReleased((event) -> updateDisplay());

        ObservableList<List<Color>> palettes = FXCollections.observableArrayList();

        palettes.add(new ArrayList<>());

        palettes.add(new ArrayList<>(Arrays.asList(
                Color.rgb( 255, 0, 101),
                Color.rgb( 255, 255, 0),
                Color.rgb( 255, 255, 255),
                Color.rgb( 0, 0, 0)
        )));

        palettes.add(new ArrayList<>(Arrays.asList(
                Color.rgb( 85, 255, 255),
                Color.rgb( 255, 85, 255),
                Color.rgb( 255, 255, 255),
                Color.rgb( 0, 0, 0)
        )));

        palettes.add(new ArrayList<>(Arrays.asList(
                Color.rgb( 85, 255, 255),
                Color.rgb( 255, 85, 255),
                Color.rgb( 255, 255, 255),
                Color.rgb( 255, 255, 85)
        )));

        palettes.add(new ArrayList<>(Arrays.asList(
                Color.rgb( 85, 255, 255),
                Color.rgb( 255, 85, 255),
                Color.rgb( 255, 255, 255),
                Color.rgb( 255, 255, 85),
                Color.rgb( 0, 0, 0)
        )));

        palettes.add(new ArrayList<>(Arrays.asList(
                Color.rgb( 255, 255, 255),
                Color.rgb( 0, 0, 0)
        )));

        paletteList.setCellFactory(list -> new PaletteCell());

        paletteList.setItems(palettes);

        paletteList.getSelectionModel().selectedItemProperty().addListener((observableValue, oldPalette, newPalette) -> {
            selectedPalette = newPalette;
            updateDisplay();
        });
    }


}