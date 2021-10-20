package servlets;

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
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");
        String pass = req.getParameter("pass");
        String email = req.getParameter("email");
        if (    login == null || login.equals("") ||
                pass  == null || pass.equals("") ||
                email == null || email.equals("")){
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.sendRedirect("/registration.jsp");
            return;
        }
        Path userDirectoryPath = Paths.get(AccountService.getHomeDirectory().toString() + '\\' + login);
        if (Files.exists(userDirectoryPath)){
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        Files.createDirectory(userDirectoryPath);
        Profile profile = new Profile(login, pass, email);
        AccountService.addNewUser(profile);
        AccountService.addSession(req.getSession().getId(), profile);
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.sendRedirect("/");
    }
}
