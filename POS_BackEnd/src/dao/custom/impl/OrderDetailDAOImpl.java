package dao.custom.impl;

import dao.custom.OrderDetailDAO;
import entity.OrderDetail;

import javax.json.JsonArray;

/**
 * @author : M-Prageeth
 * @created : 19/05/2022 - 9:32 AM
 **/
public class OrderDetailDAOImpl implements OrderDetailDAO {
    @Override
    public JsonArray getAll() {
        return null;
    }

    @Override
    public boolean add(OrderDetail orderDetail) {
        return false;
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
}
