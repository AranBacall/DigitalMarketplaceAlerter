<!DOCTYPE html>
<html>
<head>
    <#include "../fragments/head.ftl" parse="false">
    <title>Forgotten Password</title>
</head>
<body>
<h1>Change Password</h1>
<p>To change your password, please enter your email below, a link will be sent to you at this address
that will allow you to set a new password.</p>
<form method="post">
<input type="text" name="email">
<input type="submit" value="Send">
</form>
</body>
</html>