package dao.custom;

import dao.CrudDAO;
import entity.OrderDetail;

import javax.json.JsonArray;
import java.sql.SQLException;

/**
 * @author : M-Prageeth
 * @created : 19/05/2022 - 10:13 AM
 **/
public interface OrderDetailDAO extends CrudDAO<OrderDetail, String> {
    JsonArray searchOrderDetails(String id) throws SQLException, ClassNotFoundException;
}
