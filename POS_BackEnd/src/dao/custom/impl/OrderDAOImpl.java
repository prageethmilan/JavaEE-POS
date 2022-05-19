package dao.custom.impl;

import dao.custom.OrderDAO;
import entity.Order;

import javax.json.JsonArray;

/**
 * @author : M-Prageeth
 * @created : 19/05/2022 - 9:31 AM
 **/
public class OrderDAOImpl implements OrderDAO {
    @Override
    public JsonArray getAll() {
        return null;
    }

    @Override
    public boolean add(Order order) {
        return false;
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
    public Order search(String id) {
        return null;
    }
}
