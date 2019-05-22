package Controllers;

import Server.Main;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Path("sandwich/")
public class Sandwich {

    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
    public String listSandwiches() {

        System.out.println("sandwich/list");

        JSONArray list = new JSONArray();

        try {

            PreparedStatement ps = Main.db.prepareStatement("SELECT Id, Name, Layers FROM Sandwiches");

            ResultSet results = ps.executeQuery();
            while (results.next()) {
                JSONObject item = new JSONObject();
                item.put("id", results.getInt(1));
                item.put("name", results.getString(2));
                item.put("layers", results.getInt(3));
                list.add(item);
            }

            return list.toString();

        } catch (SQLException sqlEx) {
            System.out.println("Database error: " + sqlEx.getMessage());
            return "{'error': 'Unable to list items, please see server console for more info.'}";
        }

    }

    @POST
    @Path("new")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String insertSandwich(@FormDataParam("id") int id, @FormDataParam("name") String name, @FormDataParam("layers") int layers) {

        System.out.println("sandwich/new");

        try {

            PreparedStatement ps = Main.db.prepareStatement(
                    "INSERT INTO Sandwiches (Id, Name, Layers) VALUES (?, ?, ?)");

            ps.setInt(1, id);
            ps.setString(2, name);
            ps.setInt(3, layers);

            ps.execute();

            return "{'status': 'OK'}";

        } catch (SQLException sqlEx) {
            System.out.println("Database error: " + sqlEx.getMessage());
            return "{'error': 'Unable to create new item, please see server console for more info.'}";
        }
    }

    @POST
    @Path("update")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String updateSandwich(@FormDataParam("id") int id, @FormDataParam("name") String name, @FormDataParam("layers") int layers) {

        System.out.println("sandwich/update");

        try {

            PreparedStatement ps = Main.db.prepareStatement(
                    "UPDATE Sandwiches SET Name = ?, Layers = ? WHERE Id = ?");

            ps.setString(1, name);
            ps.setInt(2, layers);
            ps.setInt(3, id);

            ps.execute();

            return "{'status': 'OK'}";

        } catch (SQLException sqlEx) {
            System.out.println("Database error: " + sqlEx.getMessage());
            return "{'error': 'Unable to update item, please see server console for more info.'}";
        }
    }

    @POST
    @Path("delete")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteSandwich(@FormDataParam("id") int id) {

        System.out.println("sandwich/delete");

        try {

            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Sandwiches WHERE Id = ?");

            ps.setInt(1, id);

            ps.execute();

            return "{'status': 'OK'}";

        } catch (SQLException sqlEx) {
            System.out.println("Database error: " + sqlEx.getMessage());
            return "{'error': 'Unable to delete item, please see server console for more info.'}";
        }
    }

}
