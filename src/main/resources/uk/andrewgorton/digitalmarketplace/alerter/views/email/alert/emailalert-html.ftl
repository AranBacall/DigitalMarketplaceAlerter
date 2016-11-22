<html>
<head><style>
body {
   font-family: Tahoma, Geneva, sans-serif;
}
table {
   border-collapse: collapse;
}
table, th, td {
   border: 1px solid black;
}
th, td {
   padding: 5px;
   text-align: left;
}
th {
   background-color: #4CAF50;
   color: white;
}
th.totals, td.totals {
   text-align: center;
   font-weight: bold;
}
th.centre, td.centre {
   text - align:center;
}
 tr:nth - child(even) {
   background - color: #f2f2f2;
}
tr:hover {
   background - color: #d9ffb3;
}
</style></head>
<body>
<table>
    <tr>
        <th>Title</th>
        <td><a href="${opportunity.url}" target="_new">${opportunity.title}</a></td>
    </tr>
    <tr>
        <th>Type</th>
        <td>${opportunity.opportunityType}</td>
    </tr>
    <tr>
        <th>Location</th>
        <td>${opportunity.location}</td>
    </tr>
    <tr>
        <th>Customer</th>
        <td>${opportunity.customer}</td>
    </tr>
    <tr>
        <th>Excerpt</th>
        <td>${opportunity.excerpt}</td>
    </tr>
    <tr>
        <th>Published</th>
        <td>${opportunity.published?date}</td>
    </tr>
    <tr>
        <th>Closing</th>
        <td>${opportunity.closing?date}</td>
    </tr>
    <#if opportunity.duration gt 0>
    <tr>
        <th>Estimated Duration</th>
        <td>${opportunity.duration}</td>
    </tr>
    </#if>
    <#if opportunity.cost gt 0>
    <tr>
        <th>Estimated Cost</th>
        <td>${opportunity.cost}</td>
    </tr>
    </#if>
</table>
<p>For changes to your subscription, contact <a href="mailto:${adminEmail}
?subject=Digital Markplace Subscription">${adminName}</a>.</p>
</body>
</html>