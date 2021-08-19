package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
//Здесь используем Try with resources для autoclosable всех потоков и отлавливаем возможные исключения
// получаемые в результате ошибки в SQL запросе либо при отсутствии Util класса


    public UserDaoJDBCImpl() {}

    @Override
    public void createUsersTable() {
        try (Connection connection = Util.getMySQLConnection(); Statement statement = connection.createStatement()) {
            statement.execute("USE jdbc");
            statement.execute("CREATE TABLE IF NOT EXISTS users " +
                    "(id int PRIMARY KEY AUTO_INCREMENT UNIQUE, " +
                    "name VARCHAR(20) NOT NULL ," +
                    " lastName VARCHAR(20) NOT NULL ," +
                    " age int(3) NOT NULL)");
            System.out.println("Таблица созданна");
        } catch (SQLException e) {
            System.out.println("Таблица не создана");
        } catch (ClassNotFoundException e) {
            System.out.println("Потеряли Util.getMySQLCon");
        }
    }

    @Override
    public void dropUsersTable() {
        try (Connection conn = Util.getMySQLConnection(); Statement st = conn.createStatement()) {
            st.execute("DROP TABLE users");
            System.out.println("Таблица удаленна");
            conn.commit();
        } catch (SQLException e) {
            System.out.println("Таблица не удалена");
        } catch (ClassNotFoundException e) {
            System.out.println("Потеряли Util.getMySQLCon");
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Connection conn = Util.getMySQLConnection();
             PreparedStatement ps = conn
                     .prepareStatement("INSERT INTO users (name, lastName, age) Values (?, ?, ?)")) {
            ps.setString(1, name);
            ps.setString(2, lastName);
            ps.setByte(3, age);
            ps.executeUpdate();
            System.out.println("User с именем – " + name + " добавлен в базу данных");
            conn.commit();
        } catch (SQLException e) {
            System.out.println("Юзер не добавлен");
        } catch (ClassNotFoundException e) {
            System.out.println("Потеряли Util.getMySQLCon");
        }
    }

    @Override
    public void removeUserById(long id) {
        try (Connection conn = Util.getMySQLConnection();
             PreparedStatement preparedStatement =
                     conn.prepareStatement("DELETE FROM users WHERE id = ?;")) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
            conn.commit();
            System.out.println("User с id - " + id + " удален из таблицы");
        } catch (SQLException e) {
            System.out.println("Юзер не удален");
        } catch (ClassNotFoundException e) {
            System.out.println("Потеряли Util.getMySQLCon");
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> allUsers = new ArrayList<>();
        try (Connection conn = Util.getMySQLConnection(); PreparedStatement ps = conn.
                prepareStatement("SELECT * FROM users");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getLong("id"));
                user.setName(rs.getString("name"));
                user.setLastName(rs.getString("lastName"));
                user.setAge(rs.getByte("age"));
                allUsers.add(user);
            }
            conn.commit();
        } catch (SQLException e) {
            System.out.println("Не выполнился sql запрос на вывод юзеров из таблицы");
        } catch (ClassNotFoundException e) {
            System.out.println("Потеряли Util.getMySQLCon");
        }
        return allUsers;
    }

    @Override
    public void cleanUsersTable() {
        try (Connection conn = Util.getMySQLConnection(); Statement st = conn.createStatement()) {
            st.execute("DELETE FROM users");
            System.out.println("Таблица очищена");
            conn.commit();
        } catch (SQLException e) {
            System.out.println("Таблица не очищена");
        } catch (ClassNotFoundException e) {
            System.out.println("Потеряли Util.getMySQLCon");
        }
    }
}
