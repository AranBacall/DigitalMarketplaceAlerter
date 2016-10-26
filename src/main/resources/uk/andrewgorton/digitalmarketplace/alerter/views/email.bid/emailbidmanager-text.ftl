${opportunity.title}.

Customer:
${opportunity.customer}.

Location:
${opportunity.location}.

Type:
${opportunity.opportunityType}.

Description:
${opportunity.excerpt}.

Closing On:
${opportunity.closing?date}

Link:
${opportunity.url}

Please respond using one of the links below:
<#list responses as response>
    <tr>
        <td>
            <a href="${response.url}">${response.description}</a>
        </td>
    </tr>
</#list>

Please contact ${adminName} (${adminEmail}) for any changes to your subscription.