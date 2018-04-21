/*
Van Braeckel Simon
 */

package database.interfaces.implementations.DataAccessObjects;

import database.DataTransferObjects.TeacherDTO;
import database.interfaces.TeacherDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLiteTeacherDAO implements TeacherDAO{
    private Connection conn;
    private String tablename = "teacher";

    public SQLiteTeacherDAO(Connection conn){
        this.conn = conn;
    }

    public TeacherDTO getTeacherByID(int teacher_id){
        try (PreparedStatement statement = conn.prepareStatement("select * from " + tablename + " where id = ? order by name")){
            statement.setInt(1, teacher_id);
            ResultSet resultSet = statement.executeQuery();
            List<TeacherDTO> teachers = verwerkResultaat(resultSet);
            if (teachers.size() != 0) {
                return teachers.get(0);
            } else {
                return null;
            }
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public List<TeacherDTO> getTeachersByName(String name){
        try (PreparedStatement statement = conn.prepareStatement("select * from " + tablename + " where name = ?")){
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            return verwerkResultaat(resultSet);
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public List<TeacherDTO> getTeachers(){
        try (PreparedStatement statement = conn.prepareStatement("select * from " + tablename + " order by name COLLATE NOCASE")){
            ResultSet resultSet = statement.executeQuery();
            return verwerkResultaat(resultSet);
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public List<TeacherDTO> verwerkResultaat(ResultSet resultSet){
        List<TeacherDTO> result = new ArrayList<>();
        try {
            while (resultSet.next()) {
                result.add(new TeacherDTO(resultSet.getInt("id"), resultSet.getString("name")));
            }
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
        return result;
    }

    @Override
    public boolean addEntry(String name){
        //Check of er al iemand is met die naam, zo ja -> error
        if (name.isEmpty() || ! getTeachersByName(name).isEmpty()){
            return false;
        }

        //Voeg nieuwe entry toe aan DB
        try (PreparedStatement statement = conn.prepareStatement(
                "INSERT INTO " + tablename + "(name) VALUES (?)"
        )) {
            statement.setString(1, name);
            statement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("could not add entry");
            return false;
        }
        return true;
    }

    @Override
    public boolean renameEntry(int id, String newName){
        //Voeg nieuwe entry toe aan DB
        try (PreparedStatement statement = conn.prepareStatement(
                "UPDATE " + tablename + " SET name = ? WHERE id = ?")) {
            statement.setString(1, newName);
            statement.setInt(2, id);
            statement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Failed to rename entry");
            return false;
        }
        return true;
    }
}