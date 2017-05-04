<%@ page language="java" contentType="text/html;charset=UTF-8" session="true"
        %><%@ taglib uri="http://www.anotheria.net/ano-tags" prefix="ano"
        %>
<div class="accumulators-charts">
    <ano:iterate id="chart" name="chartBeans" type="org.moskito.control.ui.bean.ChartBean">
        <div class="chart-item chart-item-modal">
            <div id="<ano:write name="chart" property="divId"/>" class="chart-box" style="width: 800px; height: 300px;"></div>
        </div>
    </ano:iterate>

    <script type="text/javascript">
        <ano:equal name="chartsToggle" value="true">
            var chartEngine = chartEngineIniter['D3'];

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

                // Setting fullscreen buttons and handlers for chart
                var container = $('#' + chartParams.container);
                container.append("<i class='icon-resize-small'></i>");
                container.append("<i class='icon-resize-full'></i>");

                // Chart fullscreen click handler
                container.click(function(){
                    var $parent = $(this).parent();
                    $parent.toggleClass('chart_fullscreen');
                    if ( $parent.hasClass('chart_fullscreen') ){
                        $parent.css('top', $(window).scrollTop());
                        $(this).width($parent.width()).height($parent.height());
                        chartEngineIniter.d3charts.dispatch.refreshLineChart( "#" + container.attr("id"), true );
                    }
                    else{
                        $parent.css('top', 'auto');
                        $(this).width(800).height(300);
                        chartEngineIniter.d3charts.dispatch.refreshLineChart( "#" + container.attr("id"), true );
                    }
                });

                // Creating chart
                chartEngine( chartParams );
            });
        </ano:equal>
    </script>
</div>
