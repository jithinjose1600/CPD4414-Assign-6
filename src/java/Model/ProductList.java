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
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

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

    @POST
    @Consumes("application/json")
    public Response doPost(JsonObject obj) {
     p=new Products(obj);
     return doUpdate("INSERT INTO PRODUCTS (Name, Description, Quantity) VALUES(?, ?, ?)");
    }

    @PUT
    @Path("{id}")
    @Consumes({"application/json"})
    public Response doPut(@PathParam("id") String id, JsonObject obj) {
        p=new Products(obj);
        return doUpdate("UPDATE PRODUCTS SET Name=?, Description=?, Quantity=? WHERE ProductID=?", id);
    }

    @DELETE
    @Path("{id}")
    public Response doDelete(@PathParam("id") String id) {
        String query;
        int res = 0;
        try (Connection conn = DBConnection.getConnection()) {
            query = "DELETE FROM PRODUCTS WHERE ProductID=?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, id);
            res = pstmt.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(ProductList.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (res <= 0) {
            return Response.status(500).build();
        } else {
            return Response.ok("Deleted Successfully").build();
        }
    }
    
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
    private Response doUpdate(String query, String... params)
    {
        int res = 0;
        int pid = 0;
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement pstmt;
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, p.getName());
            pstmt.setString(2, p.getDescription());
            pstmt.setString(3, String.valueOf(p.getQuantity()));
            for (int i = 1; i <= params.length; i++) {
                pstmt.setString(4, params[i - 1]);
            }
            res = pstmt.executeUpdate();
            String qry = "SELECT ProductId FROM PRODUCTS WHERE  Name= ? and Description= ?";
            pstmt = conn.prepareStatement(qry);
            pstmt.setString(1, p.getName());
            pstmt.setString(2, p.getDescription());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                pid = rs.getInt("ProductId");
            }

        } catch (SQLException ex) {
            Logger.getLogger(ProductList.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (res <= 0) {
            return Response.status(500).build();
        } else {
            return Response.ok("http://localhost:8080/CPD4414-Assign4/webresources/servlet/" + pid).build();
        }
    }
}
