package Database;

import profiles.Profile;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import profiles.UsersDataSet;

public class UsersDAO {

    private final Session session;

    public UsersDAO(Session session) {
        this.session = session;
    }

    public Profile getUserProfile(String login) throws HibernateException {
        Criteria criteria = session.createCriteria(UsersDataSet.class);
        UsersDataSet usersDataSet = (UsersDataSet) criteria
                .add(Restrictions.eq("login", login))
                .uniqueResult();

        if (usersDataSet == null) {
            throw new RuntimeException("User with this login not found");
        }
        return new Profile(usersDataSet.getLogin(),
                usersDataSet.getPass(),
                usersDataSet.getEmail());
    }

    public void insertUser(Profile userProfile) throws HibernateException {
        session.save(new UsersDataSet(userProfile));
    }

    public boolean checkUserExists(String login) {
        try {
            getUserProfile(login);
        } catch (Exception e) {
            if (!e.getMessage().equals("User with this login not found")) {
                throw new RuntimeException(e.getMessage());
            }
            return false;
        }
        return true;
    }
}
