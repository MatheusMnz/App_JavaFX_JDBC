package javafx_jdbc.modelDao;


import javafx_jdbc.db.DB;
import javafx_jdbc.modelDao.impl.DepartmentDaoJDBC;
import javafx_jdbc.modelDao.impl.SellerDaoJDBC;

public class DaoFactory {

	public static SellerDao createSellerDao() {
		return new SellerDaoJDBC(DB.getConnection());
	}
	
	public static DepartmentDao createDepartmentDao() {
		return new DepartmentDaoJDBC(DB.getConnection());
	}
}
