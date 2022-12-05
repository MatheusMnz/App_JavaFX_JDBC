package javafx_jdbc.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class SellerFormController implements Initializable {


    // Inserindo dependencia para Department
    private Seller sellerEntity;

    private SellerService sellerServiceEntity;

    // Permite a inserção de outros objetos na lista e receber o event
    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

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
    }

    private Seller getFormData() {
        Seller obj = new Seller();
        ValidationException exception = new ValidationException("Validation Error");

        // Preenchendo meu objeto Department
        obj.setId(Utils.tryParseToInt(txtId.getText()));
        if(txtName.getText() == null || txtName.getText().trim().equals("")){
            exception.addError("name", "Field can't be empty");
        }
        obj.setName(txtName.getText());


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

        // Verifico se tem a chave name nos erros
        if(fields.contains("name")){
            labelError.setText(errors.get("name"));
        }
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
    }
}
