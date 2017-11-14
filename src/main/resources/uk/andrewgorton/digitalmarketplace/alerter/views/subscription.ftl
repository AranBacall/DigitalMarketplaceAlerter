<!DOCTYPE html>
<html>
<head>
    <#include "fragments/head.ftl" parse="false">
    <title>Users</title>
</head>
<body>
<h1>Subscribe</h1>
<form action="create" method="post">
<table>
    <tr>
        <th>Email</th>
        <td>
            <input type="text" name="email" value="email">
        </td>
    </tr>
    <tr>
        <th>Department</th>
        <td>
            <select multiple>
                <option value="Home Office">Home Office</option>
                <option value="MoJ">Ministry of Justice</option>
            </select>
        </td>
    </tr>
</table>
<input type="submit" value="Create">
</form>
<p><a href="${loginLocation}">Log in</a></p>
</body>
</html>