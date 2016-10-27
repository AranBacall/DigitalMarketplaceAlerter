<!DOCTYPE html>
<html>
<head>
    <#include "fragments/head.ftl" parse="false">
    <title>Users</title>
</head>
<body>
<h1>Users</h1>
<table>
    <tr>
        <th>ID</th>
        <th>Username</th>
        <th>Admin</th>
        <th>Promote to Administrator</th>
        <th>Disable user</th>
        <th>Delete user</th>
    </tr>
    <#list users as user>
    <tr>
        <td>${user.id}</a></td>
        <td>${user.username}</td>
        <td>${user.admin?string('yes', 'no')}</td>
        <td>
            <#if user.admin == true || user.disabled == true>
                <button type="button" disabled>Promote</button>
            <#else>
                 <form action="${user.id}/admin" method="post"><input type="submit" value="Promote"></form>
            </#if>
        </td>
        <td>
            <#if user.admin == true>
                <button type="button" disabled>Disable</button>
            <#elseif user.disabled == true>
                <form action="${user.id}/disable/false" method="post"><input type="submit" value="Enable"></form>
            <#elseif user.disabled == false>
                <form action="${user.id}/disable/true" method="post"><input type="submit" value="Disable"></form>
            </#if>
        </td>
        <td>
            <#if user.admin == true>
                <button type="button" disabled>Delete</button>
            <#else>
                 <form action="${user.id}/delete" method="post"><input type="submit" value="Delete"></form>
            </#if>
        </td>
    </tr>
    </#list>
</table>
<p><a href="../">Home</a></p>
</body>
</html>