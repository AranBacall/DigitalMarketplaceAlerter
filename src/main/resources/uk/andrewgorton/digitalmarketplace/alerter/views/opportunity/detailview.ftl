<html>
<head>
    <#include "../fragments/head.ftl" parse="false">
    <title>Opportunity</title>
</head>
<body>
<#if opportunity.removed??>
<h1 style="color:red;">This opportunity has been removed from the Digital Marketplace</h1>
</#if>
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
                    <form action="./duration" method="post">
                        <input type="text" name="duration" value="${opportunity.duration}"> months
                        <input type="submit" value="Update"/>
                    </form>
                </td>

            <#else>
               <td>
                     <form action="./duration" method="post">
                          <input type="text" name="duration" > months
                          <input type="submit" value="Set"/>
                     </form>
               </td>
            </#if>
    </tr>
    <tr>
            <th>Estimated Cost</th>
            <#if opportunity.cost??>
                 <td>
                     <form action="./cost" method="post">
                          <input type="text" name="cost" value="${opportunity.cost}"> daily
                          <input type="submit" value="Update"/>
                      </form>
                 </td>
            <#else>
                  <td>
                       <form action="./cost" method="post">
                            <input type="text" name="cost" > daily
                            <input type="submit" value="Set"/>
                       </form>
                  </td>
            </#if>
    </tr>
    <tr>
        <th>Email Bid Managers</th>
        <td>
            <form action="./bidmanager" method="post">
                <input type="text" name="emails" placeholder="bid.manager@cgi.com, bid.manager2@cgi.com">
                <input type="submit" value="Email"/>
            </form>
        </td>
    </tr>
</table>
<p><a href="../">Back to list</a></p>
</body>
</html>