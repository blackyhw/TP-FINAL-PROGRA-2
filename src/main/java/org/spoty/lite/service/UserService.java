package org.spoty.lite.service;

import org.spoty.lite.database.dao.UserDAO;
import org.spoty.lite.model.User;

import java.time.LocalDate;
import java.util.List;

public class UserService {
    private UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public User loginAutentication(String email, String password) {
        User user = userDAO.getUserByEmail(email);
        if (user != null) {
            if (user.getStatus() == 0) {
                System.out.println("Acceso denegado: el usuario está inactivo.");
                return null;
            }
            if (user.getPassword().equals(password)) {
                System.out.println("Login exitoso.");
                return user;
            } else {
                System.out.println("Contraseña incorrecta.");
                return null;
            }
        } else {
            System.out.println("Usuario no encontrado.");
            return null;
        }
    }

    public void saveUser(User user) {
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de usuario no puede ser nulo o vacío");
        }
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("El correo electrónico no puede ser nulo o vacío");
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede ser nula o vacía");
        }
        if (userDAO.getUserByEmail(user.getEmail()) != null) {
            throw new IllegalArgumentException("El correo electrónico ya está registrado.");
        }
        if (userDAO.getUserByUsername(user.getUsername()) != null) {
            throw new IllegalArgumentException("El nombre de usuario ya está registrado.");
        }
        userDAO.saveUser(user);
    }

    public void updateUsername(User user, String username) {
        if(user == null){
            throw new IllegalArgumentException("El usuario no puede ser nulo.");
        }
        if(username == null || username.trim().isEmpty()){
            throw new IllegalArgumentException("El nombre de usuario no puede ser nulo o vacío.");
        }
        userDAO.updateUsername(user, username);
    }

    public void updatePassword(User user, String password) {
        if(user == null){
            throw new IllegalArgumentException("El usuario no puede ser nulo.");
        }
        if(password == null || password.trim().isEmpty()){
            throw new IllegalArgumentException("La contraseña no puede ser nula o vacía.");
        }
        userDAO.updatePassword(user, password);
    }

    public void updateEmail(User user, String email) {
        if(user == null){
            throw new IllegalArgumentException("El usuario no puede ser nulo.");
        }
        if(email == null || email.trim().isEmpty()){
            throw new IllegalArgumentException("El correo electrónico no puede ser nulo o vacío.");
        }
        userDAO.updateEmail(user, email);
    }

    public void userStatus(User user) {
    if (user == null) {
        throw new IllegalArgumentException("El usuario no puede ser nulo");
    }
    if (user.getStatus() < 0 || user.getStatus() > 1) {
        throw new IllegalArgumentException("Estatus de usuario no válido");
    }
    userDAO.userStatus(user.getUser_id(), user.getStatus());
    }

    public User getUserById(int id){
        if(id <= 0){
            throw new IllegalArgumentException("El id no puede ser negativo.");
        }
        User user = userDAO.getUserById(id);
        if(user == null){
            System.out.println("Usuario no encontrado.");
        }
        return user;
    }

    public List<User> getAllUsers(){
        List<User> users = userDAO.getAllUsers();
        if(users.isEmpty()){
            System.out.println("No hay usuarios registrados.");
        }
        return users;
    }

    public List<User> getUsersByStatus(int status){
        if (status < 0 || status > 1) {
            throw new IllegalArgumentException("El estado del usuario debe ser 0 o 1.");
        }
        List<User> users = userDAO.getUsersByStatus(status);
        if(users.isEmpty()){
            System.out.println("No hay usuarios con el estatus introducido");
        }
        return users;
    }

    public List<User> getUsersByRegistrationDate(String registration_date){
        if(registration_date == null || registration_date.trim().isEmpty()){
            throw new IllegalArgumentException("La fecha de registro no puede ser nula o vacía.");
        }
        List<User> users = userDAO.getUsersByRegistrationDate(registration_date);
        if(users.isEmpty()){
            System.out.println("No hay usuarios registrados en la fecha introducida.");
        }
        return users;
    }

    public List<User> getUsersByRegistrationMonth(int month, int year){
        if(month < 1 || month > 12){
            throw new IllegalArgumentException("El mes debe estar entre 1 y 12.");
        }
        if(year < 1900 || year > LocalDate.now().getYear()){
            throw new IllegalArgumentException("El año debe estar entre 1900 y "+LocalDate.now().getYear()+".");
        }
        List<User> users = userDAO.getUsersByRegistrationMonth(month, year);
        if(users.isEmpty()){
            System.out.println("No hay usuarios registrados en el mes y año introducidos.");
        }
        return users;
    }

    public List<User> getUsersByRegistrationYear(int year){
        if(year < 1900 || year > LocalDate.now().getYear()){
            throw new IllegalArgumentException("El año debe estar entre 1900 y "+LocalDate.now().getYear()+".");
        }
        List<User> users = userDAO.getUsersByRegistrationYear(year);
        if(users.isEmpty()){
            System.out.println("No hay usuarios registrados en el año introducido.");
        }
        return users;
    }

}
