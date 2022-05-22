package controller;

import bo.BOFactory;
import bo.custom.OrderBO;
import dto.OrderDTO;
import dto.OrderDetailDTO;

import javax.annotation.Resource;
import javax.json.*;
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
import java.util.ArrayList;

/**
 * @author : M-Prageeth
 * @created : 10/05/2022 - 8:58 AM
 **/

@WebServlet(urlPatterns = "/order")
public class OrderServlet extends HttpServlet {

    @Resource(name = "java:comp/env/jdbc/pool")
    DataSource dataSource;

    OrderBO orderBO = (OrderBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.ORDER);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String option = req.getParameter("option");
            resp.setContentType("application/json");
            PrintWriter writer = resp.getWriter();

            Connection connection = dataSource.getConnection();

            switch (option) {
                case "GENERATEORDERID":
                    writer.print(orderBO.generateOrderId(connection));
                    break;

                case "GETALLORDERS":
                    writer.print(orderBO.getAllOrders(connection));
                    break;

                case "GETALLORDERDETAILS":
                    writer.print(orderBO.getAllOrderDetails(connection));
                    break;

                case "SEARCHORDER":
                    String orderId = req.getParameter("orderId");
                    OrderDTO order = orderBO.searchOrder(connection, orderId);
                    System.out.println(order);
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
                    writer.print(orderBO.searchOrderDetails(connection, oId));
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
        JsonReader reader = Json.createReader(req.getReader());
        JsonObject jsonObject = reader.readObject();
        String orderId = jsonObject.getString("orderId");
        String orderDate = jsonObject.getString("orderDate");
        String customerId = jsonObject.getString("customerId");
        double orderTotal = Double.parseDouble(jsonObject.getString("orderTotal"));
        JsonArray orderDetails = jsonObject.getJsonArray("orderDetails");
        ArrayList<OrderDetailDTO> orderDetailDTOS = new ArrayList<>();
        JsonObjectBuilder builder = Json.createObjectBuilder();

        resp.setContentType("application/json");

        PrintWriter writer = resp.getWriter();

        for (JsonValue object : orderDetails) {
            OrderDetailDTO orderDetail = new OrderDetailDTO(orderId, object.asJsonObject().getString("itemCode"), object.asJsonObject().getString("itemName"), Double.parseDouble(object.asJsonObject().getString("unitPrice")), object.asJsonObject().getInt("buyQty"), object.asJsonObject().getInt("total"));
            orderDetailDTOS.add(orderDetail);
        }

        OrderDTO orderDTO = new OrderDTO(orderId, orderDate, customerId, orderTotal, orderDetailDTOS);
        try {
            Connection connection = null;
            try {
                connection = dataSource.getConnection();
                boolean added = orderBO.placeOrder(connection, orderDTO);
                if (added) {
                    builder.add("boolean", true);
                    writer.print(builder.build());
                } else {
                    builder.add("boolean", true);
                    writer.print(builder.build());
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPut(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
    }
}
