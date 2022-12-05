package javafx_jdbc.service;

import java.util.ArrayList;
import java.util.List;

import javafx_jdbc.entities.Department;
import javafx_jdbc.modelDao.DaoFactory;
import javafx_jdbc.modelDao.DepartmentDao;

public class DepartmentService {

    // Faço a dependência e injeto a dependência
    private DepartmentDao dao = DaoFactory.createDepartmentDao();
    public List<Department> findAll() {
        return dao.findAll();
    }

    public void saveOrUpdate(Department obj){
        if(obj.getId() == null){
            dao.insert(obj);
        }
        else{
            dao.update(obj);
        }
    }

    public void remove(Department obj){
        dao.deleteById(obj.getId());
    }
}