package Database;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import profiles.Profile;

public class DatabaseService {
    private final Connection connection;

    public DatabaseService() {
        this.connection = getMysqlConnection();
        System.out.println("Соединение с СУБД выполнено.");
    }

    public Profile getUser(String login) throws DatabaseException {
        try {
            return (new UsersDAO(connection).getUser(login));
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    public boolean checkUserExists(String login) throws DatabaseException {
        try {
            return (new UsersDAO(connection).checkUserExists(login));
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    public void addUser(Profile usersDataSet) throws DatabaseException {
        try {
            connection.setAutoCommit(false);
            UsersDAO dao = new UsersDAO(connection);
            dao.createTable();
            dao.insertUser(usersDataSet.getLogin(), usersDataSet.getPassword(), usersDataSet.getEmail());
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ignore) {
            }
            throw new DatabaseException(e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ignore) {
            }
        }
    }

    public static Connection getMysqlConnection() {
        try {
            DriverManager.registerDriver((Driver) Class.forName("com.mysql.cj.jdbc.Driver").newInstance());

            StringBuilder url = new StringBuilder();

            url.
                    append("jdbc:mysql://").        //db type
                    append("localhost:").           //host name
                    append("3306/").                //port
                    append("ulearn?").          //db name
                    append("user=root&").          //login
                    append("password=admin&").   //password
                    append("serverTimezone=UTC");

            System.out.println("URL: " + url + "\n");

            return DriverManager.getConnection(url.toString());
        } catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
