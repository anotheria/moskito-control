<%@ page language="java" contentType="text/html;charset=UTF-8" session="true"
        %><%@ taglib uri="http://www.anotheria.net/ano-tags" prefix="ano"
        %>
<div class="accumulators-list">
    <table class="table table-striped table-modal">
        <thead>
        <tr>
            <th width="30">Show</th>
            <th>Accumulator name</th>
        </tr>
        </thead>
        <tbody>
        <ano:iterate name="accumulatorsNames" type="java.lang.String" id="accumulatorName">
            <tr>
                <td><input type="checkbox" name="<ano:write name="accumulatorName"/>" value="<ano:write name="accumulatorName"/>"/></td>
                <td><ano:write name="accumulatorName"/></td>
            </tr>
        </ano:iterate>
        </tbody>
    </table>
</div>