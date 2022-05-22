package bo.custom.impl;

import bo.custom.OrderBO;
import dao.DAOFactory;
import dao.custom.ItemDAO;
import dao.custom.OrderDAO;
import dao.custom.OrderDetailDAO;
import dto.OrderDTO;
import dto.OrderDetailDTO;
import entity.Order;
import entity.OrderDetail;

import javax.json.JsonArray;
import javax.json.JsonObject;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author : M-Prageeth
 * @created : 22/05/2022 - 10:15 AM
 **/
public class OrderBOImpl implements OrderBO {

    OrderDAO orderDAO = (OrderDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ORDER);
    OrderDetailDAO orderDetailDAO = (OrderDetailDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ORDERDETAIL);
    ItemDAO itemDAO = (ItemDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ITEM);

    @Override
    public JsonObject generateOrderId(Connection connection) throws SQLException, ClassNotFoundException {
        return orderDAO.generateOid(connection);
    }

    @Override
    public JsonArray getAllOrders(Connection connection) throws SQLException, ClassNotFoundException {
        return orderDAO.getAll(connection);
    }

    @Override
    public JsonArray getAllOrderDetails(Connection connection) throws SQLException, ClassNotFoundException {
        return orderDetailDAO.getAll(connection);
    }

    @Override
    public OrderDTO searchOrder(Connection connection, String id) throws SQLException, ClassNotFoundException {
        Order order = orderDAO.search(connection, id);
        System.out.println(order);
        if (order != null) {
            return new OrderDTO(order.getOrderId(), order.getOrderDate(), order.getCustomerId(), order.getTotal());
        }
        return null;
    }

    @Override
    public JsonArray searchOrderDetails(Connection connection, String id) throws SQLException, ClassNotFoundException {
        return orderDetailDAO.searchOrderDetails(connection, id);
    }

    @Override
    public boolean placeOrder(Connection connection, OrderDTO orderDTO) {
        try {
            connection.setAutoCommit(false);
            Order order = new Order(orderDTO.getOrderId(), orderDTO.getOrderDate(), orderDTO.getCustomerId(), orderDTO.getTotal());
            boolean add = orderDAO.add(connection, order);
            if (!add) {
                connection.rollback();
                connection.setAutoCommit(true);
                return false;
            }
            for (OrderDetailDTO dto : orderDTO.getOrderDetails()) {
                OrderDetail orderDetail1 = new OrderDetail(dto.getOrderId(), dto.getItemCode(), dto.getItemName(), dto.getUnitPrice(), dto.getBuyQty(), dto.getTotal());
                boolean orderDetailAdded = orderDetailDAO.add(connection, orderDetail1);
                if (!orderDetailAdded) {
                    connection.rollback();
                    connection.setAutoCommit(true);
                    return false;
                }
            }

            for (OrderDetailDTO dto : orderDTO.getOrderDetails()) {
                boolean updateQty = itemDAO.updateQty(connection, dto.getBuyQty(), dto.getItemCode());
                if (!updateQty) {
                    connection.rollback();
                    connection.setAutoCommit(true);
                    return false;
                }
            }
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
