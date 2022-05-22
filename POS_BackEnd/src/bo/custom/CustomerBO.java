package bo.custom;

import bo.SuperBO;
import dto.CustomerDTO;

import javax.json.JsonArray;
import javax.json.JsonObject;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author : M-Prageeth
 * @created : 22/05/2022 - 10:13 AM
 **/
public interface CustomerBO extends SuperBO {
    JsonArray getAllCustomers(Connection connection) throws SQLException, ClassNotFoundException;

    CustomerDTO searchCustomer(Connection connection , String id) throws SQLException, ClassNotFoundException;

    JsonObject generateCustomerId(Connection connection) throws SQLException, ClassNotFoundException;

    boolean addCustomer(Connection connection,CustomerDTO customerDTO) throws SQLException, ClassNotFoundException;

    boolean updateCustomer(Connection connection,CustomerDTO customerDTO) throws SQLException, ClassNotFoundException;

    boolean deleteCustomer(Connection connection,String id) throws SQLException, ClassNotFoundException;
}
