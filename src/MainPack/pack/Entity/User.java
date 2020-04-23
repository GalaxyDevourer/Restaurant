package MainPack.pack.Entity;

public class User {
    private int userID;
    private String login;
    private String password;
    private String role;

    public User() {

    }
    public User (String login, String password) {
        this.login = login;
        this.password = password;
    }
    public User (String login, String password, String role) {
        this(login, password);
        this.role = role;
    }
    public User (int userID, String login, String password, String role) {
        this(login, password, role);
        this.userID = userID;
    }

    public int getId() {
        return userID;
    }
    public void setId(int userID) {
        this.userID = userID;
    }

    public String getLogin() {
        return login;
    }
    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        User object = (User) obj;
        return login.equals(object.login);
    }
}