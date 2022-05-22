package controller;

import bo.BOFactory;
import bo.SuperBO;
import bo.custom.CustomerBO;
import dao.DAOFactory;
import dao.custom.CustomerDAO;
import dto.CustomerDTO;
import entity.Customer;

import javax.annotation.Resource;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author : M-Prageeth
 * @created : 10/05/2022 - 8:57 AM
 **/

@WebServlet(urlPatterns = "/customer")
public class CustomerServlet extends HttpServlet {

    @Resource(name = "java:comp/env/jdbc/pool")
    DataSource dataSource;

    CustomerBO customerBO = (CustomerBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.CUSTOMER);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Connection connection = dataSource.getConnection();
            String option = req.getParameter("option");
            resp.setContentType("application/json");
            PrintWriter writer = resp.getWriter();

            switch (option) {
                case "GETALL":
                    writer.print(customerBO.getAllCustomers(connection));
                    break;
                case "SEARCH":
                    String custId = req.getParameter("CusID");
                    CustomerDTO customer = customerBO.searchCustomer(connection, custId);

                    JsonObjectBuilder searchCustomer = Json.createObjectBuilder();
                    if (customer != null) {
                        searchCustomer.add("status", 200);
                        searchCustomer.add("id", customer.getId());
                        searchCustomer.add("name", customer.getName());
                        searchCustomer.add("address", customer.getAddress());
                        searchCustomer.add("salary", customer.getSalary());
                    } else {
                        searchCustomer.add("status", 400);
                    }
                    writer.print(searchCustomer.build());
                    break;
                case "GENERATECUSTID":
                    writer.print(customerBO.generateCustomerId(connection));
                    break;
            }
            connection.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("customerID");
        String name = req.getParameter("customerName");
        String address = req.getParameter("customerAddress");
        double salary = Double.parseDouble(req.getParameter("customerSalary"));

        CustomerDTO customer = new CustomerDTO(id, name, address, salary);

        PrintWriter writer = resp.getWriter();
        resp.setContentType("application/json");
        try {
            Connection connection = null;
            try {
                connection = dataSource.getConnection();

                boolean add = customerBO.addCustomer(connection, customer);
                if (add) {
                    JsonObjectBuilder response = Json.createObjectBuilder();
                    resp.setStatus(HttpServletResponse.SC_CREATED);
                    response.add("status", 200);
                    response.add("message", "Successfully added");
                    response.add("data", "");
                    writer.print(response.build());
                }
            } catch (ClassNotFoundException e) {
                JsonObjectBuilder response = Json.createObjectBuilder();
                response.add("status", 400);
                response.add("message", "error");
                response.add("data", e.getLocalizedMessage());
                writer.print(response.build());

                resp.setStatus(HttpServletResponse.SC_OK);
                e.printStackTrace();
            } catch (SQLException throwables) {
                JsonObjectBuilder response = Json.createObjectBuilder();
                response.add("status", 400);
                response.add("message", "error");
                response.add("data", throwables.getLocalizedMessage());
                writer.print(response.build());

                resp.setStatus(HttpServletResponse.SC_OK);
                throwables.printStackTrace();
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JsonReader reader = Json.createReader(req.getReader());
        JsonObject jsonObject = reader.readObject();
        String id = jsonObject.getString("id");
        String name = jsonObject.getString("name");
        String address = jsonObject.getString("address");
        double salary = Double.parseDouble(jsonObject.getString("salary"));
        try {

            Connection connection = null;

            CustomerDTO customer = new CustomerDTO(id, name, address, salary);

            PrintWriter writer = resp.getWriter();

            resp.setContentType("application/json");
            try {
                connection = dataSource.getConnection();

                boolean update = customerBO.updateCustomer(connection, customer);
                if (update) {
                    JsonObjectBuilder response = Json.createObjectBuilder();
                    response.add("status", 200);
                    response.add("message", "Successfully Updated");
                    response.add("data", "");
                    writer.print(response.build());
                } else {
                    JsonObjectBuilder response = Json.createObjectBuilder();
                    response.add("status", 400);
                    response.add("message", "Update Failed");
                    response.add("data", "");
                    writer.print(response.build());
                }
            } catch (ClassNotFoundException e) {
                JsonObjectBuilder response = Json.createObjectBuilder();
                response.add("status", 500);
                response.add("message", "Update Failed");
                response.add("data", e.getLocalizedMessage());
                writer.print(response.build());
            } catch (SQLException throwables) {
                JsonObjectBuilder response = Json.createObjectBuilder();
                response.add("status", 500);
                response.add("message", "Update Failed");
                response.add("data", throwables.getLocalizedMessage());
                writer.print(response.build());
            }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String cusID = req.getParameter("CusID");
        PrintWriter writer = resp.getWriter();
        resp.setContentType("application/json");
        try {
            Connection connection = null;
            try {
                connection = dataSource.getConnection();
                boolean delete = customerBO.deleteCustomer(connection, cusID);
                if (delete) {
                    JsonObjectBuilder builder = Json.createObjectBuilder();
                    builder.add("status", 200);
                    builder.add("data", "");
                    builder.add("message", "Successfully deleted");
                    writer.print(builder.build());
                } else {
                    JsonObjectBuilder builder = Json.createObjectBuilder();
                    builder.add("status", 400);
                    builder.add("data", "");
                    builder.add("message", "Delete Failed");
                    writer.print(builder.build());
                }
            } catch (ClassNotFoundException e) {
                resp.setStatus(200);
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                objectBuilder.add("status", 500);
                objectBuilder.add("message", "Error");
                objectBuilder.add("data", e.getLocalizedMessage());
                writer.print(objectBuilder.build());
            } catch (SQLException throwables) {
                resp.setStatus(200);
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                objectBuilder.add("status", 500);
                objectBuilder.add("message", "Error");
                objectBuilder.add("data", throwables.getLocalizedMessage());
                writer.print(objectBuilder.build());
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
