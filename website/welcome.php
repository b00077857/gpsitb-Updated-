<?php
session_start();
session_destroy();
?>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Welcome to Tracking</title>
    <link rel="stylesheet" type="text/css" href="style.css"
</head>
<body>
    <div id="frm">
        <h1>Login Below</h1>
        <form action="login.php" method="POST">
            Username :  <input type="text" name="user" />
            <br>
            Password:  <input type="password"  name="pass" />
            <br>
            <input type="submit" id="btn" name="submit" value="Submit" />
        </form>
    </div>
</body>
</html>
