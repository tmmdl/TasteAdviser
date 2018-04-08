package dbworks;

import org.json.JSONArray;
import org.json.JSONObject;
import telegram.Interaction;

import java.sql.*;
import java.util.ArrayList;

public class DataBase {

    public static void update(String type, Object content) {

        String query = "insert into tastebase (content_type, additional_content) values (?, ?)";
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(
                    "jdbc:postgresql://127.0.0.1:5432/postgres", "postgres","postgres");
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, type);
            ps.setObject(2, content);
            ps.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println(e);
        }
        finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static String get(String type, String key, String value) {

        StringBuilder sb = new StringBuilder();
        ArrayList<String> data = new ArrayList<String>();
        String query = "select * from tastebase where content_type like '%" + type + "%' and additional_content ->> '" + key + "' = '" + value + "'";
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/postgres", "postgres","postgres");
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {

                JSONObject jsonObject = new JSONObject(rs.getString(3));
                if (type.equals("music")) {
                    sb.append(jsonObject.get("artist") + " - " + jsonObject.get("title") + "\n");
                    Interaction.add(jsonObject.get("artist") + " - " + jsonObject.get("title") + "\n");
                }
                if (type.equals("film")) {
                    sb.append(jsonObject.get("title") + "\nDescription: " + jsonObject.get("description") + "\n");
                    Interaction.add(jsonObject.get("title") + "\nDescription: " + jsonObject.get("description") + "\n");
                }
                if (type.equals("literature")) {
                    sb.append(jsonObject.get("title") + " by " + jsonObject.get("author") + "\nDescription: " + jsonObject.get("description") + "\n");
                    Interaction.add(jsonObject.get("title") + " by " + jsonObject.get("author") + "\nDescription: " + jsonObject.get("description") + "\n");
                }
            }
        }
        catch (SQLException e) {
            System.out.println(e);
        }
        return sb.toString();
    }
}