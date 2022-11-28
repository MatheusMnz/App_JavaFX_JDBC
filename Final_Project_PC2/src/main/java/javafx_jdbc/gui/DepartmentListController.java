package javafx_jdbc.gui;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx_jdbc.Main;
import javafx_jdbc.entities.Department;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx_jdbc.service.DepartmentService;
import java.net.URL;
import java.util.ResourceBundle;

import java.util.List;


import java.net.URL;
import java.util.ResourceBundle;

public class DepartmentListController implements Initializable {
    // Criar referencias para os componentes da tela DepartamentoList


    private DepartmentService service;

    private ObservableList<Department> obsList;

    @FXML
    private TableView<Department> tableViewDepartement;

    @FXML
    private TableColumn<Department, Integer> tableColumnId;

    @FXML
    private  TableColumn<Department, String> tableColumnNome;

    @FXML
    private Button BtNovo;

    @FXML
    public void onBtNovo(){
        System.out.println("aqui");
    }

    public void setDepartmentService(DepartmentService service) {
        this.service = service;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeNodes();
    }

    // Para formatar as tables
    private void initializeNodes() {
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("name"));

        // Preencher toda a tela

        // Pego a referencia para a janela (SuperClass do Stage)
        Stage stage = (Stage) Main.getMainScene().getWindow();

        // Fazer a tabela acompanhar a janela
        tableViewDepartement.prefHeightProperty().bind(stage.heightProperty());
        tableViewDepartement.prefWidthProperty().bind(stage.widthProperty());

    }

    public void updateTableView() {
        if (service == null) {
            throw new IllegalStateException("Service was null");
        }
        List<Department> list = service.findAll();
        obsList = FXCollections.observableArrayList(list);
        tableViewDepartement.setItems(obsList);
    }


}
