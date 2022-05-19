package dao.custom.impl;

import dao.custom.ItemDAO;
import entity.Item;

import javax.json.JsonArray;

/**
 * @author : M-Prageeth
 * @created : 19/05/2022 - 9:31 AM
 **/
public class ItemDAOImpl implements ItemDAO {
    @Override
    public JsonArray getAll() {
        return null;
    }

    @Override
    public boolean add(Item item) {
        return false;
    }

    @Override
    public boolean update(Item item) {
        return false;
    }

    @Override
    public boolean delete(String s) {
        return false;
    }

    @Override
    public Item search(String id) {
        return null;
    }
}
