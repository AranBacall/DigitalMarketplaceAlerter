<!DOCTYPE html>
<html>
<head>
    <#include "../fragments/head.ftl" parse="false">
    <title>List of Bid Managers</title>
</head>
<body>
<h1>List of Bid Managers</h1>
<table>
    <tr>
        <th>Email</th>
        <th>Delete?</th>
    </tr>
    <#list bidManagers as manager>
    <tr>
        <td>${manager.email}</td>
        <td>
            <form action="${manager.id}/delete" method="post"><input type="submit" value="Delete"></form>
        </td>
    </tr>
    </#list>
</table>

<br />
<hr />
<br />

<h2>Add a new Bid Manager</h2>
<form action="create" method="post">
<table>
    <tr>
        <th>Email</th>
        <td>
          <input type="text" name="email" value="email">
          <input type="submit" value="Create">
        </td>
    </tr>
    <tr>
        <th></th>
    </tr>
</table>
</form>
<p><a href="../">Home</a></p>
</body>
</html>