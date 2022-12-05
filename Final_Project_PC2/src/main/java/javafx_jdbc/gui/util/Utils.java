package javafx_jdbc.gui.util;

import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.scene.Node;

public class Utils {
    // Pego um evento, faço um downCasting para node, pego a cena e janela e faço um downcasting para Stage
    // Acessar o stage onde o controller que recebeu o evento está
    public static Stage currentStage(ActionEvent event){
        return (Stage) ((Node) event.getSource()).getScene().getWindow();
    }

    public static  Integer tryParseToInt(String str){
        try {
            return Integer.parseInt(str);
        }
        catch (NumberFormatException e){
            return  null;
        }
    }
}
