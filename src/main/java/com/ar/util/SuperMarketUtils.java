
package com.ar.util;

import javafx.scene.image.Image;
import javafx.stage.Stage;


public class SuperMarketUtils {
    public static final String IMAGE_LOC="Supermarket.png";
    public static void setStageIcon(Stage stage){
        stage.getIcons().add(new Image(IMAGE_LOC));
    }
}
