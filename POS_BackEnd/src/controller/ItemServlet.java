package controller;

import bo.BOFactory;
import bo.SuperBO;
import bo.custom.ItemBO;
import dao.DAOFactory;
import dao.custom.ItemDAO;
import dto.ItemDTO;
import entity.Item;

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
 * @created : 10/05/2022 - 8:58 AM
 **/

@WebServlet(urlPatterns = "/item")
public class ItemServlet extends HttpServlet {
    @Resource(name = "java:comp/env/jdbc/pool")
    DataSource dataSource;

    ItemBO itemBO = (ItemBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.ITEM);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String option = req.getParameter("option");
            resp.setContentType("application/json");
            PrintWriter writer = resp.getWriter();

            Connection connection = dataSource.getConnection();

            switch (option) {
                case "GETALL":
                    writer.print(itemBO.getAllItems(connection));
                    break;
                case "SEARCH":
                    String itemCode = req.getParameter("ItemCode");
                    ItemDTO item = itemBO.searchItem(connection, itemCode);

                    JsonObjectBuilder searchItem = Json.createObjectBuilder();
                    if (item != null) {
                        searchItem.add("status", 200);
                        searchItem.add("code", item.getCode());
                        searchItem.add("name", item.getName());
                        searchItem.add("unitPrice", item.getUnitPrice());
                        searchItem.add("qty", item.getQty());
                    } else {
                        searchItem.add("status", 400);
                    }
                    writer.print(searchItem.build());
                    break;
                case "GENERATEITEMCODE":
                    writer.print(itemBO.generateItemCode(connection));
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
        String itemCode = req.getParameter("itemCode");
        String itemName = req.getParameter("itemName");
        double unitPrice = Double.parseDouble(req.getParameter("unitPrice"));
        int itemQty = Integer.parseInt(req.getParameter("itemQty"));
        try {
            Connection connection = null;


            ItemDTO item = new ItemDTO(itemCode, itemName, unitPrice, itemQty);

            PrintWriter writer = resp.getWriter();
            resp.setContentType("application/json");

            try {
                connection = dataSource.getConnection();

                boolean add = itemBO.addItem(connection, item);
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
        String code = jsonObject.getString("code");
        String name = jsonObject.getString("name");
        double unitPrice = Double.parseDouble(jsonObject.getString("unitPrice"));
        int qty = Integer.parseInt(jsonObject.getString("qty"));

        ItemDTO item = new ItemDTO(code, name, unitPrice, qty);
        try {
            Connection connection = null;

            PrintWriter writer = resp.getWriter();

            resp.setContentType("application/json");

            try {
                connection = dataSource.getConnection();

                boolean update = itemBO.updateItem(connection, item);
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
        String itemCode = req.getParameter("itemCode");
        PrintWriter writer = resp.getWriter();
        resp.setContentType("application/json");
        try {
            Connection connection = null;

            try {
                connection = dataSource.getConnection();

                boolean delete = itemBO.deleteItem(connection, itemCode);
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