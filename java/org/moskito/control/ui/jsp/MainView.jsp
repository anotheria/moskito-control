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

    <ano:notEqual name="configToggle" value="true">
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
    </ano:notEqual>
</div>
<div class="content">
    <ano:equal name="configToggle" value="true">
        <div class="infobar">
            <div class="infoline">
                <div class="pull-left">
                    <span class="configuration">Settings</span>
                </div>
                <div class="pull-right">
                    <a href="switchConfig?config=off" class="btn2"><span class="inbtn">Back</span></a>
                </div>
            </div>
        </div>

        <ul>
            <li>Notifications muting time: <ano:write name="configuration" property="notificationsMutingTime"/> minutes</li>
            <li>History items amount: <ano:write name="configuration" property="historyItemsAmount"/></li>

            <ano:notEmpty name="configuration" property="applications">
                <li><h5>Applications</h5></li>
                <ano:iterate name="configuration" property="applications" id="app" type="org.moskito.control.config.ApplicationConfig">
                    <ul>
                        <li><h6>Name: <ano:write name="app" property="name"/></h6></li>
                        <li><h6>Components</h6></li>
                        <ano:iterate name="app" property="components" id="component" type="org.moskito.control.config.ComponentConfig">
                            <ul>
                                <li>Name: <ano:write name="component" property="name"/></li>
                                <li>Category: <ano:write name="component" property="category"/></li>
                                <li>Connector type: <ano:write name="component" property="connectorType"/></li>
                                <li>Location: <ano:write name="component" property="location"/></li>
                            </ul><br/>
                        </ano:iterate>

                        <ano:notEmpty name="app" property="charts">
                            <li><h6>Charts</h6></li>
                            <ano:iterate name="app" property="charts" id="chart" type="org.moskito.control.config.ChartConfig">
                                <ul>
                                    <li>Name: <ano:write name="chart" property="name"/></li>
                                    <li>Limit: <ano:write name="chart" property="limit"/></li>
                                    <ano:iterate name="chart" property="lines" id="line" type="org.moskito.control.config.ChartLineConfig">
                                        <ul>
                                            <li>Component: <ano:write name="line" property="component"/></li>
                                            <li>Accumulator: <ano:write name="line" property="accumulator"/></li>
                                        </ul><br/>
                                    </ano:iterate>
                                </ul><br/>
                            </ano:iterate>
                        </ano:notEmpty>
                    </ul>
                </ano:iterate>
            </ano:notEmpty>
            <li><h5>Connectors</h5></li>
            <ano:iterate name="configuration" property="connectors" id="connector" type="org.moskito.control.config.ConnectorConfig">
                <ul>
                    <li>Type: <ano:write name="connector" property="type"/></li>
                    <li>Class name: <ano:write name="connector" property="className"/></li>
                </ul>
            </ano:iterate><br/>
            <li><h5>Status updater</h5></li>
            <ano:define name="configuration" id="statusUpdater" property="statusUpdater"/>
            <ul>
                <li>Check period in seconds: <ano:write name="statusUpdater" property="checkPeriodInSeconds"/></li>
                <li>Thread pool size: <ano:write name="statusUpdater" property="threadPoolSize"/></li>
                <li>Timeout in seconds: <ano:write name="statusUpdater" property="timeoutInSeconds"/></li>
                <li>Enabled: <ano:write name="statusUpdater" property="enabled"/></li>
            </ul><br/>
            <li><h5>Charts updater</h5></li>
            <ano:define name="configuration" id="chartsUpdater" property="chartsUpdater"/>
            <ul>
                <li>Check period in seconds: <ano:write name="chartsUpdater" property="checkPeriodInSeconds"/></li>
                <li>Thread pool size: <ano:write name="chartsUpdater" property="threadPoolSize"/></li>
                <li>Timeout in seconds: <ano:write name="chartsUpdater" property="timeoutInSeconds"/></li>
                <li>Enabled: <ano:write name="chartsUpdater" property="enabled"/></li>
            </ul>
        </ul>
    </ano:equal>

    <ano:notEqual name="configToggle" value="true">
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
                    <span class="mute-title">
                        <ano:equal name="notificationsMuted" value="false">Mute for <ano:write name="notificationsMutingTime"/> minutes</ano:equal>
                        <ano:equal name="notificationsMuted" value="true">Remaining muting time <ano:write name="notificationsRemainingMutingTime"/> minutes</ano:equal>
                    </span>

                    <ano:equal name="notificationsMuted" value="false">
                        <a id="mute" href="muteNotifications" class="btn2" title="Mute mail notifications"><span class="inbtn">Mute</span></a>
                    </ano:equal>
                    <ano:equal name="notificationsMuted" value="true">
                        <a id="unmute" href="unmuteNotifications" class="btn2" title="Unmute mail notifications"><span class="inbtn">Unmute</span></a>
                    </ano:equal>

                    <span class="vline"></span>
                    <ano:notEqual name="configToggle" value="true"><a href="switchConfig?config=on" class="btn2 settings"><span class="inbtn"><i class="icon-cog"></i>Settings</span></a></ano:notEqual>
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

            <%-- COMPONENTS --%>
            <ano:equal name="statusToggle" value="true">
                <ano:iterate name="componentHolders" id="holder" type="org.moskito.control.ui.bean.ComponentHolderBean" indexId="holderIndex">
                    <!-- category block for <ano:write name="holder" property="categoryName"/> -->
                    <div class="box <ano:write name="holder" property="health"/>">
                        <div class="content-title"><h3><span class="status"></span><ano:write name="holder" property="categoryName"/></h3></div>
                        <ul class="controls">
                            <ano:iterate name="holder" property="components" type="org.moskito.control.ui.bean.ComponentBean" id="component" indexId="componentIndex">
                                <li class="<ano:write name="component" property="color"/>" role="button" data-toggle="modal" href="#component-modal-<ano:write name="holderIndex"/><ano:write name="componentIndex"/>"
                                        onclick="showThresholds('${pageContext.request.contextPath}', '<ano:write name="component" property="name"/>', <ano:write name="holderIndex"/>, <ano:write name="componentIndex"/>);">
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
                    <%-- Modal for component inspection --%>
                    <ano:iterate name="holder" property="components" type="org.moskito.control.ui.bean.ComponentBean" id="component" indexId="componentIndex">
                        <div id="component-modal-<ano:write name="holderIndex"/><ano:write name="componentIndex"/>" class="modal hide fade modal-stretch" tabindex="-1" role="dialog">
                            <div class="modal-header custom-modal-header">
                                <button type="button" class="close custom-close" data-dismiss="modal" aria-hidden="true">&times;</button>
                                <h3><span class="status <ano:write name="component" property="color"/>"></span><ano:write name="component" property="name"/></h3>
                                <%-- Thresholds & Accumulators tabs --%>
                                <ul class="nav nav-tabs tabs-pane">
                                    <li class="active"><a href="#thresholds-tab-<ano:write name="holderIndex"/><ano:write name="componentIndex"/>" data-toggle="tab"
                                           onclick="showThresholds('${pageContext.request.contextPath}', '<ano:write name="component" property="name"/>', <ano:write name="holderIndex"/>, <ano:write name="componentIndex"/>)">Thresholds</a></li>
                                    <li><a href="#accumulators-tab-<ano:write name="holderIndex"/><ano:write name="componentIndex"/>" data-toggle="tab"
                                           onclick="showAccumulatorsList('${pageContext.request.contextPath}','<ano:write name="component" property="name"/>', <ano:write name="holderIndex"/>, <ano:write name="componentIndex"/>)">Accumulators</a></li>
                                </ul>
                            <%-- Thresholds & Accumulators tabs --%>
                            </div>
                            <div class="modal-body custom-modal-body">
                                <%-- Thresholds & Accumulators tabs content --%>
                                <div class="tab-content">
                                    <div class="tab-pane active" id="thresholds-tab-<ano:write name="holderIndex"/><ano:write name="componentIndex"/>">
                                        <div class="loading" style="display: none">
                                            <span class="spinner"></span>
                                        </div>
                                        <div id="thresholds-view-<ano:write name="holderIndex"/><ano:write name="componentIndex"/>">
                                                <%-- ajax content --%>
                                        </div>
                                    </div>

                                    <div class="tab-pane" id="accumulators-tab-<ano:write name="holderIndex"/><ano:write name="componentIndex"/>">
                                        <div class="loading" style="display: none">
                                            <span class="spinner"></span>
                                        </div>
                                        <div id="accumulators-view-<ano:write name="holderIndex"/><ano:write name="componentIndex"/>">
                                            <%-- ajax content --%>
                                        </div>
                                    </div>
                                </div>
                                <%-- Thresholds & Accumulators tabs content end --%>
                            </div>
                            <div class="modal-footer modal-footer-custom"></div>
                        </div>
                    </ano:iterate>
                    <%-- Modal for component inspection end --%>
                </ano:iterate>
            </ano:equal>
            <%-- COMPONENTS END --%>

            <!-- CHARTS -->
            <ano:equal name="chartsToggle" value="true">
                <script type="text/javascript">
                    <ano:iterate id="chart" name="chartBeans" type="org.moskito.control.ui.bean.ChartBean">
                        // chart data for <ano:write name="chart" property="divId"/>
                        var chartDataArray<ano:write name="chart" property="divId"/> = [<ano:iterate name="chart" property="points" id="point" indexId="i"><ano:notEqual name="i" value="0">,</ano:notEqual><ano:write name="point"/></ano:iterate>];
                    </ano:iterate>
                </script>
                <div class="box charts">
                    <div class="content-title"><h3><i class="icon-bar-chart"></i>Charts</h3></div>
                    <div class="chart-list">
                        <ano:iterate id="chart" name="chartBeans" type="org.moskito.control.ui.bean.ChartBean">
                            <div class="chart-item">
                                <div id="<ano:write name="chart" property="divId"/>" class="chart-box" style="width: 800px; height: 300px;"></div>
                                <span class="footitle one-line-text"><ano:write name="chart" property="legend"/></span>
                            </div>
                        </ano:iterate>
                    </div>
                </div>
            </ano:equal>
            <%-- CHARTS END --%>

            <%-- HISTORY --%>
            <ano:present name="historyItems">
                <div class="box history">
                    <div class="content-title "><h3><i class="icon-reorder"></i>History</h3></div>
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
            <%-- HISTORY END --%>
        </div>

    </ano:notEqual>
</div>
</div>

<script type="text/javascript" src="../ext/jquery-1.8.2/jquery-1.8.2.js"></script>
<script type="text/javascript" src="../ext/jquery-ui-1.10.0/js/jquery-ui-1.10.0.custom.min.js"></script>
<script type="text/javascript" src="https://www.google.com/jsapi"></script>
<!--[if lt IE 10]>
<script type="text/javascript" src="../ext/pie-1.0.0/pie_uncompressed.js"></script>
<![endif]-->
<script type="text/javascript" src="../js/common.js"></script>
<script type="text/javascript" src="../ext/bootstrap-2.2.2/js/bootstrap.js"></script>
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

        // fitting modal-body size
        $(".modal").each(function() {
            $(this).on("shown", function() {
                fitModalBody($(this));
            });
        });
        $(window).resize(function() {
            $(".modal").each(function() {
                fitModalBody($(this));
            });
        });

        // to prevent background scrolling under modal
        $("[id^='component-modal']").each(function(index) {
            $(this).on("show", function () { // show event seems to appear not only when you open the modal
                body = $("body,html");
                if(!body.hasClass("modal-open")) { // to avoid multiple times addition
                    body.addClass("modal-open");
                }
                if(!$(".modal-backdrop", body)) {
                    $('<div class="modal-backdrop fade in" style="z-index: 1040;"></div>').appendTo(body);
                }
            }).on("hidden", function () {
                        $("body,html").removeClass("modal-open");
                        $(".modal-backdrop").remove();
                    });
        });
    });
    google.load("visualization", "1", {packages:["corechart"]});


    <ano:equal name="chartsToggle" value="true">
        <ano:iterate id="chart" name="chartBeans" type="org.moskito.control.ui.bean.ChartBean">
            google.setOnLoadCallback(draw<ano:write name="chart" property="divId"/>);
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
                    "chartArea":{"left":60,"width":560}
                };
                
                var options = $.extend({} ,defaultOptions, opts);

                var chart = new google.visualization.LineChart(document.getElementById('<ano:write name="chart" property="divId"/>'));
                chart.draw(chartData, options);

                $('#<ano:write name="chart" property="divId"/>').append("<i class='icon-resize-small'></i>");
                $('#<ano:write name="chart" property="divId"/>').append("<i class='icon-resize-full'></i>");
            }
            $('#<ano:write name="chart" property="divId"/>').click(function(e){
                $(this).toggleClass('chart_fullscreen');
                if ( $(this).hasClass('chart_fullscreen') ){
                    $(this).css('top', $(window).scrollTop());
                    draw<ano:write name="chart" property="divId"/>(e, {"width": "auto", "height": "auto", "chartArea":{"left":60,"width":"90%"}})
                }
                else{
                    $(this).css('top', 'auto');
                    draw<ano:write name="chart" property="divId"/>(e);
                }
            });
        </ano:iterate>
    </ano:equal>


</script>
<script type="text/javascript">

    function countDown(){
        remains = remains - 1;
        if($(".modal-backdrop").length > 0 && remains <= 10) {
            // refresh fill follow 10 seconds after modal closing
            remains = remains + 1;
        }
        document.getElementById("remains").innerText = ''+remains;
        if (remains<=0){
            window.location.href = window.location.href;
        }
    }
    var remains = 60;
    window.setInterval(countDown, 1000);

</script>
<ano:equal name="configuration" property="trackUsage" value="true"><img src="//counter.moskito.org/counter/control/<ano:write name="application.version_string"/>/main" class="ipix"> </ano:equal>
</body>
</html>
