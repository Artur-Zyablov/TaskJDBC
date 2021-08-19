
package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    private Connection connection;
    private Statement statement;
    private PreparedStatement preparedStatement;

    public UserDaoJDBCImpl() {
        try {
            connection = Util.getMySQLConnection();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createUsersTable() {
        try {
            connection = Util.getMySQLConnection();
            statement = connection.createStatement();
            statement.execute("USE jdbc");
            statement.execute("CREATE TABLE IF NOT EXISTS users " +
                    "(id int PRIMARY KEY AUTO_INCREMENT UNIQUE, " +
                    "name VARCHAR(20) NOT NULL ," +
                    " lastName VARCHAR(20) NOT NULL ," +
                    " age int(3) NOT NULL)");
            if(connection != null){
                try{
                    connection.commit();
                }catch (Exception e){
                    connection.rollback();
                    System.out.println(e.getMessage());
                }
            }
            System.out.println("Таблица созданна");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dropUsersTable() {
        try {
            connection = Util.getMySQLConnection();
            connection.setAutoCommit(false);
            statement = connection.createStatement();
            statement.execute("DROP TABLE users");
            System.out.println("Таблица удаленна");
            if(connection != null){
                try{
                    connection.commit();
                }catch (Exception e){
                    connection.rollback();
                    System.out.println(e.getMessage());
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try {
            String sql = "INSERT INTO users (name, lastName, age) Values (?, ?, ?)";
            connection = Util.getMySQLConnection();
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();
            System.out.println("User с именем – " + name + " добавлен в базу данных");
            if(connection != null){
                try{
                    connection.commit();
                }catch (Exception e){
                    connection.rollback();
                    System.out.println(e.getMessage());
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeUserById(long id) {
        try {
            String sql = "DELETE FROM users WHERE id = ?;";
            connection = Util.getMySQLConnection();
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
            if(connection != null){
                try{
                    connection.commit();
                }catch (Exception e){
                    connection.rollback();
                    System.out.println(e.getMessage());
                }
            }
            System.out.println("User с id - " + id + " удален из таблицы");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> allUsers = new ArrayList<>();
        try {
            String sql = "SELECT * FROM users";
            preparedStatement = connection.prepareStatement(sql);
            connection.setAutoCommit(false);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getLong("id"));
                user.setName(rs.getString("name"));
                user.setLastName(rs.getString("lastName"));
                user.setAge(rs.getByte("age"));
                allUsers.add(user);
            }
            if(connection != null){
                try{
                    connection.commit();
                }catch (Exception e){
                    connection.rollback();
                    System.out.println(e.getMessage());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allUsers;
    }

    @Override
    public void cleanUsersTable() {
        try {
            connection = Util.getMySQLConnection();
            connection.setAutoCommit(false);
            statement = connection.createStatement();
            statement.execute("DELETE FROM users");
            System.out.println("Таблица очищена");
            if(connection != null){
                try{
                    connection.commit();
                }catch (Exception e){
                    connection.rollback();
                    System.out.println(e.getMessage());
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
