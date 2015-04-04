/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import entity.Products;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 *
 * @author JITHZZ
 */
@Path("/products")
public class ProductList {
    Products p;
    @GET
    @Produces("application/json")
    public String doGet() {
        p=getResults("SELECT * FROM PRODUCTS");
        return p.toJson().toString();
    }

    /*@GET
    @Path("{id}")
    @Produces({"application/json"})
    public String doGet(@PathParam("id") int id) {
        StringWriter out = new StringWriter();
        JsonGeneratorFactory factory = Json.createGeneratorFactory(null);
        JsonGenerator gen = factory.createGenerator(out);
        try (Connection conn = DBClass.getConnection()) {
            PreparedStatement pstmt;
            String query = "SELECT * FROM PRODUCTS WHERE ProductId = ?";
            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                gen.writeStartObject()
                        .write("productId", rs.getInt("ProductID"))
                        .write("name", rs.getString("Name"))
                        .write("description", rs.getString("Description"))
                        .write("quantity", rs.getInt("Quantity"))
                        .writeEnd();
                gen.close();
            } else {
                return "Invalid Id..";
            }
        } catch (SQLException ex) {
            Logger.getLogger(ControllerServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        return out.toString();
    }

    @POST
    @Consumes("application/json")
    public Response doPost(JsonObject obj) {
        String name = obj.getString("name");
        String description = obj.getString("description");
        String quantity = obj.getString("quantity");
        int res = 0;
        int pid = 0;
        try (Connection conn = DBClass.getConnection()) {
            PreparedStatement pstmt;
            String query = "INSERT INTO PRODUCTS (Name, Description, Quantity) VALUES (?, ?, ?)";
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, name);
            pstmt.setString(2, description);
            pstmt.setString(3, quantity);
            res = pstmt.executeUpdate();
            String qry = "SELECT ProductId FROM PRODUCTS WHERE  Name= ? and Description= ?";
            pstmt = conn.prepareStatement(qry);
            pstmt.setString(1, name);
            pstmt.setString(2, description);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                pid = rs.getInt("ProductId");
            }

        } catch (SQLException ex) {
            Logger.getLogger(ControllerServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (res <= 0) {
            return Response.status(500).build();
        } else {
            return Response.ok("http://localhost:8080/CPD4414-Assign4/webresources/servlet/" + pid).build();
        }

    }

    @PUT
    @Path("{id}")
    @Consumes({"application/json"})
    public Response doPut(@PathParam("id") String id, JsonObject obj) {

        String name = obj.getString("name");
        String description = obj.getString("description");
        String quantity = obj.getString("quantity");
        String query;
        int res = 0;
        try (Connection conn = DBClass.getConnection()) {
            query = "UPDATE PRODUCTS SET Name=?, Description=?, Quantity=? WHERE ProductID=?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, name);
            pstmt.setString(2, description);
            pstmt.setString(3, quantity);
            pstmt.setString(4, id);
            res = pstmt.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(ControllerServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (res <= 0) {
            return Response.status(500).build();
        } else {
            return Response.ok("http://localhost:8080/CPD4414-Assign4/webresources/servlet/" + id).build();
        }
    }

    @DELETE
    @Path("{id}")
    public Response doDelete(@PathParam("id") String id) {
        String query;
        int res = 0;
        try (Connection conn = DBClass.getConnection()) {
            query = "DELETE FROM PRODUCTS WHERE ProductID=?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, id);
            res = pstmt.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(ControllerServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (res <= 0) {
            return Response.status(500).build();
        } else {
            return Response.ok("Deleted Successfully").build();
        }
    }*/
    
    private Products getResults(String query, String... params)
    {
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement pstmt;
            System.out.println(query);
            pstmt = conn.prepareStatement(query);
            for (int i = 1; i <= params.length; i++) {
                pstmt.setString(i, params[i - 1]);
            }
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                System.out.println(rs.getInt("ProductID"));
                p.setProductId(rs.getInt("ProductID"));
                p.setName(rs.getString("Name"));
                p.setDescription(rs.getString("Description"));
                p.setQuantity(rs.getInt("Quantity"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductList.class.getName()).log(Level.SEVERE, null, ex);
        }
        return p;
    }
}
