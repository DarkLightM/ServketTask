package servlets;

import profiles.AccountService;
import profiles.Profile;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@WebServlet("/")
public class Browse extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Profile userProfile = AccountService.getUserBySessionId(req.getSession().getId());
        String path = req.getParameter("path");
        if (userProfile == null){
            resp.sendRedirect("/login");
        }
        if (path == null || path.equals("")) {
            path = AccountService.getHomeDirectory().toString() + '\\' + userProfile.getLogin();
        }
        if (!path.startsWith(AccountService.getHomeDirectory().toString() + '\\' + userProfile.getLogin())){
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        String parentPath = new File(path).getAbsoluteFile().getParent();
        if (req.getParameter("download") == null) {
            List<File> filesList = Stream.of(new File(path).listFiles())
                    .map(File::getAbsoluteFile)
                    .collect(Collectors.toList());

            Map<File, FileTime> filesDate = new HashMap<File, FileTime>() {};
            for (File item : filesList) {
                filesDate.put(item, getCreationDate(item.getAbsolutePath()));
            }
            req.setAttribute("filesDate", filesDate);
            req.setAttribute("filesList", filesList);
            req.setAttribute("parentPath", parentPath);
            getServletContext().getRequestDispatcher("/index.jsp").forward(req, resp);
        } else {
            resp.sendRedirect("/download");
        }
    }

    private FileTime getCreationDate(String absolutePath) {

        Path file = Paths.get(absolutePath);
        try {
            BasicFileAttributes attr = Files.readAttributes(file, BasicFileAttributes.class);
            return attr.creationTime();
        } catch (IOException e) {
            return FileTime.fromMillis(0);
        }

    }
}
