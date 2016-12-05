<html>
<head>
    <#include "../fragments/head.ftl" parse="false">
    <title>Digital Marketplace Alerting - Forbidden</title>
</head>
<body>
    <h1>Forbidden</h1>
    <#if exceptionMessage?has_content>
        <p><b>Error:</b> ${exceptionMessage}</p>
    </#if>
    <p>Perhaps you should <a href="${loginLocation}?returnLocation=${returnLocation}">log in</a>?</p>
</body>
</html>