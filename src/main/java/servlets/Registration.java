package servlets;

import Database.DatabaseException;
import Database.DatabaseService;
import profiles.AccountService;
import profiles.Profile;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@WebServlet(urlPatterns = "/registration")
public class Registration extends HttpServlet {
    private DatabaseService dbService = new DatabaseService();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");
        String password = req.getParameter("pass");
        String email = req.getParameter("email");
        clearErrors(req);
        boolean errorStatus = false;
        try {
            errorStatus = checkErrors(req, login, password, email);
        } catch (DatabaseException e){
            e.printStackTrace();
        }
        if (errorStatus) {
            req.setAttribute("login", login);
            req.setAttribute("pass", password);
            req.setAttribute("email", email);
            req.getRequestDispatcher("registration.jsp").forward(req, resp);
        } else {
            Profile userProfile = new Profile(login, password, email);
            dbService.addUser(userProfile);
            Path userDirectoryPath = Paths.get(AccountService.getHomeDirectory().toString() + '\\' + login);
            Files.createDirectory(userDirectoryPath);
            resp.sendRedirect("/");
        }
    }

    private boolean checkErrors(HttpServletRequest req, String login, String firstPassword,
                                String email) throws DatabaseException {
        if (login == null || login.equals("")) {
            req.setAttribute("loginErr", "Поле не заполнено");
        } else if (firstPassword == null || firstPassword.equals("")) {
            req.setAttribute("passErr", "Поле не заполнено");
        } else if (email == null || email.equals("")) {
            req.setAttribute("emailErr", "Поле не заполнено");
        } else return false;
        return true;
    }

    private void clearErrors(HttpServletRequest req) {
        req.setAttribute("loginErr", "");
        req.setAttribute("passErr", "");
        req.setAttribute("emailErr", "");
    }
}
