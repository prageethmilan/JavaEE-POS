package dao.custom.impl;

import dao.CrudUtil;
import dao.custom.CustomerDAO;
import entity.Customer;

import javax.json.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author : M-Prageeth
 * @created : 19/05/2022 - 9:31 AM
 **/
public class CustomerDAOImpl implements CustomerDAO {
    @Override
    public JsonArray getAll(Connection connection) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.executeQuery(connection, "SELECT * FROM Customer");
        JsonArrayBuilder customerArray = Json.createArrayBuilder();
        while (rst.next()) {
            Customer customer = new Customer(rst.getString(1), rst.getString(2), rst.getString(3), rst.getDouble(4));
            JsonObjectBuilder customerObj = Json.createObjectBuilder();
            customerObj.add("id", customer.getId());
            customerObj.add("name", customer.getName());
            customerObj.add("address", customer.getAddress());
            customerObj.add("salary", customer.getSalary());
            customerArray.add(customerObj.build());
        }
        return customerArray.build();
    }

    @Override
    public boolean add(Connection connection, Customer customer) throws SQLException, ClassNotFoundException {
        return CrudUtil.executeUpdate(connection, "INSERT INTO Customer Values (?,?,?,?)", customer.getId(), customer.getName(), customer.getAddress(), customer.getSalary());
    }

    @Override
    public boolean update(Connection connection, Customer customer) throws SQLException, ClassNotFoundException {
        return CrudUtil.executeUpdate(connection, "UPDATE Customer SET name=?,address=?,salary=? WHERE id=?", customer.getName(), customer.getAddress(), customer.getSalary(), customer.getId());
    }

    @Override
    public boolean delete(Connection connection, String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.executeUpdate(connection,"DELETE FROM Customer WHERE id=?", id);
    }

    @Override
    public Customer search(Connection connection, String id) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.executeQuery(connection,"SELECT * FROM Customer WHERE id=?", id);
        Customer customer = null;
        while (rst.next()) {
            customer = new Customer(rst.getString(1), rst.getString(2), rst.getString(3), rst.getDouble(4));
        }
        return customer;
    }

    @Override
    public JsonObject generateId(Connection connection) throws SQLException, ClassNotFoundException {
        ResultSet idSet = CrudUtil.executeQuery(connection, "SELECT id FROM Customer ORDER BY id DESC LIMIT 1");
        JsonObjectBuilder obj = Json.createObjectBuilder();
        if (idSet.next()) {
            int tempId = Integer.parseInt(idSet.getString(1).split("-")[1]);
            tempId = tempId + 1;
            if (tempId <= 9) {
                String id = "C00-000" + tempId;
                obj.add("id", id);
            } else if (tempId <= 99) {
                String id = "C00-00" + tempId;
                obj.add("id", id);
            } else if (tempId <= 999) {
                String id = "C00-0" + tempId;
                obj.add("id", id);
            } else if (tempId <= 9999) {
                String id = "C00-" + tempId;
                obj.add("id", id);
            }
        } else {
            String id = "C00-0001";
            obj.add("id", id);
        }
        return obj.build();
    }
}
