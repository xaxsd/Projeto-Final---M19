package model;

public class Player {
    private int id;
    private String username;
    private String password;
    private String email;

    public Player(int id, String username, String password, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public Player(String username, String password, String email) {
        this(0, username, password, email);
    }

    // 🟢 ГЕТТЕРИ:
    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }
}
