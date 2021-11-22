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
import java.nio.file.Paths;

@WebServlet(urlPatterns = "/login")
public class LogIn extends HttpServlet {
    private AccountService accountService = new AccountService();
    private DatabaseService dbService = new DatabaseService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");
        String password = req.getParameter("pass");
        clearErrors(req);


        boolean errorStatus = false;
        try {
            errorStatus = checkErrors(req, login, password);
        } catch (DatabaseException e) {
            e.printStackTrace();
        }

        if (errorStatus) {
            req.setAttribute("login", login);
            req.setAttribute("pass", password);
            req.getRequestDispatcher("login.jsp").forward(req, resp);
        } else {
            Profile profile = null;
            try {
                profile = dbService.getUser(login);
            } catch (DatabaseException e) {
                e.printStackTrace();
            }
            accountService.addSession(req.getSession().getId(), profile);
            resp.sendRedirect("/");
        }
    }
    private boolean checkErrors (HttpServletRequest req, String login, String password) throws
            DatabaseException {

        if (login == null || login.equals("")) {
            req.setAttribute("loginErr", "Поле не заполнено");
        } else if (password == null || password.equals("")) {
            req.setAttribute("passErr", "Поле не заполнено");
        } else if (!dbService.checkUserExists(login)) {
            req.setAttribute("loginErr", "Аккаунта с таким логином не существует");
        } else if (!dbService.getUser(login).getPassword().equals(password)) {
            req.setAttribute("passErr", "Неверный пароль");
        } else return false;
        return true;
    }

    private void clearErrors (HttpServletRequest req){
        req.setAttribute("loginErr", "");
        req.setAttribute("passErr", "");
    }
}
