<!DOCTYPE html>
<html>
<head>
    <#include "../fragments/head.ftl" parse="false">
    <title>Login</title>
</head>
<body>
<h1>Login</h1>
    <#if returnLocation??>
    <form action="login?returnLocation=${returnLocation}" method="post">
    <#else>
    <form action="login" method="post">
    </#if>
    <table>
        <tr>
            <th>Username</th>
            <#if username?has_content>
            <td><input type="text" name="Username" value="${username?html}"></td>
            <#else>
            <td><input type="text" name="Username"></td>
            </#if>
        </tr>
    <tr>
        <th>Password</th>
        <td><input type="password" name="Password"></td>
    </tr>
    </table>
    <input type="submit" value="Login">
    </form>
</body>
<p><a href="${forgottenPasswordLocation}">I've forgotten my password</a></p>
<#if returnLocation??><p><a href="${returnLocation}">Cancel</a></p></#if>
</html>