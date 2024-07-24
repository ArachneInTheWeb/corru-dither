package web.arachne.corrudither;

import javafx.geometry.Insets;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.List;

public class PaletteCell extends ListCell<List<Color>> {

    PaletteCell(){
        super();
    }

    @Override
    protected void updateItem(List<Color> input, boolean empty){
        super.updateItem(input, empty);

        if (empty) return;

        setItem(input);

        HBox paletteDisplay = new HBox();

        paletteDisplay.setPadding(new Insets(0, 5, 0, 5));

        for (Color color : input){
            Rectangle rect = new Rectangle(20, 20);
            rect.setFill(color);
            rect.setStroke(Color.BLACK);
            paletteDisplay.getChildren().add(rect);
        }

        setText(null);
        setGraphic(paletteDisplay);

    }

}
