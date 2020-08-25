<%@ page language="java" contentType="text/html;charset=UTF-8" session="true" %>
<%@ taglib uri="http://www.anotheria.net/ano-tags" prefix="ano" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="component-action-info">
    <table class="table table-striped table-modal">
        <thead>
        <tr>
            <th>Name</th>
            <th>Type</th>
            <th>Command</th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <ano:iterate name="actions" type="org.moskito.control.core.action.ComponentAction" id="action">
            <tr>
                <td>${action.name}</td>
                <td>${action.type}</td>
                <td>${action.command}</td>
                <td><input type="button" class="execute-command" onclick="executeComponentActionCommand('${pageContext.request.contextPath}', '${action.componentName}', '${action.name}', this)" value="execute"></td>
            </tr>

        </ano:iterate>
        </tbody>
    </table>
</div>
