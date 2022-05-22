package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author : M-Prageeth
 * @created : 18/05/2022 - 10:09 PM
 **/


public class CrudUtil {

    private static PreparedStatement getPreparedStatement(Connection connection, String sql, Object... args) throws SQLException, ClassNotFoundException {
        PreparedStatement pstm = connection.prepareStatement(sql);
        for (int i = 0; i < args.length; i++) {
            pstm.setObject(i + 1, args[i]);
        }
        return pstm;
    }

    public static boolean executeUpdate(Connection connection, String sql, Object... args) throws SQLException, ClassNotFoundException {
        return getPreparedStatement(connection, sql, args).executeUpdate() > 0;
    }

    public static ResultSet executeQuery(Connection connection, String sql, Object... args) throws SQLException, ClassNotFoundException {
        return getPreparedStatement(connection, sql, args).executeQuery();
    }
}
