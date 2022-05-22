package bo.custom.impl;

import bo.custom.CustomerBO;
import dao.DAOFactory;
import dao.SuperDAO;
import dao.custom.CustomerDAO;
import dto.CustomerDTO;
import entity.Customer;

import javax.json.JsonArray;
import javax.json.JsonObject;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author : M-Prageeth
 * @created : 22/05/2022 - 10:14 AM
 **/
public class CustomerBOImpl implements CustomerBO {

    CustomerDAO customerDAO = (CustomerDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.CUSTOMER);

    @Override
    public JsonArray getAllCustomers(Connection connection) throws SQLException, ClassNotFoundException {
        return customerDAO.getAll(connection);
    }

    @Override
    public CustomerDTO searchCustomer(Connection connection, String id) throws SQLException, ClassNotFoundException {
        Customer search = customerDAO.search(connection, id);
        if (search !=null) {
            return new CustomerDTO(search.getId(), search.getName(), search.getAddress(), search.getSalary());
        }
        return null;
    }

    @Override
    public JsonObject generateCustomerId(Connection connection) throws SQLException, ClassNotFoundException {
        return customerDAO.generateId(connection);
    }

    @Override
    public boolean addCustomer(Connection connection, CustomerDTO customerDTO) throws SQLException, ClassNotFoundException {
        Customer customer = new Customer(customerDTO.getId(), customerDTO.getName(), customerDTO.getAddress(), customerDTO.getSalary());
        return customerDAO.add(connection, customer);
    }

    @Override
    public boolean updateCustomer(Connection connection, CustomerDTO customerDTO) throws SQLException, ClassNotFoundException {
        Customer customer = new Customer(customerDTO.getId(), customerDTO.getName(), customerDTO.getAddress(), customerDTO.getSalary());
        return customerDAO.update(connection, customer);
    }

    @Override
    public boolean deleteCustomer(Connection connection, String id) throws SQLException, ClassNotFoundException {
        return customerDAO.delete(connection,id);
    }
}
