package javafx_jdbc.gui.util;

import javafx.event.ActionEvent;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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

    public static <T> void formatTableColumnDate(TableColumn<T, Date> tableColumn, String format){
        tableColumn.setCellFactory(column -> {
            TableCell<T, Date> cell = new TableCell<>() {
                private final SimpleDateFormat sdf = new SimpleDateFormat(format);

                @Override
                protected void updateItem(Date item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                    } else {
                        setText(sdf.format(item));
                    }
                }
            };
            return cell;
        });
    }


    public static <T> void formatTableColumnDouble(TableColumn<T, Double> tableColumn, int decimalPlaces){
        tableColumn.setCellFactory(column -> {
            TableCell<T, Double> cell = new TableCell<>() {

                @Override
                protected void updateItem(Double item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                    } else {
                        Locale.setDefault(Locale.US);
                        setText(String.format("%." + decimalPlaces + "f", item));
                    }
                }
            };
            return cell;
        });
    }





}
