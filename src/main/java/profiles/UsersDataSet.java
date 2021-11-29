package profiles;

import javax.persistence.*;

@Entity
@Table(name="users_hibernate")
public class UsersDataSet {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name="login")
    private String login;
    @Column(name = "pass")
    private String pass;
    @Column(name = "email")
    private String email;

    public UsersDataSet() {

    }
    public UsersDataSet(Profile userProfile) {
        this.id = 0;
        this.login = userProfile.getLogin();
        this.pass = userProfile.getPassword();
        this.email = userProfile.getEmail();
    }

    public String getLogin() {
        return login;
    }

    public String getPass() {
        return pass;
    }

    public String getEmail() {
        return email;
    }
}
