<html>
<head>
    <#include "../fragments/head.ftl" parse="false">
    <title>Opportunity</title>
</head>
<body>
<h1>${opportunity.title}</h1>
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
    <tr>
        <th>Closed</th>
        <td>${opportunity.closed?string("yes", "no")}</td>
    </tr>
    <tr>
        <th>First Seen</th>
        <td>${opportunity.firstSeen}</td>
    </tr>
    <tr>
        <th>Last Updated</th>
        <td>${opportunity.lastUpdated}</td>
    </tr>
    <tr>
            <th>Estimated Duration</th>
            <#if opportunity.duration??>
                <td>
                    <form action="/opportunity/${opportunity.id}/duration" method="post">
                        <input type="text" name="duration" value="${opportunity.duration}">
                        <input type="submit" value="Update"/>
                    </form>
                </td>

            <#else>
               <td>
                     <form action="/opportunity/${opportunity.id}/duration" method="post">
                          <input type="text" name="duration" >
                          <input type="submit" value="Set"/>
                     </form>
               </td>
            </#if>
    </tr>
    <tr>
            <th>Estimated Cost</th>
            <#if opportunity.cost??>
                 <td>
                     <form action="/opportunity/${opportunity.id}/cost" method="post">
                          <input type="text" name="cost" value="${opportunity.cost}">
                          <input type="submit" value="Update"/>
                      </form>
                 </td>
            <#else>
                  <td>
                       <form action="/opportunity/${opportunity.id}/cost" method="post">
                            <input type="text" name="cost" >
                            <input type="submit" value="Set"/>
                       </form>
                  </td>
            </#if>
    </tr>
</table>
<p><a href="../">Back to list</a></p>
</body>
</html>