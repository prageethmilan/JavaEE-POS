package controller;

import dao.DAOFactory;
import dao.custom.ItemDAO;
import dao.custom.OrderDAO;
import dao.custom.OrderDetailDAO;
import db.DbConnection;
import entity.Order;
import entity.OrderDetail;

import javax.json.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author : M-Prageeth
 * @created : 10/05/2022 - 8:58 AM
 **/

@WebServlet(urlPatterns = "/order")
public class OrderServlet extends HttpServlet {
    OrderDAO orderDAO = (OrderDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ORDER);
    OrderDetailDAO orderDetailDAO = (OrderDetailDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ORDERDETAIL);
    ItemDAO itemDAO = (ItemDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ITEM);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String option = req.getParameter("option");
            resp.setContentType("application/json");
            PrintWriter writer = resp.getWriter();

            switch (option) {
                case "GENERATEORDERID":
                    writer.print(orderDAO.generateOid());
                    break;

                case "GETALLORDERS":
                    writer.print(orderDAO.getAll());
                    break;

                case "GETALLORDERDETAILS":
                    writer.print(orderDetailDAO.getAll());
                    break;

                case "SEARCHORDER":
                    String orderId = req.getParameter("orderId");
                    Order order = orderDAO.search(orderId);
                    JsonObjectBuilder searchOrder = Json.createObjectBuilder();
                    if (order != null) {
                        searchOrder.add("status", 200);
                        searchOrder.add("orderId", order.getOrderId());
                        searchOrder.add("orderDate", order.getOrderDate());
                        searchOrder.add("customerId", order.getCustomerId());
                        searchOrder.add("total", order.getTotal());
                    } else {
                        searchOrder.add("status", 400);
                    }
                    writer.print(searchOrder.build());
                    break;

                case "SEARCHORDERDETAIL":
                    String oId = req.getParameter("orderId");
                    writer.print(orderDetailDAO.searchOrderDetails(oId));
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JsonReader reader = Json.createReader(req.getReader());
        JsonObject jsonObject = reader.readObject();
        String orderId = jsonObject.getString("orderId");
        String orderDate = jsonObject.getString("orderDate");
        String customerId = jsonObject.getString("customerId");
        double orderTotal = Double.parseDouble(jsonObject.getString("orderTotal"));
        JsonArray orderDetails = jsonObject.getJsonArray("orderDetails");

        JsonObjectBuilder builder = Json.createObjectBuilder();

        resp.setContentType("application/json");

        PrintWriter writer = resp.getWriter();
        Connection connection = null;

        try {
            connection = DbConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            Order order = new Order(orderId, orderDate, customerId, orderTotal);
            boolean add = orderDAO.add(order);
            if (!add) {
                connection.rollback();
                connection.setAutoCommit(true);
                builder.add("boolean", false);
                writer.print(builder.build());
            }

            for (JsonValue object : orderDetails) {
                OrderDetail orderDetail = new OrderDetail(orderId, object.asJsonObject().getString("itemCode"), object.asJsonObject().getString("itemName"), Double.parseDouble(object.asJsonObject().getString("unitPrice")), object.asJsonObject().getInt("buyQty"), object.asJsonObject().getInt("total"));
                boolean orderDetailsAdd = orderDetailDAO.add(orderDetail);
                if (!orderDetailsAdd) {
                    connection.rollback();
                    connection.setAutoCommit(true);
                    builder.add("boolean", false);
                    writer.print(builder.build());
                }
            }

            connection.commit();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        builder.add("boolean", true);
        writer.print(builder.build());
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JsonReader reader = Json.createReader(req.getReader());
        JsonObject object = reader.readObject();
        String itemCode = object.getString("code");
        int qty = object.getInt("qty");
        PrintWriter writer = resp.getWriter();
        resp.setContentType("application/json");

        try {

            boolean updated = itemDAO.updateQty(qty, itemCode);

            if (updated) {
                JsonObjectBuilder builder = Json.createObjectBuilder();
                builder.add("status", 200);
                builder.add("message", "Updated");
                writer.print(builder.build());
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
    }
}
