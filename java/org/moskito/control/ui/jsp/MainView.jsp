<%@ page language="java" contentType="text/html;charset=UTF-8"	session="true"
        %><%@ taglib uri="http://www.anotheria.net/ano-tags" prefix="ano"
        %><!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>MoSKito Control</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE">
    <META HTTP-EQUIV="EXPIRES" CONTENT="0">
    <META HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE">
    <META NAME="ROBOTS" CONTENT="NONE">
    <link rel="shortcut icon" href="../img/favicon.ico" type="image/x-icon">
    <link type="text/css" rel="stylesheet" rev="stylesheet" href="../ext/bootstrap-2.2.2/css/bootstrap.css"/>
    <link type="text/css" rel="stylesheet" rev="stylesheet" href="../ext/font-awesome-3.2.1/css/font-awesome.min.css">
    <link type="text/css" rel="stylesheet" rev="stylesheet" href="../css/common.css" />
    <!--[if IE]>
    <link type="text/css" rel="stylesheet" rev="stylesheet" href="../css/common_ie.css"/>
    <![endif]-->
</head>
<body>

<div class="wrapper">
<div class="left-bar">

    <a href="main" class="logo">
        <img src="../img/logo.png" alt="MoSKito Control" border="0"/>
        <span class="version"><ano:write name="moskito.control.version"/></span>
    </a>

    <div class="block">
        <h3 class="block-title">Category</h3>
        <ul class="category-list">
            <ano:iterate name="categories" id="category" type="org.moskito.control.ui.bean.CategoryBean">
                <li class="<ano:equal name="category" property="selected" value="true">active </ano:equal><ano:equal name="category" property="all" value="true">all </ano:equal><ano:write name="category" property="health"/>">
                    <a href="setCategory?category=<ano:write name="category" property="name"/>">
                        <ano:notEmpty name="category" property="all"><i class="icon-folder-close"></i></ano:notEmpty><ano:write name="category" property="name"/>&nbsp;(<ano:write name="category" property="componentCount"/>)<span class="status"></span>
                    </a>
                </li>
            </ano:iterate>
        </ul>
    </div>

    <div class="block">
        <h3 class="block-title">Widgets</h3>
        <ul class="widgets-list">
            <ano:equal name="statusToggle" value="true"><li class="statuses active"><a href="switchStatus?status=off"><i class="icon-adjust"></i>Status</a></li></ano:equal>
            <ano:notEqual name="statusToggle" value="true"><li class="statuses"><a href="switchStatus?status=on"><i class="icon-adjust"></i>Status</a></li></ano:notEqual>
            <ano:equal name="tvToggle" value="true"><li class="tv active"><a href="switchTv?tv=off"><i class="icon-smile"></i>TV</a></li></ano:equal>
            <ano:notEqual name="tvToggle" value="true"><li class="tv"><a href="switchTv?tv=on"><i class="icon-smile"></i>TV</a></li></ano:notEqual>
            <ano:equal name="chartsToggle" value="true"><li class="charts active"><a href="switchCharts?charts=off"><i class="icon-bar-chart"></i>Charts</a></li></ano:equal>
            <ano:notEqual name="chartsToggle" value="true"><li class="charts"><a href="switchCharts?charts=on"><i class="icon-bar-chart"></i>Charts</a></li></ano:notEqual>
            <ano:equal name="historyToggle" value="true"><li class="history active"><a href="switchHistory?history=off"><i class="icon-reorder"></i>History</a></li></ano:equal>
            <ano:notEqual name="historyToggle" value="true"><li class="history"><a href="switchHistory?history=on"><i class="icon-reorder"></i>History</a></li></ano:notEqual>
        </ul>
    </div>

    <div class="block">
        <h3 class="block-title">Statistics</h3>
        <ul class="statistics-list">
            <li class="purple"><a href="#"><ano:write name="countByStatus" property="purple"/> <span class="status"></span></a></li>
            <li class="red"><a href="#"><ano:write name="countByStatus" property="red"/> <span class="status"></span></a></li>
            <%--<li class="orange"><a href="#"><ano:write name="countByStatus" property="orange"/> <span class="status"></span></a></li>--%>
            <li class="yellow"><a href="#"><ano:write name="countByStatus" property="yellow"/> <span class="status"></span></a></li>
            <li class="green"><a href="#"><ano:write name="countByStatus" property="green"/> <span class="status"></span></a></li>
        </ul>
    </div>

</div>
<div class="content">

    <div class="header">
        <ul class="applications-list">
            <ano:iterate name="applications" id="app_lication" type="org.moskito.control.ui.bean.ApplicationBean">
                <li class="<ano:equal name="app_lication" property="active" value="true">active </ano:equal><ano:write name="app_lication" property="color"/>"><a href="setApplication?application=<ano:write name="app_lication" property="name"/>"><ano:write name="app_lication" property="name"/> <span class="status"></span></a></li>
            </ano:iterate>
        </ul>
    </div>

    <div class="infobar">
        <div class="infoline">
            <div class="pull-left">
                <span class="last-refresh"><i class="icon-time"></i>Last refresh: <ano:write name="lastRefreshTimestamp"/></span>
                <span class="next-refresh"><i class="icon-time"></i>Next refresh in <span id="remains">60</span> seconds</span>
            </div>
            <div class="pull-right">
                <span class="mute-title">Mute for 60 minutes</span>
                <a href="#" class="btn2"><span class="inbtn">Mute</span></a>
                <span class="vline"></span>
                <a href="config" class="btn2 settings"><span class="inbtn"><i class="icon-cog"></i>Settings</span></a>
            </div>
        </div>
    </div>

    <div class="box-list">

        <%-- TV start --%>
       <ano:equal name="tvToggle" value="true">
            <div class="box tv">
                <div class="content-title"><h3><i class="icon-smile"></i>TV</h3></div>
                <div class="smiley">
                    <img src="../img/smiley_<ano:write name="tvStatus"/>.png" alt="status: <ano:write name="tvStatus"/>"/>
                </div>
            </div>
        </ano:equal>
        <%-- TV END --%>


        <ano:equal name="statusToggle" value="true">
        <ano:iterate name="componentHolders" id="holder" type="org.moskito.control.ui.bean.ComponentHolderBean">
        <!-- category block for <ano:write name="holder" property="categoryName"/> -->
        <div class="box <ano:write name="holder" property="health"/>">
            <div class="content-title"><h3><span class="status"></span><ano:write name="holder" property="categoryName"/></h3></div>
            <ul class="controls">
                <ano:iterate name="holder" property="components" type="org.moskito.control.ui.bean.ComponentBean" id="component">
                    <li class="<ano:write name="component" property="color"/>">
                        <span class="control-tooltip input-block-level">
                            <ano:greaterThan name="component" property="messageCount" value="0">
                                <span class="tooltip-top-line"><span class="status"></span>
                                    <ano:iterate name="component" property="messages" id="message">
                                        <ano:write name="message"/><br/>
                                    </ano:iterate>
                                </span>
                            </ano:greaterThan>
                            <span class="tooltip-lower-line time"><ano:write name="component" property="updateTimestamp"/></span>
                            <span class="arrow"></span>
                        </span>
                        <span class="control-title">
                            <span class="status"></span><ano:write name="component" property="name"/>
                        </span>
                    </li>
                </ano:iterate>
            </ul>
        </div>
        </ano:iterate>
        </ano:equal>

        <!-- CHARTS -->
        <ano:equal name="chartsToggle" value="true">
        <script type="text/javascript">
            <ano:iterate id="chart" name="chartBeans" type="org.moskito.control.ui.bean.ChartBean">
                //chart data for <ano:write name="chart" property="divId"/>
                var chartDataArray<ano:write name="chart" property="divId"/> = [<ano:iterate name="chart" property="points" id="point" indexId="i"><ano:notEqual name="i" value="0">,</ano:notEqual><ano:write name="point"/></ano:iterate>];
            </ano:iterate>
        </script>
        <div class="box charts">
            <div class="content-title"><h3><i class="icon-bar-chart"></i>Charts</h3></div>
            <div class="chart-list">
                <ano:iterate id="chart" name="chartBeans" type="org.moskito.control.ui.bean.ChartBean">
                    <div id="<ano:write name="chart" property="divId"/>" class="chart-box" style="width: 800px; height: 300px;"></div>
                </ano:iterate>
            </div>
        </div>
        </ano:equal>


        <%-- History start --%>
        <ano:present name="historyItems">
        <div class="box history">
            <div class="content-title"><h3><i class="icon-reorder"></i>History</h3></div>
            <div class="history-box">
                <table class="table table-striped">
                    <thead>
                    <tr>
                        <th width="250">Timestamp</th>
                        <th>Name</th>
                        <th width="150">Status change</th>
                    </tr>
                    </thead>
                    <tbody>
                    <ano:iterate id="item" name="historyItems">
                    <tr>
                        <td><ano:write name="item" property="time"/></td>
                        <td><ano:write name="item" property="componentName"/></td>
                        <td><span class="status <ano:write name="item" property="oldStatus"/>"></span><span class="arrow-right"></span><span class="status <ano:write name="item" property="newStatus"/>"></span></td>
                    </tr>
                    </ano:iterate>
                    </tbody>
                </table>
            </div>
        </div>
        </ano:present>
        <%-- History END --%>
    </div>

</div>
</div>

<script type="text/javascript" src="../ext/jquery-1.8.2/jquery-1.8.2.js"></script>
<script type="text/javascript" src="../ext/jquery-ui-1.10.0/js/jquery-ui-1.10.0.custom.min.js"></script>
<script type="text/javascript" src="https://www.google.com/jsapi"></script>
<!--[if lt IE 10]>
<script type="text/javascript" src="../ext/pie-1.0.0/pie_uncompressed.js"></script>
<![endif]-->
<script type="text/javascript" src="../js/common.js"></script>
<script type="text/javascript">
    $(function() {
        $( ".controls" ).sortable({
            revert: true
        });
        $( "ul, li" ).disableSelection();

        $(".box ul.controls li").live({
            mouseenter: function(){
                $(this).find(".control-tooltip").show().animate({
                    bottom: '34',
                    opacity: 0.9
                }, 200,function(){
                    $(".control-tooltip").live({
                        mouseenter: function(){
                            $(this).hide();
                        }
                    });
                });
            },
            mouseleave: function(){
                $(this).find(".control-tooltip").hide().animate({
                    bottom: '28',
                    opacity: 0
                }, 200);
            }
        });
    });
    google.load("visualization", "1", {packages:["corechart"]});


    <ano:equal name="chartsToggle" value="true">
        <ano:iterate id="chart" name="chartBeans" type="org.moskito.control.ui.bean.ChartBean">
            google.setOnLoadCallback(draw<ano:write name="chart" property="divId"/>);
        </ano:iterate>
        <ano:iterate id="chart" name="chartBeans" type="org.moskito.control.ui.bean.ChartBean">
            function draw<ano:write name="chart" property="divId"/>() {
                var chartData = new google.visualization.DataTable();
                chartData.addColumn('string', 'Time');
                <ano:iterate name="chart"  property="lineNames" id="lineName">
                    chartData.addColumn('number', '<ano:write name="lineName"/>');
                </ano:iterate>
                chartData.addRows(chartDataArray<ano:write name="chart" property="divId"/>);
                var options = {
                    "title": "<ano:write name="chart" property="name"/>",
                    "titleTextStyle": {"color": "#444"},
                    "hAxis": {"textStyle": {"color": '#444'}},
                    "width": 800,
                    "height": 300,
                    "chartArea":{"left":60,"width":560}
                };

                var chart = new google.visualization.LineChart(document.getElementById('<ano:write name="chart" property="divId"/>'));
                chart.draw(chartData, options);
            }
        </ano:iterate>
    </ano:equal>


</script>
<script type="text/javascript">

    function countDown(){
        remains = remains - 1;
        document.getElementById("remains").innerHTML = ''+remains;
        if (remains<=0){
            window.location.href = window.location.href;
        }
    }
    var remains = 60;
    window.setInterval("countDown()",1000);


</script>

</body>
</html>