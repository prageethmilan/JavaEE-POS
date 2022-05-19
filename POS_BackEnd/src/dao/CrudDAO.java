package dao;

import javax.json.JsonArray;
import java.sql.SQLException;

/**
 * @author : M-Prageeth
 * @created : 19/05/2022 - 10:07 AM
 **/
public interface CrudDAO<T,ID> extends SuperDAO{
    JsonArray getAll() throws SQLException, ClassNotFoundException;
    boolean add(T t) throws SQLException, ClassNotFoundException;
    boolean update(T t) throws SQLException, ClassNotFoundException;
    boolean delete(ID id) throws SQLException, ClassNotFoundException;
    T search(ID id) throws SQLException, ClassNotFoundException;
}
