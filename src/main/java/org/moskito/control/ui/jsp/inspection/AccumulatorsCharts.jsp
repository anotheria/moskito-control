<%@ page language="java" contentType="text/html;charset=UTF-8" session="true"
        %><%@ taglib uri="http://www.anotheria.net/ano-tags" prefix="ano"
        %>
<div class="accumulators-charts">
    <ano:iterate id="chart" name="chartBeans" type="org.moskito.control.ui.bean.ChartBean">
        <div class="chart-item chart-item-modal">
            <div class="chart-box-name"><ano:write name="chart" property="name"/></div>
            <div id="<ano:write name="chart" property="divId"/>" class="chart-box" style="width: 800px; height: 300px;"></div>
            <span class="footitle one-line-text"><ano:write name="chart" property="legend"/></span>
        </div>
    </ano:iterate>

    <script type="text/javascript">
        <ano:equal name="chartsToggle" value="true">
            var multipleGraphData = [];
            var multipleGraphNames = [];

            <ano:iterate id="chart" name="chartBeans" type="org.moskito.control.ui.bean.ChartBean">
            multipleGraphData.push([
                <ano:iterate name="chart" property="points" id="chartPoint" indexId="i">
                <ano:notEqual name="i" value="0">, </ano:notEqual><ano:write name="chartPoint" property="JSONWithNumericTimestamp"/>
                </ano:iterate>
            ]);
            multipleGraphNames.push([
                <ano:iterate name="chart" property="lineNames" id="lineName" indexId="i">
                <ano:notEqual name="i" value="0">, </ano:notEqual>'<ano:write name="lineName"/>'
                </ano:iterate>
            ]);
            </ano:iterate>


            var names = multipleGraphNames.map(function (graphNames) {
                return graphNames;
            });

            var containerSelectors = $('.chart-box').map(function () {
                return $(this).attr("id");
            });

            multipleGraphData.forEach(function (graphData, index) {
                var chartParams = {
                    container: containerSelectors[index],
                    names: names[index],
                    data: graphData,
                    colors: [],
                    type: 'LineChart',
                    title: names[index],
                    dataType: 'datetime',
                    options: {
                        legendsPerSlice: 5,
                        margin: {top: 20, right: 20, bottom: 20, left: 40}
                    }
                };

                // Creating chart
                chartEngineIniter.init( chartParams );
            });
        </ano:equal>
    </script>
</div>
