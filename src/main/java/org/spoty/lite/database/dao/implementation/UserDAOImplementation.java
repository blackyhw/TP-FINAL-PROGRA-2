package org.spoty.lite.database.dao.implementation;

import org.spoty.lite.database.DataBaseConnection;
import org.spoty.lite.database.dao.UserDAO;
import org.spoty.lite.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImplementation implements UserDAO {

    private Connection connection;

    public UserDAOImplementation() {
        try {
            connection = DataBaseConnection.getConnection();
        } catch (SQLException e) {
            e.getMessage();
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.getMessage();
                e.printStackTrace();
            }
        }
    }

    @Override
    public void saveUser(User user) {
         String sql = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
         try {
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             preparedStatement.setString(1, user.getUsername());
             preparedStatement.setString(2, user.getPassword());
             preparedStatement.setString(3, user.getEmail());

            int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Se insertó el usuario correctamente!");
                    ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        user.setUser_id(generatedKeys.getInt(1));

                        String query = "SELECT registration_date FROM users WHERE user_id = ?";
                        try (PreparedStatement preparedStatement2 = connection.prepareStatement(query)){
                            preparedStatement2.setInt(1, user.getUser_id());
                            ResultSet resultSet = preparedStatement2.executeQuery();
                            if (resultSet.next()) {
                                user.setRegistration_date(resultSet.getTimestamp("registration_date"));
                            }
                        }
                    }
                }
        } catch (SQLException e) {
            e.getMessage();
            e.printStackTrace();
        }
    }

    @Override
    public void updateUser(User user) {
        String sql = "UPDATE user SET username = ?, password = ?, email = ?, status = ? WHERE user_id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setInt(4, user.getStatus());
            preparedStatement.setInt(5, user.getUser_id());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Usuario actualizado correctamente!");
            }else{
                System.out.println("No se pudo encontrar un usuario con ese ID.");
            }
        } catch (SQLException e) {
            e.getMessage();
            e.printStackTrace();
        }
    }

    @Override
    public void updateStatus(int userId, int status) {
        String sql = "UPDATE users SET status = ? WHERE user_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setInt(1, status);
            preparedStatement.setInt(2, userId);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Estado de usuario actualizado correctamente!");
            } else {
                System.out.println("No se pudo encontrar un usuario con ese ID.");
            }
        } catch (SQLException e) {
            e.getMessage();
            e.printStackTrace();
        }
    }

    @Override
    public User getUserById(int id) {
        String sql = "SELECT * FROM user WHERE user_id = ?";

        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                User user = new User();
                user.setUser_id(resultSet.getInt("id"));
                user.setUsername(resultSet.getString("username"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                user.setStatus(resultSet.getInt("status"));
                user.setRegistration_date(resultSet.getTimestamp("registration_date"));

                return user;
            }
        } catch (SQLException e) {
            e.getMessage();
            e.printStackTrace();
        }

        System.out.println("No se pudo encontrar un usuario con ese ID.");
        return null;
    }

    @Override
    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM user WHERE username = ?";

        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                User user = new User();
                user.setUser_id(resultSet.getInt("id"));
                user.setUsername(resultSet.getString("username"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                user.setStatus(resultSet.getInt("status"));
                user.setRegistration_date(resultSet.getTimestamp("registration_date"));

                return user;
            }
        } catch (SQLException e) {
            e.getMessage();
            e.printStackTrace();
        }

        System.out.println("No se pudo encontrar un usuario con ese Username.");
        return null;
    }

    @Override
    public User getUserByEmail(String email) {
        String sql = "SELECT * FROM user WHERE email = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                User user = new User();
                user.setUser_id(resultSet.getInt("id"));
                user.setUsername(resultSet.getString("username"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                user.setStatus(resultSet.getInt("status"));
                user.setRegistration_date(resultSet.getTimestamp("registration_date"));

                return user;
            }
        } catch (SQLException e) {
            e.getMessage();
            e.printStackTrace();

        }

        System.out.println("No se pudo encontrar un usuario con ese Username.");
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        String sql = "SELECT * FROM user";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                User user = new User();
                user.setUser_id(resultSet.getInt("id"));
                user.setUsername(resultSet.getString("username"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                user.setStatus(resultSet.getInt("status"));
                user.setRegistration_date(resultSet.getTimestamp("registration_date"));

                users.add(user);
            }
        } catch (SQLException e) {
            e.getMessage();
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public List<User> getUsersByStatus(int status) {
        List<User> users = new ArrayList<>();

        String sql = "SELECT * FROM user WHERE status = ?";

        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setInt(1, status);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                User user = new User();
                user.setUser_id(resultSet.getInt("id"));
                user.setUsername(resultSet.getString("username"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                user.setStatus(resultSet.getInt("status"));
                user.setRegistration_date(resultSet.getTimestamp("registration_date"));

                users.add(user);
            }
        } catch (SQLException e) {
            e.getMessage();
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public List<User> getUsersByRegistrationDate(String registration_date) {
        List<User> users = new ArrayList<>();

        String sql = "SELECT * FROM user WHERE DATE (registration_date)  = ?";

        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1, registration_date);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                User user = new User();
                user.setUser_id(resultSet.getInt("id"));
                user.setUsername(resultSet.getString("username"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                user.setStatus(resultSet.getInt("status"));
                user.setRegistration_date(resultSet.getTimestamp("registration_date"));

                users.add(user);
            }
        } catch (SQLException e) {
            e.getMessage();
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public List<User> getUsersByRegistrationMonth(int month, int year) {
        List<User> users = new ArrayList<>();

        String sql = "SELECT * FROM user WHERE MONTH (registration_date) = ? AND YEAR (registration_date) = ?";

        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setInt(1, month);
            preparedStatement.setInt(2, year);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                User user = new User();
                user.setUser_id(resultSet.getInt("id"));
                user.setUsername(resultSet.getString("username"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                user.setStatus(resultSet.getInt("status"));
                user.setRegistration_date(resultSet.getTimestamp("registration_date"));

                users.add(user);
            }
        } catch (SQLException e) {
            e.getMessage();
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public List<User> getUsersByRegistrationYear(int year) {
        List<User> users = new ArrayList<>();

        String sql = "SELECT * FROM user WHERE YEAR (registration_date) = ?";

        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setInt(1, year);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                User user = new User();
                user.setUser_id(resultSet.getInt("id"));
                user.setUsername(resultSet.getString("username"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                user.setStatus(resultSet.getInt("status"));
                user.setRegistration_date(resultSet.getTimestamp("registration_date"));

                users.add(user);
            }
        } catch (SQLException e) {
            e.getMessage();
            e.printStackTrace();
        }
        return users;
    }

}