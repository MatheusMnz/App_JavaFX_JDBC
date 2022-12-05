package javafx_jdbc.service;

import javafx_jdbc.entities.Seller;
import javafx_jdbc.modelDao.DaoFactory;
import javafx_jdbc.modelDao.SellerDao;

import java.util.List;

public class SellerService {

    // Faço a dependência e injeto a dependência
    private SellerDao dao = DaoFactory.createSellerDao();
    public List<Seller> findAll() {
        return dao.findAll();
    }

    public void saveOrUpdate(Seller obj){
        if(obj.getId() == null){
            dao.insert(obj);
        }
        else{
            dao.update(obj);
        }
    }

    public void remove(Seller obj){
        dao.deleteById(obj.getId());
    }
}