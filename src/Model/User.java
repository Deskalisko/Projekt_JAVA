package Model;

class User {
    private int id;
    private String login;
    private String type;

    public User(int id, String login, String type) {
        this.id = id;
        this.login = login;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}