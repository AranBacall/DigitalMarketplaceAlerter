package uk.andrewgorton.digitalmarketplace.alerter.email;

import org.apache.commons.lang3.StringEscapeUtils;
import uk.andrewgorton.digitalmarketplace.alerter.Opportunity;

import java.text.SimpleDateFormat;

public class HtmlEmailBodyFactory {
    private final SimpleDateFormat sdf = new SimpleDateFormat("EEEE, d MMMM yyyy");
    private final String adminName;
    private final String adminEmail;

    public HtmlEmailBodyFactory(String adminName, String adminEmail) {
        this.adminName = adminName;
        this.adminEmail = adminEmail;
    }

    public String create(Opportunity o) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append("<head><style>");
        sb.append("body {");
        sb.append("   font-family: Tahoma, Geneva, sans-serif;");
        sb.append("}");
        sb.append("table {");
        sb.append("   border-collapse: collapse;");
        sb.append("}");
        sb.append("table, th, td {");
        sb.append("   border: 1px solid black;");
        sb.append("}");
        sb.append("th, td {");
        sb.append("   padding: 5px;");
        sb.append("   text-align: left;");
        sb.append("}");
        sb.append("th {");
        sb.append("   background-color: #4CAF50;");
        sb.append("   color: white;");
        sb.append("}");
        sb.append("th.totals, td.totals {");
        sb.append("   text-align: center;");
        sb.append("   font-weight: bold;");
        sb.append("}");
        sb.append("th.centre, td.centre {");
        sb.append("   text - align:center;");
        sb.append("}");
        sb.append(" tr:nth - child(even) {");
        sb.append("   background - color: #f2f2f2;");
        sb.append("}");
        sb.append("tr:hover {");
        sb.append("   background - color: #d9ffb3;");
        sb.append("}");
        sb.append("</style></head>");
        sb.append("<body>");
        sb.append("<table>");
        sb.append("<tr><th>Title</th><td>");
        sb.append("<a href=\"");
        sb.append(o.getUrl());
        sb.append("\">");
        sb.append(o.getTitle());
        sb.append("</a></td></tr>");
        sb.append("<tr><th>Customer</th><td>");
        sb.append(o.getCustomer());
        sb.append("</td></tr>");
        sb.append("<tr><th>Location</th><td>");
        sb.append(o.getLocation());
        sb.append("</td></tr>");
        sb.append("<tr><th>Type</th><td>");
        sb.append(o.getOpportunityType());
        sb.append("</td></tr>");
        sb.append("<tr><th>Excerpt</th><td>");
        sb.append(o.getExcerpt());
        sb.append("</td></tr>");
        sb.append("<tr><th>Closing</th><td>");
        sb.append(sdf.format(o.getClosing()));
        sb.append("</td></tr>");
        sb.append("</table>");
        sb.append("<p>For changes to your subscription, contact <a href=\"mailto:");
        sb.append(adminEmail);
        sb.append("?subject=Digital Markplace Subscription\">");
        sb.append(adminName);
        sb.append("</a>.</p>");
        sb.append("</body>");
        sb.append("</html>");
        return sb.toString();
    }
}
