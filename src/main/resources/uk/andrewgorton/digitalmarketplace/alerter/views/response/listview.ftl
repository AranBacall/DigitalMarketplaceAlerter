<!DOCTYPE html>
<html>
<head>
    <#include "../fragments/head.ftl" parse="false">
    <title>Response</title>
</head>
<body>
<h1>Please select a response from the list below:</h1>
<form action="${opportunity}/response" method="post">
    <#list responses as response>
        <input type="radio" name="response" value="${response.id}">
        <#if response.response == "No">
            ${response.response}, ${response.reason}<br>
        <#else>
            ${response.response}<br>
        </#if>
    </#list>
    <br>
    <input type="submit" value="Respond">
</form>
</body>
</html>