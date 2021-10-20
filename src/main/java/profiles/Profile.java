package profiles;

public class Profile {
    private final String login;
    private final String password;
    private final String email;

    public Profile(String name, String password, String email){
        this.login = name;
        this.password = password;
        this.email = email;
    }

    public Profile(String name){
        this.login = name;
        this.password = name;
        this.email = name;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }
}
