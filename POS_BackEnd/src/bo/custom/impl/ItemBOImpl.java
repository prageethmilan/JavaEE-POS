package bo.custom.impl;

import bo.custom.ItemBO;
import dao.DAOFactory;
import dao.custom.ItemDAO;
import dto.ItemDTO;
import entity.Item;

import javax.json.JsonArray;
import javax.json.JsonObject;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author : M-Prageeth
 * @created : 22/05/2022 - 10:14 AM
 **/
public class ItemBOImpl implements ItemBO {

    ItemDAO itemDAO = (ItemDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ITEM);

    @Override
    public JsonArray getAllItems(Connection connection) throws SQLException, ClassNotFoundException {
        return itemDAO.getAll(connection);
    }

    @Override
    public ItemDTO searchItem(Connection connection, String code) throws SQLException, ClassNotFoundException {
        Item search = itemDAO.search(connection, code);
        if (search != null) {
            return new ItemDTO(search.getCode(), search.getName(), search.getUnitPrice(), search.getQty());
        }
        return null;
    }

    @Override
    public JsonObject generateItemCode(Connection connection) throws SQLException, ClassNotFoundException {
        return itemDAO.generateCode(connection);
    }

    @Override
    public boolean addItem(Connection connection, ItemDTO itemDTO) throws SQLException, ClassNotFoundException {
        Item item = new Item(itemDTO.getCode(), itemDTO.getName(), itemDTO.getUnitPrice(), itemDTO.getQty());
        return itemDAO.add(connection, item);
    }

    @Override
    public boolean updateItem(Connection connection, ItemDTO itemDTO) throws SQLException, ClassNotFoundException {
        Item item = new Item(itemDTO.getCode(), itemDTO.getName(), itemDTO.getUnitPrice(), itemDTO.getQty());
        return itemDAO.update(connection, item);
    }

    @Override
    public boolean deleteItem(Connection connection, String code) throws SQLException, ClassNotFoundException {
        return itemDAO.delete(connection, code);
    }
}
