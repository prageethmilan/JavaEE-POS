package dao.custom.impl;

import dao.CrudUtil;
import dao.custom.OrderDetailDAO;
import entity.OrderDetail;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author : M-Prageeth
 * @created : 19/05/2022 - 9:32 AM
 **/
public class OrderDetailDAOImpl implements OrderDetailDAO {
    @Override
    public JsonArray getAll() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.executeQuery("SELECT * FROM `Order Detail`");
        JsonArrayBuilder orderDetailsArray = Json.createArrayBuilder();
        while (rst.next()) {
            OrderDetail orderDetail = new OrderDetail(rst.getString(1), rst.getString(2), rst.getString(3), rst.getDouble(4), rst.getInt(5), rst.getDouble(6));
            JsonObjectBuilder orderDetailObj = Json.createObjectBuilder();
            orderDetailObj.add("orderId", orderDetail.getOrderId());
            orderDetailObj.add("itemCode", orderDetail.getItemCode());
            orderDetailObj.add("itemName", orderDetail.getItemName());
            orderDetailObj.add("unitPrice", orderDetail.getUnitPrice());
            orderDetailObj.add("qty", orderDetail.getBuyQty());
            orderDetailObj.add("total", orderDetail.getTotal());
            orderDetailsArray.add(orderDetailObj.build());
        }
        return orderDetailsArray.build();
    }

    @Override
    public boolean add(OrderDetail orderDetail) throws SQLException, ClassNotFoundException {
        return CrudUtil.executeUpdate("INSERT INTO `Order Detail` VALUES (?,?,?,?,?,?)", orderDetail.getOrderId(), orderDetail.getItemCode(), orderDetail.getItemName(), orderDetail.getUnitPrice(), orderDetail.getBuyQty(), orderDetail.getTotal());
    }

    @Override
    public boolean update(OrderDetail orderDetail) {
        return false;
    }

    @Override
    public boolean delete(String s) {
        return false;
    }

    @Override
    public OrderDetail search(String id) {
        return null;
    }

    @Override
    public JsonArray searchOrderDetails(String id) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.executeQuery("SELECT * FROM `Order Detail` WHERE orderId=?", id);
        JsonArrayBuilder orderDetailArray = Json.createArrayBuilder();
        while (rst.next()) {
            OrderDetail orderDetail = new OrderDetail(rst.getString(1), rst.getString(2), rst.getString(3), rst.getDouble(4), rst.getInt(5), rst.getDouble(6));
            JsonObjectBuilder obj = Json.createObjectBuilder();
            obj.add("orderId", orderDetail.getOrderId());
            obj.add("itemCode", orderDetail.getOrderId());
            obj.add("itemName", orderDetail.getOrderId());
            obj.add("unitPrice", orderDetail.getOrderId());
            obj.add("qty", orderDetail.getOrderId());
            obj.add("total", orderDetail.getOrderId());
            orderDetailArray.add(obj.build());
        }
        return orderDetailArray.build();
    }
}
