package dao.custom;

import dao.CrudDAO;
import entity.Item;

import javax.json.JsonObject;
import java.sql.SQLException;

/**
 * @author : M-Prageeth
 * @created : 19/05/2022 - 10:12 AM
 **/
public interface ItemDAO extends CrudDAO<Item, String> {
    JsonObject generateCode() throws SQLException, ClassNotFoundException;
    boolean updateQty(int qty,String code) throws SQLException, ClassNotFoundException;
}
