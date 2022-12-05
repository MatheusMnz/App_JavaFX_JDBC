package javafx_jdbc.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx_jdbc.db.DbException;
import javafx_jdbc.entities.Department;
import javafx_jdbc.gui.listeners.DataChangeListener;
import javafx_jdbc.gui.util.Alerts;
import javafx_jdbc.gui.util.Constraints;
import javafx_jdbc.gui.util.Utils;
import javafx_jdbc.modelExceptions.ValidationException;
import javafx_jdbc.service.DepartmentService;

import java.net.URL;
import java.util.*;

public class DepartmentFormController implements Initializable {


    // Inserindo dependencia para Department
    private Department departmentEntity;

    private DepartmentService departmentServiceEntity;

    // Permite a inserção de outros objetos na lista e receber o event
    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

    @FXML
    private TextField txtId;
    @FXML
    private TextField txtName;
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
        if (departmentEntity == null){
            throw  new IllegalStateException("DepartmentEntity was null");
        }
        if(departmentServiceEntity == null){
            throw  new IllegalStateException("DepartmentServiceEntity was null");
        }

        // Tento executar os métodos do DB
        try{
            departmentEntity = getFormData();
            departmentServiceEntity.saveOrUpdate(departmentEntity);
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
    public void setDepartmentEntity(Department departmentEntity) {
        this.departmentEntity = departmentEntity;
    }

    public void setDepartmentServiceEntity(DepartmentService departmentServiceEntity) {
        this.departmentServiceEntity = departmentServiceEntity;
    }


    // Methods ----------------------------------------------------------------------------------------
    public void updateFormData(){
        if (this.departmentEntity == null){
            throw  new IllegalStateException("DepartmentEntity was null");
        }
        txtId.setText(String.valueOf(this.departmentEntity.getId()));
        txtName.setText(this.departmentEntity.getName());
    }

    private Department getFormData() {
        Department obj = new Department();
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
        Constraints.setTextFieldMaxLength(txtName, 30);
    }
}
