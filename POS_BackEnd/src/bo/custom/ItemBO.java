package bo.custom;

import bo.SuperBO;
import dto.ItemDTO;

import javax.json.JsonArray;
import javax.json.JsonObject;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author : M-Prageeth
 * @created : 22/05/2022 - 10:14 AM
 **/
public interface ItemBO extends SuperBO {
    JsonArray getAllItems(Connection connection) throws SQLException, ClassNotFoundException;

    ItemDTO searchItem(Connection connection,String code) throws SQLException, ClassNotFoundException;

    JsonObject generateItemCode(Connection connection) throws SQLException, ClassNotFoundException;

    boolean addItem(Connection connection,ItemDTO itemDTO) throws SQLException, ClassNotFoundException;

    boolean updateItem(Connection connection,ItemDTO itemDTO) throws SQLException, ClassNotFoundException;

    boolean deleteItem(Connection connection,String code) throws SQLException, ClassNotFoundException;
}
