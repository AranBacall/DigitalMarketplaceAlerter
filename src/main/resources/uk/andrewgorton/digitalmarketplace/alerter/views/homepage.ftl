<!DOCTYPE html>
<html>
<head>
    <#include "fragments/head.ftl" parse="false">
    <title>Home</title>
</head>
<body>
<h1>Home</h1>
<table>
    <tr>
        <th>Resource</th>
        <th>Description</th>
    </tr>
    <tr>
        <td><a href="opportunity/">Opportunities</a></td>
        <td>All DOS opportunities we've seen</td>
    </tr>
    <tr>
        <td><a href="alert/">Alerts</a></td>
        <td>Create or modify who gets notified when a new opportunity is detected</td>
    </tr>
    <tr>
        <td><a href="report/">Reports</a></td>
        <td>View the latest DOS reports</td>
    </tr>
</table>
<form action="${logoutLocation}" method="POST"><input type="submit" value="Logout"></form>
</body>
</html>