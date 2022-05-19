package dao.custom.impl;

import dao.CrudUtil;
import dao.custom.OrderDAO;
import entity.Item;
import entity.Order;

import javax.json.*;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author : M-Prageeth
 * @created : 19/05/2022 - 9:31 AM
 **/
public class OrderDAOImpl implements OrderDAO {
    @Override
    public JsonArray getAll() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.executeQuery("SELECT * FROM `Order`");
        JsonArrayBuilder orderArray = Json.createArrayBuilder();
        while (rst.next()){
            Order order = new Order(rst.getString(1),rst.getString(2),rst.getString(3),rst.getDouble(4));
            JsonObjectBuilder obj = Json.createObjectBuilder();
            obj.add("orderId",order.getOrderId());
            obj.add("orderDate",order.getOrderDate());
            obj.add("custId",order.getCustomerId());
            obj.add("total",order.getTotal());
            orderArray.add(obj.build());
        }
        return orderArray.build();
    }

    @Override
    public boolean add(Order order) throws SQLException, ClassNotFoundException {
        return CrudUtil.executeUpdate("INSERT INTO `Order` VALUES (?,?,?,?)", order.getOrderId(), order.getOrderDate(), order.getCustomerId(), order.getTotal());
    }

    @Override
    public boolean update(Order order) {
        return false;
    }

    @Override
    public boolean delete(String s) {
        return false;
    }

    @Override
    public Order search(String id) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.executeQuery("SELECT * FROM `Order` WHERE orderId=?", id);
        Order order = null;
        while (rst.next()) {
            order = new Order(rst.getString(1), rst.getString(2), rst.getString(3), rst.getDouble(4));
        }
        return order;
    }

    @Override
    public JsonObject generateOid() throws SQLException, ClassNotFoundException {
        ResultSet orderIdSet = CrudUtil.executeQuery("SELECT orderId FROM `Order` ORDER BY orderId DESC LIMIT 1");
        JsonObjectBuilder obj = Json.createObjectBuilder();
        if (orderIdSet.next()) {
            int tempId = Integer.parseInt(orderIdSet.getString(1).split("-")[1]);
            tempId = tempId + 1;
            if (tempId <= 9) {
                String id = "O-000" + tempId;
                obj.add("orderId", id);
            } else if (tempId <= 99) {
                String id = "O-00" + tempId;
                obj.add("orderId", id);
            } else if (tempId <= 999) {
                String id = "O-0" + tempId;
                obj.add("orderId", id);
            } else if (tempId <= 9999) {
                String id = "O-" + tempId;
                obj.add("orderId", id);
            }
        } else {
            String id = "O-0001";
            obj.add("orderId", id);
        }
        return obj.build();
    }
}
