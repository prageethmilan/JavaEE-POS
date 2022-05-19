package controller;

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

@WebServlet(urlPatterns = "/item")
public class ItemServlet extends HttpServlet {
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
                case "GETALL":
                    ResultSet rst = connection.prepareStatement("SELECT * FROM Item").executeQuery();
                    JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

                    while (rst.next()) {
                        String code = rst.getString(1);
                        String name = rst.getString(2);
                        double unitPrice = rst.getDouble(3);
                        int qty = rst.getInt(4);

                        JsonObjectBuilder obj = Json.createObjectBuilder();
                        obj.add("code", code);
                        obj.add("name", name);
                        obj.add("unitPrice", unitPrice);
                        obj.add("qty", qty);

                        arrayBuilder.add(obj.build());
                    }

                    JsonObjectBuilder response = Json.createObjectBuilder();
                    response.add("status", 200);
                    response.add("message", "Done");
                    response.add("data", arrayBuilder.build());
                    writer.print(response.build());

                    break;
                case "SEARCH":
                    String itemCode = req.getParameter("ItemCode");
                    PreparedStatement pstm = connection.prepareStatement("SELECT * FROM Item WHERE code=?");
                    pstm.setObject(1, itemCode);
                    ResultSet searchSet = pstm.executeQuery();

                    JsonObjectBuilder searchItem = Json.createObjectBuilder();


                    while (searchSet.next()) {
                        String code = searchSet.getString(1);
                        String name = searchSet.getString(2);
                        double unitPrice = searchSet.getDouble(3);
                        int qty = searchSet.getInt(4);

                        searchItem.add("status", 200);
                        searchItem.add("code", code);
                        searchItem.add("name", name);
                        searchItem.add("unitPrice", unitPrice);
                        searchItem.add("qty", qty);

                    }

                    writer.print(searchItem.build());

                    break;

                case "GENERATEITEMCODE":
                    ResultSet codeSet = connection.prepareStatement("SELECT code FROM Item ORDER BY code DESC LIMIT 1").executeQuery();
                    JsonObjectBuilder obj = Json.createObjectBuilder();
                    if (codeSet.next()) {
                        int tempCode = Integer.parseInt(codeSet.getString(1).split("-")[1]);
                        tempCode = tempCode + 1;
                        if (tempCode <= 9) {
                            String code = "I00-000" + tempCode;
                            obj.add("code", code);
                        } else if (tempCode <= 99) {
                            String code = "I00-00" + tempCode;
                            obj.add("code", code);
                        } else if (tempCode <= 999) {
                            String code = "I00-0" + tempCode;
                            obj.add("code", code);
                        } else if (tempCode <= 9999) {
                            String code = "I00-" + tempCode;
                            obj.add("code", code);
                        }
                    }else{
                        String code = "I00-0001";
                        obj.add("code",code);
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
        String itemCode = req.getParameter("itemCode");
        String itemName = req.getParameter("itemName");
        double unitPrice = Double.parseDouble(req.getParameter("unitPrice"));
        int itemQty = Integer.parseInt(req.getParameter("itemQty"));

        PrintWriter writer = resp.getWriter();
        resp.setContentType("application/json");
        resp.addHeader("Access-Control-Allow-Origin", "*");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/JavaEEPOS", "root", "1234");

            PreparedStatement pstm = connection.prepareStatement("INSERT INTO Item VALUES (?,?,?,?)");
            pstm.setObject(1, itemCode);
            pstm.setObject(2, itemName);
            pstm.setObject(3, unitPrice);
            pstm.setObject(4, itemQty);

            if (pstm.executeUpdate() > 0) {
                JsonObjectBuilder response = Json.createObjectBuilder();
                resp.setStatus(HttpServletResponse.SC_CREATED);
                response.add("status", 200);
                response.add("message", "Successfully added");
                response.add("data", "");
                writer.print(response.build());
            }
        } catch (ClassNotFoundException e) {
            JsonObjectBuilder response = Json.createObjectBuilder();
            response.add("status", 400);
            response.add("message", "error");
            response.add("data", e.getLocalizedMessage());
            writer.print(response.build());

            resp.setStatus(HttpServletResponse.SC_OK);
            e.printStackTrace();
        } catch (SQLException throwables) {
            JsonObjectBuilder response = Json.createObjectBuilder();
            response.add("status", 400);
            response.add("message", "error");
            response.add("data", throwables.getLocalizedMessage());
            writer.print(response.build());

            resp.setStatus(HttpServletResponse.SC_OK);
            throwables.printStackTrace();
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JsonReader reader = Json.createReader(req.getReader());
        JsonObject jsonObject = reader.readObject();
        String code = jsonObject.getString("code");
        String name = jsonObject.getString("name");
        double unitPrice = Double.parseDouble(jsonObject.getString("unitPrice"));
        int qty = Integer.parseInt(jsonObject.getString("qty"));

        PrintWriter writer = resp.getWriter();

        resp.setContentType("application/json");

        resp.addHeader("Access-Control-Allow-Origin", "*");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/JavaEEPOS", "root", "1234");

            PreparedStatement pstm = connection.prepareStatement("UPDATE Item SET name=?,unitPrice=?,qtyOnHand=? WHERE code=?");
            pstm.setObject(1, name);
            pstm.setObject(2, unitPrice);
            pstm.setObject(3, qty);
            pstm.setObject(4, code);

            if (pstm.executeUpdate() > 0) {
                JsonObjectBuilder response = Json.createObjectBuilder();
                response.add("status", 200);
                response.add("message", "Successfully Updated");
                response.add("data", "");
                writer.print(response.build());
            } else {
                JsonObjectBuilder response = Json.createObjectBuilder();
                response.add("status", 400);
                response.add("message", "Update Failed");
                response.add("data", "");
                writer.print(response.build());
            }
        } catch (ClassNotFoundException e) {
            JsonObjectBuilder response = Json.createObjectBuilder();
            response.add("status", 500);
            response.add("message", "Update Failed");
            response.add("data", e.getLocalizedMessage());
            writer.print(response.build());
        } catch (SQLException throwables) {
            JsonObjectBuilder response = Json.createObjectBuilder();
            response.add("status", 500);
            response.add("message", "Update Failed");
            response.add("data", throwables.getLocalizedMessage());
            writer.print(response.build());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String itemCode = req.getParameter("itemCode");
        PrintWriter writer = resp.getWriter();
        resp.setContentType("application/json");

        resp.addHeader("Access-Control-Allow-Origin", "*");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/JavaEEPOS", "root", "1234");

            PreparedStatement pstm = connection.prepareStatement("DELETE FROM Item WHERE code=?");
            pstm.setObject(1, itemCode);

            if (pstm.executeUpdate() > 0) {
                JsonObjectBuilder builder = Json.createObjectBuilder();
                builder.add("status", 200);
                builder.add("data", "");
                builder.add("message", "Successfully deleted");
                writer.print(builder.build());
            } else {
                JsonObjectBuilder builder = Json.createObjectBuilder();
                builder.add("status", 400);
                builder.add("data", "");
                builder.add("message", "Delete Failed");
                writer.print(builder.build());
            }
        } catch (ClassNotFoundException e) {
            resp.setStatus(200);
            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
            objectBuilder.add("status", 500);
            objectBuilder.add("message", "Error");
            objectBuilder.add("data", e.getLocalizedMessage());
            writer.print(objectBuilder.build());
        } catch (SQLException throwables) {
            resp.setStatus(200);
            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
            objectBuilder.add("status", 500);
            objectBuilder.add("message", "Error");
            objectBuilder.add("data", throwables.getLocalizedMessage());
            writer.print(objectBuilder.build());
        }
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.addHeader("Access-Control-Allow-Methods", "DELETE, PUT");
        resp.addHeader("Access-Control-Allow-Headers", "content-type");
    }
}