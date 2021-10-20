<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.util.List" %>
<%@ page import="java.io.File" %>
<%@ page import="java.nio.file.attribute.FileTime" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.time.ZoneId" %>
<%@ page import="java.io.FileInputStream" %>
<%@ page import="java.io.OutputStream" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>


<% List<File> filesList = (List) request.getAttribute("filesList"); %>
<% Map<File, FileTime> filesDate = (Map) request.getAttribute("filesDate"); %>

<!doctype html>
<html>
<head>
    <title>First JSP</title>
</head>
<body>
<br>
<%= LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) %>
<h1><%= request.getParameter("path") %></h1>
<h2> <a href='?path=<%= request.getAttribute("parentPath") %>'> back </a> </h2>

<table>
    <tr>
        <th>Type</th>
        <th>Name</th>
        <th>Size</th>
        <th>Date</th>
    </tr>
    <% for (File item : filesList) { %>
    <tr>
        <td>
            <%if (!item.isDirectory()) { %>
            <a href="?path=<%=item.getAbsolutePath() %>&download=true" download="<%= item.getAbsolutePath()%>"> <span>📄</span> </a>

            <% } %>
            <%if (item.isDirectory()) { %>
            <a><span>📁</span> </a>

            <% } %>
        </a>
        </td>
        <td>
            <a href="?path=<%=item.getAbsolutePath() %>"> <%= item.getName() %>
            </a>
        </td>
        <td ><%= item.length() %>
        </td>
        <td><%= filesDate.get(item).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().
                format(DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss")) %>
        </td>
    </tr>
    <% } %>
</table>
<form method="POST" action="logout">
    <div>
        <input type="submit" value="Logout">
    </div>
</form>
</body>
</html>