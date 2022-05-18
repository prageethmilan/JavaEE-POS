package servlets;

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
 * @created : 10/05/2022 - 8:57 AM
 **/

@WebServlet(urlPatterns = "/customer")
public class CustomerServlet extends HttpServlet {
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
                    ResultSet rst = connection.prepareStatement("SELECT * FROM Customer").executeQuery();
                    JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

                    while (rst.next()) {
                        String id = rst.getString(1);
                        String name = rst.getString(2);
                        String address = rst.getString(3);
                        double salary = rst.getDouble(4);

                        JsonObjectBuilder obj = Json.createObjectBuilder();
                        obj.add("id", id);
                        obj.add("name", name);
                        obj.add("address", address);
                        obj.add("salary", salary);

                        arrayBuilder.add(obj.build());
                    }

                    JsonObjectBuilder response = Json.createObjectBuilder();
                    response.add("status", 200);
                    response.add("message", "Done");
                    response.add("data", arrayBuilder.build());
                    writer.print(response.build());
                    break;
                case "SEARCH":
                    String custId = req.getParameter("CusID");
                    PreparedStatement pstm = connection.prepareStatement("SELECT * FROM Customer WHERE id=?");
                    pstm.setObject(1, custId);
                    ResultSet searchSet = pstm.executeQuery();

                    JsonObjectBuilder searchCustomer = Json.createObjectBuilder();

                    while (searchSet.next()) {
                        String id = searchSet.getString(1);
                        String name = searchSet.getString(2);
                        String address = searchSet.getString(3);
                        double salary = searchSet.getDouble(4);

                        searchCustomer.add("status", 200);
                        searchCustomer.add("id", id);
                        searchCustomer.add("name", name);
                        searchCustomer.add("address", address);
                        searchCustomer.add("salary", salary);

                    }

                    writer.print(searchCustomer.build());

                    break;
                case "GENERATECUSTID":
                    ResultSet idSet = connection.prepareStatement("SELECT id FROM Customer ORDER BY id DESC LIMIT 1").executeQuery();
                    JsonObjectBuilder obj = Json.createObjectBuilder();
                    if (idSet.next()) {
                        int tempId = Integer.parseInt(idSet.getString(1).split("-")[1]);
                        tempId = tempId + 1;
                        if (tempId <= 9) {
                            String id = "C00-000" + tempId;
                            obj.add("id", id);
                        } else if (tempId <= 99) {
                            String id = "C00-00" + tempId;
                            obj.add("id", id);
                        } else if (tempId <= 999) {
                            String id = "C00-0" + tempId;
                            obj.add("id", id);
                        } else if (tempId <= 9999) {
                            String id = "C00-" + tempId;
                            obj.add("id", id);
                        }
                    }else{
                        String id = "C00-0001";
                        obj.add("id",id);
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
        String id = req.getParameter("customerID");
        String name = req.getParameter("customerName");
        String address = req.getParameter("customerAddress");
        double salary = Double.parseDouble(req.getParameter("customerSalary"));

        PrintWriter writer = resp.getWriter();
        resp.setContentType("application/json");
        resp.addHeader("Access-Control-Allow-Origin", "*");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/JavaEEPOS", "root", "1234");

            PreparedStatement pstm = connection.prepareStatement("INSERT INTO Customer Values (?,?,?,?)");
            pstm.setObject(1, id);
            pstm.setObject(2, name);
            pstm.setObject(3, address);
            pstm.setObject(4, salary);

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
        String id = jsonObject.getString("id");
        String name = jsonObject.getString("name");
        String address = jsonObject.getString("address");
        double salary = Double.parseDouble(jsonObject.getString("salary"));

        PrintWriter writer = resp.getWriter();

        resp.setContentType("application/json");

        resp.addHeader("Access-Control-Allow-Origin", "*");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/JavaEEPOS", "root", "1234");
            PreparedStatement pstm = connection.prepareStatement("UPDATE Customer SET name=?,address=?,salary=? WHERE id=?");
            pstm.setObject(1, name);
            pstm.setObject(2, address);
            pstm.setObject(3, salary);
            pstm.setObject(4, id);

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
        String cusID = req.getParameter("CusID");
        PrintWriter writer = resp.getWriter();
        resp.setContentType("application/json");

        resp.addHeader("Access-Control-Allow-Origin", "*");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/JavaEEPOS", "root", "1234");

            PreparedStatement pstm = connection.prepareStatement("DELETE FROM Customer WHERE id=?");
            pstm.setObject(1, cusID);

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
