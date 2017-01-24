<!DOCTYPE html>
<html>
<head>
    <#include "../fragments/head.ftl" parse="false">
    <title>List of Opportunities</title>
</head>
<body>
<h1>List of Opportunities</h1>
<table>
    <tr>
        <th>Title</th>
        <th>Type</th>
        <th>Customer</th>
        <th>Location</th>
        <th>Published</th>
        <th>Closing</th>
        <th>Alerted</th>
    </tr>
    <#list opportunities as opportunity>
    <tr>
        <#if opportunity.removed>
        <td><a href="${opportunity.url}" target="_new"><span class="opportunityremoved">${opportunity.title}</a></span> <span class="detailslink">[<a href="${opportunity.id}/">more</a>]</span></td>
        <#else>
        <td><a href="${opportunity.url}" target="_new">${opportunity.title}</a> <span class="detailslink">[<a href="${opportunity.id}/">more</a>]</span></td>
        </#if>
        <td>${opportunity.opportunityType}</td>
        <td>${opportunity.customer}</td>
        <td>${opportunity.location}</td>
        <td><#if opportunity.closed>Closed<#else>${opportunity.published?date}</#if></td>
        <td><#if opportunity.closed>Closed<#else>${opportunity.closing?date}</#if></td>
        <td>
        <#if opportunity.alerted>
        <form action="${opportunity.id}/clearAlert" method="post"><input type="submit" value="Unalert"></form>
        <#else>
        Not Alerted
        </#if>
        </td>
    </tr>
    </#list>
</table>
</body>
<p><a href="../">Home</a></p>
</html>