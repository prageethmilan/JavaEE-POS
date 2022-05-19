package dao.custom;

import dao.CrudDAO;
import entity.Order;

import javax.json.JsonObject;
import java.sql.SQLException;

/**
 * @author : M-Prageeth
 * @created : 19/05/2022 - 10:12 AM
 **/
public interface OrderDAO extends CrudDAO<Order, String> {
    JsonObject generateOid() throws SQLException, ClassNotFoundException;
}
