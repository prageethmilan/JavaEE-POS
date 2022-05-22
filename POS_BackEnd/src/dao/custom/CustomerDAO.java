package dao.custom;

import dao.CrudDAO;
import entity.Customer;

import javax.json.JsonObject;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author : M-Prageeth
 * @created : 19/05/2022 - 10:12 AM
 **/
public interface CustomerDAO extends CrudDAO<Customer, String> {
    JsonObject generateId(Connection connection) throws SQLException, ClassNotFoundException;
}
