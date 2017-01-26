<html>
<head>
    <#include "../fragments/head.ftl" parse="false">
    <link rel="stylesheet" type="text/css" href="/dma/static/report.css">
    <title>Reports</title>
</head>
<body>
<h1>Bid Manager Responses</h1>    <a href="../">Home</a>
<canvas id="responsesChart" width="200" height="200"></canvas>
<canvas id="bvoChart" width="200" height="200"></canvas>
<div id='location_piechart'></div>
<br />
<div id='customer_piechart'></div>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/d3/3.4.4/d3.min.js"></script>

<script src="/static/js/Chart.bundle.js"></script>
<script src="/static/js/draw.js"></script>

<script src="/static/js/d3pie.min.js"></script>
<script src="/static/js/piechart.js"></script>

<script>
$.getJSON('/dma/report/opl', function(data) {
    drawPieChart("location_piechart",data);
    drawTable("location_table",data,['label', 'value']);
});

$.getJSON('/dma/report/opc', function(data) {
    drawPieChart("customer_piechart",data);
    drawTable("customer_table",data,['label', 'value']);
});

$.getJSON('/dma/report/responses', function(data) {
    drawBarChart("responsesChart", "Responses", data);
});

$.getJSON('/dma/report/bids-against-opportunities', function(data) {
    drawBarChart("bvoChart", "Bids Vs Opportunities", data);
});
</script>
</body>
</html>
