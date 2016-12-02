function drawBarChart(elementName, chartLabel, json) {
    var labels = []
    var values = []
    var colors = []

    for(var i = 0; i < json.length; i++) {
        var elem = json[i];
        labels.push(elem.label);
        values.push(elem.value);
        colors.push(elem.color);
    }

    var data = {
        labels: labels,
        datasets: [
            {
                label: chartLabel,
                backgroundColor: colors,
                borderWidth: 1,
                data: values,
            }
        ]
    };

    var myChart = new Chart(document.getElementById(elementName), {
        type: 'bar',
        data: data,
        options: {
            maintainAspectRatio: false,
            responsive: false,
            title: {
                display: true,
                text: chartLabel
            },
            legend: {
                display: false
            },
            tooltips: {
                enabled: false
            }
        }
    });
}