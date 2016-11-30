<!DOCTYPE html>
<html>
<head>
    <#include "../fragments/head.ftl" parse="false">
    <title>Reset Password</title>
</head>
<body>
<h1>Reset Password</h1>
<p>Please enter your new password below:</p>
<form method="post">
<b>New password </b><input type="text" name="password">
<br>
<b>Repeat new password </b><input type="text" name="repeatPassword">
<input type="submit" value="Submit">
</form>
</body>
</html>