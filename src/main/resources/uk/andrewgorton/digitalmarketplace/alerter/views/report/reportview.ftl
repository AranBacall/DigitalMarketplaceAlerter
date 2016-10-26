<html>
<head>
    <#include "../fragments/head.ftl" parse="false">
    <link rel="stylesheet" type="text/css" href="/dma/static/report.css">
    <title>Opportunity</title>
</head>

<style>

.arc text {
  font: 10px sans-serif;
  text-anchor: middle;
}

.arc path {
  stroke: #fff;
}

</style>

<body>
<h1>Reports</h1>

<table>
    <tr>
        <th>Opportunities per location</th>
        <th>Opportunities per customer</th>
    </tr>
    <tr>
         <td>
            <div id='location_piechart'/>
         </td>
         <td>
             <div id='customer_piechart'/>
         </td>
    </tr>
     <tr>
         <td>
              <div id='location_table'/>
         </td>
         <td>
              <div id='customer_table'/>
         </td>
      </tr>
</table>

<script src="//cdnjs.cloudflare.com/ajax/libs/d3/3.4.4/d3.min.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<script src="/dma/static/js/d3pie.min.js"></script>
<script src="/dma/static/js/piechart.js"></script>

<script>

$.getJSON('/dma/report/opl', function(data) {
    drawPieChart("location_piechart",data);
    drawTable("location_table",data,['label', 'value']);
});

$.getJSON('/dma/report/opc', function(data) {
    drawPieChart("customer_piechart",data);
    drawTable("customer_table",data,['label', 'value']);
});

</script>

<p><a href="../">Home</a></p>
</body>
</html>
