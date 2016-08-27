package todo.iyzico.com.todoapp.models;

import java.io.Serializable;

/**
 * Created by emrealkan on 27/08/16.
 */
public class User implements Serializable {

    private enum Role {
        USER, ADMIN
    }
    private static User user;

    public static User getInstance() {
        if(user == null) {
            user = new User();
        }
        return user;
    }

    public static void setUser(User user) {
        user = user;
    }

    private long id;
    private String email;
    private String userName;
    private String password;
    private String name;
    private String role;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public static User getUser() {
        return user;
    }
}
