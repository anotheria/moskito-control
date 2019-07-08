<%@ page language="java" contentType="text/html;charset=UTF-8"	session="true" isELIgnored="false" %>
<%@ taglib uri="http://www.anotheria.net/ano-tags" prefix="ano" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>MoSKito Control</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE">
    <META HTTP-EQUIV="EXPIRES" CONTENT="0">
    <META HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE">
    <META NAME="ROBOTS" CONTENT="NONE">
    <link rel="shortcut icon" href="../img/favicon.ico" type="image/x-icon">

    <link type="text/css" rel="stylesheet" rev="stylesheet" href="../ext/bootstrap-3.3.7/css/bootstrap.css"/>
    <link type="text/css" rel="stylesheet" rev="stylesheet" href="../ext/font-awesome-3.2.1/css/font-awesome.min.css">

    <link type="text/css" rel="stylesheet" rev="stylesheet" href="../css/common.css" />
    <!--[if IE]>
    <link type="text/css" rel="stylesheet" rev="stylesheet" href="../css/common_ie.css"/>
    <![endif]-->

    <link type="text/css" rel="stylesheet" rev="stylesheet" href="../ext/jquery.qtip2-3.0.3/jquery.qtip.min.css" />
</head>
<body>

<div class="wrapper">
<div class="left-bar">

    <a href="main" class="logo">
        <img src="../img/logo.png" alt="MoSKito Control" border="0"/>
        <span class="version"><ano:write name="moskito.control.version"/></span>
    </a>


    <ano:equal name="configToggle" value="true"><c:set var="hideElements" value="true"></c:set></ano:equal>
    <ano:equal name="dataRepositoryToggle" value="true"><c:set var="hideElements" value="true"></c:set></ano:equal>

    <ano:notEqual name="hideElements" value="true">
        <div class="block">
            <h3 class="block-title">
                Category<a class="pull-right clear-filter-toggle" href="clearCategoryFilter">clear</a>
            </h3>
            <ul class="category-list">
                <ano:iterate name="categories" id="category" type="org.moskito.control.ui.bean.CategoryBean">
                    <li class="<ano:equal name="category" property="selected" value="true">active </ano:equal><ano:equal name="category" property="all" value="true">all </ano:equal>${category.health}">
                        <a href="setCategory?category=${category.name}">
                            <ano:notEmpty name="category" property="all"><i class="icon-folder-close"></i></ano:notEmpty>${category.name}&nbsp;(${category.componentCount})<span class="status"></span>
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

                <ano:equal name="dataThresholdsToggle" value="true"><li class="dataThresholds active"><a href="switchDataThresholds?dataThresholds=off"><i class="icon-eye-open"></i>Data Thresholds</a></li></ano:equal>
                <ano:notEqual name="dataThresholdsToggle" value="true"><li class="dataThresholds"><a href="switchDataThresholds?dataThresholds=on"><i class="icon-eye-open"></i>Data Thresholds</a></li></ano:notEqual>


                <ano:equal name="historyToggle" value="true"><li class="history active"><a href="switchHistory?history=off"><i class="icon-reorder"></i>History</a></li></ano:equal>
                <ano:notEqual name="historyToggle" value="true"><li class="history"><a href="switchHistory?history=on"><i class="icon-reorder"></i>History</a></li></ano:notEqual>
                <ano:equal name="statusBetaToggle" value="true"><li class="statuses active"><a href="switchStatus?statusBeta=off"><i class="icon-adjust"></i>Status (beta)</a></li></ano:equal>
                <ano:notEqual name="statusBetaToggle" value="true"><li class="statuses"><a href="switchStatus?statusBeta=on"><i class="icon-adjust"></i>Status (beta)</a></li></ano:notEqual>
            </ul>
        </div>

        <div class="block">
            <h3 class="block-title">
                Statistics<a class="pull-right clear-filter-toggle" href="clearStatusFilter">clear</a>
            </h3>
            <ul class="statistics-list">
                <li class="purple <ano:iF test="${countByStatus.purple.selected}">active</ano:iF>">
                    <ano:iF test="${countByStatus.purple.selected}"><a href="removeStatusFilter?color=purple">${countByStatus.purple.componentCount} <span class="status"></span></a></ano:iF>
                    <ano:iF test="${!countByStatus.purple.selected}"><a href="addStatusFilter?color=purple">${countByStatus.purple.componentCount} <span class="status"></span></a></ano:iF>
                </li>
                <li class="red <ano:iF test="${countByStatus.red.selected}">active</ano:iF>">
                    <ano:iF test="${countByStatus.red.selected}"><a href="removeStatusFilter?color=red">${countByStatus.red.componentCount} <span class="status"></span></a></ano:iF>
                    <ano:iF test="${!countByStatus.red.selected}"><a href="addStatusFilter?color=red">${countByStatus.red.componentCount} <span class="status"></span></a></ano:iF>
                </li>
                <li class="orange <ano:iF test="${countByStatus.orange.selected}">active</ano:iF>">
                    <ano:iF test="${countByStatus.orange.selected}"><a href="removeStatusFilter?color=orange">${countByStatus.orange.componentCount} <span class="status"></span></a></ano:iF>
                    <ano:iF test="${!countByStatus.orange.selected}"><a href="addStatusFilter?color=orange">${countByStatus.orange.componentCount} <span class="status"></span></a></ano:iF>
                </li>
                <li class="yellow <ano:iF test="${countByStatus.yellow.selected}">active</ano:iF>">
                    <ano:iF test="${countByStatus.yellow.selected}"><a href="removeStatusFilter?color=yellow">${countByStatus.yellow.componentCount} <span class="status"></span></a></ano:iF>
                    <ano:iF test="${!countByStatus.yellow.selected}"><a href="addStatusFilter?color=yellow">${countByStatus.yellow.componentCount} <span class="status"></span></a></ano:iF>
                </li>
                <li class="green <ano:iF test="${countByStatus.green.selected}">active</ano:iF>">
                    <ano:iF test="${countByStatus.green.selected}"><a href="removeStatusFilter?color=green">${countByStatus.green.componentCount} <span class="status"></span></a></ano:iF>
                    <ano:iF test="${!countByStatus.green.selected}"><a href="addStatusFilter?color=green">${countByStatus.green.componentCount} <span class="status"></span></a></ano:iF>
                </li>
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

        <div class="box">
            <div class="box-title">
                <h3 class="">
                    Current configuration.
                </h3>
            </div>
            <div>
                <pre class="prettyprint linenums">
                    <code class="language-js"><ano:write name="configstring"/></code>
                </pre>
            </div>
        </div>
    </ano:equal>

    <ano:equal name="dataRepositoryToggle" value="true">
        <div class="infobar">
            <div class="infoline">
                <div class="pull-left">
                    <span class="configuration">Data Repository</span>
                </div>
                <div class="pull-right">
                    <a href="dataRepository?dataRepository=off" class="btn2"><span class="inbtn">Back</span></a>
                </div>
            </div>
        </div>

        <div class="box">
            <div class="box-title">
                <h3 class="">
                    Current data repository configuration.
                </h3>
            </div>

            <div class="container">
                <table class="table-striped table-bordered">
                    <tr>
                        <th class="text-center">Name</th>
                        <th class="text-center">Value</th>
                        <th class="text-center">Formula</th>
                    </tr>

                    <ano:iterate name="processing" id="processorName" type="java.util.Map.Entry">
                    <tr>
                        <td class="text-center">
                            <ano:write name="processorName" property="key"/>
                        </td>
                        <td class="text-center">
                                ${processingData[processorName.key]}
                        </td>
                        <td class="text-left">
                            <ano:iterate name="processorName" property="value" id="formula">
                                <p><ano:write name="formula"/></p>
                            </ano:iterate>
                        </td>
                        </ano:iterate>
                    </tr>
                </table>
            </div>
        </div>
    </ano:equal>

    <ano:notEqual name="hideElements" value="true">
        <div class="header">
            <ul class="applications-list">
                <ano:iterate name="applications" id="app_lication" type="org.moskito.control.ui.bean.ApplicationBean">
                    <li class="<ano:equal name="app_lication" property="active" value="true">active </ano:equal><ano:write name="app_lication" property="color"/>"><a href="setApplication?application=<ano:write name="app_lication" property="name"/>"><ano:write name="app_lication" property="name"/> <span class="status"></span></a></li>
                </ano:iterate>
            </ul>

            <div class="pull-right">
                <a class="design-toggle" href="${pageContext.request.contextPath}/beta">
                    Beta
                </a>
            </div>
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
                    <ano:notEqual name="dataRepositoryToggle" value="true"><a href="dataRepository?dataRepository=on" class="btn2 settings"><span class="inbtn"></i>Data Repository</span></a></ano:notEqual>
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
                                <li class="component-inspection-modal-toggle <ano:write name="component" property="color"/>" role="button" data-toggle="modal" href="#component-modal-<ano:write name="holderIndex"/><ano:write name="componentIndex"/>"
                                    onclick="applyConnectorConfiguration('${pageContext.request.contextPath}', '<ano:notEmpty name="currentApplication"><ano:write name="currentApplication" property="name" /></ano:notEmpty>', '<ano:write name="component" property="name"/>', <ano:write name="holderIndex"/>, <ano:write name="componentIndex"/>)">
                                    <span class="control-tooltip form-control">
                                        <ano:greaterThan name="component" property="messageCount" value="0">
                                            <span class="tooltip-top-line">
                                                <span class="status"></span>
                                                <ano:iterate name="component" property="messages" id="message">
                                                    <ano:notEmpty name="message">
                                                        <ano:write name="message"/><br/>
                                                    </ano:notEmpty>
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
                        <div id="component-modal-<ano:write name="holderIndex"/><ano:write name="componentIndex"/>" class="modal fade modal-stretch component-inspection" tabindex="-1" role="dialog">
                            <div class="modal-dialog components-inspection-modal">
                                <div class="modal-content">
                                <div class="modal-header custom-modal-header">
                                    <button type="button" class="close custom-close" data-dismiss="modal" aria-hidden="true">&times;</button>
                                    <h3><span class="status <ano:write name="component" property="color"/>"></span><ano:write name="component" property="name"/></h3>
                                    <%-- Thresholds & Accumulators tabs --%>
                                    <ul class="nav nav-tabs tabs-pane">
                                        <li id="thresholds-tab-toggle-<ano:write name="holderIndex"/><ano:write name="componentIndex"/>" class="active"><a href="#thresholds-tab-<ano:write name="holderIndex"/><ano:write name="componentIndex"/>" data-toggle="tab"
                                                              onclick="showThresholds('${pageContext.request.contextPath}', '<ano:write name="component" property="name"/>', <ano:write name="holderIndex"/>, <ano:write name="componentIndex"/>)">Thresholds</a></li>

                                        <li id="accumulators-tab-toggle-<ano:write name="holderIndex"/><ano:write name="componentIndex"/>"><a href="#accumulators-tab-<ano:write name="holderIndex"/><ano:write name="componentIndex"/>" data-toggle="tab"
                                               onclick="showAccumulatorsList('${pageContext.request.contextPath}','<ano:write name="component" property="name"/>', <ano:write name="holderIndex"/>, <ano:write name="componentIndex"/>)">Accumulators</a></li>

                                        <li id="info-tab-toggle-<ano:write name="holderIndex"/><ano:write name="componentIndex"/>"><a href="#info-tab-<ano:write name="holderIndex"/><ano:write name="componentIndex"/>" data-toggle="tab"
                                               onclick="showConnectorInformation('${pageContext.request.contextPath}','<ano:write name="component" property="name"/>', <ano:write name="holderIndex"/>, <ano:write name="componentIndex"/>)">Connector Information</a></li>

                                        <li id="history-tab-toggle-<ano:write name="holderIndex"/><ano:write name="componentIndex"/>"><a href="#history-tab-<ano:write name="holderIndex"/><ano:write name="componentIndex"/>" data-toggle="tab"
                                               onclick="showHistory('${pageContext.request.contextPath}','<ano:write name="component" property="name"/>', <ano:write name="holderIndex"/>, <ano:write name="componentIndex"/>)">History</a></li>
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

                                        <div class="tab-pane" id="info-tab-<ano:write name="holderIndex"/><ano:write name="componentIndex"/>">
                                            <div class="loading" style="display: none">
                                                <span class="spinner"></span>
                                            </div>
                                            <div id="info-view-<ano:write name="holderIndex"/><ano:write name="componentIndex"/>">
                                                <%-- ajax content --%>
                                            </div>
                                        </div>

                                        <div class="tab-pane" id="history-tab-<ano:write name="holderIndex"/><ano:write name="componentIndex"/>">
                                            <div class="loading" style="display: none">
                                                <span class="spinner"></span>
                                            </div>
                                            <div id="history-view-<ano:write name="holderIndex"/><ano:write name="componentIndex"/>">
                                                    <%-- ajax content --%>
                                            </div>
                                        </div>
                                    </div>
                                    <%-- Thresholds & Accumulators tabs content end --%>
                                </div>
                                <div class="modal-footer modal-footer-custom"></div>
                                </div>
                            </div>
                        </div>
                    </ano:iterate>
                    <%-- Modal for component inspection end --%>
                </ano:iterate>
            </ano:equal>
            <%-- COMPONENTS END --%>

                    <%-- COMPONENTS --%>
                <ano:equal name="statusBetaToggle" value="true">
                        <div class="box">
                            <div class="content-title"><h3>This is a beta view on the statuses with reduced space (removed component sections).</h3></div>
                            <ul class="controls">
                                <ano:iterate name="componentsBeta" type="org.moskito.control.ui.bean.ComponentBean" id="component" indexId="componentIndex">
                                    <li class="<ano:write name="component" property="color"/>" role="button" data-toggle="modal" href="#component-modal-<ano:write name="componentIndex"/>"
                                        onclick="applyConnectorConfiguration('${pageContext.request.contextPath}', '<ano:notEmpty name="currentApplication"><ano:write name="currentApplication" property="name" /></ano:notEmpty>', '<ano:write name="component" property="name"/>', '', <ano:write name="componentIndex"/>)">
                                    <span class="control-tooltip form-control">
                                        <ano:greaterThan name="component" property="messageCount" value="0">
                                            <span class="tooltip-top-line">
                                                <span class="status"></span>
                                                <ano:iterate name="component" property="messages" id="message">
                                                    <ano:notEmpty name="message">
                                                        <ano:write name="message"/><br/>
                                                    </ano:notEmpty>
                                                </ano:iterate>
                                            </span>
                                        </ano:greaterThan>
                                        <span class="tooltip-lower-line time"><ano:write name="component" property="updateTimestamp"/></span>
                                        <span class="arrow"></span>
                                    </span>
                                        <span class="control-title">
                                        <span class="status"></span>${component.categoryName}:${component.name}
                                    </span>
                                    </li>
                                </ano:iterate>
                            </ul>
                        </div>
                        <%-- Modal for component inspection --%>
                        <ano:iterate name="componentsBeta" type="org.moskito.control.ui.bean.ComponentBean" id="component" indexId="componentIndex">
                            <div id="component-modal-<ano:write name="componentIndex"/>" class="modal fade modal-stretch component-inspection" tabindex="-1" role="dialog">
                                <div class="modal-dialog components-inspection-modal">
                                    <div class="modal-content">
                                        <div class="modal-header custom-modal-header">
                                            <button type="button" class="close custom-close" data-dismiss="modal" aria-hidden="true">&times;</button>
                                            <h3><span class="status <ano:write name="component" property="color"/>"></span><ano:write name="component" property="name"/></h3>
                                                <%-- Thresholds & Accumulators tabs --%>
                                            <ul class="nav nav-tabs tabs-pane">
                                                <li class="active"><a href="#thresholds-tab-<ano:write name="componentIndex"/>" data-toggle="tab"
                                                                      onclick="showThresholds('${pageContext.request.contextPath}', '<ano:write name="component" property="name"/>', '', <ano:write name="componentIndex"/>)">Thresholds</a></li>

                                                <li><a href="#accumulators-tab-<ano:write name="componentIndex"/>" data-toggle="tab"
                                                       onclick="showAccumulatorsList('${pageContext.request.contextPath}','<ano:write name="component" property="name"/>', '', <ano:write name="componentIndex"/>)">Accumulators</a></li>

                                                <li>
                                                    <a href="#info-tab-<ano:write name="componentIndex"/>" data-toggle="tab"
                                                       onclick="showConnectorInformation('${pageContext.request.contextPath}','<ano:write name="component" property="name"/>', '', <ano:write name="componentIndex"/>)">Connector Information</a>
                                                </li>
                                                <li>
                                                    <a href="#history-tab-<ano:write name="componentIndex"/>" data-toggle="tab"
                                                       onclick="showHistory('${pageContext.request.contextPath}','<ano:write name="component" property="name"/>', '', <ano:write name="componentIndex"/>)">History</a>
                                                </li>
                                            </ul>
                                                <%-- Thresholds & Accumulators tabs --%>
                                        </div>
                                        <div class="modal-body custom-modal-body">
                                                <%-- Thresholds & Accumulators tabs content --%>
                                            <div class="tab-content">
                                                <div class="tab-pane active" id="thresholds-tab-<ano:write name="componentIndex"/>">
                                                    <div class="loading" style="display: none">
                                                        <span class="spinner"></span>
                                                    </div>
                                                    <div id="thresholds-view-<ano:write name="componentIndex"/>">
                                                            <%-- ajax content --%>
                                                    </div>
                                                </div>

                                                <div class="tab-pane" id="accumulators-tab-<ano:write name="componentIndex"/>">
                                                    <div class="loading" style="display: none">
                                                        <span class="spinner"></span>
                                                    </div>
                                                    <div id="accumulators-view-<ano:write name="componentIndex"/>">
                                                            <%-- ajax content --%>
                                                    </div>
                                                </div>

                                                    <div class="tab-pane" id="info-tab-<ano:write name="componentIndex"/>">
                                                        <div class="loading" style="display: none">
                                                            <span class="spinner"></span>
                                                        </div>
                                                        <div id="info-view-<ano:write name="componentIndex"/>">
                                                            <%-- ajax content --%>
                                                        </div>
                                                    </div>

                                                <div class="tab-pane" id="history-tab-<ano:write name="componentIndex"/>">
                                                    <div class="loading" style="display: none">
                                                        <span class="spinner"></span>
                                                    </div>
                                                    <div id="info-view-<ano:write name="componentIndex"/>">
                                                            <%-- ajax content --%>
                                                    </div>
                                                </div>
                                            </div>
                                            <%-- Thresholds & Accumulators tabs content end --%>
                                        </div>
                                        <div class="modal-footer modal-footer-custom"></div>
                                    </div>
                                </div>
                            </div>
                        </ano:iterate>
                        <%-- Modal for component inspection end --%>
                </ano:equal>
                    <%-- COMPONENTS END (statusBetaToggle)--%>


                <%-- DATA --%>
                <ano:present name="dataWidgets">
                <div class="box charts">
                    <div class="content-title"><h3><i class="icon-bar-chart"></i>Data</h3></div>
                    <div class="chart-list">
                        <div class="row">
                            <ano:iterate id="widget" name="dataWidgets" type="org.moskito.control.ui.bean.DataWidgetBean">
                                <ano:equal name="widget" property="type" value="Number">
                                <div class="col-sm-6 col-md-3">
                                    <div class="widget-item gauge-item">

                                        <div class="widget-num text-center">
                                            <span class="widget-data">${widget.data['number']}</span>
                                            <%--<span class="widget-percent arrow-up"><i class="icon-arrow-up" aria-hidden="true"></i>1.59%</span>--%>
                                            <span class="widget-title">${widget.caption}</span>
                                        </div>

                                    </div>
                                </div>
                                </ano:equal>
                                <ano:equal name="widget" property="type" value="NumberWithCompare">
                                    <div class="col-sm-6 col-md-3">
                                        <div class="widget-item gauge-item">

                                            <div class="widget-num text-center">
                                                <span class="widget-data">${widget.data['number']}</span>
                                                    <span class="widget-percent ${widget.data['arrow']}"><i class="icon-${widget.data['arrow']}" aria-hidden="true"></i>${widget.data['change']}</span>
                                                <span class="widget-title">${widget.caption}</span>
                                            </div>

                                        </div>
                                    </div>
                                </ano:equal>
                                <ano:equal name="widget" property="type" value="HalfGauge">
                                    <div class="col-sm-6 col-md-3">
                                        <div class="widget-item gauge-item">

                                        <div class="metric green" data-ratio="${widget.data['percent']}">
                                            <span class="widget-data">${widget.data['number']}</span>
                                            <span class="widget-title">${widget.caption}</span>
                                            <svg viewBox="0 0 1000 500">
                                                <path d="M 950 500 A 450 450 0 0 0 50 500"></path>
                                                <text class='percentage' text-anchor="middle" alignment-baseline="middle" x="500" y="340" font-size="60" font-weight="bold">0%</text>
                                            </svg>
                                        </div>

                                    </div>
                                </div>
                                </ano:equal>


                            </ano:iterate>
<%--
                            <div class="col-sm-6 col-md-3">
                                <div class="widget-item gauge-item">

                                    <div class="widget-num text-center">
                                        <span class="widget-data">103K</span>
                                        <!--<span class="widget-percent arrow-down"><i class="icon-arrow-down" aria-hidden="true"></i>-1.59%</span>-->
                                        <span class="widget-title">Page Views</span>
                                    </div>

                                </div>
                            </div>
                            <div class="col-sm-6 col-md-3">
                                <div class="widget-item gauge-item">

                                    <div class="widget-num text-center">
                                        <span class="widget-data">103K</span>
                                        <span class="widget-percent arrow-down"><i class="icon-arrow-down" aria-hidden="true"></i>-1.59%</span>
                                        <span class="widget-title">Page Views</span>
                                    </div>

                                </div>
                            </div>
                            <div class="col-sm-6 col-md-3">
                                <div class="widget-item gauge-item">

                                    <div class="widget-num text-center">
                                        <span class="widget-data">103K</span>
                                        <span class="widget-percent arrow-up"><i class="icon-arrow-up" aria-hidden="true"></i>1.59%</span>
                                        <span class="widget-title">Page Views</span>
                                    </div>

                                </div>
                            </div>
            --%>
                        </div>
                    </div>
                </div>
                </ano:present>



                <!-- CHARTS -->
            <ano:equal name="chartsToggle" value="true">
                <ano:notEmpty name="chartBeans">
                    <div class="box charts">
                        <div class="content-title"><h3><i class="icon-bar-chart"></i>Charts</h3></div>
                        <div class="chart-list">
                            <div class="row">
                                <ano:iterate id="chart" name="chartBeans" type="org.moskito.control.ui.bean.ChartBean">
                                    <div class="col-md-6">
                                        <div class="chart-item">
                                            <div class="chart-box-name"><ano:write name="chart" property="name"/></div>
                                            <div id="<ano:write name="chart" property="divId"/>" class="chart-box"></div>
                                            <i class='icon-resize-small'></i>
                                            <i class='icon-resize-full'></i>
                                            <span class="footitle one-line-text"><ano:write name="chart" property="legend"/></span>
                                        </div>
                                    </div>
                                </ano:iterate>
                            </div>
                        </div>
                    </div>
                </ano:notEmpty>
            </ano:equal>
            <%-- CHARTS END --%>


                <!-- DATA_THRESHOLDS -->
                <ano:equal name="dataThresholdsToggle" value="true">
                    <ano:notEmpty name="dataThresholds">
                        <div class="box thresholds">
                            <div class="content-title"><h3><i class="icon-eye-open"></i>Data Thresholds</h3></div>

                            <table class="table table-striped">
                                <thead>
                                <tr>
                                    <th>Name</th>
                                    <th>Status</th>
                                    <th>Value</th>
                                    <th>Status change</th>
                                    <th>Change timestamp</th>
                                    <th>Flip count</th>
                                </tr>
                                </thead>
                                <tbody>
                                <ano:iterate id="threshold" name="dataThresholds">
                                    <tr>
                                        <td><ano:write name="threshold" property="name"/></td>
                                        <td><span class="status <ano:write name="threshold" property="statusColor"/>"></span></td>
                                        <td><ano:write name="threshold" property="value"/></td>
                                        <td><span class="status <ano:write name="threshold" property="previousStatusColor"/>"></span><span class="arrow-right"></span><span class="status <ano:write name="threshold" property="statusColor"/>"></span></td>
                                        <td><ano:write name="threshold" property="timestamp"/></td>
                                        <td><ano:write name="threshold" property="flipCount"/></td>
                                    </tr>
                                </ano:iterate>
                                </tbody>
                            </table>

                            <%--DATA_THRESHOLDS HISTORY --%>
                            <table class="table table-striped" style="margin-bottom: 0;">
                                <tr>
                                    <th style="width: 25%">Timestamp</th>
                                    <th style="width: 25%">Name</th>
                                    <th style="width: 25%">Status change</th>
                                    <th style="width: 25%">Value change</th>
                                </tr>
                            </table>

                            <div style="overflow-y: auto;height: 200px">
                                <table class="table table-striped">
                                        <ano:iterate id="item" name="dataThresholdsHistory">
                                            <tr>
                                                <td style="width: 25%"><ano:write name="item" property="timestamp"/></td>
                                                <td style="width: 25%"><ano:write name="item" property="name"/></td>
                                                <td style="width: 25%"><span class="status <ano:write name="item" property="previousStatusColor"/>"></span><span class="arrow-right"></span><span class="status <ano:write name="item" property="statusColor"/>"></span></td>
                                                <td style="width: 25%"><ano:write name="item" property="previousValue"/><span class="arrow-right"></span><ano:write name="item" property="value"/></td>
                                            </tr>
                                        </ano:iterate>
                                </table>
                            </div>
                            <%--DATA_THRESHOLDS HISTORY END--%>
                            
                            </div>
                        </div>
                    </ano:notEmpty>
                </ano:equal>
                <%-- DATA_THRESHOLDS END --%>

            <%-- HISTORY --%>
            <ano:present name="historyItems">
                <ano:notEmpty name="historyItems">
                    <div class="box history">
                        <div class="content-title "><h3><i class="icon-reorder"></i>History</h3></div>
                        <div class="history-box">
                            <table class="table table-striped">
                                <thead>
                                <tr>
                                    <th width="250">Timestamp</th>
                                    <th>Name</th>
                                    <th>Reason</th>
                                    <th width="200">Status change</th>
                                </tr>
                                </thead>
                                <tbody>
                                <ano:iterate id="item" name="historyItems">
                                    <tr>
                                        <td><ano:write name="item" property="time"/></td>
                                        <td><ano:write name="item" property="componentName"/></td>
                                        <td><ano:write name="item" property="messages"/></td>
                                        <td><span class="status <ano:write name="item" property="oldStatus"/>"></span><span class="arrow-right"></span><span class="status <ano:write name="item" property="newStatus"/>"></span></td>
                                    </tr>
                                </ano:iterate>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </ano:notEmpty>
            </ano:present>
            <%-- HISTORY END --%>
        </div>

    </ano:notEqual>
</div>
</div>

<script type="text/javascript" src="../ext/jquery-1.10.2/jquery-1.10.2.js"></script>
<script type="text/javascript" src="../ext/lodash-4.17.4/lodash.min.js"></script>
<script type="text/javascript" src="../ext/d3-js-v3/d3.v3.js"></script>
<script type="text/javascript" src="../ext/jquery-ui-1.10.0/js/jquery-ui-1.10.0.custom.min.js"></script>
<script type="text/javascript" src="../ext/jquery.qtip2-3.0.3/jquery.qtip.min.js"></script>
<script type="text/javascript" src="../ext/bootstrap-3.3.7/js/bootstrap.js"></script>
<!--[if lt IE 10]>
<script type="text/javascript" src="../ext/pie-1.0.0/pie_uncompressed.js"></script>
<![endif]-->
<script type="text/javascript" src="../ext/snap.svg-0.4.1/snap.svg-min.js"></script>
<script type="text/javascript" src="../js/common.js"></script>
<script type="text/javascript" src="../js/main.js"></script>
<script type="text/javascript" src="../js/chartEngineIniter.js"></script>

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

            // Setting fullscreen buttons and handlers for chart
            var container = $('#' + chartParams.container);

            var previous_chart_params = {
                width: container.width(),
                height: container.height()
            };

            // Chart fullscreen click handler
            container.click(function(){
                var svg = container.find('svg');
                var body = $('body');

                container.toggleClass('chart_fullscreen');
                body.toggleClass('fullscreen');

                if (!container.hasClass('chart_fullscreen')) {
                    svg.attr("width", previous_chart_params.width).attr("height", previous_chart_params.height);

                    previous_chart_params.width = container.width();
                    previous_chart_params.height = container.height();
                }

                chartEngineIniter.d3charts.dispatch.refreshLineChart( "#" + container.attr("id"), true );
            });

            // Creating chart
            chartEngineIniter.init( chartParams );
        });
    </ano:equal>
</script>

<ano:equal name="configuration" property="trackUsage" value="true"><img src="//counter.moskito.org/counter/control/<ano:write name="application.version_string"/>/main" class="ipix"> </ano:equal>
<script>
    $(function() {

        var polar_to_cartesian, svg_circle_arc_path, animate_arc;

        polar_to_cartesian = function(cx, cy, radius, angle) {
            var radians;
            radians = (angle - 90) * Math.PI / 180.0;
            return [Math.round((cx + (radius * Math.cos(radians))) * 100) / 100, Math.round((cy + (radius * Math.sin(radians))) * 100) / 100];
        };

        svg_circle_arc_path = function(x, y, radius, start_angle, end_angle) {
            var end_xy, start_xy;
            start_xy = polar_to_cartesian(x, y, radius, end_angle);
            end_xy = polar_to_cartesian(x, y, radius, start_angle);
            return "M " + start_xy[0] + " " + start_xy[1] + " A " + radius + " " + radius + " 0 0 0 " + end_xy[0] + " " + end_xy[1];
        };

        animate_arc = function(ratio, svg, perc) {
            var arc, center, radius, startx, starty;
            arc = svg.path('');
            center = 500;
            radius = 450;
            startx = 0;
            starty = 450;
            return Snap.animate(0, ratio, (function(val) {
                var path;
                arc.remove();
                path = svg_circle_arc_path(500, 500, 450, -90, val * 180.0 - 90);
                arc = svg.path(path);
                arc.attr({
                    class: 'data-arc'
                });
                perc.text(Math.round(val * 100) + '%');
            }), Math.round(2000 * ratio), mina.easeinout);
        };

        $('.metric').each(function() {
            var ratio, svg, perc;
            ratio = $(this).data('ratio');
            svg = Snap($(this).find('svg')[0]);
            perc = $(this).find('text.percentage');
            animate_arc(ratio, svg, perc);
        });
    });
</script>
</body>
</html>
