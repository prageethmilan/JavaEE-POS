package dao;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author : M-Prageeth
 * @created : 18/05/2022 - 10:09 PM
 **/


public class CrudUtil {
    @Resource(name = "java:comp/env/jdbc/pool")
    static DataSource ds;

    private static PreparedStatement getPreparedStatement(String sql, Object... args) throws SQLException {
        Connection connection = ds.getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        for (int i = 0; i < args.length; i++) {
            pstm.setObject(i + 1, args[i]);
        }
        return pstm;
    }

    public static boolean executeUpdate(String sql, Object... args) throws SQLException {
        return getPreparedStatement(sql, args).executeUpdate() > 0;
    }

    public static ResultSet executeQuery(String sql, Object... args) throws SQLException {
        return getPreparedStatement(sql, args).executeQuery();
    }
}
