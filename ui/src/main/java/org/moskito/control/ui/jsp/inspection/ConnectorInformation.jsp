<%@ page language="java" contentType="text/html;charset=UTF-8" session="true"
%><%@ taglib uri="http://www.anotheria.net/ano-tags" prefix="ano"
%><%@ taglib prefix="c" uri="jakarta.tags.core" %>

<div class="connector-info">
    <table class="table table-striped table-modal">
        <thead>
        <tr>
            <th>Property</th>
            <th>Value</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="property" items="${ connectorInformation }">
            <tr>
                <td>${ property.key }</td>
                <td>${ property.value }</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
<ano:present name="componentAttributes">
    <br>
    <div class="connector-info">
        <table class="table table-striped table-modal">
            <thead>
            <tr>
                <th>Attribute</th>
                <th>Value</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="property" items="${ componentAttributes }">
                <tr>
                    <td>${ property.key }</td>
                    <td>${ property.value }</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>

</ano:present>