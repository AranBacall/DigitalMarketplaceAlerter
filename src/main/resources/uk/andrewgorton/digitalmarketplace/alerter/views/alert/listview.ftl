<!DOCTYPE html>
<html>
<head>
    <#include "../fragments/head.ftl" parse="false">
    <title>List of Alerts</title>
</head>
<body>
<h1>List of Alerts</h1>
<table>
    <tr>
        <th>Email</th>
        <th>Customer Regex</th>
        <th>Status</th>
        <th>Delete?</th>
    </tr>
    <#list alerts as alert>
    <tr>
        <td>${alert.email}</td>
        <td>${alert.customerMatchRegex}</td>
        <td>
        <#if alert.enabled>
        <form action="${alert.id}/disable" method="post"><input type="submit" value="Disable"></form>
        <#else>
        <form action="${alert.id}/enable" method="post"><input type="submit" value="Enable"></form>
        </#if>
        </td>
        <td>
        <form action="${alert.id}/delete" method="post"><input type="submit" value="Delete"></form>
        </td>
    </tr>
    </#list>
</table>

<br />
<hr />
<br />

<h2>Add a new alert</h2>
<form action="create" method="post">
<table>
    <tr>
        <th>Email</th>
        <td><input type="text" name="email" value="email"></td>
    </tr>
    <tr>
        <th>Customer RegEx</th>
        <td><input type="text" name="customerRegex" value="customerRegex"></td>
    </tr>
    <tr>
        <th></th>
        <td><input type="submit" value="Create"></td>
    </tr>
</table>
</form>
<p><a href="../">Home</a></p>
</body>
</html>