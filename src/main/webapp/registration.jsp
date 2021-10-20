<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <title>Registration</title>
</head>
<body>
<p>Sign up</p>

<form action="registration" method="POST">
    Login: <input type="text" name="login"/> ${loginError}
    Password: <input type="password" name="pass"/>${passError}
    Email: <input type="email" name="email"/>${emailError}
    <input type="submit" value="Sign up">
</form>
</body>
</html>