package javafx_jdbc.gui;

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
import javafx_jdbc.gui.util.Alerts;
import javafx_jdbc.service.DepartmentService;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

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
        loadView("/javafx_jdbc/listDepartamento.fxml", (DepartmentListController controller) -> {
            controller.setDepartmentService(new DepartmentService());
            controller.updateTableView();
        });
    }

    @FXML
    public void onMenuItemSobreAction(){
        loadView("/javafx_jdbc/Sobre.fxml", x -> {});
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }



    // Carregar uma nova janela, com base no caminho passado
    // Usando synchronized para garantir que a thread n pare
    // Utilizando Lógica de Expressão Lambda para não precisar ficar criando várias funções LoadView
    private synchronized <T> void loadView(String absoluteNameView, Consumer<T> initializingAction){
        try {
            // Rerenciando a Main class pois estavd tendo um problema com diretorios usando getClass
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteNameView));
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

            // Retorna um controller do tipo do Consumer
            T controller = loader.getController();

            // Executa a expressão lambda
            initializingAction.accept(controller);

        } catch (IOException e) {
            Alerts.showAlert("IO exception", "Erro Carregando a Página", e.getMessage(), Alert.AlertType.ERROR);
        }
    }


}
