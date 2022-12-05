package javafx_jdbc.gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx_jdbc.entities.Department;
import javafx_jdbc.gui.util.Constraints;

import java.net.URL;
import java.util.ResourceBundle;

public class DepartmentFormController implements Initializable {


    // Inserindo dependencia para Department
    private Department departmentEntity;

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



    // FXML METHODS
    @FXML
    public void onBtSave(){
        System.out.println("OnBtSave");
    }

    @FXML
    public void onBtCancel(){
        System.out.println("OnBtCancel");
    }



    public void setDepartmentEntity(Department departmentEntity) {
        this.departmentEntity = departmentEntity;
    }

    public void updateFormData(){
        if (this.departmentEntity == null){
            throw  new IllegalStateException("DepartmentEntity was null");
        }
        txtId.setText(String.valueOf(this.departmentEntity.getId()));
        txtName.setText(String.valueOf(this.departmentEntity.getName()));
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
