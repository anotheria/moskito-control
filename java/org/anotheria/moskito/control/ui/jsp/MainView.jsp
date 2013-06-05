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
    <link type="text/css" rel="stylesheet" rev="stylesheet" href="../css/common.css" />
    <!--[if IE]>
    <link type="text/css" rel="stylesheet" rev="stylesheet" href="../css/common_ie.css"/>
    <![endif]-->
</head>
<body>

<div class="wrapper">
<div class="left-bar">

    <a href="main" class="logo"><img src="../img/logo.png" alt="MoSKito Control" border="0"/></a>

    <div class="block">
        <h3 class="block-title">Category</h3>
        <ul class="category-list">
            <ano:iterate name="categories" id="category" type="org.anotheria.moskito.control.ui.bean.CategoryBean">
                <li class="<ano:equal name="category" property="selected" value="true">active </ano:equal><ano:equal name="category" property="all" value="true">all </ano:equal><ano:write name="category" property="health"/>">
                    <a href="setCategory?category=<ano:write name="category" property="name"/>">
                        <ano:write name="category" property="name"/>&nbsp;(<ano:write name="category" property="componentCount"/>)<span class="status"></span>
                    </a>
                </li>
            </ano:iterate>
<%--
            <li class="purple"><a href="#">Extapi <span class="status"></span></a></li>
            <li class="green"><a href="#">Admintool <span class="status"></span></a></li>
            <li class="green"><a href="#">Registry <span class="status"></span></a></li>
            <li class="active green"><a href="#">Service <span class="status"></span></a></li>
            <li class="yellow"><a href="#">Apps<span class="status"></span></a></li>
            <li class="red"><a href="#">Photoserver<span class="status"></span></a></li>
            <li class="green"><a href="#">Payment<span class="status"></span></a></li>
--%>
        </ul>
    </div>

    <div class="block">
        <h3 class="block-title">Widgets</h3>
        <ul class="widgets-list">
            <li class="charts active"><a href="#">Charts</a></li>
            <li class="history active"><a href="#">History</a></li>
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
            <ano:iterate name="applications" id="app_lication" type="org.anotheria.moskito.control.ui.bean.ApplicationBean">
                <li class="<ano:equal name="app_lication" property="active" value="true">active </ano:equal><ano:write name="app_lication" property="color"/>"><a href="setApplication?application=<ano:write name="app_lication" property="name"/>"><ano:write name="app_lication" property="name"/> <span class="status"></span></a></li>
            </ano:iterate>
<%--
            <li class="green"><a href="#">Application1 <span class="status"></span></a></li>
            <li class="active green"><a href="#">Application2 <span class="status"></span></a></li>
            <li class="yellow"><a href="#">Application3 <span class="status"></span></a></li>
            <li class="green"><a href="#">Application4 <span class="status"></span></a></li>--%>
        </ul>
    </div>

    <div class="infobar">
        <div class="infoline">
            <div class="pull-left">
                <span class="last-refresh">Last refresh: 16:44:38</span>
                <span class="next-refresh">Next refresh in 00 seconds</span>
            </div>
            <div class="pull-right">
                <span class="mute-title">Mute for 60 minutes</span>
                <a href="#" class="btn2"><span class="inbtn">Mute</span></a>
                <span class="vline"></span>
                <a href="#" class="btn2 settings"><span class="inbtn"><span class="icon"></span>Settings</span></a>
            </div>
        </div>
    </div>

    <div class="box-list">

        <ano:iterate name="componentHolders" id="holder" type="org.anotheria.moskito.control.ui.bean.ComponentHolderBean">
        <!-- category block for <ano:write name="holder" property="categoryName"/> -->
        <div class="box <ano:write name="holder" property="health"/>">
            <div class="content-title"><h3><span class="status"></span><ano:write name="holder" property="categoryName"/></h3></div>
            <ul class="controls">
                <ano:iterate name="holder" property="components" type="org.anotheria.moskito.control.ui.bean.ComponentBean" id="component">
                    <li class="<ano:write name="component" property="color"/>">
                        <span class="control-tooltip input-block-level">
                            <span class="tooltip-top-line"><span class="status"></span>Status: Ok</span>
                            <span class="tooltip-lower-line time">2012-12-10T10:44:54</span>
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

        <%--
        <div class="box charts">
            <div class="content-title"><h3><span class="status"></span>Charts</h3></div>
            <div class="chart-list">
                <div id="chart_div" class="chart-box" style="width: 556px; height: 250px;"></div>
                <div id="chart_div2" class="chart-box" style="width: 556px; height: 250px;"></div>
                <div id="chart_div3" class="chart-box" style="width: 556px; height: 250px;"></div>
            </div>
        </div>
        --%>

        <%--
        <div class="box history">
            <div class="content-title"><h3><span class="status"></span>History</h3></div>
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
                    <tr>
                        <td>2012-12-10T10:44:54,919</td>
                        <td>AdminAccessService1</td>
                        <td><span class="status red"></span><span class="arrow-right"></span><span class="status green"></span></td>
                    </tr>
                    <tr>
                        <td>2012-12-10T10:44:54,919</td>
                        <td>AdminAccessService1</td>
                        <td><span class="status green"></span><span class="arrow-right"></span><span class="status yellow"></span></td>
                    </tr>
                    <tr>
                        <td>2012-12-10T10:44:54,919</td>
                        <td>AdminAccessService1</td>
                        <td><span class="status red"></span><span class="arrow-right"></span><span class="status green"></span></td>
                    </tr>
                    <tr>
                        <td>2012-12-10T10:44:54,919</td>
                        <td>AdminAccessService1</td>
                        <td><span class="status red"></span><span class="arrow-right"></span><span class="status green"></span></td>
                    </tr>
                    <tr>
                        <td>2012-12-10T10:44:54,919</td>
                        <td>AdminAccessService1</td>
                        <td><span class="status red"></span><span class="arrow-right"></span><span class="status purple"></span></td>
                    </tr>
                    <tr>
                        <td>2012-12-10T10:44:54,919</td>
                        <td>AdminAccessService1</td>
                        <td><span class="status red"></span><span class="arrow-right"></span><span class="status green"></span></td>
                    </tr>
                    <tr>
                        <td>2012-12-10T10:44:54,919</td>
                        <td>AdminAccessService1</td>
                        <td><span class="status red"></span><span class="arrow-right"></span><span class="status green"></span></td>
                    </tr>
                    <tr>
                        <td>2012-12-10T10:44:54,919</td>
                        <td>AdminAccessService1</td>
                        <td><span class="status green"></span><span class="arrow-right"></span><span class="status red"></span></td>
                    </tr>
                    <tr>
                        <td>2012-12-10T10:44:54,919</td>
                        <td>AdminAccessService1</td>
                        <td><span class="status yellow"></span><span class="arrow-right"></span><span class="status green"></span></td>
                    </tr>
                    <tr>
                        <td>2012-12-10T10:44:54,919</td>
                        <td>AdminAccessService1</td>
                        <td><span class="status green"></span><span class="arrow-right"></span><span class="status red"></span></td>
                    </tr>
                    <tr>
                        <td>2012-12-10T10:44:54,919</td>
                        <td>AdminAccessService1</td>
                        <td><span class="status yellow"></span><span class="arrow-right"></span><span class="status green"></span></td>
                    </tr>
                    <tr>
                        <td>2012-12-10T10:44:54,919</td>
                        <td>AdminAccessService1</td>
                        <td><span class="status green"></span><span class="arrow-right"></span><span class="status red"></span></td>
                    </tr>
                    <tr>
                        <td>2012-12-10T10:44:54,919</td>
                        <td>AdminAccessService1</td>
                        <td><span class="status yellow"></span><span class="arrow-right"></span><span class="status green"></span></td>
                    </tr>

                    </tbody>
                </table>
            </div>
        </div>
        --%>

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
    google.setOnLoadCallback(drawChart);
    google.setOnLoadCallback(drawChart2);
    google.setOnLoadCallback(drawChart3);
    function drawChart() {
        var data = google.visualization.arrayToDataTable([
            ['Day', 'Online'],
            ['01.02',  1000],
            ['02.02',  1170],
            ['03.02',  660],
            ['04.02',  1000],
            ['05.02',  1170],
            ['06.02',  660],
            ['07.02',  1000]

        ]);

        var options = {
            title: 'Online users (Last Week)',
            titleTextStyle: {color: '#444'},
            hAxis: {textStyle: {color: '#444'}},
            colors:['#5983ED']
        };

        var chart = new google.visualization.AreaChart(document.getElementById('chart_div'));
        chart.draw(data, options);
    }

    function drawChart2() {
        var data = google.visualization.arrayToDataTable([
            ['Day', 'Usage'],
            ['01.02',  2234],
            ['02.02',  343],
            ['03.02',  2324],
            ['04.02',  2223],
            ['05.02',  1112],
            ['06.02',  2233],
            ['07.02',  334]

        ]);

        var options = {
            title: 'Usage (Last Week)',
            titleTextStyle: {color: '#444'},
            hAxis: {textStyle: {color: '#444'}},
            colors:['#5983ED']
        };

        var chart = new google.visualization.AreaChart(document.getElementById('chart_div2'));
        chart.draw(data, options);
    }

    function drawChart3() {
        var data = google.visualization.arrayToDataTable([
            ['Day', 'Premium'],
            ['01.02',  112],
            ['02.02',  4345],
            ['03.02',  222],
            ['04.02',  3345],
            ['05.02',  2233],
            ['06.02',  2222],
            ['07.02',  1111]
        ]);

        var options = {
            title: 'Premium (Last Week)',
            titleTextStyle: {color: '#444'},
            hAxis: {textStyle: {color: '#444'}},
            colors:['#5983ED']
        };

        var chart = new google.visualization.AreaChart(document.getElementById('chart_div3'));
        chart.draw(data, options);
    }
</script>
</body>
</html>