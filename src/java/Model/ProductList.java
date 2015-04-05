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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 *
 * @author JITHZZ
 */
@Path("/products")
public class ProductList {
    //@Inject
    Products p;
    List<Products> pr=new ArrayList();
    @GET
    @Produces("application/json")
    public String doGet() {
        pr=getResults("SELECT * FROM PRODUCTS");
        JsonArrayBuilder builder = Json.createArrayBuilder();
        for(Products p : pr){
           builder.add(p.toJson()).build();
        }
        JsonArray json=builder.build();
        return json.toString();
    }

    @GET
    @Path("{id}")
    @Produces({"application/json"})
    public String doGet(@PathParam("id") int id) {
       
        getResults("SELECT * FROM PRODUCTS WHERE ProductId = ?", String.valueOf(id));
            
        return p.toJson().toString();
    }
/*
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
    
    private List getResults(String query, String... params)
    {
        p= new Products();
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement pstmt;
            System.out.println(query);
            pstmt = conn.prepareStatement(query);
            for (int i = 1; i <= params.length; i++) {
                pstmt.setString(i, params[i - 1]);
            }
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                //System.out.println(rs.getInt("ProductID"));
                p.setProductId(rs.getInt("ProductID"));
                p.setName(rs.getString("Name"));
                p.setDescription(rs.getString("Description"));
                p.setQuantity(rs.getInt("Quantity"));
                pr.add(p);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductList.class.getName()).log(Level.SEVERE, null, ex);
        }
        return pr;
    }
}
