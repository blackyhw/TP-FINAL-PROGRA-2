package org.spoty.lite.service;

import org.spoty.lite.database.dao.UserDAO;
import org.spoty.lite.model.User;

import java.util.List;

public class UserService {
    private UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public boolean loginAutentication(String email, String password) {
        User user = userDAO.getUserByEmail(email);
        if (user != null) {
            if (user.getStatus() == 0) {
                System.out.println("Acceso denegado: el usuario está inactivo.");
                return false;
            }
            if (user.getPassword().equals(password)) {
                System.out.println("Login exitoso.");
                return true;
            } else {
                System.out.println("Contraseña incorrecta.");
                return false;
            }
        } else {
            System.out.println("Usuario no encontrado.");
            return false;
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

    public void updateUser(User user) {
        if (user.getUser_id() <= 0) {
            throw new IllegalArgumentException("ID del usuario no válido");
        }
        User aux = userDAO.getUserById(user.getUser_id());
        if (aux == null) {
            throw new IllegalArgumentException("El usuario con ID " + user.getUser_id() + " no existe");
        }

        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de usuario no puede ser nulo o vacío");
        }
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("El correo electrónico no puede ser nulo o vacío");
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede ser nula o vacía");
        }
        userDAO.updateUser(user);
    }

    public void updateStatus(User user) {
    if (user == null) {
        throw new IllegalArgumentException("El usuario no puede ser nulo");
    }
    if (user.getStatus() < 0 || user.getStatus() > 1) {
        throw new IllegalArgumentException("Estatus de usuario no válido");
    }
    userDAO.updateStatus(user.getUser_id(), user.getStatus());
    }

    public List<User> getAllUsers(){
        List<User> users = userDAO.getAllUsers();
        if(users.isEmpty()){
            System.out.println("No hay usuarios registrados.");
        }
        return users;
    }

    public List<User> getUsersByStatus(int status){
        List<User> users = userDAO.getUsersByStatus(status);
        if(users.isEmpty()){
            System.out.println("No hay usuarios con el estatus introducido");
        }
        return users;
    }

    public List<User> getUsersByRegistrationDate(String registration_date){
        List<User> users = userDAO.getUsersByRegistrationDate(registration_date);
        if(users.isEmpty()){
            System.out.println("No hay usuarios registrados en la fecha introducida.");
        }
        return users;
    }

    public List<User> getUsersByRegistrationMonth(int month, int year){
        List<User> users = userDAO.getUsersByRegistrationMonth(month, year);
        if(users.isEmpty()){
            System.out.println("No hay usuarios registrados en el mes y año introducidos.");
        }
        return users;
    }

    public List<User> getUsersByRegistrationYear(int year){
        List<User> users = userDAO.getUsersByRegistrationYear(year);
        if(users.isEmpty()){
            System.out.println("No hay usuarios registrados en el año introducido.");
        }
        return users;
    }


}
