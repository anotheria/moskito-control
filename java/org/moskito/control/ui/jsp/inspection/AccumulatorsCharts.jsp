<%@ page language="java" contentType="text/html;charset=UTF-8" session="true"
        %><%@ taglib uri="http://www.anotheria.net/ano-tags" prefix="ano"
        %>
<div class="accumulators-charts">
    <ano:iterate id="chart" name="chartBeans" type="org.moskito.control.ui.bean.ChartBean">
        <div class="chart-item chart-item-modal">
            <div id="<ano:write name="chart" property="divId"/>" class="chart-box" style="width: 800px; height: 300px;"></div>
            <span class="footitle one-line-text"><ano:write name="chart" property="legend"/></span>
        </div>
    </ano:iterate>
    <script type="text/javascript">
        <ano:iterate id="chart" name="chartBeans" type="org.moskito.control.ui.bean.ChartBean">
        // chart data for <ano:write name="chart" property="divId"/>
        var chartDataArray<ano:write name="chart" property="divId"/> = [<ano:iterate name="chart" property="points" id="point" indexId="i"><ano:notEqual name="i" value="0">,</ano:notEqual><ano:write name="point"/></ano:iterate>];
        </ano:iterate>
        <ano:iterate id="chart" name="chartBeans" type="org.moskito.control.ui.bean.ChartBean">
        function draw<ano:write name="chart" property="divId"/>(e, opts) {
            var chartData = new google.visualization.DataTable();
            chartData.addColumn('string', 'Time');
            <ano:iterate name="chart"  property="lineNames" id="lineName">
            chartData.addColumn('number', '<ano:write name="lineName"/>');
            </ano:iterate>
            chartData.addRows(chartDataArray<ano:write name="chart" property="divId"/>);
            var defaultOptions = {
                "title": "<ano:write name="chart" property="name"/>",
                "titleTextStyle": {"color": "#444"},
                "hAxis": {"textStyle": {"color": '#444'}},
                "width": 800,
                "height": 300,
                "chartArea":{"left":100,"width":680}
            };

            var options = $.extend({} ,defaultOptions, opts);

            var chart = new google.visualization.LineChart(document.getElementById('<ano:write name="chart" property="divId"/>'));
            chart.draw(chartData, options);
        }

        </ano:iterate>
        <ano:iterate id="chart" name="chartBeans" type="org.moskito.control.ui.bean.ChartBean">
        draw<ano:write name="chart" property="divId"/>();
        </ano:iterate>
    </script>
</div>
