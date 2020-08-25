<%@ page language="java" contentType="text/html;charset=UTF-8" session="true" %>
<%@ taglib uri="http://www.anotheria.net/ano-tags" prefix="ano" %>
<table class="table table-striped">
    <thead>
    <tr>
        <th width="250">Timestamp</th>
        <th>Name</th>
        <th width="200">Status change</th>
    </tr>
    </thead>
    <tbody>
    <ano:iterate id="item" name="history">
        <tr>
            <td><ano:write name="item" property="time"/></td>
            <td><ano:write name="item" property="componentName"/></td>
            <td>
                <span class="status <ano:write name="item" property="oldStatus"/>"></span>
                <span class="arrow-right"></span>
                <span class="status <ano:write name="item" property="newStatus"/>"></span>
            </td>
        </tr>
    </ano:iterate>
    </tbody>
</table>