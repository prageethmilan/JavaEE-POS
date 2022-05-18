import javax.json.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

/**
 * @author : M-Prageeth
 * @created : 10/05/2022 - 8:58 AM
 **/

@WebServlet(urlPatterns = "/order")
public class OrderServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String option = req.getParameter("option");
            resp.setContentType("application/json");


            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/JavaEEPOS", "root", "1234");
            PrintWriter writer = resp.getWriter();

            resp.addHeader("Access-Control-Allow-Origin", "*");

            switch (option) {
                case "GENERATEORDERID":
                    ResultSet orderIdSet = connection.prepareStatement("SELECT orderId FROM `Order` ORDER BY orderId DESC LIMIT 1").executeQuery();
                    JsonObjectBuilder obj = Json.createObjectBuilder();
                    if (orderIdSet.next()) {
                        int tempId = Integer.parseInt(orderIdSet.getString(1).split("-")[1]);
                        tempId = tempId + 1;
                        if (tempId <= 9) {
                            String id = "O-000" + tempId;
                            obj.add("orderId", id);
                        } else if (tempId <= 99) {
                            String id = "O-00" + tempId;
                            obj.add("orderId", id);
                        } else if (tempId <= 999) {
                            String id = "O-0" + tempId;
                            obj.add("orderId", id);
                        } else if (tempId <= 9999) {
                            String id = "O-" + tempId;
                            obj.add("orderId", id);
                        }
                    } else {
                        String id = "O-0001";
                        obj.add("orderId", id);
                    }

                    writer.print(obj.build());

                    break;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JsonReader reader = Json.createReader(req.getReader());
        JsonObject jsonObject = reader.readObject();
        String orderId = jsonObject.getString("orderId");
        String orderDate = jsonObject.getString("orderDate");
        String customerId = jsonObject.getString("customerId");
        String orderTotal = jsonObject.getString("orderTotal");
        JsonArray orderDetails = jsonObject.getJsonArray("orderDetails");

        JsonObjectBuilder builder = Json.createObjectBuilder();

        resp.setContentType("application/json");
        resp.addHeader("Access-Control-Allow-Origin", "*");

        PrintWriter writer = resp.getWriter();


        Connection connection = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/JavaEEPOS", "root", "1234");
            connection.setAutoCommit(false);

            PreparedStatement pstm = connection.prepareStatement("INSERT INTO `Order` VALUES (?,?,?,?)");
            pstm.setObject(1, orderId);
            pstm.setObject(2, orderDate);
            pstm.setObject(3, customerId);
            pstm.setObject(4, orderTotal);

            if (pstm.executeUpdate() > 0) {
                for (int i = 0; i < orderDetails.size(); i++) {

                    JsonObject object = orderDetails.getJsonObject(i);
                    String itemCode = object.getString("itemCode");
                    String itemName = object.getString("itemName");
                    double unitPrice = Double.parseDouble(object.getString("unitPrice"));
                    int buyQty = object.getInt("buyQty");
                    double total = object.getInt("total");

                    PreparedStatement preStm = connection.prepareStatement("INSERT INTO `Order Detail` VALUES (?,?,?,?,?,?)");
                    preStm.setObject(1, orderId);
                    preStm.setObject(2, itemCode);
                    preStm.setObject(3, itemName);
                    preStm.setObject(4, unitPrice);
                    preStm.setObject(5, buyQty);
                    preStm.setObject(6, total);

                    if (preStm.executeUpdate()>0){
                        connection.commit();
                        builder.add("status", 200);
                        builder.add("message", "Place Order Successful");
                    }else{
                        connection.rollback();
                        builder.add("status", 400);
                        builder.add("message", "failed");
                    }
                }
            }else{
                connection.rollback();
                builder.add("status", 400);
                builder.add("message", "failed");
            }

            writer.print(builder.build());

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JsonReader reader = Json.createReader(req.getReader());
        JsonObject object = reader.readObject();
        String itemCode = object.getString("code");
        int qty = object.getInt("qty");
        PrintWriter writer = resp.getWriter();

        resp.setContentType("application/json");

        resp.addHeader("Access-Control-Allow-Origin", "*");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection root = DriverManager.getConnection("jdbc:mysql://localhost:3306/JavaEEPOS", "root", "1234");
            PreparedStatement pstm = root.prepareStatement("UPDATE Item SET qtyOnHand=(qtyOnHand-" + qty + ") WHERE code='" + itemCode + "'");

            if (pstm.executeUpdate()>0){
                JsonObjectBuilder builder = Json.createObjectBuilder();
                builder.add("status",200);
                builder.add("message","Updated");
                writer.print(builder.build());
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.addHeader("Access-Control-Allow-Methods", "DELETE, PUT");
        resp.addHeader("Access-Control-Allow-Headers", "Content-Type");
    }
}
