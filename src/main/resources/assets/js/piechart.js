function drawPieChart(elementName, data)
{
    var pie = new d3pie(elementName, {
    	"header": {
    	},
    	"footer": {
    	},
    	"size": {
    		"canvasWidth": 650,
    		"pieInnerRadius": "30%",
    		"pieOuterRadius": "90%"
    	},
    	"data": {
    		"sortOrder": "value-desc",
    		"content": data
    	},
    	"labels": {
        		"outer": {
        			"hideWhenLessThanPercentage": 3,
        			"pieDistance": 32
        		},
        		"inner": {
        			"format": "value",
        			"hideWhenLessThanPercentage": 3
        		},
        		"mainLabel": {
        			"fontSize": 11
        		},
        		"percentage": {
        			"color": "#ffffff",
        			"decimalPlaces": 0
        		},
        		"value": {
        			"color": "#adadad",
        			"fontSize": 11
        		},
        		"lines": {
        			"enabled": true
        		},
        		"truncation": {
        			"enabled": true
        		}
        	},
        	"tooltips": {
        		"enabled": true,
        		"type": "placeholder",
        		"string": "{label}: {value}, {percentage}%"
        	},
    	"effects": {
    		"pullOutSegmentOnClick": {
    			"effect": "linear",
    			"speed": 400,
    			"size": 8
    		},
    		"highlightSegmentOnMouseover": true,
    		"highlightLuminosity": -0.5
    	},
    	"misc": {
    		"gradient": {
    			"enabled": true,
    			"percentage": 100
    		}
    	}
    });
}

function drawTable(elementName, data, datacolumns) {

            data.sort(compare);

    		var table = d3.select('#'+elementName).append('table')
    		var thead = table.append('thead')
    		var	tbody = table.append('tbody');

    		// append the header row
    		thead.append('tr')
    		  .selectAll('th')
    		  .data(datacolumns).enter()
    		  .append('th')
    		  .text(function (column) { return column; });

    		// create a row for each object in the data
    		var rows = tbody.selectAll('tr')
    		  .data(data)
    		  .enter()
    		  .append('tr');

    		// create a cell in each row for each column
    		var cells = rows.selectAll('td')
    		  .data(function (row) {
    		    return datacolumns.map(function (column) {
    		      return {column: column, value: row[column]};
    		    });
    		  })
    		  .enter()
    		  .append('td')
    		  .text(function (d) { return d.value; });

    	  return table;
}

//sort in descending order
function compare(a,b) {
   if (a.value > b.value)
       return -1;
   if (a.value < b.value)
       return 1;
   return 0;
}



