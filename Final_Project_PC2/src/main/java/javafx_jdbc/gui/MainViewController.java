package javafx_jdbc.gui;

import javafx.stage.Stage;
import javafx_jdbc.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx_jdbc.service.DepartmentService;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainViewController  implements Initializable {
    @FXML
    private  MenuItem  menuItemVendedor;
    @FXML
    private  MenuItem menuItemDepartamento;
    @FXML
    private  MenuItem menuItemSobre;

    @FXML
    public void onMenuItemVendedorAction(){
        System.out.println("onMenuItemVendedorAction");
    }

    @FXML
    public void onMenuItemDepartamentoAction(){
        loadView2("/javafx_jdbc/listDepartamento.fxml");
    }

    @FXML
    public void onMenuItemSobreAction(){
        loadView("/javafx_jdbc/Sobre.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }



    // Carregar uma nova janela, com base no caminho passado
    // Usando synchronized para garantir que a thread n pare
    private synchronized void loadView(String absoluteNameView){

        // Rerenciando a Main class pois estavd tendo um problema com diretorios usando getClass
        FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteNameView));
        try {
            VBox newVBox = loader.load();

            // Mostrar a view dentro da janela principal (Referencia da cena principal)
            Scene mainScene = Main.getMainScene();

            // Pega o primeiro elemento da View Principal (ScrollPane) e acesso o contéudo dele (VBox)
            VBox mainVBox =  (VBox) ((ScrollPane) mainScene.getRoot()).getContent();

            // Guardando uma referencia para o menu principal
            Node mainMenu = mainVBox.getChildren().get(0);

            // Limpando todos os filhos do mainVBox
            mainVBox.getChildren().clear();

            // Adicionando o mainMenu e os childrens do newVBox
            mainVBox.getChildren().add(mainMenu);
            mainVBox.getChildren().addAll(newVBox.getChildren()); //addAll adiciona uma coleção

        } catch (IOException e) {
            Alerts.showAlert("IO exception", "Erro carregando a página", e.getMessage(), Alert.AlertType.ERROR);
        }
    }



    private synchronized void loadView2(String absoluteName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
            VBox newVBox = loader.load();

            Scene mainScene = Main.getMainScene();
            VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();

            Node mainMenu = mainVBox.getChildren().get(0);
            mainVBox.getChildren().clear();
            mainVBox.getChildren().add(mainMenu);
            mainVBox.getChildren().addAll(newVBox.getChildren());

            DepartmentListController controller = loader.getController();
            controller.setDepartmentService(new DepartmentService());
            controller.updateTableView();
        }
        catch (IOException e) {
            Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), Alert.AlertType.ERROR);
        }
    }


}
