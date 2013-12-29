<%@ page language="java" contentType="text/html;charset=UTF-8" session="true"
        %><%@ taglib uri="http://www.anotheria.net/ano-tags" prefix="ano"
        %>
<div class="thresholds">
    <table class="table table-striped table-modal">
        <thead>
        <tr>
            <th>Threshold name</th>
            <th width="50">Status</th>
            <th width="90">Last value</th>
            <th width="150">Last change timestamp</th>
        </tr>
        </thead>
        <tbody>
        <ano:iterate name="thresholds" type="org.moskito.control.ui.bean.ThresholdBean" id="threshold">
            <tr>
                <td><ano:write name="threshold" property="name"/></td>
                <td><div class="<ano:write name="threshold" property="status"/> status"></div></td>
                <td><ano:write name="threshold" property="lastValue"/></td>
                <td><ano:write name="threshold" property="statusChangeTimestamp"/></td>
            </tr>
        </ano:iterate>
        </tbody>
    </table>
</div>
