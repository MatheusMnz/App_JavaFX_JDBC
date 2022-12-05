package javafx_jdbc.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;
import javafx_jdbc.db.DbException;
import javafx_jdbc.entities.Department;
import javafx_jdbc.entities.Seller;
import javafx_jdbc.gui.listeners.DataChangeListener;
import javafx_jdbc.gui.util.Alerts;
import javafx_jdbc.gui.util.Constraints;
import javafx_jdbc.gui.util.Utils;
import javafx_jdbc.modelExceptions.ValidationException;
import javafx_jdbc.service.DepartmentService;
import javafx_jdbc.service.SellerService;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class SellerFormController implements Initializable {
    // Declarações -----------------------------------------------------------------

    private Seller sellerEntity;

    private SellerService sellerServiceEntity;

    private DepartmentService departmentService;

    // Permite a inserção de outros objetos na lista e receber o event
    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

    private ObservableList<Department> obsList;

    // FXML Declarations ----------------------------------------------------------
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtEmail;
    @FXML
    private DatePicker dpBirthDate;
    @FXML
    private TextField txtBaseSalary;
    @FXML
    private ComboBox<Department> comboBoxDepartment;
    @FXML
    private Label labelErrorEmail;
    @FXML
    private Label labelErrorBirthDate;
    @FXML
    private Label labelErrorBaseSalary;
    @FXML
    private Label labelError;
    @FXML
    private Button btSave;
    @FXML
    private  Button btCancel;


    // FXML METHODS ------------------------------------------------------------------------------------
    @FXML
    public void onBtSave(ActionEvent event){
        // Programação defensiva para verificar as injeções
        if (sellerEntity == null){
            throw  new IllegalStateException("SellerEntity was null");
        }
        if(sellerServiceEntity == null){
            throw  new IllegalStateException("SellerServiceEntity was null");
        }

        // Tento executar os métodos do DB
        try{
            sellerEntity = getFormData();
            sellerServiceEntity.saveOrUpdate(sellerEntity);
            notifyDataChangeListeners();
            Utils.currentStage(event).close();
        }
        catch (DbException e){
            Alerts.showAlert("Error saving object", "null", e.getMessage(), Alert.AlertType.ERROR);
        }
        catch (ValidationException e){
            setErrorMessages(e.getErros());
        }

    }

    @FXML
    public void onBtCancel(ActionEvent event){
        Utils.currentStage(event).close();
    }


    // Setters ----------------------------------------------------------------------------------------
    public void setSellerEntity(Seller sellerEntity) {
        this.sellerEntity = sellerEntity;
    }

    public void setDepartmentServiceEntity(SellerService departmentServiceEntity) {
        this.sellerServiceEntity = departmentServiceEntity;
    }

    //Injeto 2 services de uma vez
    public void setServices(SellerService sellerServiceEntity, DepartmentService departmentService) {
        this.sellerServiceEntity = sellerServiceEntity;
        this.departmentService = departmentService;
    }

    // Methods ----------------------------------------------------------------------------------------
    public void updateFormData(){
        if (this.sellerEntity == null){
            throw  new IllegalStateException("SellerEntity was null");
        }

        // Com base no meu objeto, eu seto o formulário
        txtId.setText(String.valueOf(this.sellerEntity.getId()));
        txtName.setText(this.sellerEntity.getName());
        txtEmail.setText(this.sellerEntity.getEmail());
        Locale.setDefault(Locale.US);
        txtBaseSalary.setText(String.format("%.2f", this.sellerEntity.getBaseSalary()));

        if(this.sellerEntity.getBirthDate() != null){
            // Pego a data com base no horário local do usuário
            dpBirthDate.setValue(LocalDate.ofInstant(sellerEntity.getBirthDate().toInstant(), ZoneId.systemDefault()));
        }

        // Preenchendo comboBOx
        if(sellerEntity.getDepartment() == null){
            // Definindo comboBox como primeiro elemento
            comboBoxDepartment.getSelectionModel().selectFirst();
        }
        else {
            comboBoxDepartment.setValue(this.sellerEntity.getDepartment());
        }
    }

    // Método que chama o Department Service e carrega os Departamentos do banco de dados preenchendo a lista com os departamentos
    public  void loadAssociatedObjects(){
        if(departmentService == null){
            throw  new IllegalStateException("DepartmentService Was null");
        }

        // Carrego os departamentos do bando de dados
        List<Department> list = departmentService.findAll();

        // Jogo para observable list
        obsList = FXCollections.observableArrayList(list);

        // Seto como a lista associada ao meu comboBox
        comboBoxDepartment.setItems(obsList);
    }
    private Seller getFormData() {
        Seller obj = new Seller();
        ValidationException exception = new ValidationException("Validation Error");

        // Preenchendo meu objeto Department

        // ID
        obj.setId(Utils.tryParseToInt(txtId.getText()));

        // Name
        if(txtName.getText() == null || txtName.getText().trim().equals("")){
            exception.addError("name", "Field can't be empty");
        }
        obj.setName(txtName.getText());

        // Email
        if(txtEmail.getText() == null || txtEmail.getText().trim().equals("")){
            exception.addError("email", "Field can't be empty");
        }
        obj.setEmail(txtEmail.getText());

        // BirthDay - Converter a data no horário local do usuário, para o instant, que é uma data independente de localidade
        if(dpBirthDate.getValue() == null){
            exception.addError("birthDate", "Field can't be empty");
        }
        else {
            Instant instant = Instant.from(dpBirthDate.getValue().atStartOfDay(ZoneId.systemDefault()));
            obj.setBirthDate(Date.from(instant));
        }

        // BaseSalary
        if(txtBaseSalary.getText() == null || txtBaseSalary.getText().trim().equals("")){
            exception.addError("baseSalary", "Field can't be empty");
        }
        obj.setBaseSalary(Utils.tryParseToDouble(txtBaseSalary.getText()));

        // Department
        obj.setDepartment(comboBoxDepartment.getValue());


        // Verifico se na minha exceção há pelo menos um erro
        if(exception.getErros().size() > 0){
            throw exception;
        }

        return obj;
    }

    public void notifyDataChangeListeners(){
        for(DataChangeListener listener: dataChangeListeners){
            listener.onDataChange();
        }
    }

    public void subscribeDataChangeListener(DataChangeListener listener){
        dataChangeListeners.add(listener);
    }

    private  void setErrorMessages(Map<String, String> errors){
        Set<String> fields = errors.keySet();

        // Verifico se contém as respectivas keys nos erros
        labelError.setText(fields.contains("name") ? errors.get("name") : "");
        labelErrorEmail.setText(fields.contains("email") ? errors.get("email") : "");
        labelErrorBirthDate.setText(fields.contains("birthDate") ? errors.get("birthDate") : "");
        labelErrorBaseSalary.setText(fields.contains("baseSalary") ? errors.get("baseSalary") : "");
    }

    private void initializeComboBoxDepartment() {
        Callback<ListView<Department>, ListCell<Department>> factory = lv -> new ListCell<Department>() {
            @Override
            protected void updateItem(Department item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getName());
            }
        };
        comboBoxDepartment.setCellFactory(factory);
        comboBoxDepartment.setButtonCell(factory.call(null));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();

    }

    // Colocar restrições nas caixas de ID e Nome
    private void initializeNodes(){
        Constraints.setTextFieldInteger(txtId);
        Constraints.setTextFieldMaxLength(txtName, 50);
        Constraints.setTextFieldDouble(txtBaseSalary);
        Constraints.setTextFieldMaxLength(txtEmail, 60);
        Utils.formatDatePicker(dpBirthDate, "dd/MM/yyyy");
        initializeComboBoxDepartment();
    }
}
