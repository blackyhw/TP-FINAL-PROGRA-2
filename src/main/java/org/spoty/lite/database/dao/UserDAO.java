package org.spoty.lite.database.dao;

import org.spoty.lite.model.User;

import java.util.List;

public interface UserDAO {
    void saveUser(User user);
    void updateUser(User user);
    void updateStatus(int userId, int status);
    User getUserById(int id);
    User getUserByUsername(String username);
    User getUserByEmail(String email);
    List<User> getAllUsers();
    List<User> getUsersByStatus(int status);
    List<User> getUsersByRegistrationDate(String registration_date);
    List<User> getUsersByRegistrationMonth(int month, int year);
    List<User> getUsersByRegistrationYear(int year);
}
