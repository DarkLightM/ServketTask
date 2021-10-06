package ulearnTask;

import javax.servlet.ServletContext;
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
public class MainServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getParameter("path") == null ? "../" : req.getParameter("path");
        String parentPath = new File(path).getAbsoluteFile().getParent();

        if (req.getParameter("download") == null) {
            List<File> filesList = Stream.of(new File(path).listFiles())
                    .map(File::getAbsoluteFile)
                    .collect(Collectors.toList());

            Map<File, FileTime> filesDate = new HashMap<File, FileTime>() {
            };
            for (File item : filesList) {
                filesDate.put(item, getCreationDate(item.getAbsolutePath()));
            }

            req.setAttribute("filesDate", filesDate);
            req.setAttribute("filesList", filesList);
            req.setAttribute("parentPath", parentPath);
            getServletContext().getRequestDispatcher("/first-jsp.jsp").forward(req, resp);
        } else {
            String filePath = req.getParameter("path");
            File downloadFile = new File(filePath);
            FileInputStream inStream = new FileInputStream(downloadFile);

            ServletContext context = getServletContext();

            String mimeType = context.getMimeType(filePath);
            if (mimeType == null) {
                mimeType = "application/octet-stream";
            }

            resp.setContentType(mimeType);
            resp.setContentLength((int) downloadFile.length());

            String headerKey = "Content-Disposition";
            String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
            resp.setHeader(headerKey, headerValue);

            OutputStream outStream = resp.getOutputStream();

            byte[] buffer = new byte[4096];
            int bytesRead = -1;

            while ((bytesRead = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
            inStream.close();
            outStream.close();
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
