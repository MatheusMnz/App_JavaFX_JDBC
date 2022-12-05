package javafx_jdbc.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx_jdbc.Main;
import javafx_jdbc.entities.Department;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx_jdbc.gui.listeners.DataChangeListener;
import javafx_jdbc.gui.util.Alerts;
import javafx_jdbc.gui.util.Utils;
import javafx_jdbc.service.DepartmentService;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import java.util.List;




public class DepartmentListController implements Initializable, DataChangeListener {
    private DepartmentService service;

    private ObservableList<Department> obsList;

    @FXML
    private TableView<Department> tableViewDepartement;

    @FXML
    private TableColumn<Department, Integer> tableColumnId;

    @FXML
    private  TableColumn<Department, String> tableColumnNome;

    @FXML
    private Button newBt;

    @FXML
    private ToolBar tB;

    // FXML METHODS --------------------------------------------------------------------------------
    @FXML
    public void onBtNovo(ActionEvent event){
        Stage parentStage = Utils.currentStage(event);
        Department departmentObj = new Department();
        createDialogForm(departmentObj, parentStage,"/javafx_jdbc/DepartmentForm.fxml");
    }


    // METHODS -------------------------------------------------------------------------------------
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

    // Recebo como parâmetro uma referência para o stage que criou a janela de dialogo e o caminho da view
    private void createDialogForm(Department departmentObject, Stage parentStage, String absoluteName){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
            Pane pane = loader.load();

            // Injetar o departamento no controlador da tela de Formulario (DepartmentForm Controller)
            // Pego referência do controlador da tela criada
            DepartmentFormController controller = loader.getController();

            // Seto as minhas entidades
            controller.setDepartmentEntity(departmentObject);
            controller.setDepartmentServiceEntity(new DepartmentService());

            // Inscrevendo como Listener para receber os eventos do onDataChange
            controller.subscribeDataChangeListener(this);

            // Carrego a minha entidade no formulário
            controller.updateFormData();


            // Quando carregar uma janela de dialogo modal, na frente de uma janela já existente eu devo instanciar um novo Stage
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Insira os dados do departamento");
            dialogStage.setScene(new Scene(pane));

            // Pode ou não redimensionar a janela
            dialogStage.setResizable(false);

            // Passo o stage "pai" da janela
            dialogStage.initOwner(parentStage);

            // Janela modal ou não. Enquanto não fechar essa janela, não possuo acesso à janela anterior
            dialogStage.initModality(Modality.WINDOW_MODAL);

            dialogStage.showAndWait();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onDataChange() {
        updateTableView();
    }
}
